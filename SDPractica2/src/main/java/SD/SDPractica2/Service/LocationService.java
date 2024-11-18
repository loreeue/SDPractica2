package SD.SDPractica2.Service;

import SD.SDPractica2.Entities.Event;
import SD.SDPractica2.Entities.Location;
import SD.SDPractica2.Repositories.EventRepository;
import SD.SDPractica2.Repositories.LocationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.Normalizer;
import java.util.*;
import java.time.LocalDateTime;

@Service
public class LocationService { // In this service we use LocationRepository and EventRepository
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EventRepository eventRepository;

    public LocationService(){
    }

    /**
     * Adds a new location to the location repository.
     * Sets the ID, creation and update timestamps, and adds the location to the repository.
     * @param newLocation The location to be added.
     * @return The added location.
     */
    public Location addLocation(Location newLocation) {
        newLocation.setUpdatedAt(LocalDateTime.now());
        newLocation.setCreatedAt(LocalDateTime.now());
        locationRepository.save(newLocation);
        return newLocation;
    }

    /**
     * Updates an existing location partially using patch data.
     * Retrieves the existing location by ID, applies patch data, and updates the location in the repository.
     * @param id The ID of the location to be updated.
     * @param patchLocation The partial data to update the location.
     * @return The updated location.
     */
    public Location patchLocation(Long id, Location patchLocation) {
        Location preLocation = locationRepository.findById(id).orElse(null);

        if (preLocation == null) {
            return null;
        }

        if (patchLocation.getName() != null && !patchLocation.getName().equals(preLocation.getName())) {
            preLocation.setName(patchLocation.getName());
        }

        if (patchLocation.getAddress() != null && !patchLocation.getAddress().equals(preLocation.getAddress())) {
            preLocation.setAddress(patchLocation.getAddress());
        }

        if (patchLocation.getCapacity() != 0 && patchLocation.getCapacity() != preLocation.getCapacity()) {
            preLocation.setCapacity(patchLocation.getCapacity());
        }

        if (patchLocation.getType() != null && !patchLocation.getType().equals(preLocation.getType())) {
            preLocation.setType(patchLocation.getType());
        }

        if (patchLocation.getAccessibility() != null && !patchLocation.getAccessibility().equals(preLocation.getAccessibility())) {
            preLocation.setAccessibility(patchLocation.getAccessibility());
        }

        if (patchLocation.getCity() != null && !patchLocation.getCity().equals(preLocation.getCity())) {
            preLocation.setCity(patchLocation.getCity());
        }

        if (patchLocation.getCountry() != null && !patchLocation.getCountry().equals(preLocation.getCountry())) {
            preLocation.setCountry(patchLocation.getCountry());
        }

        if (patchLocation.getImage() != null && !patchLocation.getImage().equals(preLocation.getImage())) {
            preLocation.setImage(patchLocation.getImage());
        }

        preLocation.setUpdatedAt(LocalDateTime.now());
        locationRepository.save(preLocation);
        return preLocation;
    }

    /**
     * Retrieves a location by its ID from the location repository.
     * @param id The ID of the location to retrieve.
     * @return The location with the specified ID, or null if not found.
     */
	public Location obtainLocation (Long id){
		return locationRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all locations from the location repository.
     * @return A collection of all locations.
     */
	public Collection<Location> obtainAllLocations() {
        return locationRepository.findAll();
    }

    /**
     * Retrieves the events that are associated to a location with a specific ID.
     * @param id The ID of the location to retrieve.
     * @return A collection of events.
     */
    public Collection<Event> obtainAllEvents(Long id) {
        Location location = obtainLocation(id);
        return location.getEvents();
    }

    /**
     * Updates an existing location completely using new data.
     * Retrieves the existing location by ID, updates its data, and replaces it in the repository.
     * @param id The ID of the location to be updated.
     * @param updateLocation The new data for the location.
     * @return The updated location.
     */
    public Location updateLocation(Long id, Location updateLocation) {
        Location existingLocation = locationRepository.findById(id).orElse(null);
        if (existingLocation == null) {
            return null;
        }
        updateLocation.setId(id);
        updateLocation.setCreatedAt(locationRepository.findById(id).get().getCreatedAt());
        updateLocation.setUpdatedAt(LocalDateTime.now());
        updateLocation.setEvents(existingLocation.getEvents());
        locationRepository.save(updateLocation);
        return updateLocation;
    }

    /**
     * Deletes a location from the location repository by its ID.
     * @param id The ID of the location to delete.
     */
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
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
     * Searches for locations based on a given string.
     * Normalizes the search string and compares it with location names, cities, countries and addresses.
     * @param s The search string.
     * @return An ArrayList of locations matching the search criteria.
     */
    public ArrayList<Location> search(String s) {
        ArrayList<Location> results = new ArrayList<>();
        String string = normalizeString(s.toUpperCase(Locale.ROOT));
        if (s.isEmpty()){
            return null;
        }
        for (Location l : obtainAllLocations()) {
            if (normalizeString(l.getCity().toUpperCase()).contains(string) || normalizeString(l.getCountry().toUpperCase()).contains(string) || normalizeString(l.getName().toUpperCase()).contains(string) || normalizeString(l.getAddress().toUpperCase()).contains(string)) {
                results.add(l);
            }
        }
        if (results.isEmpty()) {
            return null;
        }
        return results;
    }

    /**
     * Updates an existing location with new events.
     * Retrieves the existing location by ID, updates its data, and replaces it in the repository.
     * @param locationId The ID of the location to be updated.
     * @param eventsLong The IDs of the events.
     * @return The updated location.
     */
    public Location updateLocationEvents(Long locationId, Set<Long> eventsLong) {
        Location location = locationRepository.findById(locationId).orElse(null);
        location.getEvents().clear();
        if (eventsLong != null) {
            Set<Event> events  = new HashSet<>();
            for (Long id : eventsLong){
                Event event = eventRepository.findById(id).orElse(null);
                if (event != null) {
                    events.add(event);
                }
                else{
                    System.out.println("el evento " + id + " no se ha podido añadir porque no existe");
                }
            }
            location.setEvents(events);
        }
        return locationRepository.save(location);
    }

    /**
     * Convert the JSON node to two Set<Long> and creates a map with them.
     * Retrieves a map with 2 elements: "locations" and "users".
     * @param jsonNode The JSON node with "locations" and "users" attributes.
     * @return The map.
     */
    public Set<Long> convertJsonToSetLong(JsonNode jsonNode){
        Set<Long> eventsIds = new HashSet<>();
        JsonNode eventsNode = jsonNode.get("events");
        if (eventsNode != null && !eventsNode.isEmpty()) {
            for (JsonNode id : eventsNode) {
                eventsIds.add(id.asLong());
            }
        }
        return eventsIds;
    }
}