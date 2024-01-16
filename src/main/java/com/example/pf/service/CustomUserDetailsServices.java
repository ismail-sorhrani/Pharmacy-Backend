package com.example.pf.service;


import com.example.pf.entities.User;
import com.example.pf.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

@Service
public class CustomUserDetailsServices  implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username or Password not found");
        }
        return new CustomUserDetails(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities(user),//j'ai ajouter user
                user.getPharmacies(),
                user.getRole());
    }

    public Collection<? extends GrantedAuthority> authorities (User user) {
        return Arrays.asList(new SimpleGrantedAuthority(user.getRole().toString()));//j'ai placer user par cette expression
    }

}