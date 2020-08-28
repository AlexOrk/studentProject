package alex.ork.springboot.course.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    // Login controller

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/showLoginPage")
    public String showLoginPage() {
        logger.info("\"/showLoginPage\"");
        return "plain-login";
    }
}
