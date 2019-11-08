package rocks.process.acrm.data.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

 @Entity
public class Setting {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGeneralTemplate() {
        return generalTemplate;
    }

    public void setGeneralTemplate(String generalTemplate) {
        this.generalTemplate = generalTemplate;
    }

    public String getBoardTemplate() {
        return boardTemplate;
    }

    public void setBoardTemplate(String boardTemplate) {
        this.boardTemplate = boardTemplate;
    }

    @Id
    @GeneratedValue
    private Long id;
    private String generalTemplate;
    private String boardTemplate;
    @OneToOne
    @JsonBackReference
    private Profile profile;
}
