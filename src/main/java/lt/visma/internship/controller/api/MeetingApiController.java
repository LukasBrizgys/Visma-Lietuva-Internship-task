package lt.visma.internship.controller.api;

import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import lt.visma.internship.classes.Response;
import lt.visma.internship.entities.Meeting;
import lt.visma.internship.entities.Person;
import lt.visma.internship.exception.DeleteAccessException;
import lt.visma.internship.exception.ErrorDetails;
import lt.visma.internship.services.CategoryService;
import lt.visma.internship.services.MeetingService;
import lt.visma.internship.services.PersonService;
import lt.visma.internship.services.TypeService;

@RestController
public class MeetingApiController {
	@Autowired
	MeetingService meetingService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	TypeService typeService;
	
	@ExceptionHandler(value = UsernameNotFoundException.class)
	public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException usernameNotFoundException, WebRequest request){
		ErrorDetails errorDetails = new ErrorDetails(new Date(), usernameNotFoundException.getMessage(),request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = DeleteAccessException.class)
	public ResponseEntity<?> handleDeleteAccessException(DeleteAccessException deleteAccessException, WebRequest request){
		ErrorDetails errorDetails = new ErrorDetails(new Date(), deleteAccessException.getMessage(),request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
	}
	@ExceptionHandler(value = NullPointerException.class)
	public ResponseEntity<?> handleNullPointerException(NullPointerException nullPointerException, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), nullPointerException.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
	}
	@ExceptionHandler(value = DateTimeParseException.class)
	public ResponseEntity<?> handleDateTimeParseException(DateTimeParseException dateTimeParseException, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), dateTimeParseException.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	@GetMapping("/api/meetings")
	ResponseEntity<List<Meeting>> all(@RequestParam(required = false) String description,
			@RequestParam(required = false) String personID,
			@RequestParam(required = false) Integer categoryID,
			@RequestParam(required = false) Integer typeID,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate,
			@RequestParam(required = false) Integer attendees) throws StreamReadException, DatabindException, IOException {
		List<Meeting> meetings = meetingService.getAllMeetings();
		List<Meeting> toRemove = new ArrayList<Meeting>();
		for(Meeting meeting : meetings) {
			if(description != null) {
				String convertedDescription = Normalizer.normalize(description, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
				String convertedMeetingDescription = Normalizer.normalize(meeting.getDescription(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
				if(!convertedMeetingDescription.contains(convertedDescription)) toRemove.add(meeting);
				
			}
			if(personID != null) {
				Person person = personService.getPersonById(UUID.fromString(personID));
				if(!meeting.getResponsiblePerson().equals(person)) {
					toRemove.add(meeting);
				}
			}
			if(categoryID != null) {
				if(!meeting.getCategory().getId().equals(categoryID)) {
					toRemove.add(meeting);
				}
			}
			if(typeID != null) {
				if(!meeting.getType().getId().equals(typeID)) {
					toRemove.add(meeting);
				}
			}
			if(startDate != null) {
				
					LocalDate parsedFilterDate = LocalDate.parse(startDate);
					LocalDate parsedMeetingDate = LocalDate.parse(meeting.getStartDate());
				if(parsedMeetingDate.isBefore(parsedFilterDate)) {
					toRemove.add(meeting);
				}
				
				
			}
			if(endDate != null) {
				LocalDate parsedFilterDate = LocalDate.parse(endDate);
				LocalDate parsedMeetingDate = LocalDate.parse(meeting.getEndDate());
				if(parsedMeetingDate.isAfter(parsedFilterDate)) {
					toRemove.add(meeting);
				}
			}
			if(attendees != null) {
				if(meeting.getAttendeeCount() < attendees) {
					toRemove.add(meeting);
				}
			}
		}
		meetings.removeAll(toRemove);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(meetings);
	}
	
	@RequestMapping(value = "/api/newMeeting", method = RequestMethod.POST)
	ResponseEntity<Meeting> newMeeting(
			@ModelAttribute Meeting newMeeting,
			@RequestParam String responsibleID,
			@RequestParam Integer categoryID,
			@RequestParam Integer typeID) throws StreamReadException, DatabindException, IOException {
		Person responsiblePerson = personService.getPersonById(UUID.fromString(responsibleID));
		newMeeting.setCategory(categoryService.getCategoryById(categoryID));
		newMeeting.setType(typeService.getTypeById(typeID));
		newMeeting.setResponsiblePerson(responsiblePerson);
		Meeting addedMeeting = meetingService.addMeeting(newMeeting);
		meetingService.addMeetingAttendee(responsiblePerson, addedMeeting);
		return ResponseEntity.ok().body(addedMeeting);
	}
	
	@DeleteMapping("/api/deleteMeeting/{meetingID}")
	ResponseEntity<Response>deleteMeeting(@PathVariable("meetingID") String meetingID,
			Authentication auth) throws StreamReadException, DatabindException, IOException {
			Person user = (Person) auth.getPrincipal();
			Meeting meeting = meetingService.getMeetingById(UUID.fromString(meetingID));
			if(meeting.getResponsiblePerson().equals(user)) {
				meetingService.deleteMeeting(UUID.fromString(meetingID));
			}else {
				throw new DeleteAccessException("You can't delete responsible person");
			}
		return new ResponseEntity<Response>(new Response("Meeting added"), HttpStatus.OK);
	}
}
