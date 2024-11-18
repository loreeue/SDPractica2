package SD.SDPractica2.Controllers;

import SD.SDPractica2.Entities.User;
import SD.SDPractica2.Service.EventService;
import SD.SDPractica2.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    /**
     * This method handles GET requests to "/web/userList" endpoint.
     * @param model The model to which users are added.
     * @param session For managing session state between a web client and server.
     * @return The name of the view "ListUser" or redirects to "/web".
     */
    @GetMapping("/web/userList")
    public String userList(Model model, HttpSession session) {
        model.addAttribute("users", userService.obtainAllUsers());
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        model.addAttribute("loggedUser", loggedUser);
        return "ListUser";
    }

    /**
     * This method handles GET requests to "/web/userList/{id}" endpoint.
     * @param id The ID of the user.
     * @param model The model to which users are added.
     * @param session For managing session state between a web client and server.
     * @return The name of the view "ListUser" or redirects to "/web".
     */
    @GetMapping("/web/userList/{id}")
    public String listUsersForEvent(@PathVariable Long id, Model model, HttpSession session) {
        List<User> userList = eventService.getUsersByEventId(id);
        model.addAttribute("users", userList);
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        model.addAttribute("loggedUser", loggedUser);
        return "ListUser";
    }

    /**
     * This method handles GET requests to "/web/addUser" endpoint.
     * @param model The model to which users are added.
     * @return The name of the view "AddUser".
     */
    @GetMapping("/web/addUser")
    public String addUser(Model model) {
        User newUser = new User();
        model.addAttribute("user", newUser);
        return "AddUser";
    }

    /**
     * This method handles POST requests to "/web/addUser" endpoint.
     * @param model The model to which users are added.
     * @param newUser The new user that we add to the model.
     * @param session For managing session state between a web client and server.
     * @return Redirects to "/web/loggedUser/{id}".
     */
    @PostMapping("/web/addUser")
    public String addUser(User newUser, Model model, HttpSession session) {
        userService.addUser(newUser);
        model.addAttribute("success", true);
        session.setAttribute("loggedUser", newUser);
        return "redirect:/web/loggedUser/" + newUser.getId();
    }

    /**
     * This method handles GET requests to "/web/userDetails/{id}" endpoint.
     * @param model The model to which users are added.
     * @param id The ID of the user.
     * @param session For managing session state between a web client and server.
     * @return Redirects to "/web/userList" or the name of the view "DetailUser" or redirects to "/web".
     */
    @GetMapping("/web/userDetails/{id}")
    public String userDetails(@PathVariable Long id, Model model, HttpSession session) {
        User newUser = userService.obtainUser(id);
        if (newUser == null) {
            return "redirect:/web/userList";
        }
        model.addAttribute("user", newUser);
        model.addAttribute("event", userService.obtainUser(id).getEvent());
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        model.addAttribute("loggedUser", loggedUser);
        boolean bol = false;
        if (newUser.getUsername().equals(loggedUser.getUsername())) {
            bol = true;
        }
        model.addAttribute("booleano", bol);
        return "DetailUser";
    }

    /**
     * This method handles GET requests to "/web/editUser/{id}" endpoint.
     * @param model The model to which users are added.
     * @param id The ID of the user.
     * @param session For managing session state between a web client and server.
     * @return Redirects to "/web/userList" or the name of the view "EditUser" or redirects to "/web".
     */
    @GetMapping("/web/editUser/{id}")
    public String editUser(@PathVariable Long id, Model model, HttpSession session) {
        User newUser = userService.obtainUser(id);
        if (newUser == null) {
            return "redirect:/web/userList";
        }
        model.addAttribute("user", newUser);
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/web";
        }
        model.addAttribute("loggedUser", loggedUser);
        return "EditUser";
    }

    /**
     * This method handles POST requests to "/web/editUser" endpoint.
     * @param model The model to which users are added.
     * @param newUser The user that we want to edit.
     * @return Redirects to "/web/userList" or redirects to "/web/userDetails/{id}".
     */
    @PostMapping("/web/editUser")
    public String editUser(User newUser, Model model) {
        User updatedUser = userService.updateUser(newUser.getId(), newUser);
        if (updatedUser == null) {
            return "redirect:/web/userList";
        }
        model.addAttribute("success", true);
        Long id = updatedUser.getId();
        return "redirect:/web/userDetails/" + id;
    }

    /**
     * This method handles GET requests to "/web/deleteUser/{id}" endpoint.
     * @param id The ID of the user.
     * @return Redirects to "/web/userList".
     */
    @GetMapping("/web/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        User newUser = userService.obtainUser(id);
        userService.deleteUser(id);
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (newUser.getUsername().equals(loggedUser.getUsername())) {
            return "redirect:/web";
        }
        return "redirect:/web/userList";
    }

    /**
     * This method handles GET requests to "/web" endpoint.
     * @param model The model to which users are added.
     * @return The name of the view "LogIn".
     */
    @GetMapping("/web")
    public String logIn(Model model) {
        User newUser = new User();
        model.addAttribute("user", newUser);
        return "LogIn";
    }

    /**
     * This method handles POST requests to "/web" endpoint.
     * @param model The model to which users are added.
     * @param newUser The user that we want to know if exists or not.
     * @param session For managing session state between a web client and server.
     * @return The name of the view "LogIn" or redirects to "/web/loggedUser/{id}".
     */
	@PostMapping("/web")
	public String logIn(User newUser, HttpSession session, Model model) {
        User user1 = userService.existsUser(newUser.getUsername(), newUser.getPassword());
        if (!userService.logIn(user1)) {
            model.addAttribute("error", true);
            return "LogIn";
        }
        session.setAttribute("loggedUser", user1);
        model.addAttribute("success", true);
        return "redirect:/web/loggedUser/" + user1.getId();
    }

    /**
     * This method handles GET requests to "/web/loggedUser/{id}" endpoint.
     * @param model The model to which users are added.
     * @param id The ID of the user.
     * @param session For managing session state between a web client and server.
     * @return The name of the view "MainScreen".
     */
	@GetMapping("/web/loggedUser/{id}")
    public String loggedUser(@PathVariable Long id, HttpSession session, Model model){
        User loggedUser = userService.obtainUser(id);
        session.setAttribute("loggedUser", loggedUser);
        model.addAttribute("loggedUser", loggedUser);
        return "MainScreen";
    }
}