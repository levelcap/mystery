package com.brave.mystery.services;

import com.brave.mystery.repository.ItemRepository;
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

/**
 * Created by dcohen on 7/22/15.
 */
@Service
public class PuzzlePageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PuzzlePageService.class);
    private static final String BASE_PAGE = "http://web.mit.edu/puzzle/www/2015/";
    private Set<String> parsedLinks = new HashSet<String>();
    private Set<String> puzzles = new HashSet<String>();

    @Autowired
    ItemRepository itemRepository;

    @Scheduled(fixedDelay=60000)
    public void parsePuzzlePages() {
        try {
            LOGGER.info("Parsing base URL: {} ", BASE_PAGE);
            Elements links = getLinksFromPage(BASE_PAGE);
            parseLinks(links);
            LOGGER.info("ALL DONE!  Here's the list of puzzle links: " + puzzles.toString());
        } catch (IOException e) {
            LOGGER.error("Couldn't do the thing because of reasons: " + e.getMessage(), e);
        }
    }

    private Elements getLinksFromPage(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc.select("a[href]");
    }

    private void parseLinks(Elements links) {
        for (Element link : links) {
            String url = link.attr("abs:href");
            if (url.indexOf("puzzle/") != url.lastIndexOf("puzzle/")) {
                puzzles.add(url);
            } else if (url.contains(BASE_PAGE)) {
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
