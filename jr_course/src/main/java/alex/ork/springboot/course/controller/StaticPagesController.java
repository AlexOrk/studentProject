package alex.ork.springboot.course.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticPagesController {
    // Controller for Static pages

    private Logger logger = LoggerFactory.getLogger(StaticPagesController.class);

    @GetMapping("/jlpt")
    public String jlpt() {
        logger.info("\"/jlpt\"");
        return "jlpt/jlpt";
    }

    @GetMapping("/jrCourse")
    public String jrCourse() {
        logger.info("\"/jrCourse\"");
        return "jrCourse/jrCourse";
    }
}
