package com.example.pf.controller;

import com.example.pf.entities.*;
import com.example.pf.repo.*;
import com.example.pf.service.CustomUserDetails;
import com.example.pf.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

        @Autowired
        private UserDetailsService userDetailsService;
        @Autowired
        private VilleRepository villeRepository;
        @Autowired
        private ZoneRepository zoneRepository;
    @Autowired
    private UserRepository userRepository;
     @Autowired
    private PharmacieRepository pharmacieRepository;
     @Autowired
     private GardeRepository gardeRepository;
    @Autowired
    private PharmacieGardeRepository pharmacieGardeRepository;




        private UserService userService;



        public AdminController(UserService userService) {

            this.userService = userService;
        }

    @GetMapping
    public String admin(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail" , userDetails);
        return "redirect:/admin/barChart";
    }
    @GetMapping("/adming")
    public String adming(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail" , userDetails);
        User admin = userRepository.findByUsername(userDetails.getUsername());

        List<PharmacieGarde> pharmacies = pharmacieGardeRepository.findAll();
//        System.out.println("------------------------");
////        for(PharmacieGarde ph:pharmacies){
////        System.out.println(ph.getPharmacie().getNom());
////        }
//        //pharmacieGardeRepository.delete();
//        List<PharmacieGarde> pks=pharmacieGardeRepository.findByPharmacie_Id(7);
//        for(PharmacieGarde ph:pks){
//            pharmacieGardeRepository.delete(ph);
//
//        }
//
//        System.out.println("------------------------");
        List<String> etats = new ArrayList<>();
        etats.add("ENATTEND");
        etats.add("APPROVEE");
        etats.add("REFUSEE");
        model.addAttribute("admin", admin);
        model.addAttribute("pharmacies", pharmacies);
        model.addAttribute("zones",zoneRepository.findAll());
        model.addAttribute("gardes",gardeRepository.findAll());
        model.addAttribute("etats",etats);
        return "gestiogarde";
    }

    @GetMapping("/gestiongarde")
    public String gestiongarde(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail" , userDetails);

        model.addAttribute("gardes",pharmacieGardeRepository.findAll());
        model.addAttribute("gardess",gardeRepository.findAll());
        return "gestiogarde";
    }



