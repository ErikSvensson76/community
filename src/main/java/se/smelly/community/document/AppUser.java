package se.smelly.community.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import se.smelly.community.security.Role;

import java.time.LocalDate;
import java.util.Objects;

@Document
public class AppUser {

    @Id
    private String id;
    @Indexed
    private String email;
    @Indexed
    private Role role = Role.USER;
    private String firstName;
    private String lastName;
    @Indexed
    private LocalDate regDate;
    @JsonIgnore
    private String password;
    @Indexed
    private boolean active = true;

    @PersistenceConstructor
    public AppUser(String id, String email, Role role, String firstName, String lastName, LocalDate regDate, String password) {
        this(email, role, firstName, lastName, regDate, password);
        this.id = id;
    }

    public AppUser(String email, Role role, String firstName, String lastName, LocalDate regDate, String password) {
        this(email, firstName, lastName, password);
        this.role = role;
        this.regDate = regDate;
    }

    public AppUser(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.regDate = LocalDate.now();
    }

    protected AppUser(){}

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getRegDate() {
        return regDate;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public boolean isActive() {return active;}

    public void setActive(boolean active) {this.active = active;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(id, appUser.id) &&
                email.equals(appUser.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", regDate=" + regDate +
                '}';
    }
}
