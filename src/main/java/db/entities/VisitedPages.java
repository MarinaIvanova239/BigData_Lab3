package java.db.entities;

import org.springframework.data.annotation.Id;

public class VisitedPages {

    @Id
    private Integer id;
    private String link;

    public Integer getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
