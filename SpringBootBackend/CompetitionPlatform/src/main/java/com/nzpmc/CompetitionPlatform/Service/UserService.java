package com.nzpmc.CompetitionPlatform.Service;

import com.nzpmc.CompetitionPlatform.dto.LoginRequest;
import com.nzpmc.CompetitionPlatform.dto.LoginResponse;
import com.nzpmc.CompetitionPlatform.dto.UpdateNameRequest;
import com.nzpmc.CompetitionPlatform.dto.UserSignUpRequest;
import com.nzpmc.CompetitionPlatform.models.Event;
import com.nzpmc.CompetitionPlatform.models.User;
import com.nzpmc.CompetitionPlatform.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    public Object registerUser(UserSignUpRequest userSignUpRequest) {
        // Check if user already exists
        if (userRepository.existsById(userSignUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Encrypt the password
        String passwordHash = passwordEncoder.encode(userSignUpRequest.getPassword());

        // Create a new User entity
        User user = new User(
                userSignUpRequest.getName(),
                userSignUpRequest.getEmail(),
                passwordHash,
                "user"
        );

        // Save the user
        userRepository.save(user);

        return user;
    }

    public User updateUserName(String authorizationHeader, UpdateNameRequest updateNameRequest) {
        // Extract and validate the token
        String token = jwtService.extractToken(authorizationHeader);
        if (token == null) {
            throw new IllegalArgumentException("Authorization header missing or invalid");
        }

        // Extract claims from the token
        Claims claims = jwtService.extractAllClaims(token);

        // Retrieve user by email and update the name
        String email = claims.get("email", String.class);
        String nameToUpdateTo = updateNameRequest.getName();
        User updatedUser = updateName(email, nameToUpdateTo);

        // Save the user
        userRepository.save(updatedUser);
        return updatedUser;
    }

    public List<Event> getUserEvents(String authorizationHeader) {
        // Extract JWT token from Authorization header
        String token = jwtService.extractToken(authorizationHeader);
        if (token == null) {
            throw new IllegalArgumentException("Authorization header missing or invalid");
        }

        // Validate and parse JWT token
        Claims claims = jwtService.extractAllClaims(token);

        // Extract user ID (email) from token
        String userEmail = claims.get("email", String.class);
        if (userEmail == null) {
            throw new IllegalArgumentException("Token does not contain user email");
        }

        // Retrieve user from the database
        Optional<User> userOptional = findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();

        // Fetch events
        return user.getEvents();
    }

    public ResponseEntity<Object> deleteUserById(String userId) {
        // Retrieve user from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.deleteById(userId);
        return ResponseEntity.ok(userId + " is deleted");
    }
}
