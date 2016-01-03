package com.brave.mystery.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class PuzzlePageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PuzzlePageService.class);
    private String basePage = null;
    private Set<String> parsedLinks = new HashSet<String>();
    private Set<String> puzzles = new HashSet<String>();

    @Autowired
    DriveService driveService;

    @Scheduled(fixedDelay = 600000)
    public void parsePuzzlePages() {
        if (null != basePage) {
            try {
                LOGGER.info("Parsing base URL: {} ", basePage);
                Elements links = getLinksFromPage(basePage);
                parseLinks(links);
                LOGGER.info("ALL DONE!  Here's the list of puzzle links: " + puzzles.toString());
            } catch (IOException e) {
                LOGGER.error("Couldn't do the thing because of reasons: " + e.getMessage(), e);
            }
        }
    }

    private Elements getLinksFromPage(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc.select("a[href]");
    }

    private void parseLinks(Elements links) {
        for (Element link : links) {
            String url = link.attr("abs:href");
            if (!puzzles.contains(url)) {
                if (url.indexOf("puzzle/") != url.lastIndexOf("puzzle/")) {
                    createSpreadsheet(url);
                } else if (url.contains(basePage)) {
                    try {
                        if (!parsedLinks.contains(url)) {
                            LOGGER.info("Found internal non-puzzle link " + url);
                            parsedLinks.add(url);
                            parseLinks(getLinksFromPage(url));
                        }
                    } catch (IOException e) {
                        LOGGER.warn("Could not follow a link, may not have been to a valid HTML document: " + url);
                    }
                }
            }
        }
    }

    private void createSpreadsheet(String url) {
        try {
            Document puzzleDoc = Jsoup.connect(url).get();
            driveService.createSpreadsheet(puzzleDoc.title(), puzzleDoc.title());
            puzzles.add(url);
            Thread.sleep(5000);
        } catch (Exception e) {
            LOGGER.error("Error creating a spreadsheet for: " + url + " " + e.getMessage(), e);
        }
    }

    public void setBasePage(String basePage) {
        this.basePage = basePage;
    }
}
