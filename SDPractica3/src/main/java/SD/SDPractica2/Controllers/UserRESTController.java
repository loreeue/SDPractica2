package SD.SDPractica2.Controllers;

import SD.SDPractica2.Entities.Event;
import SD.SDPractica2.Entities.User;
import SD.SDPractica2.Service.EventService;
import SD.SDPractica2.Service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserRESTController {
    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    /**
     * This method handles POST requests.
     * Adds a new user received in the request body.
     * @param newUser The user to be added.
     * @return ResponseEntity with status 201 (Created) and the added user in the body or status 400 (Bad Request).
     */
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody JsonNode newUser) {
        if (newUser.get("id") != null || newUser.get("username") == null || newUser.get("email") == null || newUser.get("password") == null || newUser.get("firstName") == null || newUser.get("lastName") == null || newUser.get("birthdate") == null || newUser.get("gender") == null || newUser.get("selectedAvatar") == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String username = newUser.get("username").asText();
        String email = newUser.get("email").asText();
        String password = newUser.get("password").asText();
        String firstName = newUser.get("firstName").asText();
        String lastName = newUser.get("lastName").asText();
        String birthdate = newUser.get("birthdate").asText();
        String gender = newUser.get("gender").asText();
        String selectedAvatar = newUser.get("selectedAvatar").asText();

        User userCreated = new User(username, email, password, firstName, lastName, birthdate, gender, selectedAvatar);
        User userUpdated = userService.addUser(userCreated);
        if (newUser.has("evento")) {
            Long id = newUser.get("evento").asLong();
            userUpdated = userService.assignNewUser(userUpdated.getId(), eventService.obtainEvent(id));
        }
        return ResponseEntity.status(201).body(userUpdated);
    }

    /**
     * This method handles GET requests.
     * Retrieves a user by its ID.
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity with status 201 (OK) and the user in the body if found, or status 404 (Not Found) if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> obtainUser(@PathVariable Long id){
        User newUser = userService.obtainUser(id);
        if (newUser == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(201).body(newUser);
    }

    /**
     * This method handles GET requests.
     * Retrieves all users.
     * @return ResponseEntity with status 200 (OK) and a collection of users in the body.
     */
    @GetMapping
    public ResponseEntity<Collection<User>> obtainAllUsers() {
        return ResponseEntity.ok(userService.obtainAllUsers());
    }

    /**
     * This method handles PUT requests.
     * Updates a user with the specified ID using data from the request body.
     * @param id The ID of the user to update.
     * @param updateUser The updated user data.
     * @return ResponseEntity with status 200 (OK) and the updated user in the body or status 400 (Bad Request) or status 404 (Not Found).
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody JsonNode updateUser) {
        if (updateUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (updateUser.get("id") != null || updateUser.get("username") == null || updateUser.get("email") == null || updateUser.get("password") == null || updateUser.get("firstName") == null || updateUser.get("lastName") == null || updateUser.get("birthdate") == null || updateUser.get("gender") == null || updateUser.get("selectedAvatar") == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String username = updateUser.get("username").asText();
        String email = updateUser.get("email").asText();
        String password = updateUser.get("password").asText();
        String firstName = updateUser.get("firstName").asText();
        String lastName = updateUser.get("lastName").asText();
        String birthdate = updateUser.get("birthdate").asText();
        String gender = updateUser.get("gender").asText();
        String selectedAvatar = updateUser.get("selectedAvatar").asText();

        User userCreated = new User(username, email, password, firstName, lastName, birthdate, gender, selectedAvatar);
        User userUpdated = userService.updateUser(id, userCreated);
        if (updateUser.has("evento")) {
            Long newId = updateUser.get("evento").asLong();
            userUpdated = userService.assignNewUser(userUpdated.getId(), eventService.obtainEvent(newId));
        }
        return ResponseEntity.status(201).body(userUpdated);
    }

    /**
     * This method handles PATCH requests.
     * Updates a user with the specified ID using partial data from the request body.
     * @param id The ID of the user to update.
     * @param updateUser The partial updated user data.
     * @return ResponseEntity with status 200 (OK) and a success message or status 400 (Bad Request) or status 404 (Not Found).
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchUser (@PathVariable Long id, @RequestBody JsonNode updateUser) {
        User existingUser = userService.obtainUser(id);
        if (updateUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (existingUser.getId() != null && !Objects.equals(existingUser.getId(), id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String username = updateUser.has("username") ? updateUser.get("username").asText() : null;
        String email = updateUser.has("email") ? updateUser.get("email").asText() : null;
        String password = updateUser.has("password") ? updateUser.get("password").asText() : null;
        String firstName = updateUser.has("firstName") ? updateUser.get("firstName").asText() : null;
        String lastName = updateUser.has("lastName") ? updateUser.get("lastName").asText() : null;
        String birthdate = updateUser.has("birthdate") ? updateUser.get("birthdate").asText() : null;
        String gender = updateUser.has("gender") ? updateUser.get("gender").asText() : null;
        String selectedAvatar = updateUser.has("selectedAvatar") ? updateUser.get("selectedAvatar").asText() : null;

        User userCreated = new User(username, email, password, firstName, lastName, birthdate, gender, selectedAvatar);
        User userUpdated = userService.patchUser(id, userCreated);
        if (updateUser.has("evento")) {
            Long newId = updateUser.get("evento").asLong();
            userUpdated = userService.assignNewUser(userUpdated.getId(), eventService.obtainEvent(newId));
        }
        return ResponseEntity.status(201).body(userUpdated);
    }

    /**
     * This method handles DELETE requests.
     * Deletes a user with the specified ID.
     * @param id The ID of the user to delete.
     * @return ResponseEntity with status 200 (OK) and a success message or status 404 (Not Found) if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userService.obtainUser(id) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        userService.deleteUser(id);
        return ResponseEntity.ok("Usuario con ID: " + id + " eliminado con éxito");
    }

    /**
     * This method handles POST requests.
     * LogIn of the web.
     * @param username The attribute username of the user.
     * @param password The attribute password of the user.
     * @return ResponseEntity with status 200 (OK) and a success message or status 404 (Not Found) if not found.
     */
    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestParam String username, @RequestParam String password) {
        User user = userService.existsUser(username, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok("El usuario con el username: " + username + "se ha logueado con éxito");
    }

    /**
     * This method handles GET requests.
     * Obtain the event of one user.
     * @param userId The ID of the user.
     * @return ResponseEntity with status 200 (OK) and a success message or status 404 (Not Found) if not found.
     */
    @GetMapping("/{userId}/event")
    public ResponseEntity<Event> obtainEventForUser(@PathVariable Long userId) {
        User user = userService.obtainUser(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Event event = user.getEvent();
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(event);
    }
}