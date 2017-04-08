package com.spbstu.database.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class VisitedPages {

    @Id
    private String id;
    private String link;

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public VisitedPages() {}

    public VisitedPages(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return String.format(
                "VisitedPages[id=%s, link='%s']",
                id, link);
    }
}
