package com.json.flexpay.service;

import com.json.flexpay.dto.AuthRequest;
import com.json.flexpay.dto.AuthResponse;
import com.json.flexpay.dto.UserDto;
import com.json.flexpay.entity.Account;
import com.json.flexpay.entity.Role;
import com.json.flexpay.entity.User;
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

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepo;
    private final AccountHelper accountHelper;

    public User register(UserDto userDto) throws Exception {
        User user = User.builder()
                .username(userDto.getUsername())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .dob(userDto.getDob())
                .gender(userDto.getGender())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(List.of(Role.USER.name()))
                .build();
        User savedUser = userRepo.save(user);
        Account account = accountHelper.createAccount(savedUser);
        savedUser.setAccounts(List.of(account));
        return savedUser;
    }

    public AuthResponse authenticateUser(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        User user = (User) userDetailsService.loadUserByUsername(authRequest.getUsername());

        String token = jwtService.generateToken(user); // JwtService expects UserDetails

        UserDto userDto = new UserDto(user);
        return new AuthResponse(token, userDto);
//        Map<String, Object> authObject = new HashMap<>();
//        authObject.put("token", token);
//        authObject.put("user", user);
//
//        return authObject;
    }
}
