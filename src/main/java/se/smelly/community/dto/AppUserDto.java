package se.smelly.community.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import se.smelly.community.security.Role;

import java.time.LocalDate;
import java.util.Objects;

public class AppUserDto {

    private String id;
    private String email;
    private Role role;
    private String firstName;
    private String lastName;
    private LocalDate regDate;
    private boolean active;
    private String password;

    public AppUserDto(){}

    public AppUserDto(String id, String email, Role role, String firstName, String lastName, LocalDate regDate, boolean active, String password) {
        setId(id);
        setEmail(email);
        setRole(role);
        setFirstName(firstName);
        setLastName(lastName);
        setRegDate(regDate);
        setActive(active);
        setPassword(password);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @JsonSetter
    public void setPassword(String password){
        this.password = password;
    }

    @JsonIgnore
    public String getPassword(){
        return this.password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUserDto that = (AppUserDto) o;
        return active == that.active &&
                Objects.equals(id, that.id) &&
                Objects.equals(email, that.email) &&
                role == that.role &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(regDate, that.regDate) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, role, firstName, lastName, regDate, active, password);
    }
}
