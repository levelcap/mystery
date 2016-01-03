package com.brave.mystery.controller;

import com.brave.mystery.services.DriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    @Autowired
    DriveService driveService;

	@RequestMapping("/")
	public String index(Model model) {
        try {
            model.addAttribute("authUrl", driveService.getAuthorizationUrl("cohen.davids@gmail.com", null));
        } catch (Exception e) {

        }
        return "index";
	}

}