@GetMapping("/ville")
    public String Showville(Ville ville,Model model,Authentication authentication){
    if(authentication != null && authentication.getPrincipal() instanceof UserDetails) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User admin = userRepository.findByUsername(userDetails.getUsername());

        model.addAttribute("admin", admin);
    }
    model.addAttribute("villes",villeRepository.findAll());

      return "ville";
    }


    @GetMapping("/prepadd")
    public String Showaddvilleform(Model model, Authentication authentication){
            if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
                CustomUserDetails userDetails =(CustomUserDetails) authentication.getPrincipal();
                User admin= userRepository.findByUsername(userDetails.getUsername());
                model.addAttribute("villes",villeRepository.findAll());
                model.addAttribute("admin",admin);
                model.addAttribute("ville",new Ville());
                model.addAttribute("mode","add");

            }

        return "ville";
    }

    @GetMapping("/editprepa/{id}")
    public String editVille(@PathVariable("id") int id,Model model,Authentication authentication){
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User admin= userRepository.findByUsername(userDetails.getUsername());

            model.addAttribute("admin", admin);
        }

        Ville ville = villeRepository.findById(id);

        model.addAttribute("villes", villeRepository.findAll());

        model.addAttribute("ville", ville);
        model.addAttribute("mode", "update");
            return "ville";
    }
    @PostMapping("/addville")
    public String addVille(Ville ville, BindingResult result, Model model,Authentication authentication){
        if(result.hasErrors()){
            return"ville";
        }
        villeRepository.save(ville);
        model.addAttribute("villes",villeRepository.findAll());
        return "redirect:/admin/ville";
    }

    @PostMapping("/updateville/{id}")
    public String updateVille(@PathVariable("id") int id,Ville ville,BindingResult result,Model model,Authentication authentication){
        if (result.hasErrors()){
            ville.setId(id);
            return "editville";
        }
        Ville ville1=villeRepository.findById(id);
        if(ville != null){
            ville1.setNom(ville.getNom());
        }
        villeRepository.save(ville1);
        model.addAttribute("villes",villeRepository.findAll());
        return "redirect:/admin/ville";
    }

    @GetMapping("/deleteville/{id}")
    public String deletUser(@PathVariable("id") int id,Model model,Authentication authentication){
       try {
           if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
               CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
               User admin= userRepository.findByUsername(userDetails.getUsername());

               model.addAttribute("admin", admin);
           }
           Ville ville1= villeRepository.findById(id);
           villeRepository.delete(ville1);
           model.addAttribute("villes",villeRepository.findAll());
           return "redirect:/admin/ville";
       }catch(Exception e){
           e.printStackTrace();
           return "error 50z";
       }

    }
    @GetMapping("/deletepharmacie/{id}")
    public String deletePharmacie(@PathVariable("id") int id,Model model,Authentication authentication){
        try {
            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                User admin= userRepository.findByUsername(userDetails.getUsername());

                model.addAttribute("admin", admin);
            }
            Pharmacie pharmacie= pharmacieRepository.findById(id);
            pharmacieRepository.delete(pharmacie);
            model.addAttribute("pharmacies",pharmacieRepository.findAll());
            return "redirect:/admin/pharmacie";
        }catch(Exception e){
            e.printStackTrace();
            return "error 50";
        }

    }
    @GetMapping("/zone")
    public String Showzone(Zone zone, Model model,Authentication authentication){
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User admin= userRepository.findByUsername(userDetails.getUsername());
            model.addAttribute("admin", admin);
        }
        List<Zone> zones = zoneRepository.findAll();
        if (zones != null){
            model.addAttribute("zones",zones);
        }
        model.addAttribute("villes",villeRepository.findAll());
        return "zone";
    }
    @GetMapping("/addz")
    public String Showaddzonee(Model model,Authentication authentication){
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User admin= userRepository.findByUsername(userDetails.getUsername());
            model.addAttribute("admin", admin);
            List<Zone> zones = zoneRepository.findAll();
            if (zones != null){
                model.addAttribute("zones",zones);
            }
            model.addAttribute("villes",villeRepository.findAll());
            model.addAttribute("mode","add");
            model.addAttribute("zone",new Zone());
        }

        return "zone";

    }

    @GetMapping("/addgardetype")
    public String showaddgardetype(Model model,Authentication authentication){
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User userdetail= userRepository.findByUsername(userDetails.getUsername());
            model.addAttribute("userdetail", userdetail);
            List<Garde> gardes = gardeRepository.findAll();
            if (gardes != null){
                model.addAttribute("gardess",gardes);
            }

            model.addAttribute("mode","add");
            model.addAttribute("garde",new Garde());
        }

        return "gestiogarde";

    }
