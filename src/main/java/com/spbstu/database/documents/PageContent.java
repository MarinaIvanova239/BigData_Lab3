package com.spbstu.database.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PageContent {

    @Id
    private String id;
    private String link;
    private String content;

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getContent() {
        return content;
    }

    public void setId(String id) {
        this.id = id;
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
                "PageContent[id='%s', link='%s', content='%s']",
                id, link, content);
    }
}
