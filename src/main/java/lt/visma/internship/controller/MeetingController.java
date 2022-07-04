package lt.visma.internship.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import lt.visma.internship.entities.Meeting;
import lt.visma.internship.services.CategoryService;
import lt.visma.internship.services.MeetingService;
import lt.visma.internship.services.PersonService;
import lt.visma.internship.services.TypeService;

@Controller
public class MeetingController {
	@Autowired
	MeetingService meetingService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	TypeService typeService;
	
	@GetMapping("/meetingNew")
	String meeting(Model model) throws StreamReadException, DatabindException, IOException {
		model.addAttribute("meeting", new Meeting());
		model.addAttribute("persons", personService.getPersons());
		model.addAttribute("categories", categoryService.getAllCategories());
		model.addAttribute("types", typeService.getAllTypes());
		return "newMeeting";
	}
	
	
	
}
