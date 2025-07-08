package com.jabaddon.tutorials.embabel.basic_embabel_agent.domain;

import java.util.List;

public record WebPageLinks(List<String> links) {
    @Override
    public String toString() {
        return "WebPageLinks{" +
                "links=" + links +
                '}';
    }
}
