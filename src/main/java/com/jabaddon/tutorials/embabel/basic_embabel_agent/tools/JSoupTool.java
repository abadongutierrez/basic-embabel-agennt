package com.jabaddon.tutorials.embabel.basic_embabel_agent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JSoupTool {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(JSoupTool.class);

    @Tool(name = "jsoup", description = "A tool to extract text from web pages using JSoup")
    public String getPageTextTool(String url) {
        logger.info("*** Fetching page text from URL: {} ***", url);
        try {
            String pageText = getPageText(url);
            logger.info("*** Successfully fetched page text from URL: {}:\n<text>\n{} ...\n</text> ***", url, pageText.substring(0, 50));
            return pageText;
        } catch (Exception e) {
            logger.error("*** Error fetching page text from URL: {} ***", url, e);
            return "Error fetching page text: " + e.getMessage();
        }
    }

    public String getPageText(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc.text();
    }
}
