package SD.SDPractica2.Service;

import SD.SDPractica2.Entities.Event;
import SD.SDPractica2.Entities.User;
import SD.SDPractica2.Repositories.EventRepository;
import SD.SDPractica2.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDateTime;

@Service
public class UserService { // In this service we use UserRepository and EventRepository
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    public UserService() {
    }

    /**
     * Adds a new user to the user repository.
     * Sets the ID, creation and update timestamps, and adds the user to the repository.
     * @param newUser The user to be added.
     * @return The added user.
     */
    public User addUser(User newUser) {
        newUser.setUpdatedAt(LocalDateTime.now());
        newUser.setCreatedAt(LocalDateTime.now());
        userRepository.save(newUser);
        return newUser;
    }

    /**
     * Assign a new event to a user.
     * @param userId The ID of the user to be modified.
     * @param event The event we associate to the user.
     * @return The user updated.
     */
    public User assignNewUser(Long userId, Event event) {
        Event newEvent = eventRepository.findById(event.getId()).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || newEvent == null) {
            return null;
        }
        user.setEvent(event);
        userRepository.save(user);
        return user;
    }

    /**
     * Updates an existing user partially using patch data.
     * Retrieves the existing user by ID, applies patch data, and updates the user in the repository.
     * @param id The ID of the user to be updated.
     * @param patchUser The partial data to update the user.
     * @return The updated user.
     */
    public User patchUser(Long id, User patchUser) {
        User preUser = userRepository.findById(id).orElse(null);

        if (patchUser.getUsername() != null && !patchUser.getUsername().equals(preUser.getUsername())) {
            preUser.setUsername(patchUser.getUsername());
        }

        if (patchUser.getEmail() != null && !patchUser.getEmail().equals(preUser.getEmail())) {
            preUser.setEmail(patchUser.getEmail());
        }

        if (patchUser.getPassword() != null && !patchUser.getPassword().equals(preUser.getPassword())) {
            preUser.setPassword(patchUser.getPassword());
        }

        if (patchUser.getFirstName() != null && !patchUser.getFirstName().equals(preUser.getFirstName())) {
            preUser.setFirstName(patchUser.getFirstName());
        }

        if (patchUser.getLastName() != null && !patchUser.getLastName().equals(preUser.getLastName())) {
            preUser.setLastName(patchUser.getLastName());
        }

        if (patchUser.getBirthdate() != null && !patchUser.getBirthdate().equals(preUser.getBirthdate())) {
            preUser.setBirthdate(patchUser.getBirthdate());
        }

        if (patchUser.getGender() != null && !patchUser.getGender().equals(preUser.getGender())) {
            preUser.setGender(patchUser.getGender());
        }

        if (patchUser.getSelectedAvatar() != null && !patchUser.getSelectedAvatar().equals(preUser.getSelectedAvatar())) {
            preUser.setSelectedAvatar(patchUser.getSelectedAvatar());
        }

        preUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(preUser);
        return preUser;
    }

    /**
     * Retrieves a user by its ID from the user repository.
     * @param id The ID of the user to retrieve.
     * @return The user with the specified ID, or null if not found.
     */
    public User obtainUser (Long id){
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Checks if a user exists with the provided username and password.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The user object if it exists with the provided credentials, otherwise null.
     */
    public User existsUser(String username, String password){
        for (User u : obtainAllUsers()) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Retrieves all users from the user repository.
     * @return A collection of all users.
     */
    public Collection<User> obtainAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Updates an existing user completely using new data.
     * Retrieves the existing user by ID, updates its data, and replaces it in the repository.
     * @param id The ID of the user to be updated.
     * @param updateUser The new data for the user.
     * @return The updated user.
     */
    public User updateUser(Long id, User updateUser) {
        if (!userRepository.existsById(id)) {
            return null;
        }
        updateUser.setId(id);
        updateUser.setCreatedAt(userRepository.findById(id).get().getCreatedAt());
        updateUser.setUpdatedAt(LocalDateTime.now());
        userRepository.saveAndFlush(updateUser);
        return updateUser;
    }

    /**
     * Deletes a user from the user repository by its ID.
     * @param id The ID of the user to delete.
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Checks if a user can log in.
     * @param user The user attempting to log in.
     * @return True if the user exists and the provided credentials match, otherwise false.
     */
    public boolean logIn(User user){
        if (user == null) {
            return false;
        }
        if (!userRepository.existsById(user.getId())) {
            return false;
        }
        for (User u : obtainAllUsers()) {
            if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }
}