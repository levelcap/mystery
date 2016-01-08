package com.brave.mystery.services;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
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
    private String titleCut = " — MIT Mystery Hunt 2015";
    private Set<String> parsedLinks = new HashSet<String>();
    private Set<String> puzzles = new HashSet<String>();

    private String username = "notaplanet";
    private String password = "otulp";
    private String login = username + ":" + password;
    private String cookieName = "";
    private String cookieValue = "";

    @Autowired
    DriveService driveService;

    @Scheduled(fixedDelay = 60000)
    public void parsePuzzlePages() {
        if (null != basePage && null != parsePage) {
            try {
                LOGGER.info("Parsing parse page URL: {} ", parsePage);
                Elements links = getLinksFromPage(parsePage);
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
        Document doc = Jsoup.connect(url)
                .cookie(cookieName, cookieValue)
                .get();
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
            String title = puzzleDoc.title();
            if (!StringUtil.isBlank(titleCut)) {
                title = title.replace(titleCut, "");
            }

            String sheet = driveService.createSpreadsheet(title, puzzleDoc.title());
            driveService.addRow(title, url, sheet);
            puzzles.add(url);
            Thread.sleep(1000);
        } catch (Exception e) {
            LOGGER.error("Error creating a spreadsheet for: " + url + " " + e.getMessage(), e);
        }
    }

    public void setBasePage(String basePage) {
        this.basePage = basePage;
    }

    public String getBasePage() {
        return this.basePage;
    }

    public void setParsePage(String parsePage) {
        this.parsePage = parsePage;
    }

    public String getParsePage() {
        return this.parsePage;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setTitleCut(String titleCut) {
        this.titleCut = titleCut;
    }

    public String getTitleCut() {
        return this.titleCut;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getCookieValue() {
        return cookieValue;
    }

    public void setCookieValue(String cookieValue) {
        this.cookieValue = cookieValue;
    }
}
