package SD.SDPractica2.Service;

import SD.SDPractica2.Entities.Event;
import SD.SDPractica2.Entities.Location;
import SD.SDPractica2.Entities.User;
import SD.SDPractica2.Repositories.EventRepository;
import SD.SDPractica2.Repositories.LocationRepository;
import SD.SDPractica2.Repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EventService { // In this service we use EventRepository, LocationRepository and UserRepository
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    public EventService() {
    }

    /**
     * Adds a new event to the event repository.
     * Sets the ID, creation and update timestamps, and adds the event to the repository.
     * @param newEvent The event to be added.
     * @return The added event.
     */
    public Event addEvent(Event newEvent) {
        newEvent.setUpdatedAt(LocalDateTime.now());
        newEvent.setCreatedAt(LocalDateTime.now());
        eventRepository.save(newEvent);
        return newEvent;
    }

    /**
     * Updates an existing event partially using patch data.
     * Retrieves the existing event by ID, applies patch data, and updates the event in the repository.
     * @param id The ID of the event to be updated.
     * @param patchEvent The partial data to update the event.
     * @return The updated event.
     */
    public Event patchEvent(Long id, Event patchEvent) {
        Event preEvent = eventRepository.findById(id).orElse(null);

        if (preEvent == null) {
            return null;
        }

        if (patchEvent.getName() != null && !patchEvent.getName().equals(patchEvent.getName())) {
            preEvent.setName(patchEvent.getName());
        }

        if (patchEvent.getDescription() != null && !patchEvent.getDescription().equals(preEvent.getDescription())) {
            preEvent.setDescription(patchEvent.getDescription());
        }

        if (patchEvent.getDateTime() != null && !patchEvent.getDateTime().equals(preEvent.getDateTime())) {
            preEvent.setDateTime(patchEvent.getDateTime());
        }

        if (patchEvent.getImage() != null && !patchEvent.getImage().equals(preEvent.getImage())) {
            preEvent.setImage(patchEvent.getImage());
        }

        preEvent.setUpdatedAt(LocalDateTime.now());
        eventRepository.save(preEvent);
        return preEvent;
    }

    /**
     * Retrieves an event by its ID from the event repository.
     * @param id The ID of the event to retrieve.
     * @return The event with the specified ID, or null if not found.
     */
    public Event obtainEvent(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all events from the event repository.
     * @return A collection of all events.
     */
    public Collection<Event> obtainAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * Retrieves the locations that are associated to an event with a specific ID.
     * @param id The ID of the event to retrieve.
     * @return A collection of locations.
     */
    public Collection<Location> obtainAllLocations(Long id) {
        Event event = obtainEvent(id);
        return event.getLocations();
    }

    /**
     * Updates an existing event completely using new data.
     * Retrieves the existing event by ID, updates its data, and replaces it in the repository.
     * @param id The ID of the event to be updated.
     * @param newEvent The new data for the event.
     * @return The updated event.
     */
    public Event updateEvent(Long id, Event newEvent) {
        if (!eventRepository.existsById(id)) {
            return null;
        }
        newEvent.setId(id);
        newEvent.setCreatedAt(eventRepository.findById(id).get().getCreatedAt());
        newEvent.setUpdatedAt(LocalDateTime.now());
        eventRepository.save(newEvent);
        return newEvent;
    }

    /**
     * Convert the JSON node to two Set<Long> and creates a map with them.
     * Retrieves a map with 2 elements: "locations" and "users".
     * @param jsonNode The JSON node with "locations" and "users" attributes.
     * @return The map.
     */
    public Map<String,Set<Long>> convertJsonToSetLong(JsonNode jsonNode){
        Map<String, Set<Long>> idMap = new HashMap<>();

        JsonNode locationsNode = jsonNode.get("locations");
        if (locationsNode != null && !locationsNode.isEmpty()) {
            Set<Long> locationsIds = new HashSet<>();
            for (JsonNode id : locationsNode) {
                locationsIds.add(id.asLong());
            }
            idMap.put("locations", locationsIds);
        }

        JsonNode usersNode = jsonNode.get("users");
        if (usersNode != null && !usersNode.isEmpty()) {
            Set<Long> usersIds = new HashSet<>();
            for (JsonNode id : usersNode) {
                usersIds.add(id.asLong());
            }
            idMap.put("users", usersIds);
        }
        return idMap;
    }

    /**
     * Deletes an event from the event repository by its ID.
     * @param id The ID of the event to delete.
     */
    public void deleteEvent(Long id) {
        Event event = obtainEvent(id);
        List<User> users = event.getUsers();
        for (User u : users){
            u.setEvent(null);
            unjoinToEvent(u.getId(),event.getId());
        }
        Set<Location> locations = event.getLocations();
        Iterator<Location> locationIterator = locations.iterator();
        while (locationIterator.hasNext()) {
            Location location = locationIterator.next();
            Iterator<Event> eventIterator = location.getEvents().iterator();
            while (eventIterator.hasNext()) {
                Event e = eventIterator.next();
                if (e.getId().equals(event.getId())) {
                    eventIterator.remove();
                }
            }
            if (location.getEvents().isEmpty()) {
                locationIterator.remove();
            }
        }
        eventRepository.deleteById(id);
    }

    /**
     * Normalizes a string to avoid problems with accents.
     * @param input The input string to be normalized.
     * @return The normalized string.
     */
    private String normalizeString(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     * Searches for events based on a given string.
     * Normalizes the search string and compares it with event names and descriptions.
     * @param s The search string.
     * @return An ArrayList of events matching the search criteria.
     */
    public ArrayList<Event> search(String s) {
        ArrayList<Event> results = new ArrayList<>();
        String string = normalizeString(s.toUpperCase(Locale.ROOT));
        if (s.isEmpty()){
            return null;
        }
        for (Event e : obtainAllEvents()) {
            if (normalizeString(e.getName().toUpperCase()).contains(string) || normalizeString(e.getDescription().toUpperCase()).contains(string)) {
                results.add(e);
            }
        }
        if (results.isEmpty()) {
            return null;
        }
        return results;
    }

    /**
     * This function establishes the relationship between event and user.
     * @param userId The ID of the user.
     * @param eventId The ID of the event.
     * @return True or false depending on whether the user who is logged in already has an event associated with it or not.
     */
    public boolean joinToEvent(Long userId, Long eventId) {
        User loggedUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
        if (loggedUser.getEvent() != null) {
            return false;
        }
        loggedUser.setEvent(event);
        event.addUser(loggedUser);
        userRepository.save(loggedUser);
        return true;
    }

    /**
     * This function eliminate the relationship between event and user.
     * @param userId The ID of the user.
     * @param eventId The ID of the event.
     * @return True or false depending on whether the user who is logged in already has an event associated with it or not.
     */
    public boolean unjoinToEvent(Long userId, Long eventId) {
        User loggedUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
        if (loggedUser.getEvent() == null) {
            return false;
        }
        loggedUser.setEvent(null);
        event.getUsers().remove(loggedUser);
        userRepository.save(loggedUser);
        return true;
    }

    /**
     * Gets the users that are associated with an event.
     * @param eventId The ID of the event.
     * @return The users associated to the event.
     */
    public List<User> getUsersByEventId(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
        return event.getUsers();
    }

    /**
     * Updates an existing event with new locations.
     * Retrieves the existing event by ID, updates its data, and replaces it in the repository.
     * @param eventId The ID of the event to be updated.
     * @param locations The Set with the ID of the locations.
     * @return The updated event.
     */
    public Event updateEventLocations(Long eventId, Set<Long> locations) {
        Event currentEvent = eventRepository.findById(eventId).orElse(null);
        currentEvent.getLocations().forEach(location -> {
            location.getEvents().remove(currentEvent);
            locationRepository.save(location);
        });
        currentEvent.getLocations().clear();

        //add the new locations to this event
        for (Long id : locations){
            Location l = locationRepository.findById(id).orElse(null);
            if (l != null) {
                currentEvent.getLocations().add(l);
                l.getEvents().add(currentEvent);
            }
            else{
                System.out.println("la localización " + id + " no se ha podido añadir porque no existe");
            }
        }
        return eventRepository.save(currentEvent);
    }

    /**
     * Updates an existing event with new users.
     * Retrieves the existing event by ID, updates its data, and replaces it in the repository.
     * @param eventId The ID of the event to be updated.
     * @param users The Set with the ID of the users.
     * @return The updated event.
     */
    public Event updateEventUsers(Long eventId, Set<Long> users) {
        Event currentEvent = eventRepository.findById(eventId).orElse(null);
        currentEvent.getUsers().forEach(user -> {
            user.setEvent(null);
            userRepository.save(user);
        });
        currentEvent.getUsers().clear();

        //add the new users to this event
        for (Long id : users){
            User u = userRepository.findById(id).orElse(null);
            if (u != null) {
                currentEvent.getUsers().add(u);
                u.setEvent(currentEvent);
            }
            else{
                System.out.println("el usuario " + id + " no se ha podido añadir porque no existe");
            }
        }
        return eventRepository.save(currentEvent);
    }
}