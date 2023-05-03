package ute.udn.userservice.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.web.bind.annotation.*;
import ute.udn.userservice.dto.*;
import ute.udn.userservice.entity.*;
import ute.udn.userservice.exception.BadRequestException;
import ute.udn.userservice.exception.TokenRefreshException;
import ute.udn.userservice.security.jwt.JwtUtils;
import ute.udn.userservice.security.services.CustomUserDetailsService;
import ute.udn.userservice.security.services.RefreshTokenService;
import ute.udn.userservice.security.services.UserDetailsImpl;
import ute.udn.userservice.service.*;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/v1")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils JwtUtils;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    CountService countService;
    @Autowired
    OtpService otpService;

    @Value("${spring.social.facebook.appSecret}")
    String appSecret;

    @Value("${spring.social.facebook.appId}")
    String appId;

    @Value("${redirect_uri}")
    String redirect_uri;

    @Value("${scope}")
    String scope;

    @Value("${googleInfo}")
    String googleInfo;

    @Value("${defaultPassword}")
    String defaultPassword;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
        Authentication authentication = null;
        if (loginDTO.getTypeAccount() != null) {
            loginDTO.setPassword(defaultPassword);
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUserName(), loginDTO.getPassword()));
        } else {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUserName(), loginDTO.getPassword()));
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = JwtUtils.generateJwtToken(userDetailsImpl);

        List<String> roles = userDetailsImpl.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetailsImpl.getId());
        return ResponseEntity.ok(new JwtDTO(jwt,
                refreshToken.getToken(),
                userDetailsImpl.getId(),
                userDetailsImpl.getUsername(),
                userDetailsImpl.getEmail(),
                userDetailsImpl.getFirstName(),
                userDetailsImpl.getLastName(),
                userDetailsImpl.getPhone(),
                userDetailsImpl.getStatus(),
                userDetailsImpl.getTypeUser(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpDTO signUpRequest) {
        if (userService.existsByUserName(signUpRequest.getUserName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageDTO("Error: Username is already taken!"));
        }
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageDTO("Error: Email is already in use!"));
        }
        User user = new User(signUpRequest.getUserName(), signUpRequest.getFirstName(), signUpRequest.getLastName(),
                signUpRequest.getEmail(), signUpRequest.getPhone(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getTypeUser());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = userService.getOneByRoleName(ERole.ROLE_USER);
            if (userRole == null) throw new RuntimeException("Error: Role is not found.");
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = userService.getOneByRoleName(ERole.ROLE_ADMIN);
                        if (adminRole == null) throw new RuntimeException("Error: Role is not found.");
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = userService.getOneByRoleName(ERole.ROLE_USER);
                        if (userRole == null) throw new RuntimeException("Error: Role is not found.");
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);

        Count count = countService.getOneById((long) 1);
        count.setNumberOfReg(count.getNumberOfReg() + 1);

        countService.saveOrUpdate(count);
        userService.saveUser(user);
        return ResponseEntity.ok(new MessageDTO("User registered successfully!"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenDTO request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = JwtUtils.generateTokenFromUsername(user.getUserName());
                    return ResponseEntity.ok(new TokenRefreshDTO(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/sendemail")
    public ResponseEntity<?> sendVerificationMail(@Valid @RequestBody
                                                          VerifyEmailDTO emailRequest) {
        if (userService.existsByEmail(emailRequest.getEmail()) != null) {
            if (userDetailsService.isAccountVerified(emailRequest.getEmail())) {
                throw new BadRequestException("Email is already verified");
            } else {
                if (emailRequest.getOtpNo() != null) {
                    if (otpService.validateOTP(emailRequest.getEmail(), emailRequest.getOtpNo())) {
                        User user = userService.findByEmail(emailRequest.getEmail());
                        ConfirmationToken token = authService.createToken(user);
                        userService.updateResetPasswordToken(token.getConfirmationToken(), emailRequest.getEmail());
                        emailSenderService.sendMail(user.getEmail(), token.getConfirmationToken());
                        return ResponseEntity.ok(new ApiPassDTO(true, "Verification link is sent on your mail id"));
                    }
                }
                throw new BadRequestException("Invalid OTP");
            }
        } else {
            throw new BadRequestException("Email is not associated with any account");
        }
    }

    @GetMapping("/confirmaccount")
    public ResponseEntity<?> getMethodName(@RequestParam("token") String token) {

        ConfirmationToken confirmationToken = authService.findByConfirmationToken(token);

        if (confirmationToken == null) {
            throw new BadRequestException("Invalid token");
        }

        User user = confirmationToken.getUser();
        Calendar calendar = Calendar.getInstance();

        if ((confirmationToken.getExpiryDate().getTime() -
                calendar.getTime().getTime()) <= 0) {
            return ResponseEntity.badRequest().body("Link expired. Generate new link from http://localhost:8081/ddt/login");
        }

        user.setEmailVerified(true);
        userService.saveUser(user);
        return ResponseEntity.ok("Account verified successfully!");
    }

    @PostMapping("/resetpassword")
    public ResponseEntity processResetPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        User user = userService.getByResetPasswordToken(forgotPasswordDto.getToken());
        if (user == null) {
            throw new BadRequestException("Token Invalid");
        } else {
            userService.updatePassword(user, forgotPasswordDto.getNewPassword());
            return ResponseEntity.ok(new ApiPassDTO(true, "Password changed successfully"));
        }
    }

    @GetMapping("/createFacebookAuthorization")
    public String createFacebookAuthorization() {
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(appId, appSecret);
        OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri(redirect_uri);
        params.setScope(scope);
        return oauthOperations.buildAuthorizeUrl(params);
    }

    @GetMapping("/facebook")
    public ResponseEntity<?> createFacebookAccessToken(@Valid @RequestParam("code") String code) {
        if (code == null || code.equals("")) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageDTO("Error: code incorrect"));
        } else {
            FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(appId, appSecret);
            AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(code, redirect_uri, null);
            Connection<Facebook> connection = connectionFactory.createConnection(accessGrant);
            Facebook facebook = connection.getApi();
            String[] fields = {"email"};
            User userProfile = facebook.fetchObject("me", User.class, fields);
            if (userService.existsByEmail(userProfile.getEmail())) {
                User oldPass = userService.findByEmail(userProfile.getEmail());
                userProfile.setPassword(oldPass.getPassword());
                userProfile.setId(oldPass.getId());
                return ResponseEntity.ok(new UserDTO(
                        userProfile.getId(),
                        userProfile.getEmail(),
                        userProfile.getPassword(),
                        oldPass.getStatus()));
            } else {
                User user = new User();
                TypeUser typeUser = new TypeUser();
                typeUser.setId((long) 1);
                Set<Role> roles = new HashSet<>();
                Role userRole = userService.getOneByRoleName(ERole.ROLE_USER);
                roles.add(userRole);
                user.setUserName("facebook" + userProfile.getId());
                user.setEmail(userProfile.getEmail());
                user.setPassword(encoder.encode(defaultPassword));
                user.setLastName(connection.getDisplayName());
                user.setAvatar(connection.getImageUrl());
                user.setTypeUserId(typeUser);
                user.setRoles(roles);
                user.setStatus(1);
                userService.saveUser(user);
                return ResponseEntity.ok(new UserDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getStatus()));
            }
        }
    }

    @GetMapping("/google")
    public ResponseEntity loginGoogle(@Valid @RequestParam("code") String code) throws IOException, JSONException {
        if (code == null || code.equals("")) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageDTO("Error: code incorrect"));
        } else {
            String sURL = googleInfo + code;
            URL url = new URL(sURL);
            InputStream input = url.openStream();
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder json = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                json.append((char) c);
            }
            JSONObject getJson = new JSONObject(json.toString());
            if (userService.existsByEmail(getJson.getString("email"))) {
                User oldUser = userService.findByEmail(getJson.getString("email"));
                return ResponseEntity.ok(new UserDTO(
                        oldUser.getId(),
                        oldUser.getEmail(),
                        oldUser.getPassword(),
                        oldUser.getStatus()));
            } else {
                User user = new User();
                TypeUser typeUser = new TypeUser();
                typeUser.setId((long) 1);
                Set<Role> roles = new HashSet<>();
                Role userRole = userService.getOneByRoleName(ERole.ROLE_USER);
                roles.add(userRole);
                user.setUserName("google" + getJson.getString("id"));
                user.setEmail(getJson.getString("email"));
                user.setPassword(encoder.encode(defaultPassword));
                user.setFirstName(getJson.getString("family_name"));
                user.setLastName(getJson.getString("given_name"));
                user.setAvatar(getJson.getString("picture"));
                user.setTypeUserId(typeUser);
                user.setRoles(roles);
                user.setStatus(1);
                userService.saveUser(user);
                return ResponseEntity.ok(new UserDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getStatus()));
            }
        }
    }

    @GetMapping("/countAccess")
    public void countAccess() {
        Count count = countService.getOneById((long) 1);
        count.setAccess(count.getAccess() + 1);
        countService.saveOrUpdate(count);
    }

    @GetMapping("/showCount")
    public ResponseEntity<?> showCount() {
        Count count = countService.getOneById((long) 1);
        return ResponseEntity.ok(new Count(count.getId(), count.getAccess(), count.getNumberOfReg()));
    }
}