@GetMapping("/zoneByVille")
    public String zoneByVille(@RequestParam("ville") Ville ville,Model model ,Authentication authentication){
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User admin= userRepository.findByUsername(userDetails.getUsername());
        model.addAttribute("admin", admin);
    }

    model.addAttribute("villes",villeRepository.findAll());
      List<Zone> zones = zoneRepository.findZoneByVille(ville);
      model.addAttribute("zones",zones);
            return "zone";
    }

    @GetMapping("/editzoneprepa/{id}")
    public String editzonepreapre(@PathVariable("id") int id,Model model ,Authentication authentication){
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User admin= userRepository.findByUsername(userDetails.getUsername());
            model.addAttribute("admin", admin);
        }
       Zone zone=zoneRepository.findById(id);
        System.out.println(zone.getNom());
        model.addAttribute("villes",villeRepository.findAll());
        model.addAttribute("zones",zoneRepository.findAll());
       model.addAttribute("zone",zone);
        model.addAttribute("mode","update");

        return "zone";
    }

    @PostMapping("/addzone")
    public String addZonee(Zone zone,Model model,Authentication authentication){
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User admin= userRepository.findByUsername(userDetails.getUsername());
            model.addAttribute("admin", admin);
        }

        zoneRepository.save(zone);
        model.addAttribute("zones",zoneRepository.findAll());
        model.addAttribute("villes",villeRepository.findAll());
        return "redirect:/admin/zone";
    }

    @PostMapping("/addgarde")
    public String addgarde(Garde garde,Model model,Authentication authentication){
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User userdetail= userRepository.findByUsername(userDetails.getUsername());
            model.addAttribute("userdetail", userdetail);
        }

       gardeRepository.save(garde);
        model.addAttribute("gardess",gardeRepository.findAll());

        return "redirect:/admin/gestiongarde";
    }


    @PostMapping("/updatezone/{id}")
    public String updateZone(@PathVariable("id") int id,Zone zone,Model model,@RequestParam("ville") Ville ville,Authentication authentication){
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User admin= userRepository.findByUsername(userDetails.getUsername());
            model.addAttribute("admin", admin);

        }
         zone.setId(id);

        System.out.println(ville.getNom());
        zone.setVille(ville);

        zoneRepository.save(zone);
        model.addAttribute("zones",zoneRepository.findAll());
        return "redirect:/admin/zone";
    }


    @GetMapping("/pharmacie")
    public String pharmacie(Pharmacie pharmacie, Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User admin = userRepository.findByUsername(userDetails.getUsername());

            List<Pharmacie> pharmacies = pharmacieRepository.findAll();
            List<String> encodedPhotos = new ArrayList<>();
            for (Pharmacie stu :pharmacies ) {
                byte[] photo1 = stu.getImage();
                if (photo1 != null && photo1.length > 0) {
                    String encodedPhot = Base64.getEncoder().encodeToString(photo1);
                    encodedPhotos.add(encodedPhot);
                } else {
                    encodedPhotos.add("");
                }
            }
            List<String> etats = new ArrayList<>();
            etats.add("ENATTEND");
            etats.add("APPROVEE");
            etats.add("REFUSEE");
            model.addAttribute("encodedPhotos", encodedPhotos);
            model.addAttribute("admin", admin);
            model.addAttribute("pharmacies", pharmacies);
            model.addAttribute("zones",zoneRepository.findAll());
            model.addAttribute("gardes",gardeRepository.findAll());
            model.addAttribute("etats",etats);
            model.addAttribute("villes",villeRepository.findAll());
        }
        return "pharmacie";
        }


    @GetMapping("/pharmacieByZone")
    public String studentByGroupe(@RequestParam("zone") Zone zone, Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User admin = userRepository.findByUsername(userDetails.getUsername());
            model.addAttribute("admin", admin);
            List<Pharmacie> pharmacies = pharmacieRepository.getPharmaciesByZone(zone);
            model.addAttribute("pharmacies", pharmacies);
            model.addAttribute("zones",zoneRepository.findAll()); model.addAttribute("villes",villeRepository.findAll());
            model.addAttribute("gardes",gardeRepository.findAll());

            List<String> etats = new ArrayList<>();
            etats.add("ENATTEND");
            etats.add("APPROVEE");
            etats.add("REFUSEE");
            model.addAttribute("etats",etats);
//            byte[] photo = prof1.getPhoto();

//            if (photo != null) {
//                String encodedPhoto = Base64.getEncoder().encodeToString(photo);
//                model.addAttribute("photo", encodedPhoto);
//            }
        }
            return "pharmacie";
        }

    @GetMapping("/pharmacieByEtat")
    public String pharmacieByEtat(@RequestParam("str") String status, Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User admin = userRepository.findByUsername(userDetails.getUsername());
            model.addAttribute("admin", admin);
            if (status.equalsIgnoreCase("REFUSEE")) {
                System.out.println("4151111111111111111111");
                List<Pharmacie> pharmacies = pharmacieRepository.getRefusee();
                model.addAttribute("pharmacies", pharmacies);


            }else if(status.equalsIgnoreCase("APPROVEE")){

                List<Pharmacie> pharmacies = pharmacieRepository.getApprovees();
                model.addAttribute("pharmacies", pharmacies);
            }else if(status.equalsIgnoreCase("ENATTEND")){
                List<Pharmacie> pharmacies = pharmacieRepository.getEnAttend();
                model.addAttribute("pharmacies", pharmacies);
            }
            List<String> etats = new ArrayList<>();
            etats.add("ENATTEND");
            etats.add("APPROVEE");
            etats.add("REFUSEE");

            model.addAttribute("zones",zoneRepository.findAll());
            model.addAttribute("etats",etats);
            model.addAttribute("gardes",gardeRepository.findAll());
            model.addAttribute("villes",villeRepository.findAll());
//            byte[] photo = prof1.getPhoto();

//            if (photo != null) {
//                String encodedPhoto = Base64.getEncoder().encodeToString(photo);
//                model.addAttribute("photo", encodedPhoto);
//            }
        }
        return "pharmacie";
    }

    @GetMapping("/pharmacieByGardeg")
    public String pharmacieByGardeg(@RequestParam("garde") Garde garde,Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User admin = userRepository.findByUsername(userDetails.getUsername());
            model.addAttribute("admin", admin);
//            List<PharmacieGarde> pharmacies = pharmacieGardeRepository.findAll();
            System.out.println(garde.getId());

            if(garde.getType()==GardeType.J){
                System.out.println("-----------------------------------------------");
                List<PharmacieGarde> pharmacies = pharmacieGardeRepository.findPharmacieGardeByGarde_Type(GardeType.J);
                model.addAttribute("pharmacies", pharmacies);
            } else if (garde.getType()==GardeType.N) {

                List<PharmacieGarde> pharmacies = pharmacieGardeRepository.findPharmacieGardeByGarde_Type(GardeType.N);
                model.addAttribute("pharmacies", pharmacies);
            }

            model.addAttribute("zones",zoneRepository.findAll());
            model.addAttribute("gardes",gardeRepository.findAll());
//            byte[] photo = prof1.getPhoto();

//            if (photo != null) {
//                String encodedPhoto = Base64.getEncoder().encodeToString(photo);
//                model.addAttribute("photo", encodedPhoto);
//            }
        }
        return "gestiogarde";
    }
    @GetMapping("/pharmacieByGardedate")
    public String pharmacieByGardedate(@RequestParam("startDate") String startDate, Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User admin = userRepository.findByUsername(userDetails.getUsername());
            model.addAttribute("admin", admin);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                // Parse the strings to Date objects
              Date startDatel = dateFormat.parse(startDate);
                List<PharmacieGarde> pharmacies = pharmacieGardeRepository.findPharmacieGardesByDateFinBefore(startDatel);
                // Now you have startDate and endDate as java.util.Date objects
                model.addAttribute("pharmacies", pharmacies);
                // Your controller logic here

            } catch (ParseException e) {
                // Handle parsing exception if necessary
                e.printStackTrace();
            }

