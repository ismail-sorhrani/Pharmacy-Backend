package com.example.pf.controller;

import com.example.pf.entities.Pharmacie;
import com.example.pf.repo.PharmacieRepository;
import com.example.pf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pharmacies")
@CrossOrigin
public class ControllerMapPharmacie {

    @Autowired
    private PharmacieRepository repository;







    @GetMapping("/all")
    public List<Pharmacie> findAll() {
        return repository.findAll();
    }


    @GetMapping(value = "/count")
    public long countPharmacie() {
        return repository.count();
    }

    @GetMapping("/byId/{id}")
    public Pharmacie getPharmaciebyID(@PathVariable String id) {
        return repository.findById(Integer.parseInt(id));

    }
    @GetMapping("/pharmacie/ville={id}")
    public List<Pharmacie> findPharmacieByVille(@PathVariable int id){
        return repository.findAllPharmacieByVille(id);
    }




    @GetMapping("/gardeAndZoneId/{id}/{zone_id}")
    public List<Pharmacie> getPharmaciebyGardeAndZone(@PathVariable int id,@PathVariable int zone_id) {
        return repository.findAllPharmacieEnGardeByGardeId(id,zone_id);

    }

}
