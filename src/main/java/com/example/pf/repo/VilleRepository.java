package com.example.pf.repo;


import com.example.pf.entities.Ville;
import com.example.pf.entities.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
@Repository
public interface VilleRepository extends JpaRepository<Ville, Integer> {
    Ville findById(int id);
    @Query("SELECT z FROM Zone z, Ville v WHERE z.ville = v AND v.id = :id")
    List<Zone> getZones(@Param("id") int id);
    @Query("select v.nom as nom, count(*) as nbr from Ville v, Zone z, Pharmacie p where p.zone = z and z.ville = v group by v.nom")
    Collection<?> findPharmacies();


    @Query("select count(p) from Ville p")
    long getVilleCount();


}
