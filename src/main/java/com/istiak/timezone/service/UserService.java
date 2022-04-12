package com.istiak.timezone.service;

import com.istiak.timezone.model.User;
import com.istiak.timezone.model.UserDTO;
import com.istiak.timezone.model.UserData;
import com.istiak.timezone.model.UserResponse;
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
    private UserRepository UserRepository;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    public UserResponse getAllUser(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> users = UserRepository.findAll(pageable);

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
        Optional<User> user = UserRepository.findById(id);
        UserData userData = null;
        if(user.isPresent()) {
            userData = UserData.of(user.get());
        }
        return userData;
    }

    public void createUser(UserDTO userDTO) {
        User user = User.of(userDTO);
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        UserRepository.saveAndFlush(user);
    }

    public boolean updateUser(UserData userData) {
        User user = UserRepository.findByEmail(userData.getUsername());
        if(user != null && userData.getUsername() != null) {
            user.setSysadmin(userData.getSysadmin());

            UserRepository.saveAndFlush(user);

            return true;
        }
        else {
            return false;
        }
    }

    public boolean deleteUser(String username) {
        long count = UserRepository.deleteByEmail(username);
        return (count>0);
    }
}
