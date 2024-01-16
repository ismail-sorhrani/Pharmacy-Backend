package com.example.pf.entities;


import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
     *
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  userId;
    private String username;
    @Column(unique = true)
    private String email;
    private String password;

    @OneToOne(fetch = FetchType.EAGER)
    private Pharmacie pharmacies;

    @Enumerated(EnumType.STRING)
    private Role role;





}