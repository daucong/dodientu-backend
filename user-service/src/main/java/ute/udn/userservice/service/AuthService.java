package ute.udn.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ute.udn.userservice.dto.UpdatePasswordDTO;
import ute.udn.userservice.entity.ConfirmationToken;
import ute.udn.userservice.entity.User;
import ute.udn.userservice.exception.BadRequestException;
import ute.udn.userservice.exception.UpdatePasswordException;
import ute.udn.userservice.repository.ConfirmationTokenRepository;
import ute.udn.userservice.repository.UserRepository;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    private Boolean currentPasswordMatches(User currentUser, String password) {
        return passwordEncoder.matches(password, currentUser.getPassword());
    }

    public Optional<User> updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        User currentUser = userService.getOneById(updatePasswordDTO.getId());
        if (currentUser == null) {
            throw new NullPointerException("Invalid current user");
        }
        if (!currentPasswordMatches(currentUser, updatePasswordDTO.getOldPassword())) {
            throw new UpdatePasswordException(currentUser.getEmail(), "Invalid current password");
        } else {
            if (updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getOldPassword())){
                throw new BadRequestException("Password new must be different old password");
            }
            else {
                String newPassword = passwordEncoder.encode(updatePasswordDTO.getNewPassword());
                currentUser.setPassword(newPassword);
                userRepository.save(currentUser);
            }
        }
        return Optional.of(currentUser);
    }

    public ConfirmationToken findByConfirmationToken(String token) {
        return confirmationTokenRepository.findByConfirmationToken(token);
    }

    public ConfirmationToken createToken(User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        return confirmationTokenRepository.save(confirmationToken);
    }

}
