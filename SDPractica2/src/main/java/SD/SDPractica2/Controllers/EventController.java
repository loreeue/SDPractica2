package SD.SDPractica2.Controllers;

import SD.SDPractica2.Entities.Event;
import SD.SDPractica2.Entities.Location;
import SD.SDPractica2.Entities.User;
import SD.SDPractica2.Service.EventService;
import SD.SDPractica2.Service.LocationService;
import SD.SDPractica2.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@SessionAttributes("loggedUser")
@Controller
public class EventController {
    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    /**
     * This method handles GET requests to "/web/listEvent" endpoint.
     * @param model The model to which events are added.
     * @param session For managing session state between a web client and server.
     * @return The name of the view "ListEvent" or redirects to "/web".
     */
    @GetMapping("/web/listEvent")
    public String listEvents(Model model, HttpSession session) {
        model.addAttribute("event", eventService.obtainAllEvents());
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        //we bring it updated
        loggedUser = userService.obtainUser(loggedUser.getId());
        session.setAttribute("loggedUser", loggedUser);
        model.addAttribute("loggedUser", loggedUser);
        return "ListEvent";
    }

    /**
     * This method handles GET requests to "/web/listEventLocation/{id}" endpoint.
     * @param id The ID of the event.
     * @param model The model to which events are added.
     * @param session For managing session state between a web client and server.
     * @return The name of the view "ListLocation" or redirects to "/web".
     */
    @GetMapping("/web/listEventLocation/{id}")
    public String listEventsLocations(@PathVariable Long id, Model model, HttpSession session) {
        model.addAttribute("location", eventService.obtainAllLocations(id));
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        model.addAttribute("loggedUser", loggedUser);
        return "ListLocation";
    }

    /**
     * This method handles GET requests to "/web/addEvent" endpoint.
     * @param model The model to which events are added.
     * @param session For managing session state between a web client and server.
     * @return The name of the view "AddEvent" or redirects to "/web".
     */
    @GetMapping("/web/addEvent")
    public String addEvent(Model model, HttpSession session) {
        Event newEvent = new Event();
        model.addAttribute("event", newEvent);
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("location", locationService.obtainAllLocations());
        return "AddEvent";
    }

    /**
     * This method handles POST requests to "/web/addEvent" endpoint.
     * @param model The model to which events are added.
     * @param newEvent The new event that we add to the model.
     * @param selectedLocations The set of the IDs of the locations that we are going to link with one event.
     * @return Redirects to "/web/listEvent".
     */
    @PostMapping("/web/addEvent")
    public String addEvent(Event newEvent, Model model, @RequestParam(value = "selectedLocations", required = false) Set<Long> selectedLocations) {
        Set<Location> listLocations = new HashSet<>();
        for (Long id : selectedLocations){
            Location location = locationService.obtainLocation(id);
            listLocations.add(location);
            location.getEvents().add(newEvent);
        }
        newEvent.setLocations(listLocations);
        eventService.addEvent(newEvent);
        model.addAttribute("success", true);
        return "redirect:/web/listEvent";
    }

    /**
     * This method handles GET requests to "/web/eventDetails/{id}" endpoint.
     * @param model The model to which events are added.
     * @param id The ID of the event.
     * @param session For managing session state between a web client and server.
     * @return Redirects to "/web/listEvent" or the name of the view "DetailEvent" or redirects to "/web".
     */
    @GetMapping("/web/eventDetails/{id}")
    public String eventDetails(@PathVariable Long id, HttpSession session, Model model) {
        Event newEvent = eventService.obtainEvent(id);
        if (newEvent == null) {
            return "redirect:/web/listEvent";
        }
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        //we bring it updated
        loggedUser = userService.obtainUser(loggedUser.getId());
        session.setAttribute("loggedUser", loggedUser);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("event", newEvent);
        return "DetailEvent";
    }

    /**
     * This method handles GET requests to "/web/joinEvent/{eventId}" endpoint.
     * @param model The model to which events are added.
     * @param eventId The ID of the event.
     * @param session For managing session state between a web client and server.
     * @return Redirects to "/web/eventDetails/{eventId}" or to "/web".
     */
    @GetMapping("/web/joinEvent/{eventId}")
    public String joinToEvent(@PathVariable Long eventId, HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        boolean b = eventService.joinToEvent(loggedUser.getId(), eventId);
        //we bring it updated
        loggedUser = userService.obtainUser(loggedUser.getId());
        session.setAttribute("loggedUser", loggedUser);
        model.addAttribute("loggedUser", loggedUser);
        //in userService we return false if there is already an associated event
        if(!b){
            model.addAttribute("error", true);
        }
        return "redirect:/web/eventDetails/" + eventId;
    }

