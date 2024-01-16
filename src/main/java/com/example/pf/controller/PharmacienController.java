package com.example.pf.controller;

import com.example.pf.entities.*;
import com.example.pf.repo.*;
import com.example.pf.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/pharmacien")

public class PharmacienController {
    @Autowired
    private PharmacieRepository pharmacieRepository;
    @Autowired
    private ZoneRepository zoneRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    VilleRepository villeRepository;
    @Autowired
    PharmacieGardeRepository pharmacieGardeRepository;
    @Autowired
    GardeRepository gardeRepository;
    private User user1;

    @GetMapping("/pharmtest")
    public String Showpharmacy(Pharmacie pharmacie, Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);
        return "pharmacien";
    }

    @GetMapping("/show")
    public String pharmtest(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("villes", villeRepository.findAll());
        model.addAttribute("zones", zoneRepository.findAll());
        model.addAttribute("userdetail", userDetails);
        String s = userDetails.getUsername();
        System.out.println(s);
        int l = userRepository.findByUsername(s).getPharmacies().getId();
        Pharmacie pharmacie = pharmacieRepository.findById(l);
        System.out.println(pharmacie.getNom());
        byte[] photo = pharmacie.getImage();

        if (photo != null) {
            String encodedPhoto = Base64.getEncoder().encodeToString(photo);
            model.addAttribute("photo", encodedPhoto);
            System.out.println("encodedPhoto envoyer");
            System.out.println("#####################################");
            System.out.println(pharmacie.getStatus());
        }
        String pharmaciie = principal.getName();
        System.out.println("++++++++++pr++++++++");
        System.out.println(pharmaciie);
        int pharmacieID = userRepository.findByUsername(pharmaciie).getPharmacies().getId();
        List<PharmacieGarde> pharmacieGardesList=pharmacieGardeRepository.findPharmaciesGardeWithFutureEndDate(pharmacieID);
        model.addAttribute("pharmacie", pharmacie);
        if(pharmacieGardesList !=null) {
            for(PharmacieGarde ph :pharmacieGardesList) {
                model.addAttribute("dateFin", ph.getDateFin());
                return "pharmacien";
            }
        }

        return "pharmacien";
    }

    @GetMapping("/addg")
    public String showAddForm(Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            //model.addAttribute("gardes", pharmacieGardeRepository);
            model.addAttribute("gardestypes", gardeRepository.findAll());
            model.addAttribute("mode", "add");
            model.addAttribute("userdetail", userDetails);
            model.addAttribute("pharmaciegarde", new PharmacieGarde());
        }
        return "garde";

    }

    @PostMapping("/addgrade")
    public String savegarde(@RequestParam("gardtype") int garde, @RequestParam("dateDebut") String dateDebut,
                            @RequestParam("dateFin") String dateFin,
                            Model model, Authentication authentication,Principal principal) throws ParseException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = dateFormat.parse(dateDebut);
        Date date2 = dateFormat.parse(dateFin);
        Date dateSysteme =new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateSysteme);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = calendar.getTime();
        String pharmaciie = principal.getName();
        System.out.println("++++++++++pr++++++++");
        System.out.println(pharmaciie);
        int pharmacieID = userRepository.findByUsername(pharmaciie).getPharmacies().getId();
        List <PharmacieGarde> pharmacieGardesList=pharmacieGardeRepository.findPharmaciesGardeWithFutureEndDate(pharmacieID);
        System.out.println("cest Bien");
        if(date1.before(yesterday)|| date2.before(date1)){


            model.addAttribute("gardes", pharmacieGardeRepository.findPharmaciesGardeWithFutureEndDate(pharmacieID));
            model.addAttribute("userdetail", userDetails);
            System.out.println("date1 est inférieure à la date actuelle");
            model.addAttribute("additionFailed","inferieur");
            if(date1.before(dateSysteme)) {
                model.addAttribute("additionFailed", "inferieur");
            }
            if(date2.before(date1)) {
                model.addAttribute("additionFailed", "verifier");
            }


            return "garde";
        }

        if(pharmacieGardesList !=null){
            for(PharmacieGarde ph :pharmacieGardesList){
                if(date1.before(ph.getDateFin())){

                    model.addAttribute("gardes", pharmacieGardeRepository.findPharmaciesGardeWithFutureEndDate(pharmacieID));
                    model.addAttribute("userdetail", userDetails);
                    model.addAttribute("additionFailed","exit");
                    System.out.println("deja en garde");
                    return "garde";
                }
                if(date2.before(date1)){
                    model.addAttribute("gardes", pharmacieGardeRepository.findPharmaciesGardeWithFutureEndDate(pharmacieID));
                    model.addAttribute("userdetail", userDetails);
                    model.addAttribute("additionFailed","verifier");
                    System.out.println("date de fin infeerieur date debut verifier!!!!");
                    return "garde";
                  }
            }
        }

        Garde garde1 = gardeRepository.findById(garde);
        Pharmacie pharmacie = userDetails.getPharmacies();
        PharmacieGardePK pk = new PharmacieGardePK();
        pk.setDateDebut(date1);
        pk.setDateFin(date2);
        pk.setGardeId(garde);
        pk.setPharmacieId(pharmacie.getId());
        PharmacieGarde pharmacieGarde = new PharmacieGarde(pk, date1, date2, pharmacie, garde1);
        pharmacieGardeRepository.save(pharmacieGarde);
        return "redirect:/pharmacien/garde";
    }

    @GetMapping("/garde")
    public String showGard(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        String pharmacie = principal.getName();
        System.out.println("++++++++++pr++++++++");
        System.out.println(pharmacie);
        int pharmacieID = userRepository.findByUsername(pharmacie).getPharmacies().getId();
        model.addAttribute("gardes", pharmacieGardeRepository.findPharmaciesGardeWithFutureEndDate(pharmacieID));
        model.addAttribute("userdetail", userDetails);
        return "garde";
    }


    @PostMapping("/update/{id}")
    public String update(@RequestParam("zone") Zone zone, @RequestParam("file") MultipartFile image, Model model, Pharmacie pharmacie, @PathVariable int id) {


        Pharmacie pharmacie1 = pharmacieRepository.findById(id);

        pharmacie1.setAdresse(pharmacie.getAdresse());
        pharmacie1.setLat(pharmacie.getLat());
        pharmacie1.setLog(pharmacie.getLog());
        pharmacie1.setNom(pharmacie.getNom());
        pharmacie1.setZone(zone);
        pharmacie1.setZone(pharmacie.getZone());

        if (image != null && !image.isEmpty()) {
            try {
                byte[] photoBytes = image.getBytes();
                pharmacie1.setImage(photoBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pharmacieRepository.save(pharmacie1);
        return "redirect:/pharmacien/show";
    }

    @GetMapping("/historique")
    public String historique(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        String pharmacie = principal.getName();
        System.out.println("++++++++++pr++++++++");
        System.out.println(pharmacie);
        int pharmacieID = userRepository.findByUsername(pharmacie).getPharmacies().getId();
        System.out.println(pharmacieID);
        model.addAttribute("gardes", pharmacieGardeRepository.findByPharmacie_Id(pharmacieID));
        model.addAttribute("userdetail", userDetails);
        return "historique";
    }
    @GetMapping("/byCity")
    public ResponseEntity<List<Zone>> getZonesByCity(@RequestParam int cityId) {
        List<Zone> zones = zoneRepository.findZoneByVille(villeRepository.findById(cityId));

        if (zones.isEmpty()) {
            System.out.println("####################000000");
            return ResponseEntity.noContent().build();
        }
        System.out.println("####################++++");
        System.out.println(zones.get(0).getNom());

        return ResponseEntity.ok(zones);
    }
}