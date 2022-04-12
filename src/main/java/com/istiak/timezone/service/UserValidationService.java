package com.istiak.timezone.service;

import com.istiak.timezone.model.User;
import com.istiak.timezone.model.UserDTO;
import com.istiak.timezone.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserValidationService {
    public final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$", Pattern.CASE_INSENSITIVE);

    String regex = "^(?=.*[0-9])" // digit at least once
            + "(?=.*[a-z])(?=.*[A-Z])" // upper and lower case at least once
            + "(?=.*[@#$%^&+=])" // special charater at least once
            + "(?=\\S+$).{8,20}$"; // no white space and length min 8, max 20
    public final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile(regex);
    @Autowired
    private UserRepository userRepository;

    public String validate(UserDTO userDTO) {
        String errors = checkIfInvalidData(userDTO);
        if(errors.length()>0) {
            JSONObject errorMsg = new JSONObject();
            errorMsg.put("msg", errors);
            return errorMsg.toString();
        }
        if(checkIfUserExists(userDTO.getUsername())) {
            JSONObject errorMsg = new JSONObject();
            errorMsg.put("msg", "User name already exists!");
            return errorMsg.toString();
        }
        return errors;
    }

    public String checkIfInvalidData (UserDTO userDTO) {
        String errorMsg = "";
        if(userDTO == null) {
            errorMsg += "No user information.";
            return errorMsg;
        }

        if(userDTO.getUsername() == null || userDTO.getUsername().length()==0) {
            errorMsg += "Email should not be null.";
        } else if(!validateEmail(userDTO.getUsername())) {
            errorMsg += "Email is not valid.";
        }

        if(userDTO.getPassword() == null || userDTO.getPassword().length() == 0) {
            errorMsg += "Password can not be empty.";
        } else if(!validatePassword(userDTO.getPassword())) {
            errorMsg += "Password must contain upper case,lower case, digits and special characters.Max length 20, min length 8.";
        }

        return errorMsg;
    }

    public Boolean checkIfUserExists (String email) {
        User user = userRepository.findByEmail(email);
        return user == null ? false : true;
    }

    public String userModifyValidate (String username) {
        final String authenticatedUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String errorMsg = "";
        if(username == null || username.length()==0) {
            errorMsg = "Username is empty.";
        } else if(username.equalsIgnoreCase(authenticatedUser)) {
            errorMsg = "Can't modify own data.";
        } else {
            User user = userRepository.findByEmail(username);
            if(user == null) {
                errorMsg = "Username is not found";
            }
        }
        return errorMsg;
    }


    private boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private boolean validatePassword(String password) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
        return matcher.find();
    }

}
