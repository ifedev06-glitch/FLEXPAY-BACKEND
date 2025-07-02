package com.json.flexpay.service;

import com.json.flexpay.dto.ApiResponse;
import com.json.flexpay.dto.AuthRequest;
import com.json.flexpay.dto.AuthResponse;
import com.json.flexpay.dto.UserDto;
import com.json.flexpay.entity.Account;
import com.json.flexpay.entity.Role;
import com.json.flexpay.entity.User;
import com.json.flexpay.exceptions.AuthenticationException;
import com.json.flexpay.exceptions.BadRequestException;
import com.json.flexpay.exceptions.UserAlreadyExistsException;
import com.json.flexpay.helper.AccountHelper;
import com.json.flexpay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepo;
    private final AccountHelper accountHelper;


    public ApiResponse<User> register(UserDto userDto) {
        Optional<User> existingUser = userRepo.findByUsername(userDto.getUsername());
        if (existingUser.isPresent()) {
            return ApiResponse.<User>builder()
                    .status("SUCCESS")
                    .statusCode("200")
                    .message("User already exists")
                    .build();
        }

        User user = User.builder()
                .username(userDto.getUsername())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .dob(userDto.getDob())
                .gender(userDto.getGender())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(List.of(Role.USER.name()))
                .build();
        try {
            User savedUser = userRepo.save(user);
            Account account = accountHelper.createAccount(savedUser);
            savedUser.setAccounts(List.of(account));

            return ApiResponse.<User>builder()
                    .status("SUCCESS")
                    .statusCode("200")
                    .message("User registered successfully")
                    .data(savedUser)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<User>builder()
                    .status("ERROR")
                    .statusCode("500")
                    .message("Failed to create account: " + e.getMessage())
                    .build();
        }
    }

    public ApiResponse<AuthResponse> authenticateUser(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            return ApiResponse.<AuthResponse>builder()
                    .status("ERROR")
                    .statusCode("200")
                    .message("Invalid username or password")
                    .build();
        }

        User user = (User) userDetailsService.loadUserByUsername(authRequest.getUsername());

        String token = jwtService.generateToken(user);

        UserDto userDto = new UserDto(user);

        AuthResponse authResponse = new AuthResponse(token, userDto);

        return ApiResponse.<AuthResponse>builder()
                .status("SUCCESS")
                .statusCode("200")
                .message("Authentication successful")
                .data(authResponse)
                .build();
    }
}






    /////////////////////////////////////////////////////////////////////////////////////////////

//    public User register(UserDto userDto) throws Exception {
//        if (userRepo.findByUsername(userDto.getUsername()).isPresent()) {
//            throw new UserAlreadyExistsException("Username already exists");
//        }
//        User user = User.builder()
//                .username(userDto.getUsername())
//                .firstname(userDto.getFirstname())
//                .lastname(userDto.getLastname())
//                .dob(userDto.getDob())
//                .gender(userDto.getGender())
//                .password(passwordEncoder.encode(userDto.getPassword()))
//                .roles(List.of(Role.USER.name()))
//                .build();
//        User savedUser = userRepo.save(user);
//        Account account = accountHelper.createAccount(savedUser);
//        savedUser.setAccounts(List.of(account));
//        return savedUser;
//    }
//
//    public AuthResponse authenticateUser(AuthRequest authRequest) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        authRequest.getUsername(),
//                        authRequest.getPassword()
//                )
//        );
//        User user = (User) userDetailsService.loadUserByUsername(authRequest.getUsername());
//
//        String token = jwtService.generateToken(user); // JwtService expects UserDetails
//
//        UserDto userDto = new UserDto(user);
//        return new AuthResponse(token, userDto);
////        Map<String, Object> authObject = new HashMap<>();
////        authObject.put("token", token);
////        authObject.put("user", user);
////
////        return authObject;
//    }

