package com.istiak.timezone.service;

import com.istiak.timezone.repository.AuthorityRepository;
import com.istiak.timezone.repository.UserRepository;
import com.istiak.timezone.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userDao;

    @Autowired
    private AuthorityRepository authorityRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    private List<SimpleGrantedAuthority> getAuthority(User user) {
        Set<String> authorities = authorityRepository.getAuthoritiesForUser(user.getId());
        List<SimpleGrantedAuthority> grantedAuthorities = authorities.
                stream().map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());
        return grantedAuthorities;
    }
}
