package ute.udn.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ute.udn.userservice.entity.ERole;
import ute.udn.userservice.entity.Role;
import ute.udn.userservice.entity.User;
import ute.udn.userservice.exception.BadRequestException;
import ute.udn.userservice.repository.RoleRepository;
import ute.udn.userservice.repository.UserRepository;

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
