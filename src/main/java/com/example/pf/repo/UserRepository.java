package com.example.pf.repo;


import com.example.pf.entities.Pharmacie;
import com.example.pf.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(" select u from User u " + " where u.username = ?1")
    Optional<User> findUserWithName(String username);

    @Query("select p from User u, Pharmacie p " + " where p.user = u and u.username = ?1 ")
    List<Pharmacie> findPharmaciesWithUserName(String username);

    User findByUsername(String username);
    User findByEmail(String email);
}