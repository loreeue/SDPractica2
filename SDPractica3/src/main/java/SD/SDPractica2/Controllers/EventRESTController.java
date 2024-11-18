package SD.SDPractica2.Controllers;

import SD.SDPractica2.Entities.Event;
import SD.SDPractica2.Entities.Location;
import SD.SDPractica2.Entities.User;
import SD.SDPractica2.Service.EventService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/event")
public class EventRESTController {
    @Autowired
    private EventService eventService;

    /**
     * This method handles POST requests.
     * Adds a new event received in the request body.
     * @param newEvent The event to be added.
     * @return ResponseEntity with status 201 (Created) and the added event in the body or status 400 (Bad Request).
     */
    @PostMapping
    public ResponseEntity<Event> addEvent(@RequestBody JsonNode newEvent) {
        if (newEvent.get("id") != null || newEvent.get("name") == null || newEvent.get("description") == null || newEvent.get("dateTime") == null || newEvent.get("image") == null || newEvent.get("locations") == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String name = newEvent.get("name").asText();
        String description = newEvent.get("description").asText();
        String dateTime = newEvent.get("dateTime").asText();
        String image = newEvent.get("image").asText();

        //create the "basic" event.
        Event e = eventService.addEvent(new Event(name, description, dateTime, image));

        //checking for locations and users
        Map<String, Set<Long>> map = eventService.convertJsonToSetLong(newEvent);

        //locations
        if (map.containsKey("locations")) {
            eventService.updateEventLocations(e.getId(), map.get("locations"));
        }

        //users
        if (map.containsKey("users")) {
            eventService.updateEventUsers(e.getId(), map.get("users"));
        }

        Event event = eventService.updateEvent(e.getId(), e);
        return ResponseEntity.status(201).body(event);
    }

    /**
     * This method handles GET requests.
     * Retrieves an event by its ID.
     * @param id The ID of the event to retrieve.
     * @return ResponseEntity with status 201 (OK) and the event in the body if found, or status 404 (Not Found) if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> obtainEvent(@PathVariable Long id) {
        Event newEvent = eventService.obtainEvent(id);
        if (newEvent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(201).body(newEvent);
    }

    /**
     * This method handles GET requests.
     * Retrieves all events.
     * @return ResponseEntity with status 200 (OK) and a collection of events in the body.
     */
    @GetMapping
    public ResponseEntity<Collection<Event>> obtainAllEvents() {
        return ResponseEntity.ok(eventService.obtainAllEvents());
    }

    /**
     * This method handles PUT requests.
     * Updates an event with the specified ID using data from the request body.
     * @param id The ID of the event to update.
     * @param updateEvent The updated event data.
     * @return ResponseEntity with status 200 (OK) and the updated event in the body or status 400 (Bad Request) or status 404 (Not Found).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody JsonNode updateEvent) {
        Event existingEvent = eventService.obtainEvent(id);
        if (existingEvent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if ((updateEvent.get("id") != null && !Objects.equals(updateEvent.get("id").asLong(), id)) || updateEvent.get("name") == null || updateEvent.get("description") == null || updateEvent.get("dateTime") == null || updateEvent.get("image") == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String name = updateEvent.get("name").asText();
        String description = updateEvent.get("description").asText();
        String dateTime = updateEvent.get("dateTime").asText();
        String image = updateEvent.get("image").asText();

        //checking for locations and users
        Map<String, Set<Long>> map = eventService.convertJsonToSetLong(updateEvent);

        //locations
        if (map.containsKey("locations")) {
            eventService.updateEventLocations(existingEvent.getId(), map.get("locations"));
        }

        //users
        if (map.containsKey("users")) {
            eventService.updateEventUsers(existingEvent.getId(), map.get("users"));
        }

        Event updatedEvent = eventService.updateEvent(existingEvent.getId(), new Event(name, description, dateTime, image));
        return ResponseEntity.status(201).body(updatedEvent);
    }

    /**
     * This method handles PATCH requests.
     * Updates an event with the specified ID using partial data from the request body.
     * @param id The ID of the event to update.
     * @param updateEvent The partial updated event data.
     * @return ResponseEntity with status 200 (OK) and a success message or status 400 (Bad Request) or status 404 (Not Found).
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchEvent (@PathVariable Long id, @RequestBody JsonNode updateEvent) {
        Event existingEvent = eventService.obtainEvent(id);
        if (existingEvent == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (existingEvent.getId() != null && !Objects.equals(existingEvent.getId(), id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String name = updateEvent.has("name") ? updateEvent.get("name").asText() : null;
        String description = updateEvent.has("description") ? updateEvent.get("description").asText() : null;
        String dateTime = updateEvent.has("dateTime") ? updateEvent.get("dateTime").asText() : null;
        String image = updateEvent.has("image") ? updateEvent.get("image").asText() : null;

        //checking for locations and users
        Map<String, Set<Long>> map = eventService.convertJsonToSetLong(updateEvent);

        //locations
        if (map.containsKey("locations")) {
            eventService.updateEventLocations(existingEvent.getId(), map.get("locations"));
        }

        //users
        if (map.containsKey("users")) {
            eventService.updateEventUsers(existingEvent.getId(), map.get("users"));
        }

        Event updatedEvent = eventService.patchEvent(id, new Event(name, description, dateTime, image));
        return ResponseEntity.status(201).body(updatedEvent);
    }

    /**
     * This method handles DELETE requests.
     * Deletes an event with the specified ID.
     * @param id The ID of the event to delete.
     * @return ResponseEntity with status 200 (OK) and a success message or status 404 (Not Found) if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        if (eventService.obtainEvent(id) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        eventService.deleteEvent(id);
        return ResponseEntity.ok("Evento con ID: " + id + " eliminado con éxito");
    }

    /**
     * This method handles GET requests.
     * Obtains all the locations associated to one event.
     * @param id The ID of the event.
     * @return ResponseEntity with status 200 (OK).
     */
    @GetMapping("/{id}/locations")
    public ResponseEntity<Collection<Location>> obtainAllLocations(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.obtainAllLocations(id));
    }

    /**
     * This method handles GET requests.
     * Obtains all the users associated to one event.
     * @param id The ID of the event.
     * @return ResponseEntity with status 200 (OK).
     */
    @GetMapping("/{id}/users")
    public ResponseEntity<Collection<User>> obtainAllUsers(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getUsersByEventId(id));
    }
}