//            System.out.println(garde.getId());
//
//            if(garde.getType()==GardeType.J){
//                System.out.println("-----------------------------------------------");
//                List<PharmacieGarde> pharmacies = pharmacieGardeRepository.findPharmacieGardeByGarde_Type(GardeType.J);
//                model.addAttribute("pharmacies", pharmacies);
//            } else if (garde.getType()==GardeType.N) {
//
//                List<PharmacieGarde> pharmacies = pharmacieGardeRepository.findPharmacieGardeByGarde_Type(GardeType.N);
//                model.addAttribute("pharmacies", pharmacies);
//            }

            model.addAttribute("zones",zoneRepository.findAll());
            model.addAttribute("gardes",gardeRepository.findAll());
//            byte[] photo = prof1.getPhoto();

//            if (photo != null) {
//                String encodedPhoto = Base64.getEncoder().encodeToString(photo);
//                model.addAttribute("photo", encodedPhoto);
//            }
        }
        return "gestiogarde";
    }


    @GetMapping("/delete/{id}/{id1}/{id2}/{id3}")
    public String deleteGarde(@PathVariable("id") int id,@PathVariable("id1") int id1
            ,@PathVariable("id2") String id2,@PathVariable("id3") String id3
            ,Model model, Authentication authentication,Principal principal) throws ParseException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail" , userDetails);
        System.out.println("SEEEEEEEEEEE     :"+id);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1=dateFormat.parse(id2);
        Date date2=dateFormat.parse(id3);
        PharmacieGardePK pharmacieGardePK =new PharmacieGardePK(id,id1,date1,date2);
        pharmacieGardeRepository.deleteById(pharmacieGardePK);
        System.out.println("SEEEEEEEEEEEooooooooooooooo      :"+id1);
        System.out.println("SEEEEEEEEEEE     :"+id2);
        //pharmacieGardeRepository.deleteById(pk);
        System.out.println("SEEEEEEEEEEEooooooooooooooo      :"+id3);
        return "redirect:/admin/adming";


        }

    @GetMapping("/pharmacieByGardedatestart")
    public String pharmacieByGardedatestart(@RequestParam("enddate") String startDate, Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User admin = userRepository.findByUsername(userDetails.getUsername());
            model.addAttribute("admin", admin);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                // Parse the strings to Date objects
                Date startDatel = dateFormat.parse(startDate);
                List<PharmacieGarde> pharmacies = pharmacieGardeRepository.findPharmacieGardesByDateDebutAfter(startDatel);
                // Now you have startDate and endDate as java.util.Date objects
                model.addAttribute("pharmacies", pharmacies);
                // Your controller logic here

            } catch (ParseException e) {
                // Handle parsing exception if necessary
                e.printStackTrace();
            }

