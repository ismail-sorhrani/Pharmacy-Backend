package com.example.pf.dto;

import com.example.pf.entities.Pharmacie;
import com.example.pf.entities.Role;

public class UserDto {
    private String username;
    private String email;
    private String password;
    private Pharmacie pharmacies;
    private Role role;

    public UserDto(String username, String email, String password, Pharmacie pharmacies, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.pharmacies = pharmacies;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Pharmacie getPharmacies() {
        return pharmacies;
    }

    public Role getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPharmacies(Pharmacie pharmacies) {
        this.pharmacies = pharmacies;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", pharmacies=" + pharmacies +
                ", role=" + role +
                '}';
    }
}
