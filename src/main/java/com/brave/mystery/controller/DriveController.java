package com.brave.mystery.controller;

import com.brave.mystery.model.OvercomeResult;
import com.brave.mystery.services.DriveService;
import com.brave.mystery.services.PuzzlePageService;
import com.google.api.client.auth.oauth2.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    @RequestMapping("/api/setTitleCut")
    public boolean setTitleCut(@RequestParam(value = "titleCut") String titleCut) {
        try {
            puzzlePageService.setTitleCut(titleCut);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @RequestMapping("/api/setCookieName")
    public boolean setCookieName(@RequestParam(value = "cookieName") String cookieName) {
        try {
            puzzlePageService.setCookieName(cookieName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @RequestMapping("/api/setCookieValue")
    public boolean setCookieValue(@RequestParam(value = "cookieValue") String cookieValue) {
        try {
            puzzlePageService.setCookieValue(cookieValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @RequestMapping("/api/generate")
    public boolean generateSpreadsheets(@RequestParam(value = "start") Integer start,
                                        @RequestParam(value = "end") Integer end) {
        try {
            GenerationRunner runner = new GenerationRunner(start, end);
            Thread t = new Thread(runner);
            t.start();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @RequestMapping("/api/fate")
    public List<OvercomeResult> testObstacle(@RequestParam(value = "difficulty") Integer difficulty,
                                @RequestParam(value = "bonus") Integer bonus,
                                @RequestParam(value = "advantageDifficulty") Integer advantageDifficulty,
                                @RequestParam(value = "helpers") Integer helpers) {

        List<OvercomeResult> results = new ArrayList<OvercomeResult>();

        for (int i = 0; i < 100; i++) {
            OvercomeResult result = new OvercomeResult(difficulty, randomFateRoll(bonus));
            for (int j = 0; j < helpers; j++) {
                if (randomFateRoll(bonus) >= advantageDifficulty) {
                    result.addSuccessfulHelper();
                } else {
                    result.addFailedHelper();
                }
            }
            result.calculateOutcome();
            results.add(result);
        }
        return results;
    }

    private int randomFateRoll(int bonus) {
        Random rand = new Random();
        return (-4 + rand.nextInt((4 - -4) + 1)) + bonus;
    }

    class GenerationRunner implements Runnable {
        Integer start;
        Integer end;

        public GenerationRunner(Integer start, Integer end) {
            this.start = start;
            this.end = end;
        }

        public void run() {
            puzzlePageService.generateSheets(start, end);
        }
    }
}
