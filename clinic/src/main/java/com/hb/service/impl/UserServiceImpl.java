/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.hb.dto.request.UserCreateRequest;
import com.hb.exception.DuplicateResourceException;
import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.User;
import com.hb.service.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.hb.repository.UserRepository;
import com.hb.service.PatientService;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
@PropertySource("classpath:configs.properties")
public class UserServiceImpl implements UserService {
    
    @Autowired
    private Environment env;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PatientService patientService;

    @Override
    public User getUserByUsername(String username) {
        return userRepo.getUserByUsername(username);
    }

    @Override
    public User saveOrUpdateUser(UserCreateRequest urq) {
        User u;

        if (urq.getId() != null) {
            u = userRepo.getUserById(urq.getId());
            if (u == null) {
                throw new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + urq.getId());
            }

            if (!u.getUsername().equals(urq.getUsername())) {
                User checkUser = userRepo.existsByUsername(urq.getUsername());
                if (checkUser != null) {
                    throw new DuplicateResourceException("Tên tài khoản đã tồn tại!");
                }
                u.setUsername(urq.getUsername());
            }

            if (!u.getEmail().equals(urq.getEmail())) {
                User checkEmail = userRepo.existsByEmail(urq.getEmail());
                if (checkEmail != null) {
                    throw new DuplicateResourceException("Email này đã được sử dụng!");
                }
                u.setEmail(urq.getEmail());
            }

            if (urq.getPassword() != null && !urq.getPassword().trim().isEmpty()) {
                u.setPassword(passwordEncoder.encode(urq.getPassword()));
            }


        } else {
            User checkUser = userRepo.existsByUsername(urq.getUsername());
            if (checkUser != null) {
                throw new DuplicateResourceException("Tên tài khoản đã tồn tại!");
            }

            User checkEmail = userRepo.existsByEmail(urq.getEmail());
            if (checkEmail != null) {
                throw new DuplicateResourceException("Email này đã được sử dụng!");
            }

            u = new User();
            u.setUsername(urq.getUsername());
            u.setEmail(urq.getEmail());
            u.setPassword(passwordEncoder.encode(urq.getPassword()));
            u.setRole("ROLE_PATIENT");
            u.setCreatedAt(LocalDateTime.now());
        }


        if (urq.getAvatar() != null && !urq.getAvatar().isEmpty()) {

            if (u.getPublicId()!= null) { 
                this.cloudinaryService.deleteFile(u.getPublicId()); 
            }
            Map res = this.cloudinaryService.uploadFile(urq.getAvatar(), "avatar");
            u.setSecureUrl(res.get("secureUrl").toString());
            u.setPublicId(res.get("publicId").toString());
        }
        else{
            String url = this.env.getProperty("avatar.default", String.class);
            u.setSecureUrl(url);
        }

        return this.userRepo.saveOrUpdate(u);
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
    public List<User> getUsers(Map<String, String> params
    ) {
        return this.userRepo.getUsers(params);
    }

    @Override
    public void deleteUser(Long id) {
//        User u = userRepo.getUserById(id);
//        if (u == null) {
//            throw new ResourceNotFoundException("User not found!");
//        }
//        if (u.getPublicId()!= null) { 
//                this.cloudinaryService.deleteFile(u.getPublicId()); 
//        }
        this.userRepo.deleteUser(id);
        System.out.println("Xoa thanh congggggggggggggggggggggggggggg");
    }

    @Override
    public User processSocialLogin(String email, String name,
            String providerId, String providerName
    ) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public long countUsers(Map<String, String> params
    ) {
        return userRepo.count(params, User.class);
    }

    @Override
    public User getUserByEmail(String email
    ) {
        return userRepo.getUserByEmail(email);
    }

    @Override
    @Transactional
    public void updateFcmToken(String username, String fcmToken
    ) {
        User user = this.userRepo.getUserByUsername(username);
        if (user != null) {
            user.setFcmToken(fcmToken);
        }
    }

    @Override
    public String getRoleByUsername(String username
    ) {
        User user = this.userRepo.getUserByUsername(username);
        return user != null ? user.getRole() : null;
    }

}
