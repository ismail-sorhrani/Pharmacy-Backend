package com.example.pf.controller;

import com.example.pf.entities.Garde;
import com.example.pf.repo.GardeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("gardes")
@CrossOrigin
public class GardeMapController {
    @Autowired
    private GardeRepository repository;

    @GetMapping("/all")
    public List<Garde> findAll() {
        return repository.findAll();
    }
}
