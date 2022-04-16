package com.istiak.timezone.service;

import com.istiak.timezone.model.User;
import com.istiak.timezone.model.UserCreateModel;
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

    String regex = "(?=\\S+$).{6,20}$"; // no white space and length min 8, max 20
    public final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile(regex);
    @Autowired
    private UserRepository userRepository;

    public String validate(UserCreateModel userCreateModel) {
        String errors = checkIfInvalidData(userCreateModel);
        if(errors.length()>0) {
            JSONObject errorMsg = new JSONObject();
            errorMsg.put("msg", errors);
            return errorMsg.toString();
        }
        if(checkIfUserExists(userCreateModel.getEmail())) {
            JSONObject errorMsg = new JSONObject();
            errorMsg.put("msg", "User name already exists!");
            return errorMsg.toString();
        }
        return errors;
    }

    public String checkIfInvalidData (UserCreateModel userCreateModel) {
        String errorMsg = "";
        if(userCreateModel == null) {
            errorMsg += "No user information.";
            return errorMsg;
        }

        if(userCreateModel.getEmail() == null || userCreateModel.getEmail().length()==0) {
            errorMsg += "Email should not be null.";
        } else if(!validateEmail(userCreateModel.getEmail())) {
            errorMsg += "Email is not valid.";
        }

        if(userCreateModel.getPassword() == null || userCreateModel.getPassword().length() == 0) {
            errorMsg += "Password can not be empty.";
        } else if(!validatePassword(userCreateModel.getPassword())) {
            errorMsg += "Password must be between 6 to 20 characters long";
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
