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

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "index";
	}

    @RequestMapping("/obstacle")
    public String obstacleTest(Model model) {
        try {

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "obstacle";
    }

    @RequestMapping("/map")
    public String mapPage(Model model) {
        try {

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "map";
    }

}
