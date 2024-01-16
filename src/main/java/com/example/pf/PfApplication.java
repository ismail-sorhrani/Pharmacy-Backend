package com.example.pf;

import com.example.pf.entities.*;
import com.example.pf.repo.GardeRepository;
import com.example.pf.repo.PharmacieRepository;
import com.example.pf.repo.UserRepository;
import com.example.pf.repo.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootApplication
public class PfApplication {

    public static void main(String[] args) {
        SpringApplication.run(PfApplication.class, args);
    }
//  @Autowired
//    PharmacieRepository pharmacieRepository;
//
//    @Autowired
//    ZoneRepository zoneRepository;
//    @Bean
//    CommandLineRunner init() {
//        return args -> {
//            List<Pharmacie> pharmacie=pharmacieRepository.findPharmacieByZone(zoneRepository.findById(13));
//            System.out.println(pharmacie);
//        };
//    }

    @Autowired
    UserRepository userRepository;
    @Autowired
    GardeRepository gardeRepository;
    @Bean
    CommandLineRunner init() {
        return args -> {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User user = new User();
            user.setUsername("admin@admin");
            user.setEmail("admin@findpharma.com");
            user.setRole(Role.ADMIN);
            user.setPassword(passwordEncoder.encode("admin@admin"));
            Garde garde1 = new Garde();
            garde1.setType(GardeType.J);
            Garde garde2 = new Garde();
            garde2.setType(GardeType.N);
            List<Garde> gardes=gardeRepository.findAll();
            try{
                userRepository.save(user);
            }catch (Exception e){
                System.out.println("user exeist");
            }

            try{
                if (gardes.isEmpty()){
                    gardeRepository.save(garde1);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                if(gardes.isEmpty()){
                    gardeRepository.save(garde2);
                }

            }catch (Exception e){
                e.printStackTrace();
            }


        };
    }
}
