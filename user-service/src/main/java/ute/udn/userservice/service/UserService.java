package com.sdt.userservice.service;

import com.sdt.userservice.entity.ERole;
import com.sdt.userservice.entity.Role;
import com.sdt.userservice.entity.User;
import com.sdt.userservice.exception.BadRequestException;
import com.sdt.userservice.repository.RoleRepository;
import com.sdt.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public User saveUser(User entity) {
        return userRepository.save(entity);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getOneById(Long id) {
        return userRepository.findById(id).get();
    }

    public User getOneByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    public Role getOneByRoleName(ERole rolename) {
        return roleRepository.findByName(rolename);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean delete(Long id) {
        userRepository.deleteById(id);
        return false;
    }

    public Boolean existsByUserName(String username) {
        return userRepository.existsByUserName(username);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void updateResetPasswordToken(String token, String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new BadRequestException("Could not find any customer with the email " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);
    }
}
