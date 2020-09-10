package com.javatechie.spring.ajax.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ViewController {

	@Autowired
	SqlService sqlService;

	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("bookNames",sqlService.getBookNames());
		return "home";
	}

}
