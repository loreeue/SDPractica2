package SD.SDPractica2.Controllers;

import SD.SDPractica2.Entities.Location;
import SD.SDPractica2.Entities.User;
import SD.SDPractica2.Service.EventService;
import SD.SDPractica2.Service.LocationService;
import SD.SDPractica2.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;

@Controller
public class LocationController {
    @Autowired
    private LocationService locationService;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    /**
     * This method handles GET requests to "/web/listLocation" endpoint.
     * @param model The model to which locations are added.
     * @param session For managing session state between a web client and server.
     * @return The name of the view "ListLocation" or redirects to "/web".
     */
    @GetMapping("/web/listLocation")
    public String listLocation(Model model, HttpSession session) {
        model.addAttribute("location", locationService.obtainAllLocations());
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        //we bring it updated
        loggedUser = userService.obtainUser(loggedUser.getId());
        model.addAttribute("loggedUser", loggedUser);
        return "ListLocation";
    }

    /**
     * This method handles GET requests to "/web/listLocationsEvents/{id}" endpoint.
     * @param id The ID of the location.
     * @param model The model to which locations are added.
     * @param session For managing session state between a web client and server.
     * @return The name of the view "ListEvent" or redirects to "/web".
     */
    @GetMapping("/web/listLocationsEvents/{id}")
    public String listLocationsEvents(@PathVariable Long id, Model model, HttpSession session) {
        model.addAttribute("event", locationService.obtainAllEvents(id));
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        model.addAttribute("loggedUser", loggedUser);
        return "ListEvent";
    }

    /**
     * This method handles GET requests to "/web/addLocation" endpoint.
     * @param model The model to which locations are added.
     * @param session For managing session state between a web client and server.
     * @return The name of the view "AddLocation" or redirects to "/web".
     */
    @GetMapping("/web/addLocation")
    public String addLocation(Model model, HttpSession session) {
        Location newLocation = new Location();
        model.addAttribute("location", newLocation);
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("event", eventService.obtainAllEvents());
        return "AddLocation";
    }

    /**
     * This method handles POST requests to "/web/addLocation" endpoint.
     * @param model The model to which locations are added.
     * @param newLocation The new location that we add to the model.
     * @param selectedEvents The set of the IDs of the events that we are going to link with one location.
     * @return Redirects to "/web/listLocation".
     */
    @PostMapping("/web/addLocation")
    public String addLocation(Location newLocation, Model model, @RequestParam(value = "selectedEvents", required = false) Set<Long> selectedEvents) {
        locationService.addLocation(newLocation);
        if (selectedEvents != null) {
            locationService.updateLocationEvents(newLocation.getId(), selectedEvents);
        }
        model.addAttribute("success", true);
        return "redirect:/web/listLocation";
    }

    /**
     * This method handles GET requests to "/web/locationDetails/{id}" endpoint.
     * @param model The model to which locations are added.
     * @param id The ID of the location.
     * @param session For managing session state between a web client and server.
     * @return Redirects to "/web/listLocation" or the name of the view "DetailLocation" or redirects to "/web".
     */
    @GetMapping("/web/locationDetails/{id}")
    public String locationDetails(@PathVariable Long id, Model model, HttpSession session) {
        Location newLocation = locationService.obtainLocation(id);
        if (newLocation == null) {
            return "redirect:/web/listLocation";
        }
        model.addAttribute("location", newLocation);
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        //we bring it updated
        loggedUser = userService.obtainUser(loggedUser.getId());
        model.addAttribute("loggedUser", loggedUser);
        return "DetailLocation";
    }

    /**
     * This method handles GET requests to "/web/editLocation/{id}" endpoint.
     * @param model The model to which locations are added.
     * @param id The ID of the location.
     * @param session For managing session state between a web client and server.
     * @return Redirects to "/web/listLocation" or the name of the view "EditLocation" or redirects to "/web".
     */
    @GetMapping("/web/editLocation/{id}")
    public String editLocation(@PathVariable Long id, Model model, HttpSession session) {
        Location newLocation = locationService.obtainLocation(id);
        if (newLocation == null) {
            return "redirect:/web/listLocation";
        }
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("event", eventService.obtainAllEvents());
        model.addAttribute("location", newLocation);
        return "EditLocation";
    }

    /**
     * This method handles POST requests to "/web/editLocation" endpoint.
     * @param model The model to which locations are added.
     * @param newLocation The location that we want to edit.
     * @param selectedEvents The set of the IDs of the events that we are going to link with one location.
     * @return Redirects to "/web/error" or redirects to "/web/locationDetails/{id}".
     */
    @PostMapping("/web/editLocation")
    public String editLocation(Location newLocation, Model model, @RequestParam(value = "selectedEvents", required = false) Set<Long> selectedEvents) {
        //get the current location from the database to ensure we are working with the most recent version
        Location currentLocation = locationService.obtainLocation(newLocation.getId());
        if (currentLocation == null) {
            return "redirect:/web/error";
        }
        currentLocation.setName(newLocation.getName());
        currentLocation.setAddress(newLocation.getAddress());
        currentLocation.setCapacity(newLocation.getCapacity());
        currentLocation.setType(newLocation.getType());
        currentLocation.setAccessibility(newLocation.getAccessibility());
        currentLocation.setCity(newLocation.getCity());
        currentLocation.setCountry(newLocation.getCountry());
        currentLocation.setImage(newLocation.getImage());

        locationService.updateLocationEvents(currentLocation.getId(), selectedEvents);
        locationService.updateLocation(currentLocation.getId(), currentLocation);
        model.addAttribute("success", true);
        Long id = currentLocation.getId();
        return "redirect:/web/locationDetails/" + id;
    }

    /**
     * This method handles GET requests to "/web/deleteLocation/{id}" endpoint.
     * @param id The ID of the location.
     * @return Redirects to "/web/listLocation".
     */
    @GetMapping("/web/deleteLocation/{id}")
    public String deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return "redirect:/web/listLocation";
    }

    /**
     * This method handles GET requests to "" endpoint.
     * @param model The model to which locations are added.
     * @return The name of the view "MainScreen".
     */
    @GetMapping("")
    public String search(Model model) {
        String search = null;
        model.addAttribute("search", search);
        return "MainScreen";
    }

    /**
     * This method handles POST requests to "/web/searchLocation" endpoint.
     * @param model The model to which locations are added.
     * @param search The string we are searching in the web search.
     * @param session For managing session state between a web client and server.
     * @return The name of the view "ErrorSearch" or "ResultsLocation" or redirects to "/web".
     */
    @PostMapping("/web/searchLocation")
    public String search(String search, Model model, HttpSession session){
        ArrayList<Location> results = locationService.search(search);
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        model.addAttribute("loggedUser", loggedUser);
        if (results == null) {
            model.addAttribute("error", true);
            return "ErrorSearch";
        }
        model.addAttribute("array", results);
        return "ResultsLocation";
    }
}