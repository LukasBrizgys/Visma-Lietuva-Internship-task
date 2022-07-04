package lt.visma.internship.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import lt.visma.internship.entities.Person;
import lt.visma.internship.services.PersonService;

@Controller
public class RegisterController {
	@Autowired
	PersonService personService;
	@GetMapping("/register")
	String register(Model model) {
		model.addAttribute("person", new Person());
		return "register";
	}
	@PostMapping("/register")
	String newRegistration(@Valid
			@ModelAttribute Person person,
			BindingResult result,
			@RequestParam("password") String password,
			@RequestParam("passwordRepeat") String passwordRepeat) {
		if(!password.equals(passwordRepeat)) result.rejectValue("passwordRepeat", "error.person.passwordRepeat", "Slaptažodžiai nesutampa");
		if(result.hasErrors()) return "register";
		try {
			personService.addNewPerson(person);
		} catch (StreamReadException e) {
			e.printStackTrace();
		} catch (DatabindException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/";
		
	}
}
