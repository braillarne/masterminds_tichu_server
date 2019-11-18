package rocks.process.acrm.data.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
public class Profile {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    @Id
    @GeneratedValue
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private boolean isGuest;
    @ManyToMany(mappedBy = "profile")
    private List<Result> results;
    @OneToOne
    @JsonBackReference
    private Setting setting;
}
