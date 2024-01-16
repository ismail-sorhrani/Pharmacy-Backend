package com.example.pf.controller;

import com.example.pf.entities.Pharmacie;
import com.example.pf.entities.PharmacieStatus;
import com.example.pf.entities.Zone;
import com.example.pf.repo.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("zones")
@CrossOrigin
public class ControllerMapZone {
    @Autowired
    private ZoneRepository repository;

    @GetMapping("/phByZone/{id}")
    public List<Pharmacie> findZone(@PathVariable(required = true) String id) {
        Zone s = repository.findById(Integer.parseInt(id));
        return s.getPharmacies();
    }
    @GetMapping("/pharmacies/{id}")
    public List<Pharmacie> findByZone(@PathVariable(required = true) String id) {
        Zone s = repository.findById(Integer.parseInt(id));
        List<Pharmacie> t = new ArrayList<>();
        for(Pharmacie p : s.getPharmacies() ) {
            if(p.getStatus()== PharmacieStatus.APPROVEE) {
                t.add(p);
            }
        }
        return t ;
    }


}
