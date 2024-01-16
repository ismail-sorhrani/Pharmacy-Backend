package com.example.pf.service;

import com.example.pf.dto.UserDto;
import com.example.pf.entities.Pharmacie;
import com.example.pf.entities.PharmacieStatus;
import com.example.pf.entities.Role;
import com.example.pf.entities.User;
import com.example.pf.repo.PharmacieRepository;
import com.example.pf.repo.UserRepository;
import com.example.pf.repo.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;
@Autowired
    ZoneRepository zoneRepository;
    private UserRepository userRepository;
@Autowired
    private PharmacieRepository pharmacieRepository;
    Pharmacie pharmacieme;
  User userme;
    public UserServiceImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {

        return userRepository.findByUsername(username);
    }
    public Pharmacie save(String name,User user){
      Pharmacie pharmacie=new Pharmacie(0,name,null,13,12,  PharmacieStatus.ENATTEND,null,null,null,user);
     pharmacieRepository.save(pharmacie);
   return pharmacie;

    }

    @Override
    public User save(UserDto userDto) {


        User user=new User();
        user.setUsername(userDto.getUsername());

        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.PHARMACIEN);
        userme=userRepository.save(user);
        pharmacieme=save(userDto.getUsername(),user);
        userme.setPharmacies(pharmacieme);
        userRepository.save(userme);

        return  userme;

    }





}