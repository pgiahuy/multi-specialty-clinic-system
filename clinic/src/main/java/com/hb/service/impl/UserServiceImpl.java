/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.UserCreateRequest;
import com.hb.pojo.User;
import com.hb.service.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.hb.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HUY
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        return userRepo.getUserByUsername(username);
    }

    @Override
    public User addUser(UserCreateRequest urq) {
        User u = new User();

        u.setEmail(urq.getEmail());
        u.setUsername(urq.getUsername());
        u.setPassword(passwordEncoder.encode(urq.getPassword()));
        u.setRole("ROLE_USER");
        u.setCreatedAt(LocalDateTime.now());

        if (!urq.getAvatar().isEmpty()) {
            Map res = this.cloudinaryService.uploadFile(urq.getAvatar(), "avatar");

            u.setSecureUrl(res.get("secureUrl").toString());
            u.setPublicId(res.get("publicId").toString());
        }

        return this.userRepo.addUser(u);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepo.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Không tồn tại!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), authorities);
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        return this.userRepo.getUsers(params);
    }

    @Override
    public void deleteUser(Long id) {
        this.userRepo.deleteUser(id);
    }

    @Override
    public User processSocialLogin(String email, String name, String providerId, String providerName) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public long countUsers(Map<String, String> params) {
        return userRepo.count(params, User.class);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.getUserByEmail(email);
    }


}
