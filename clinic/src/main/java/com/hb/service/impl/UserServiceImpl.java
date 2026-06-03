/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hb.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.hb.dto.request.UserCreateRequest;
import com.hb.enums.UserRole;
import com.hb.exception.BadRequestException;
import com.hb.exception.DuplicateResourceException;
import com.hb.exception.ResourceNotFoundException;
import com.hb.pojo.SocialAccount;
import com.hb.pojo.User;
import com.hb.repository.SocialAccountRepository;
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
import java.util.UUID;
import org.springframework.context.annotation.Lazy;
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
    private SocialAccountRepository socialRepo;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PatientService patientService;

    @Override
    public User getUserByUsername(String username) {
        return userRepo.getUserByUsername(username);
    }

    @Override
    public User getUserById(Long id) {
        User u = userRepo.getUserById(id);
        if (u == null) {
            throw new ResourceNotFoundException("Tài khoản không tồn tại!");
        }
        return u;
    }

    @Override
    public User saveOrUpdateUser(UserCreateRequest urq) {

        validateUsername(urq.getUsername());

        validateEmail(urq.getEmail());

        User u;

        if (urq.getId() != null) {
            u = userRepo.getUserById(urq.getId());

            if (urq.getPassword() != null && !urq.getPassword().isEmpty() && !this.passwordEncoder.matches(urq.getPassword(), u.getPassword())) {
                validatePassword(urq.getPassword());
            }

            if (u == null) {
                throw new ResourceNotFoundException("Không tìm thấy người dùng!");
            }

            if (!u.getUsername().equals(urq.getUsername())) {
                throw new DuplicateResourceException("Không thể thay đổi tên tài khoản!");
            }

            if (!u.getEmail().equals(urq.getEmail())) {
                User checkUser = userRepo.existsByEmail(urq.getEmail());
                if (checkUser != null) {
                    throw new DuplicateResourceException("Email đã tồn tại!");
                }
            }

            if (urq.getPassword() != null && !urq.getPassword().trim().isEmpty()) {
                u.setPassword(passwordEncoder.encode(urq.getPassword()));
            }

            if (urq.getName() != null && !urq.getName().trim().isEmpty()) {
                u.setName(urq.getName());
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

            validatePassword(urq.getPassword());

            u = new User();
            u.setUsername(urq.getUsername());
            u.setEmail(urq.getEmail());
            u.setPassword(passwordEncoder.encode(urq.getPassword()));
            u.setRole(UserRole.ROLE_PATIENT);
            u.setIsActive(true);
            u.setName(urq.getName());
            u.setCreatedAt(LocalDateTime.now());
        }

        if (urq.getAvatar() != null && !urq.getAvatar().isEmpty()) {

            if (u.getPublicId() != null) {
                this.cloudinaryService.deleteFile(u.getPublicId());
            }
            Map res = this.cloudinaryService.uploadFile(urq.getAvatar(), "avatar");
            u.setSecureUrl(res.get("secureUrl").toString());
            u.setPublicId(res.get("publicId").toString());
        } else if (u.getId() == null) {
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
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

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
        this.userRepo.deleteUser(id);
    }

    @Override
    public User processSocialLoginGoogle(GoogleIdToken.Payload payload, String fcmToken) {
        String googleId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String provider = "GOOGLE";
        String avatarUrl = (String) payload.get("picture");

        SocialAccount social = socialRepo.findByProviderAndProviderId(provider, googleId);
        if (social != null) {
            return social.getUserId();
        }

        User user = userRepo.getUserByEmail(email);

        if (user == null) {
            user = new User();
            user.setUsername(email);
            user.setEmail(email);
            user.setName(name);
            user.setSecureUrl(avatarUrl);
            user.setRole(UserRole.ROLE_PATIENT);
            user.setCreatedAt(LocalDateTime.now());
            String randomPassword = UUID.randomUUID().toString();
            user.setPassword(passwordEncoder.encode(randomPassword));

            userRepo.saveOrUpdate(user);
        }

        SocialAccount newSocial = new SocialAccount();
        newSocial.setProvider(provider);
        newSocial.setProviderId(googleId);
        newSocial.setUserId(user);

        socialRepo.save(newSocial);

        if (fcmToken != null && !fcmToken.isEmpty()) {
            user.setFcmToken(fcmToken);
            userRepo.saveOrUpdate(user);
        }

        return user;
    }

    public User processSocialLoginFacebook(String facebookId, String email, String name) {
        SocialAccount social = socialRepo.findByProviderAndProviderId("FACEBOOK", facebookId);
        if (social != null) {
            return social.getUserId();
        }

        User user = userRepo.getUserByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setUsername(email);
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            userRepo.saveOrUpdate(user);
        }

        SocialAccount newSocial = new SocialAccount();
        newSocial.setProvider("FACEBOOK");
        newSocial.setProviderId(facebookId);
        newSocial.setUserId(user);
        socialRepo.save(newSocial);

        return user;
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
    public void updateFcmToken(String username, String fcmToken) {
        User user = this.userRepo.getUserByUsername(username);
        if (user != null) {
            user.setFcmToken(fcmToken);
        }
    }

    @Override
    public String getRoleByUsername(String username) {
        User user = this.userRepo.getUserByUsername(username);
        return user != null ? user.getRole().toString() : null;
    }

    @Override
    public List<User> getActiveUsers(String kw) {
        return this.userRepo.getActiveUsers(kw);
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BadRequestException("Email không được để trống!");
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new BadRequestException("Email không hợp lệ!");
        }
        if (email.contains(" ")) {
            throw new BadRequestException("Email không được chứa khoảng trắng!");
        }
    }

    private void validateUsername(String username) {

        if (username == null || username.trim().isEmpty()) {
            throw new BadRequestException("Tên tài khoản không được để trống!");
        }
        if (username.length() < 3 || username.length() > 20) {
            throw new BadRequestException("Tên tài khoản phải có từ 3 đến 20 ký tự!");
        }
        if (!username.matches("^[a-zA-Z0-9._-]+$")) {
            throw new BadRequestException("Tên tài khoản chỉ được chứa chữ cái, số, dấu chấm, gạch dưới và gạch ngang!");
        }
        if (username.contains(" ")) {
            throw new BadRequestException("Tên tài khoản không được chứa khoảng trắng!");
        }
        if (username.matches(".*[A-Z].*")) {
            throw new BadRequestException("Tên tài khoản không được chứa chữ cái viết hoa!");
        }
        if (username.matches(".*[!@#$%^&*()].*")) {
            throw new BadRequestException("Tên tài khoản không được chứa ký tự đặc biệt!");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new BadRequestException("Mật khẩu không được để trống!");
        }
        if (password.length() < 6) {
            throw new BadRequestException("Mật khẩu phải có ít nhất 6 ký tự!");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new BadRequestException("Mật khẩu phải chứa ít nhất một chữ cái viết hoa!");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new BadRequestException("Mật khẩu phải chứa ít nhất một chữ cái viết thường!");
        }
        if (!password.matches(".*\\d.*")) {
            throw new BadRequestException("Mật khẩu phải chứa ít nhất một chữ số!");
        }
        if (!password.matches(".*[!@#$%^&*()].*")) {
            throw new BadRequestException("Mật khẩu phải chứa ít nhất một ký tự đặc biệt!");
        }
        if (password.contains(" ")) {
            throw new BadRequestException("Mật khẩu không được chứa khoảng trắng!");
        }

    }

}
