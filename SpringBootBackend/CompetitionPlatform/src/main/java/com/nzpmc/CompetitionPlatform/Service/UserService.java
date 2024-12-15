package com.nzpmc.CompetitionPlatform.Service;

import com.nzpmc.CompetitionPlatform.models.User;
import com.nzpmc.CompetitionPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
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
}
