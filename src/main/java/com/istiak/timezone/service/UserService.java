package com.istiak.timezone.service;

import com.istiak.timezone.model.User;
import com.istiak.timezone.model.UserDTO;
import com.istiak.timezone.model.UserResponse;
import com.istiak.timezone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public UserResponse getAllUser(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> users = UserRepository.findAll(pageable);

        List<UserDTO> userDtoList = users.getContent().stream()
                .map(u -> UserDTO.of(u))
                .collect(Collectors.toList());

        UserResponse userResponse = new UserResponse();
        userResponse.setUserList(userDtoList);
        userResponse.setPageNo(users.getNumber());
        userResponse.setPageSize(users.getSize());
        userResponse.setTotalElements(users.getTotalElements());
        userResponse.setTotalPages(users.getTotalPages());
        userResponse.setLast(users.isLast());

        return userResponse;
    }

    public UserDTO getSingleUser(Long id) {
        Optional<User> user = UserRepository.findById(id);
        UserDTO userDTO = null;
        if(user.isPresent()) {
            userDTO = UserDTO.of(user.get());
        }
        return userDTO;
    }

    public void createUser(UserDTO userDTO) {
        User user = User.of(userDTO);
        UserRepository.saveAndFlush(user);
    }

    public boolean updateUser(UserDTO userDTO) {
        User user = UserRepository.findByEmail(userDTO.getUsername());
        if(user != null && userDTO.getUsername() != null) {
            user.setFirstname(userDTO.getFirstname());
            user.setLastname(userDTO.getLastname());
            user.setSysadmin(userDTO.getSysadmin());

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
