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


    public static class Builder{
        private String id;
        private String email;
        private Role role;
        private String firstName;
        private String lastName;
        private LocalDate regDate;
        private String password;
        private boolean active;

        public Builder(LocalDate regDate){
            this.active = true;
            this.role = Role.USER;
            this.regDate = regDate;
        }
        public Builder withEmail(String email){
            this.email = email;
            return this;
        }
        public Builder asRole(Role role){
            this.role = role;
            return this;
        }
        public Builder firstName(String firstName){
            this.firstName = firstName;
            return this;
        }
        public Builder lastName(String lastName){
            this.lastName = lastName;
            return this;
        }
        public Builder password(String password){
            this.password = password;
            return this;
        }
        public Builder setActiveStatus(boolean status){
            this.active = status;
            return this;
        }

        public Builder setId(String id){
            this.id = id;
            return this;
        }

        public AppUser build(){
            AppUser user = new AppUser(id, email, role, firstName, lastName, regDate, password, active);
            return user;
        }


    }

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
    private boolean active;


    public AppUser(String id, String email, Role role, String firstName, String lastName, LocalDate regDate, String password, boolean active) {
        this(email, role, firstName, lastName, regDate, password, active);
        this.id = id;
    }

    public AppUser(String email, Role role, String firstName, String lastName, LocalDate regDate, String password, boolean active) {
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
        this.active = active;
        setRole(role);
        this.regDate = regDate;
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