    /**
     * This method handles GET requests to "/web/unjoinEvent/{eventId}" endpoint.
     * @param model The model to which events are added.
     * @param eventId The ID of the event.
     * @param session For managing session state between a web client and server.
     * @return Redirects to "/web/eventDetails/{eventId}" or to "/web".
     */
    @GetMapping("/web/unjoinEvent/{eventId}")
    public String unjoinToEvent(@PathVariable Long eventId, HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        eventService.unjoinToEvent(loggedUser.getId(), eventId);
        //we bring it updated
        loggedUser = userService.obtainUser(loggedUser.getId());
        session.setAttribute("loggedUser", loggedUser);
        model.addAttribute("loggedUser", loggedUser);
        return "redirect:/web/eventDetails/" + eventId;
    }

    /**
     * This method handles GET requests to "/web/editEvent/{id}" endpoint.
     * @param model The model to which events are added.
     * @param id The ID of the event.
     * @param session For managing session state between a web client and server.
     * @return Redirects to "/web/listEvent" or the name of the view "EditEvent" or redirects to "/web".
     */
    @GetMapping("/web/editEvent/{id}")
    public String editEvent(@PathVariable Long id, Model model, HttpSession session) {
        Event newEvent = eventService.obtainEvent(id);
        if (newEvent == null) {
            return "redirect:/web/listEvent";
        }
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        loggedUser = userService.obtainUser(loggedUser.getId());
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("location", locationService.obtainAllLocations());
        model.addAttribute("selectedLocations", newEvent.getLocations());
        model.addAttribute("event", newEvent);
        return "EditEvent";
    }

    /**
     * This method handles POST requests to "/web/editEvent" endpoint.
     * @param model The model to which events are added.
     * @param newEvent The event that we want to edit.
     * @param selectedLocations The set of the IDs of the locations that we are going to link with one event.
     * @return Redirects to "/web/error" or redirects to "/web/eventDetails/{id}".
     */
    @PostMapping("/web/editEvent")
    public String editEvent(Event newEvent, Model model, @RequestParam(value = "selectedLocations", required = false) Set<Long> selectedLocations) {
        //get the current event from the database to ensure we are working with the most recent version
        Event currentEvent = eventService.obtainEvent(newEvent.getId());
        if (currentEvent == null) {
            return "redirect:/web/error";
        }
        currentEvent.setName(newEvent.getName());
        currentEvent.setDescription(newEvent.getDescription());
        currentEvent.setDateTime(newEvent.getDateTime());

        eventService.updateEventLocations(currentEvent.getId(), selectedLocations);
        eventService.updateEvent(currentEvent.getId(), currentEvent);
        model.addAttribute("success", true);
        return "redirect:/web/eventDetails/" + currentEvent.getId();
    }

    /**
     * This method handles GET requests to "/web/deleteEvent/{id}" endpoint.
     * @param id The ID of the event.
     * @param session For managing session state between a web client and server.
     * @return Redirects to "/web/listEvent" or to "/web".
     */
    @GetMapping("/web/deleteEvent/{id}")
    public String deleteEvent(@PathVariable Long id, HttpSession session) {
        eventService.deleteEvent(id);
        User loggedUser = (User) session.getAttribute("loggedUser");
        //we bring it updated (maybe the user was linked to the event who has been removed)
        if (loggedUser == null) {
            return "redirect:/web";
        }
        //we bring it updated
        loggedUser = userService.obtainUser(loggedUser.getId());
        session.setAttribute("loggedUser", loggedUser);
        return "redirect:/web/listEvent";
    }

    /**
     * This method handles GET requests to "/web/error" endpoint.
     * @param model The model to which events are added.
     * @param session For managing session state between a web client and server.
     * @return The name of the view "Error" or redirects to "/web".
     */
    @GetMapping("/web/error")
    public String error(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        model.addAttribute("loggedUser", loggedUser);
        return "Error";
    }

    /**
     * This method handles POST requests to "/web/searchEvent" endpoint.
     * @param model The model to which events are added.
     * @param search The string we are searching in the web search.
     * @param session For managing session state between a web client and server.
     * @return The name of the view "ErrorSearch" or "ResultsEvent" or redirects to "/web".
     */
    @PostMapping("/web/searchEvent")
    public String search(String search, Model model, HttpSession session){
        ArrayList<Event> results = eventService.search(search);
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
        return "ResultsEvent";
    }
}