//            System.out.println(garde.getId());
//
//            if(garde.getType()==GardeType.J){
//                System.out.println("-----------------------------------------------");
//                List<PharmacieGarde> pharmacies = pharmacieGardeRepository.findPharmacieGardeByGarde_Type(GardeType.J);
//                model.addAttribute("pharmacies", pharmacies);
//            } else if (garde.getType()==GardeType.N) {
//
//                List<PharmacieGarde> pharmacies = pharmacieGardeRepository.findPharmacieGardeByGarde_Type(GardeType.N);
//                model.addAttribute("pharmacies", pharmacies);
//            }

            model.addAttribute("zones",zoneRepository.findAll());
            model.addAttribute("gardes",gardeRepository.findAll());
//            byte[] photo = prof1.getPhoto();

//            if (photo != null) {
//                String encodedPhoto = Base64.getEncoder().encodeToString(photo);
//                model.addAttribute("photo", encodedPhoto);
//            }
        }
        return "gestiogarde";
    }

    @GetMapping("/pharmacieByGarde")
    public String pharmacieByGarde(@RequestParam("garde") Garde garde,Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User admin = userRepository.findByUsername(userDetails.getUsername());
            model.addAttribute("admin", admin);

            if(garde.getType()==GardeType.J){
                List<Pharmacie> pharmacies = pharmacieRepository.getPharmaciesByGardeJour(GardeType.J);
                model.addAttribute("pharmacies", pharmacies);
            } else if (garde.getType()==GardeType.N) {

                List<Pharmacie> pharmacies = pharmacieRepository.getPharmaciesByGardeJour(GardeType.N);
                model.addAttribute("pharmacies", pharmacies);
            }
            List<String> etats = new ArrayList<>();
            etats.add("ENATTEND");
            etats.add("APPROVEE");
            etats.add("REFUSEE");

            model.addAttribute("zones",zoneRepository.findAll());
            model.addAttribute("gardes",gardeRepository.findAll());
            model.addAttribute("etats",etats);
            model.addAttribute("villes",villeRepository.findAll());
//            byte[] photo = prof1.getPhoto();

//            if (photo != null) {
//                String encodedPhoto = Base64.getEncoder().encodeToString(photo);
//                model.addAttribute("photo", encodedPhoto);
//            }
        }
        return "pharmacie";
    }



    @PostMapping("/editstatut/{id}")
    public String editetudiant(@PathVariable("id") int id, Model model,@RequestParam("selectedString") String status, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User admin = userRepository.findByUsername(userDetails.getUsername());
        model.addAttribute("admin", admin);

       Pharmacie pharmacie=pharmacieRepository.findById(id);
        System.out.println(status);
        if (status.equalsIgnoreCase("REFUSEE")) {
            System.out.println("4151111111111111111111");

            pharmacie.setStatus(PharmacieStatus.REFUSEE);

        }else {

            pharmacie.setStatus(PharmacieStatus.APPROVEE);
        }

      pharmacieRepository.save(pharmacie);
        model.addAttribute("pharmacie", pharmacie);


        return "redirect:/admin/pharmacie";
    }
