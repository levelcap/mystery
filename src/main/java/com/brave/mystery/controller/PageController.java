package com.brave.mystery.controller;

import com.brave.mystery.services.DriveService;
import com.brave.mystery.services.PuzzlePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    @Autowired
    DriveService driveService;

    @Autowired
    PuzzlePageService puzzlePageService;

	@RequestMapping("/")
	public String index(Model model) {
        try {
            model.addAttribute("authUrl", driveService.getAuthorizationUrl("cohen.davids@gmail.com", null));
            model.addAttribute("basePage", puzzlePageService.getBasePage());
            model.addAttribute("parsePage", puzzlePageService.getParsePage());
            model.addAttribute("prefix", puzzlePageService.getPrefix());
        } catch (Exception e) {

        }
        return "index";
	}

}
