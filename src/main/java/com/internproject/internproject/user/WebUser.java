package com.internproject.internproject.user;

import jakarta.validation.constraints.NotNull;

public class WebUser {

    @NotNull(message = "is required")
    private String username;

    @NotNull(message = "is required")
    private String password;

    @NotNull(message = "is required")
    private String email;

    @NotNull(message = "is required")
    private String firstName;

    @NotNull(message = "is required")
    private String lastName;

    public WebUser(){}

    public WebUser(String username, String password, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
