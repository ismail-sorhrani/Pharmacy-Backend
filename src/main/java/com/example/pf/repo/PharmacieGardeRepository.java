package com.example.pf.repo;


import com.example.pf.entities.Garde;
import com.example.pf.entities.GardeType;
import com.example.pf.entities.PharmacieGarde;
import com.example.pf.entities.PharmacieGardePK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.util.Date;
import java.util.List;
@Repository
public interface PharmacieGardeRepository extends JpaRepository<PharmacieGarde, PharmacieGardePK> {
    PharmacieGarde findOneByPk(PharmacieGardePK pk);
    @Query("select pg from User u, Pharmacie p, PharmacieGarde pg " + " where pg.pharmacie=p and p.user = u and u.username = ?1 ")
    List<PharmacieGarde> user(String username);

    @Query("SELECT pg FROM PharmacieGarde pg WHERE pg.dateFin > CURRENT_DATE AND pg.pharmacie.id=:id")
    List<PharmacieGarde> findPharmaciesGardeWithFutureEndDate(int id);
    List<PharmacieGarde> findByPharmacie_Id(int id);

    List<PharmacieGarde> findPharmacieGardeByGarde_Type(GardeType type);
    List<PharmacieGarde> findPharmacieGardesByDateFinBefore(Date date);
    List<PharmacieGarde> findPharmacieGardesByDateDebutAfter(Date date);
}
