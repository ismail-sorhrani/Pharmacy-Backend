package com.example.pf.repo;

import com.example.pf.entities.Ville;
import com.example.pf.entities.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Integer> {
    Zone findById(int id);
    @Query("select z.nom as nom, count(*) as nbr from Zone z, Pharmacie p where p.zone = z group by z.nom")
    Collection<?> findPharmacies();

    Integer countZoneByVille(Ville ville);

    List<Zone> findZoneByVille(Ville ville);
    @Query("select count(p) from Zone p")
    long getZoneCount();
}

