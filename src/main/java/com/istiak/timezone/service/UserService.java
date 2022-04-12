package com.istiak.timezone.service;

import com.istiak.timezone.model.*;
import com.istiak.timezone.repository.AuthorityRepository;
import com.istiak.timezone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    public UserResponse getAllUser(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> users = userRepository.findAll(pageable);

        List<UserData> userDataList = users.getContent().stream()
                .map(u -> UserData.of(u))
                .collect(Collectors.toList());

        UserResponse userResponse = new UserResponse();
        userResponse.setUserList(userDataList);
        userResponse.setPageNo(users.getNumber());
        userResponse.setPageSize(users.getSize());
        userResponse.setTotalElements(users.getTotalElements());
        userResponse.setTotalPages(users.getTotalPages());
        userResponse.setLast(users.isLast());

        return userResponse;
    }

    public UserData getSingleUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        UserData userData = null;
        if(user.isPresent()) {
            userData = UserData.of(user.get());
        }
        return userData;
    }

    public User createUser(UserDTO userDTO) {
        User user = User.of(userDTO);
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        User createdUser =  userRepository.save(user);
        List<Authority> authorityList = authorityRepository.findAll();
        if(!userDTO.getSysadmin()) {
            authorityList = authorityList.stream().filter(a -> !a.getName().equalsIgnoreCase(AuthorityConstants.ADMIN) ).collect(Collectors.toList());
        }
        for(Authority a : authorityList) {
            authorityRepository.insertUserAuthorities(userDTO.getUsername(),a.getName());
        }

        return createdUser;
    }

    public void updateUser(UserData userData) {
        User user = userRepository.findByEmail(userData.getUsername());
        if (user != null && userData.getUsername() != null) {
            // Same , nothing to update
            if (userData.getSysadmin() == user.getSysadmin()) {
                return;
            }
            user.setSysadmin(userData.getSysadmin());
            userRepository.save(user);
            authorityRepository.updateUserAuthorities(userData.getUsername(),
                    userData.getSysadmin() ? AuthorityConstants.ADMIN : AuthorityConstants.USER);

        }

    }

    public void deleteUser(String username) {
        authorityRepository.deleteUserAuthorities(username);
        long count = userRepository.deleteByEmail(username);
    }
}
