package com.example.pf.service;


import com.example.pf.entities.Pharmacie;
import com.example.pf.entities.Role;
import com.example.pf.repo.PharmacieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {


    private String username;
    private String email;
    private String password;

    private Collection<? extends GrantedAuthority> authorities;
    private Pharmacie pharmacies;
    private Role role;




    public CustomUserDetails(String username, String email,String password, Collection<? extends GrantedAuthority> authorities,
                             Pharmacie pharmacie,Role role) {

        this.username = username;
        this.email=email;
        this.password = password;
        this.authorities = authorities;
        this.pharmacies=pharmacie;
        this.role=role;


    }





    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;
    }

    @Override
    public String getPassword() {

        return password;
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

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setPharmacies(Pharmacie pharmacies) {
        this.pharmacies = pharmacies;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public Pharmacie getPharmacies() {
        return pharmacies;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

}
