package lt.visma.internship.controller.api;

import java.io.IOException;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import lt.visma.internship.classes.Response;
import lt.visma.internship.entities.Meeting;
import lt.visma.internship.entities.Person;
import lt.visma.internship.exception.DateIntersectException;
import lt.visma.internship.exception.DeleteAccessException;
import lt.visma.internship.exception.ErrorDetails;
import lt.visma.internship.exception.PersonAlreadyExistsException;
import lt.visma.internship.services.MeetingService;
import lt.visma.internship.services.PersonService;

@RestController
public class PersonMeetingApiController {
	@Autowired
	PersonService personService;
	
	@Autowired
	MeetingService meetingService;

	@ExceptionHandler(value = PersonAlreadyExistsException.class)
	public ResponseEntity<?> handlePersonAlreadyExistsException(PersonAlreadyExistsException
			PersonAlreadyExistsException, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), PersonAlreadyExistsException.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
	}
	@ExceptionHandler(value = DeleteAccessException.class)
	public ResponseEntity<?> handleDeleteAccessException(DeleteAccessException deleteAccessException, WebRequest request){
		ErrorDetails errorDetails = new ErrorDetails(new Date(), deleteAccessException.getMessage(),request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
	}
	@ExceptionHandler(value = DateIntersectException.class)
	public ResponseEntity<?> handleDateIntersectException(DateIntersectException
			dateIntersectException, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), dateIntersectException.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
	}
	@PutMapping("/api/addAttendee/{addTime}/{meetingID}")
	ResponseEntity<?> newPersonMeeting(
			@RequestParam String personID,
			@PathVariable("addTime") String time,
			@PathVariable("meetingID") String meetingID) throws StreamReadException , DatabindException, IOException {
		Person person = personService.getPersonById(UUID.fromString(personID));
		Meeting meeting = meetingService.getMeetingById(UUID.fromString(meetingID));
		if(meetingService.isPersonAlreadyInAMeeting(person, meeting)) throw new PersonAlreadyExistsException("Person is already added to the meeting");
		if(meetingService.isIntersecting(person, meeting)) System.out.println("Meetings are intersecting");
		meetingService.addMeetingAttendee(person, meeting);
		return new ResponseEntity<Response>(new Response("Person added"),HttpStatus.OK);
		
	}
	@DeleteMapping("/api/deleteMeetingAttendee/{attendeeID}/{meetingID}")
	ResponseEntity<?> deleteMeetingAttendee(
			@PathVariable("attendeeID") String attendeeID,
			@PathVariable("meetingID") String meetingID
			) throws StreamReadException, DatabindException, IOException {
		Meeting meeting = meetingService.getMeetingById(UUID.fromString(meetingID));
		Person attendee = personService.getPersonById(UUID.fromString(attendeeID));
		if(meeting.getResponsiblePerson().equals(attendee)) throw new DeleteAccessException("This person cannot be deleted");
		meetingService.deleteMeetingAttendee(attendee, meeting);
		
		return new ResponseEntity<Response>(new Response("Person deleted from meeting"), HttpStatus.OK);
	}
}
