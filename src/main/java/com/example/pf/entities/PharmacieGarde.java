package com.example.pf.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacieGarde {
    @EmbeddedId
    private PharmacieGardePK pk;

    @Temporal(TemporalType.DATE)
    @Column( insertable = false, updatable = false)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    @Column( insertable = false, updatable = false)
    private Date dateFin;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "pharmacieId", insertable = false, updatable = false)
    private Pharmacie pharmacie;

    @ManyToOne
    @JoinColumn(name = "gardeId", insertable = false, updatable = false)
    private Garde garde;
}