package lt.visma.internship.controller;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lt.visma.internship.services.CategoryService;
import lt.visma.internship.services.MeetingService;
import lt.visma.internship.services.TypeService;

@Controller
public class HomeController {
	
	@Autowired
	MeetingService meetingService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	TypeService typeService;
	
	@GetMapping("/")
	public String home(Model model) {
		try {
			model.addAttribute("meetings", meetingService.getAllMeetings());
			model.addAttribute("responsibles", meetingService.getAllUniqueResponsiblePersons());
			model.addAttribute("types", typeService.getAllTypes());
			model.addAttribute("categories", categoryService.getAllCategories());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "home";
	}
}
