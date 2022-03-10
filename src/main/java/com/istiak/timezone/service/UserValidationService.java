package com.istiak.timezone.service;

import com.istiak.timezone.model.User;
import com.istiak.timezone.model.UserDTO;
import com.istiak.timezone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService {

    @Autowired
    private UserRepository userRepository;

    public Boolean checkIfUserExists (UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getUsername());
        return user == null ? false : true;
    }

}
