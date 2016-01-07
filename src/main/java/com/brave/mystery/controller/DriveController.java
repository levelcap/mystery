package com.brave.mystery.controller;

import com.brave.mystery.services.DriveService;
import com.brave.mystery.services.PuzzlePageService;
import com.google.api.client.auth.oauth2.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class DriveController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriveController.class);
    @Autowired
    DriveService driveService;

    @Autowired
    PuzzlePageService puzzlePageService;

    @RequestMapping("/api/auth")
    public boolean authorizeToken(@RequestParam(value = "code") String code) {
        try {
            driveService.getCredentials(code, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @RequestMapping("/api/setUrl")
    public boolean setUrl(@RequestParam(value = "url") String url) {
        try {
            puzzlePageService.setBasePage(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @RequestMapping("/api/setParsePage")
    public boolean setParsePage(@RequestParam(value = "url") String url) {
        try {
            puzzlePageService.setParsePage(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @RequestMapping("/api/setPrefix")
    public boolean setPrefix(@RequestParam(value = "prefix") String prefix) {
        try {
            puzzlePageService.setPrefix(prefix);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
