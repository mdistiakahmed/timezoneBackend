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

import java.util.List;
import java.util.stream.Collectors;

@Service
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
}