@GetMapping("/showinfo/{id}")
public String editstatus(@PathVariable("id") int id,Model model ,Authentication authentication){
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User admin= userRepository.findByUsername(userDetails.getUsername());
        model.addAttribute("admin", admin);
    }
    Pharmacie pharmacie=pharmacieRepository.findById(id);
    List<String> status= new ArrayList<>();
    status.add("APPROVEE");
    status.add("REFUSEE");

    model.addAttribute("pharmacies",pharmacieRepository.findAll());
    model.addAttribute("zones",zoneRepository.findAll());
    model.addAttribute("pharmacie",pharmacie);
    model.addAttribute("mode","update");
    model.addAttribute("status",status);
    return "pharmacie";
}











        @GetMapping("/deletezone/{id}")
    public String deletZone(@PathVariable("id") int id,Zone zone,Model model){
            try{
                Zone zone1= zoneRepository.findById(id);
                zoneRepository.delete(zone1);
                model.addAttribute("zones",zoneRepository.findAll());
                return "redirect:/admin/zone";
            }catch (Exception e){
                return "error 50z";
            }

    }
    @GetMapping("/barChart")
    public String getAllVille(Model model,Authentication authentication) {
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User admin = userRepository.findByUsername(userDetails.getUsername());

            model.addAttribute("admin", admin);
        }
        model.addAttribute("nombreDePharmacies",pharmacieRepository.getPharmacieCount());
        model.addAttribute("nombreDeZones",zoneRepository.getZoneCount());
        model.addAttribute("nombreDeVilles",villeRepository.getVilleCount());
        model.addAttribute("nombreDePharmaciesEnAttente",pharmacieRepository.getEnAttendCount());
        model.addAttribute("nombreDePharmaciesRefusees",pharmacieRepository.getCountRefusee());
        model.addAttribute("nombreDePharmaciesValidees",pharmacieRepository.getCountApprovees());
        List<String> villeList= villeRepository.findAll().stream().map(x->x.getNom()).collect(Collectors.toList());
        List<Integer> zoneList = villeRepository.findAll().stream().map(x-> zoneRepository.countZoneByVille(x)).collect(Collectors.toList());
        List<Long> pharmaList = villeRepository.findAll().stream().map(x-> pharmacieRepository.CountPharmacieByVille(x.getId())).collect(Collectors.toList());
        List<Long> gardeList = villeRepository.findAll().stream().map(x-> pharmacieRepository.getCountPharmaciesByVilleAndGarde(x,GardeType.J,GardeType.N)).collect(Collectors.toList());
        model.addAttribute("pharm", pharmaList);
        model.addAttribute("gardeList", gardeList);
        model.addAttribute("name", villeList);
        model.addAttribute("age", zoneList);
        return "statistique";

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
