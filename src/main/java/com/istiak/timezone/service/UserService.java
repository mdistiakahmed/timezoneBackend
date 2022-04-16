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

    public UserTableResponse getAllUser(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> users = userRepository.findAll(pageable);

        List<UserUpdateModel> userUpdateModelList = users.getContent().stream()
                .map(u -> UserUpdateModel.of(u))
                .collect(Collectors.toList());

        UserTableResponse userTableResponse = new UserTableResponse();
        userTableResponse.setUserList(userUpdateModelList);
        userTableResponse.setPageNo(users.getNumber());
        userTableResponse.setPageSize(users.getSize());
        userTableResponse.setTotalElements(users.getTotalElements());
        userTableResponse.setTotalPages(users.getTotalPages());
        userTableResponse.setLast(users.isLast());

        return userTableResponse;
    }

    public User createUser(UserCreateModel userCreateModel) {
        User user = User.of(userCreateModel);
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        User createdUser =  userRepository.save(user);
        List<Authority> authorityList = authorityRepository.findAll();
        authorityList = authorityList.stream()
                .filter(a -> !a.getName().equalsIgnoreCase(userCreateModel.getSysadmin()?AuthorityConstants.USER: AuthorityConstants.ADMIN) )
                .collect(Collectors.toList());
        for(Authority a : authorityList) {
            authorityRepository.insertUserAuthorities(userCreateModel.getEmail(),a.getName());
        }

        return createdUser;
    }

    public void updateUser(UserUpdateModel userUpdateModel) {
        User user = userRepository.findByEmail(userUpdateModel.getEmail());
        if (user != null && userUpdateModel.getEmail() != null) {
            // Same , nothing to update
            if (userUpdateModel.getSysadmin() == user.getSysadmin()) {
                return;
            }
            user.setSysadmin(userUpdateModel.getSysadmin());
            userRepository.save(user);
            authorityRepository.updateUserAuthorities(userUpdateModel.getEmail(),
                    userUpdateModel.getSysadmin() ? AuthorityConstants.ADMIN : AuthorityConstants.USER);

        }

    }

    public void deleteUser(String username) {
        authorityRepository.deleteUserAuthorities(username);
        long count = userRepository.deleteByEmail(username);
    }
}
