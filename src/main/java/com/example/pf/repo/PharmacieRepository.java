package com.example.pf.repo;

import com.example.pf.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface PharmacieRepository extends JpaRepository<Pharmacie, Integer> {
    Pharmacie findById(int id);
    Pharmacie findByNom(String nom);
   List<Pharmacie> findPharmacieByZone(Zone zone);

    @Query("select Distinct p from Pharmacie p, PharmacieGarde pg Where pg.pharmacie = p AND pg.dateDebut > :date1 AND pg.dateFin < :date2 and p.status = 1")
    List<Pharmacie> getPharmaciesByDate(@Param("date1") Date date1, @Param("date2") Date date2);

    @Query("select Distinct p from Pharmacie p, PharmacieGarde pg Where pg.pharmacie = p AND (pg.garde.type = :J or pg.garde.type = :N) AND p.status = 1")
    List<Pharmacie> getPharmaciesByGarde(@Param("J") GardeType J, @Param("N") GardeType N);
    @Query("select Distinct p from Pharmacie p, PharmacieGarde pg Where pg.pharmacie = p AND (pg.garde.type = :J ) AND p.status = 1")
    List<Pharmacie> getPharmaciesByGardeJour(@Param("J") GardeType J);
    @Query("select p from Pharmacie p, Zone z Where p.zone = z AND z = :zone AND p.status = 1")
    List<Pharmacie> getPharmaciesByZone(@Param("zone") Zone zone);

 @Query("select p from Pharmacie p where p.zone.ville.id =:id")
 List<Pharmacie> findAllPharmacieByVille(@Param("id") int id);
    @Query("select count(p) from Pharmacie p where p.zone.ville.id =:id")
    long CountPharmacieByVille(@Param("id") int id);
 @Query("select pg.pharmacie from PharmacieGarde pg where  pg.garde.id = :id and pg.pharmacie.zone.id = :zone_id")
 List<Pharmacie> findAllPharmacieEnGardeByGardeId(@Param("id") int id,@Param("zone_id") int zone_id);


 @Query("select p from Pharmacie p, Zone z, User u Where p.zone = z AND z = :zone AND p.user = u AND u.username = :username AND p.status = 1")
    List<Pharmacie> getPharmaciesByZoneAndUser(@Param("zone") Zone zone,@Param("username") String username);



    @Query("select Distinct p from Pharmacie p, PharmacieGarde pg Where pg.pharmacie = p AND pg.dateDebut > :date1 AND pg.dateFin < :date2 AND (pg.garde.type = :J or pg.garde.type = :N) and p.status = 1")
    List<Pharmacie> getPharmaciesByDateAndGarde(@Param("date1") Date date1, @Param("date2") Date date2,@Param("J") GardeType J, @Param("N") GardeType N);


    @Query("select Distinct p from Pharmacie p, PharmacieGarde pg, Zone z "
            + "Where p.zone = z AND z = :zone "
            + "AND pg.pharmacie = p  "
            + "AND (pg.garde.type = :J or pg.garde.type = :N) "
            + "AND p.status = 1")
    List<Pharmacie> getPharmaciesByZoneAndGarde(@Param("zone") Zone zone, @Param("J") GardeType J, @Param("N") GardeType N);

    @Query("SELECT DISTINCT p FROM Pharmacie p, PharmacieGarde pg, Zone z "
            + "WHERE p.zone = z AND z = :zone "
            + "AND pg.pharmacie = p "
            + "AND (pg.garde.type = :J ) "
            + "AND p.status = :APPROVEE "
            )
    List<Pharmacie> getPharmaciesByZoneAndGardeJour(@Param("zone") Zone zone, @Param("J") GardeType J, @Param("APPROVEE") PharmacieStatus APPROVEE);

    @Query("select Distinct p from Pharmacie p, PharmacieGarde pg, Zone z, Ville v "
            + "Where p.zone = z AND v = z.ville "
            + "AND v = :ville "
            + "AND pg.pharmacie = p "
            + "AND (pg.garde.type = :J or pg.garde.type = :N) "
            + "AND p.status = 1")
    List<Pharmacie> getPharmaciesByVilleAndGarde(@Param("ville") Ville ville, @Param("J") GardeType J, @Param("N") GardeType N);

    @Query("select Distinct count(p) from Pharmacie p, PharmacieGarde pg, Zone z, Ville v "
            + "Where p.zone = z AND v = z.ville "
            + "AND v = :ville "
            + "AND pg.pharmacie = p "
            + "AND (pg.garde.type = :J or pg.garde.type = :N) "
            + "AND p.status = 1")
long getCountPharmaciesByVilleAndGarde(@Param("ville") Ville ville, @Param("J") GardeType J, @Param("N") GardeType N);









    @Query("select p from Pharmacie p, Zone z, Ville v Where p.zone = z AND z.ville = v AND v = :ville AND p.status = 1")
    List<Pharmacie> getPharmaciesByVille(@Param("ville") Ville ville);

    @Query("select p from Pharmacie p Where p.status = 1")
    List<Pharmacie> getApprovees();

    @Query("select count(p) from Pharmacie p Where p.status = 1")
    long getCountApprovees();
    @Query("select p from Pharmacie p Where p.status = 2")
    List<Pharmacie> getRefusee();
    @Query("select count(p) from Pharmacie p Where p.status = 2")
    long getCountRefusee();
    @Query("select p from Pharmacie p Where p.status = 0")
    List<Pharmacie> getEnAttend();

    @Query("select count(p) from Pharmacie p Where p.status = 0")
    long getEnAttendCount();

    @Query("select count(p) from Pharmacie p")
    long getPharmacieCount();


    @Query("select p from Pharmacie p Where (p.log - :log) >= (-1* :threshold) AND (p.log - :log) <= :threshold AND (p.lat - :lat) <= :threshold AND (p.lat - :lat) >= (-1* :threshold) AND p.status = 1")
    List<Pharmacie> nearby(@Param("log") double log, @Param("lat") double lat, @Param("threshold") double threshold);
}
