package alex.ork.springboot.course.controller;

import alex.ork.springboot.course.entity.UsersData;
import alex.ork.springboot.course.service.UsersDataService;
import alex.ork.springboot.course.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/users")
public class AdminController {
    // Controller for admin section

    private Logger logger = LoggerFactory.getLogger(AdminController.class);
    private UsersDataService usersDataService;
    private WordService wordService;

    @Autowired
    public AdminController(UsersDataService usersDataService, WordService wordService) {
        this.usersDataService = usersDataService;
        this.wordService = wordService;
    }


    @GetMapping("/showUsers")
    public String showUsers(Model model, @AuthenticationPrincipal User user) {
        logger.info("\"/users/showUsers\"");

        List<UsersData> users = usersDataService.findAll(user);
        model.addAttribute("users", users);

        logger.info("Return \"users/users\"");
        return "users/users";
    }

    @GetMapping("/deleteUser")
    public String deleteUsers(@RequestParam("userId") int userId) {
        logger.info("\"/users/deleteUser\"");

        usersDataService.deleteById(userId);

        logger.info("Ready to \"redirect:/users/showUsers\"");
        return "redirect:/users/showUsers";
    }

    @GetMapping("/searchUser")
    public String search(@RequestParam(value = "user", required = false) String user,
                         Model model) {
        logger.info("\"/users/searchUser\"");

        if (user == null || user.trim().isEmpty()) {
            logger.info("Ready to \"redirect:/users/showUsers\"");
            return "redirect:/users/showUsers";
        }

        List<UsersData> users = usersDataService.findUser(user);
        model.addAttribute("users", users);

        logger.info("Return \"users/users\"");
        return "/users/users";
    }

    @GetMapping("/deleteAllUsers")
    public String deleteAllUsers(@AuthenticationPrincipal User user) {
        logger.info("\"/users/deleteAllUsers\"");

        int id = usersDataService.findByUsername(user.getUsername()).getId();

        usersDataService.deleteAllExceptAdmin(id);

        logger.info("Ready to \"redirect:/users/showUsers\"");
        return "redirect:/users/showUsers";
    }
}
