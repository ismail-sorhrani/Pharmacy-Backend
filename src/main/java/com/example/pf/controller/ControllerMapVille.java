package com.example.pf.controller;

import com.example.pf.entities.Ville;
import com.example.pf.entities.Zone;
import com.example.pf.repo.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("villes")
@CrossOrigin
public class ControllerMapVille {

    @Autowired
    private VilleRepository repository;

    @PostMapping("/save")
    public void save(@RequestBody Ville Ville) {
        repository.save(Ville);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable(required = true) String id) {
        Ville s = repository.findById(Integer.parseInt(id));
        repository.delete(s);
    }

    @GetMapping("/all")
    public List<Ville> findAll() {
        return repository.findAll();
    }

    @GetMapping(value = "/count")
    public long countVille() {
        return repository.count();
    }

    @GetMapping("/zonesbyville/{id}")
    public List<Zone> findZone(@PathVariable(required = true) String id) {
        Ville s = repository.findById(Integer.parseInt(id));
        return s.getZones();
    }

    @GetMapping("/zones/{id}")
    public List<Zone> findByZone(@PathVariable(required = true) String id) {
        Ville s = repository.findById(Integer.parseInt(id));
        return s.getZones();
    }

//    @GetMapping("/NbrPharmacie")
//    public List findNbrPharmacieVille(){
//        return repository.findNbrPharmacieVille();
//    }
//
//    @GetMapping("/NbrPharmacieZ")
//    public List getCountPharmacieByVille(){
//        return repository.countpharmacyByZone();
//    }

}
