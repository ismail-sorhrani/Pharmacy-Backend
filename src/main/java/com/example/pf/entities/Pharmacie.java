package com.example.pf.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Pharmacie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    private String adresse;
    private double lat, log;

    @Enumerated(EnumType.ORDINAL)
    private PharmacieStatus status = PharmacieStatus.ENATTEND;
    @ManyToOne
    private Zone zone;

    @OneToMany(mappedBy = "pharmacie", fetch = FetchType.EAGER)
    private List<PharmacieGarde> gardes;



    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}
