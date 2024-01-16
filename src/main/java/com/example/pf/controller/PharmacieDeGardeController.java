package com.example.pf.controller;

import com.example.pf.entities.*;
import com.example.pf.repo.PharmacieGardeRepository;
import com.example.pf.repo.PharmacieRepository;
import com.example.pf.repo.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("pharmacieDeGardes")
@CrossOrigin
public class PharmacieDeGardeController {

    @Autowired
    private PharmacieGardeRepository repository;
    @Autowired
    private PharmacieRepository repository1;
    @Autowired
    private ZoneRepository repository2;

    @GetMapping("/getActual/jour/{id}")
    public List<Pharmacie> getActualJour(@PathVariable(required = true) String id) {
      Zone s = repository2.findById(Integer.parseInt(id));
        List<Pharmacie> tmp=repository1.getPharmaciesByZoneAndGardeJour(s,GardeType.J,PharmacieStatus.APPROVEE);

        return tmp;
    }


    @GetMapping("/getActual/nuit/{id}")
    public List<Pharmacie> getActualNuit(@PathVariable(required = true) String id) {
        Zone s = repository2.findById(Integer.parseInt(id));
        List<Pharmacie> tmp=repository1.getPharmaciesByZoneAndGardeJour(s,GardeType.N,PharmacieStatus.APPROVEE);

        return tmp;
    }





}