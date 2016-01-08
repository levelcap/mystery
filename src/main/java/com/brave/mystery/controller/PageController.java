package com.brave.mystery.controller;

import com.brave.mystery.services.DriveService;
import com.brave.mystery.services.PuzzlePageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PageController.class);
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
            model.addAttribute("titleCut", puzzlePageService.getTitleCut());
            model.addAttribute("cookieName", puzzlePageService.getCookieName());
            model.addAttribute("cookieValue", puzzlePageService.getCookieValue());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "index";
	}

}
