package com.spbstu.database.entities;

import org.springframework.data.annotation.Id;

public class PageContent {

    @Id
    private String link;
    private String content;

    public String getLink() {
        return link;
    }

    public String getContent() {
        return content;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PageContent() {}

    public PageContent(String link, String content) {
        this.link = link;
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format(
                "PageContent[link='%s', content='%s']",
                link, content);
    }
}
