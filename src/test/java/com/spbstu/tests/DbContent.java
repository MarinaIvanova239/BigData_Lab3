package com.spbstu.tests;

public class DbContent {

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

    public DbContent() {}

    public DbContent(String link, String content) {
        this.link = link;
        this.content = content;
    }
}
