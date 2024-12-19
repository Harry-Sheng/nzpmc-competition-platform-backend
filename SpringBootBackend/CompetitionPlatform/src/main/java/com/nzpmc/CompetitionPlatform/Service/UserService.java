package com.nzpmc.CompetitionPlatform.Service;

import com.nzpmc.CompetitionPlatform.dto.LoginRequest;
import com.nzpmc.CompetitionPlatform.dto.LoginResponse;
import com.nzpmc.CompetitionPlatform.models.User;
import com.nzpmc.CompetitionPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    public void saveUser(User user){
        userRepository.save(user);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public boolean existsById(String email) {
        return userRepository.existsById(email);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateName(String email, String nameToUpdateTo) {
            Optional<User> userOptional = findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setName(nameToUpdateTo);
                userRepository.save(user);
                return user;
            }

            throw new IllegalStateException("Student with id" + email + "does not exist");

    }

    public void save(User user) {
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // Find user by email
        User user = findByEmail(email).orElseThrow(() ->
                new RuntimeException("Invalid email or password"));

        // Validate password
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Generate JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("id", user.getEmail());
        claims.put("role", user.getRole());

        String token = jwtService.createToken(claims);

        // Return response
        return new LoginResponse(
                token,
                user.getEmail(),
                user.getName(),
                user.getRole()
        );
    }
}
