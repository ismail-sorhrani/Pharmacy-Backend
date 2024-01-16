package com.example.pf.repo;

import com.example.pf.entities.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {

    ForgotPassword findByToken(String token);

}
