package lt.visma.internship.services;

import java.io.File;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;


import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import lt.visma.internship.entities.Meeting;
import lt.visma.internship.entities.Person;

@Service
public class MeetingService {
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final File meetingPath = new File("./src/main/resources/json/meeting.json");
	public MeetingService() throws StreamReadException, DatabindException, IOException {
		try {
			mapper.readValue(meetingPath, new TypeReference<List<Meeting>>(){});
		}catch(MismatchedInputException e) { //Jeigu serverio sukūrimo metu JSON failas VISIŠKAI tuščias, sukuria tuščią masyvą jame 
			ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
			writer.writeValue(meetingPath, new String[0]);
		}
	}
	public List<Meeting> getAllMeetings() throws StreamReadException, DatabindException, IOException {
		List<Meeting> meetings = mapper.readValue(meetingPath, new TypeReference<List<Meeting>>(){});
		return meetings;
	}
	public List<Person> getAllMeetingAttendees(Meeting meeting) throws StreamReadException, DatabindException, IOException {
		return meeting.getAttendees();
	}
	public Person addMeetingAttendee(Person person, Meeting addToMeeting) throws StreamReadException, DatabindException, IOException {
		List<Meeting> meetings = mapper.readValue(meetingPath, new TypeReference<List<Meeting>>(){});
		List<Person> attendees = addToMeeting.getAttendees();
		for(Meeting meeting : meetings) {
			if(meeting.getId().compareTo(addToMeeting.getId()) == 0) {
				attendees.add(person);
				meeting.setAttendees(attendees);
				meeting.setAttendeeCount(getAttendeeCount(meeting));
			}
		}
		
		mapper.writerWithDefaultPrettyPrinter().writeValue(meetingPath, meetings);
		return person;
	}
	public Meeting getMeetingById(UUID id) throws StreamReadException, DatabindException, IOException {
		List<Meeting> meetings = mapper.readValue(meetingPath, new TypeReference<List<Meeting>>(){});
		for(Meeting meeting: meetings) {
			if(meeting.getId().compareTo(id) == 0) {
				return meeting;
			}
		}
		return null;
	}
	
	public Meeting addMeeting(Meeting meeting) throws StreamReadException, DatabindException, IOException {
		List<Meeting> meetings = mapper.readValue(meetingPath, new TypeReference<List<Meeting>>(){});
		meetings.add(meeting);
		mapper.writerWithDefaultPrettyPrinter().writeValue(meetingPath, meetings);
		return meeting;
	}
	public void deleteMeeting(UUID id) throws StreamReadException, DatabindException, IOException {
		List<Meeting> meetings = mapper.readValue(meetingPath, new TypeReference<List<Meeting>>(){});
		List<Meeting> toRemove = new ArrayList<Meeting>();
		for(Meeting meeting: meetings) {
			if(meeting.getId().compareTo(id) == 0) {
				toRemove.add(meeting);
			}
		}
		meetings.removeAll(toRemove);
		mapper.writerWithDefaultPrettyPrinter().writeValue(meetingPath, meetings);
		
	}
	public void deleteMeetingAttendee(Person attendeeToRemove, Meeting meetingToCheck) throws StreamReadException, DatabindException, IOException {
		List<Meeting> meetings = mapper.readValue(meetingPath, new TypeReference<List<Meeting>>(){});
		List<Person> attendees = meetingToCheck.getAttendees();
		if(attendees.contains(attendeeToRemove)) attendees.remove(attendeeToRemove);
		for(Meeting meeting : meetings) {
			if(meeting.equals(meetingToCheck)) {
				meeting.setAttendees(attendees);
				meeting.setAttendeeCount(getAttendeeCount(meeting));
			}
		}
		mapper.writerWithDefaultPrettyPrinter().writeValue(meetingPath, meetings);
	}
	public List<Person> getAllUniqueResponsiblePersons() throws StreamReadException, DatabindException, IOException {
		List<Meeting> meetings = mapper.readValue(meetingPath, new TypeReference<List<Meeting>>(){});
		List<Person> uniqueList = new ArrayList<Person>();
		for(Meeting meeting : meetings){
			if(!uniqueList.contains(meeting.getResponsiblePerson())) {
				uniqueList.add(meeting.getResponsiblePerson());
			}
		}
		return uniqueList;
	}
	public boolean isPersonAlreadyInAMeeting(Person personToCheck, Meeting meetingToCheck) {
			List<Person> attendees = meetingToCheck.getAttendees();
			if(attendees.contains(personToCheck)) return true;
		return false;
	}
	public boolean isIntersecting(Person personToCheck, Meeting meetingToCheck) throws StreamReadException, DatabindException, IOException {
		List<Meeting> meetings = mapper.readValue(meetingPath, new TypeReference<List<Meeting>>(){});
		List<Meeting> personMeetings = new ArrayList<>();
		for(Meeting meeting : meetings) {
			List<Person> meetingAttendees = meeting.getAttendees();
			if(meetingAttendees.contains(personToCheck)) {
				personMeetings.add(meeting);
			}
		}
		
		LocalDate meetingToCheckStartDate = LocalDate.parse(meetingToCheck.getStartDate());
		LocalDate meetingToCheckEndDate = LocalDate.parse(meetingToCheck.getEndDate());
		for(Meeting meeting : personMeetings) {
			LocalDate meetingStartDate = LocalDate.parse(meeting.getStartDate());
			LocalDate meetingEndDate = LocalDate.parse(meeting.getEndDate());
				if(meetingToCheckStartDate.isBefore(meetingEndDate) 
					|| meetingToCheckEndDate.isAfter(meetingStartDate)) {
					return true;
				}
			
		}
		return false;
	}
	public void updateMeeting(Meeting updatedMeeting) throws StreamReadException, DatabindException, IOException {
		List<Meeting> meetings = mapper.readValue(meetingPath, new TypeReference<List<Meeting>>(){});
		for(Meeting old : meetings) {
			if(old.getId() == updatedMeeting.getId()) {
				old.setName(updatedMeeting.getName());
				old.setDescription(updatedMeeting.getDescription());
				old.setStartDate(updatedMeeting.getStartDate());
				old.setEndDate(updatedMeeting.getEndDate());
				
			}
		}
	}
	public Integer getAttendeeCount(Meeting meeting) {
		return meeting.getAttendees().size();
	}
	public List<Person> getAllResponsiblePersons() {
		try {
			List<Meeting> meetings = mapper.readValue(meetingPath, new TypeReference<List<Meeting>>(){});
			List<Person> responsiblePersons = new ArrayList<>();
			for(Meeting meeting : meetings) {
				responsiblePersons.add(meeting.getResponsiblePerson());
			}
			return responsiblePersons;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
