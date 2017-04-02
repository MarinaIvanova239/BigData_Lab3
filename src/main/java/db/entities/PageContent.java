package java.db.entities;

import org.springframework.data.annotation.Id;

public class PageContent {

    @Id
    private Integer id;
    private String link;
    private String content;

    public Integer getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getContent() {
        return content;
    }

    public void setId(Integer id) {
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
                "PageContent[id=%s, link='%s', content='%s']",
                id, link, content);
    }
}
