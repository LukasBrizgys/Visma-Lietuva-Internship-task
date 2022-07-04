package lt.visma.internship.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import lt.visma.internship.entities.Meeting;
import lt.visma.internship.services.MeetingService;
import lt.visma.internship.services.PersonService;

@Controller
public class MeetingPersonController {
	@Autowired
	PersonService personService;
	
	@Autowired
	MeetingService meetingService;
	
	@GetMapping("/newPersonMeeting")
	String personMeeting(Model model) throws StreamReadException, DatabindException, IOException {
		model.addAttribute("persons", personService.getPersons());
		model.addAttribute("meetings", meetingService.getAllMeetings());
		return "newPersonMeeting";
	}
	@GetMapping("/meeting/{meetingID}")
	String meetingAttendees(Model model, @PathVariable("meetingID") String meetingID) throws StreamReadException, DatabindException, IOException {
		Meeting meeting = meetingService.getMeetingById(UUID.fromString(meetingID));
		model.addAttribute("attendees", meetingService.getAllMeetingAttendees(meeting));
		model.addAttribute("meetingID", meetingID);
		return "attendeeList";
	}

}
