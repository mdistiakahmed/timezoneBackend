package com.istiak.timezone.dao;

import com.istiak.timezone.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao  extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
