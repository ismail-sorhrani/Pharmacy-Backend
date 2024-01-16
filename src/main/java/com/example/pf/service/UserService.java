package com.example.pf.service;

import com.example.pf.dto.UserDto;
import com.example.pf.entities.User;

public interface UserService {

    User findByUsername(String username);


    User save (UserDto userDto);

}
