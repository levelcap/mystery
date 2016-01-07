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
    private String parsePage = null;
    private String prefix = "puzzle/";
    private Set<String> parsedLinks = new HashSet<String>();
    private Set<String> puzzles = new HashSet<String>();

    @Autowired
    DriveService driveService;

    @Scheduled(fixedDelay = 60000)
    public void parsePuzzlePages() {
        if (null != basePage && null != parsePage) {
            try {
                LOGGER.info("Parsing base URL: {} ", basePage);
                Elements links = getLinksFromPage(basePage);
                parseLinks(links);
                LOGGER.info("ALL DONE!  Here's the list of puzzle links: " + puzzles.toString());
            } catch (IOException e) {
                LOGGER.error("Couldn't do the thing because of reasons: " + e.getMessage(), e);
            }
        } else {
            LOGGER.info("No base URL or parse URL set yet");
        }
    }

    private Elements getLinksFromPage(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc.select("a[href]");
    }

    private void parseLinks(Elements links) {
        for (Element link : links) {
            String url = link.attr("abs:href");
            if (url.substring(url.length() - 1).equals("/")) {
                url = url.substring(0, url.length() - 1);
            }
            if (!puzzles.contains(url)) {
                if (url.indexOf(prefix) > 0) {
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
            String sheet = driveService.createSpreadsheet(puzzleDoc.title(), puzzleDoc.title());
            driveService.addRow(puzzleDoc.title(), url, sheet);
            puzzles.add(url);
            Thread.sleep(1000);
        } catch (Exception e) {
            LOGGER.error("Error creating a spreadsheet for: " + url + " " + e.getMessage(), e);
        }
    }

    public void setBasePage(String basePage) {
        this.basePage = basePage;
    }
    public void setParsePage(String parsePage) {
        this.parsePage = parsePage;
    }
}
