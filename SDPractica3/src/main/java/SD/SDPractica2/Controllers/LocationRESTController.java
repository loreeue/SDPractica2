package SD.SDPractica2.Controllers;

import SD.SDPractica2.Entities.Event;
import SD.SDPractica2.Entities.Location;
import SD.SDPractica2.Service.LocationService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/location")
public class LocationRESTController {
	@Autowired
    private LocationService locationService;

    /**
     * This method handles POST requests.
     * Adds a new location received in the request body.
     * @param newLocation The location to be added.
     * @return ResponseEntity with status 201 (Created) and the added location in the body or status 400 (Bad Request).
     */
    @PostMapping
    public ResponseEntity<Location> addLocation(@RequestBody JsonNode newLocation) {
        if (newLocation.get("id") != null || newLocation.get("name") == null || newLocation.get("address") == null || newLocation.get("type") == null || newLocation.get("accessibility") == null || newLocation.get("city") == null || newLocation.get("country") == null || newLocation.get("image") == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String name = newLocation.get("name").asText();
        String address = newLocation.get("address").asText();
        int capacity = newLocation.get("capacity").asInt();
        String type = newLocation.get("type").asText();
        String accessibility = newLocation.get("accessibility").asText();
        String city = newLocation.get("city").asText();
        String country = newLocation.get("country").asText();
        String image = newLocation.get("image").asText();

        //create the "basic" location.
        Location l = locationService.addLocation(new Location(name, address, capacity, type, accessibility, city, country, image));

        //checking for events
        Set<Long> eventsList = locationService.convertJsonToSetLong(newLocation);

        //events
        if (eventsList != null) {
            locationService.updateLocationEvents(l.getId(), eventsList);
        }

        Location location = locationService.updateLocation(l.getId(), l);
        return ResponseEntity.status(201).body(location);
    }

    /**
     * This method handles GET requests.
     * Retrieves a location by its ID.
     * @param id The ID of the location to retrieve.
     * @return ResponseEntity with status 201 (OK) and the location in the body if found, or status 404 (Not Found) if not found.
     */
	@GetMapping("/{id}")
	public ResponseEntity<Location> obtainLocation(@PathVariable Long id){
		Location newLocation = locationService.obtainLocation(id);
		if (newLocation == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(201).body(newLocation);
	}

    /**
     * This method handles GET requests.
     * Retrieves all locations.
     * @return ResponseEntity with status 200 (OK) and a collection of locations in the body.
     */
	@GetMapping
    public ResponseEntity<Collection<Location>> obtainAllLocations() {
        return ResponseEntity.ok(locationService.obtainAllLocations());
    }

    /**
     * This method handles PUT requests.
     * Updates a location with the specified ID using data from the request body.
     * @param id The ID of the location to update.
     * @param updateLocation The updated location data.
     * @return ResponseEntity with status 200 (OK) and the updated location in the body or status 400 (Bad Request) or status 404 (Not Found).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @RequestBody JsonNode updateLocation) {
        Location existingLocation = locationService.obtainLocation(id);
        if (existingLocation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (updateLocation.get("id") != null && !Objects.equals(updateLocation.get("id").asLong(), id) ||
                updateLocation.get("name") == null || updateLocation.get("name").asText().isEmpty() ||
                updateLocation.get("address") == null || updateLocation.get("address").asText().isEmpty() ||
                updateLocation.get("type") == null || updateLocation.get("type").asText().isEmpty() ||
                updateLocation.get("accessibility") == null || updateLocation.get("accessibility").asText().isEmpty() ||
                updateLocation.get("city") == null || updateLocation.get("city").asText().isEmpty() ||
                updateLocation.get("country") == null || updateLocation.get("country").asText().isEmpty() ||
                updateLocation.get("image") == null || updateLocation.get("image").asText().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String name = updateLocation.get("name").asText();
        String address = updateLocation.get("address").asText();
        int capacity = updateLocation.get("capacity").asInt();
        String type = updateLocation.get("type").asText();
        String accessibility = updateLocation.get("accessibility").asText();
        String city = updateLocation.get("city").asText();
        String country = updateLocation.get("country").asText();
        String image = updateLocation.get("image").asText();

        //checking for events
        Set<Long> eventsList = locationService.convertJsonToSetLong(updateLocation);

        //events
        if (!eventsList.isEmpty()) {
            locationService.updateLocationEvents(existingLocation.getId(), eventsList);
        }

        Location updatedLocation = locationService.updateLocation(existingLocation.getId(), new Location(name, address, capacity, type, accessibility, city, country, image));
        return ResponseEntity.status(201).body(updatedLocation);
    }

    /**
     * This method handles PATCH requests.
     * Updates a location with the specified ID using partial data from the request body.
     * @param id The ID of the location to update.
     * @param updateLocation The partial updated location data.
     * @return ResponseEntity with status 200 (OK) and a success message or status 400 (Bad Request) or status 404 (Not Found).
     */
	@PatchMapping("/{id}")
	public ResponseEntity<?> patchLocation (@PathVariable Long id, @RequestBody JsonNode updateLocation) {
        Location existingLocation = locationService.obtainLocation(id);
        if (existingLocation == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (existingLocation.getId() != null && !Objects.equals(existingLocation.getId(), id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String name = updateLocation.has("name") ? updateLocation.get("name").asText() : null;
        String address = updateLocation.has("address") ? updateLocation.get("address").asText() : null;
        int capacity = updateLocation.has("capacity") ? updateLocation.get("capacity").asInt() : 0;
        String type = updateLocation.has("type") ? updateLocation.get("type").asText() : null;
        String accessibility = updateLocation.has("accessibility") ? updateLocation.get("accessibility").asText() : null;
        String city = updateLocation.has("city") ? updateLocation.get("city").asText() : null;
        String country = updateLocation.has("country") ? updateLocation.get("country").asText() : null;
        String image = updateLocation.has("image") ? updateLocation.get("image").asText() : null;

        //checking for events
        Set<Long> eventsList = locationService.convertJsonToSetLong(updateLocation);

        //events
        if (!eventsList.isEmpty()) {
            locationService.updateLocationEvents(existingLocation.getId(), eventsList);
        }

        Location updatedLocation = locationService.patchLocation(id, new Location(name, address, capacity, type, accessibility, city, country, image));
        return ResponseEntity.status(201).body(updatedLocation);
	}

    /**
     * This method handles DELETE requests.
     * Deletes a location with the specified ID.
     * @param id The ID of the location to delete.
     * @return ResponseEntity with status 200 (OK) and a success message or status 404 (Not Found) if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable Long id) {
        if (locationService.obtainLocation(id) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        locationService.deleteLocation(id);
        return ResponseEntity.ok("Localizacion con ID: " + id + " eliminada con éxito");
    }

    /**
     * This method handles GET requests.
     * Obtains all the events associated to one location.
     * @param id The ID of the location.
     * @return ResponseEntity with status 200 (OK).
     */
    @GetMapping("/{id}/events")
    public ResponseEntity<Collection<Event>> obtainAllEvents(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.obtainAllEvents(id));
    }
}