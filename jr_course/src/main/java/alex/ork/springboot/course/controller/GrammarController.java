package alex.ork.springboot.course.controller;

import alex.ork.springboot.course.entity.Grammar;
import alex.ork.springboot.course.entity.UsersData;
import alex.ork.springboot.course.service.GrammarService;
import alex.ork.springboot.course.service.UsersDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/grammar")
public class GrammarController {
    // Controller for grammar section

    private Logger logger = LoggerFactory.getLogger(GrammarController.class);
    private GrammarService grammarService;
    private UsersDataService usersDataService;

    @Autowired
    public GrammarController(GrammarService grammarService, UsersDataService usersDataService) {
        this.grammarService = grammarService;
        this.usersDataService = usersDataService;
    }

    @GetMapping("/")
    public String grammar(@AuthenticationPrincipal User user, Model model) {
        logger.info("\"/grammar/\"");

        // check rolls
        if (user != null && user.getAuthorities().toString().contains("USER")) {
            UsersData ud = usersDataService.findByUsername(user.getUsername());
            model.addAttribute("ud", ud);
        }
        List<Grammar> grammar = grammarService.findAll();
        model.addAttribute("grammar", grammar);
        model.addAttribute("hasLvl", false);

        logger.info("Return \"/grammar/grammar\"");
        return "grammar/grammar";
    }

    @GetMapping("/n{lvl}")
    public String grammarN(@AuthenticationPrincipal User user,
                         @PathVariable Integer lvl, Model model) {
        logger.info("\"/grammar/n\"" + lvl);

        // check rolls
        if (user != null && user.getAuthorities().toString().contains("USER")) {
            UsersData ud = usersDataService.findByUsername(user.getUsername());
            model.addAttribute("ud", ud);
        }
        List<Grammar> grammar = grammarService.findByJlptLvlContains(lvl);
        model.addAttribute("grammar", grammar);
        model.addAttribute("hasLvl", true);

        logger.info("Return \"grammar/grammar\"");
        return "grammar/grammar";
    }

    @GetMapping("/showFormForAddGrammar")
    public String showFormForAddGrammar(@RequestParam("lvl") String lvl, Model model) {
        logger.info("\"/grammar/showFormForAddWord\"");

        Grammar grammar = new Grammar();
        grammar.setJlptLvl(lvl);
        model.addAttribute("grammar", grammar);

        logger.info("Return \"grammar/grammar-form\"");
        return "grammar/grammar-form";
    }

    @GetMapping("/showFormForUpdateGrammar")
    public String showFormForUpdateGrammar(@RequestParam("grammarId") int grammarId,
                                           @RequestParam("lvl") String lvl, Model model) {
        logger.info("\"/grammar/showFormForUpdateGrammar\"");

        Grammar grammar = grammarService.findById(grammarId);
        grammar.setJlptLvl(lvl);
        model.addAttribute("grammar", grammar);

        logger.info("Return \"grammar/grammar-form\"");
        return "grammar/grammar-form";
    }

    @PostMapping("/saveGrammar")
    public String saveGrammar(@ModelAttribute("grammar") Grammar grammar) {
        logger.info("\"/grammar/saveGrammar\"");

        grammarService.save(grammar);

        logger.info("Grammar was saved!");
        logger.info("Ready to \"redirect:/grammar/\"");
        return "redirect:/grammar/";
    }

    @GetMapping("/deleteGrammar")
    public String deleteGrammar(@RequestParam("id") int id) {
        logger.info("\"/grammar/deleteGrammar\"");

        grammarService.deleteById(id);

        logger.info("Grammar was deleted!");
        logger.info("Ready to \"redirect:/grammar/\"");
        return "redirect:/grammar/";
    }

    @GetMapping("/search")
    public String search(@AuthenticationPrincipal User user,
                         @RequestParam(value = "grammar", required = false) String grammar,
                         @RequestParam(value = "lvl", required = false) String lvl,
                         Model model) {
        logger.info("\"/grammar/search\"");

        if (grammar == null || grammar.trim().isEmpty()) {
            logger.info("Ready to \"redirect:/grammar/\"");
            return "redirect:/grammar/";
        }

        // check rolls
        if (user != null && user.getAuthorities().toString().contains("USER")) {
            UsersData ud = usersDataService.findByUsername(user.getUsername());
            model.addAttribute("ud", ud);
        }

        List<Grammar> grammarList = null;
        if (lvl != null) {
            logger.info("Search query was redirected to /grammar/search");
            grammarList = grammarService.findAllByGrammarAndLevel(grammar, lvl);
            model.addAttribute("hasLvl", true);
        } else {
            logger.info("RequestParam \"lvl\" not exist. Searching all grammar.");
            grammarList = grammarService.searchBy(grammar);
            model.addAttribute("hasLvl", false);
        }
        model.addAttribute("grammar", grammarList);

        logger.info("Return \"grammar/grammar\"");
        return "/grammar/grammar";
    }


    @GetMapping("/search/n{lvl}")
    public String searchN1(@RequestParam(value = "grammar", required = false) String grammar,
                           @PathVariable int lvl, RedirectAttributes redirectAttributes) {
        logger.info("\"/grammar/search/n" + lvl + "\"");

        if (grammar == null || grammar.trim().isEmpty()) {
            logger.info("Ready to \"redirect:/grammar/n\"" + lvl);
            return "redirect:/grammar/n" + lvl;
        }

        redirectAttributes.addAttribute("grammar", grammar);
        redirectAttributes.addAttribute("lvl", lvl);

        logger.info("Ready to \"redirect:/grammar/search\"");
        return "redirect:/grammar/search";
    }

    @GetMapping("/addGrammarInPersonal")
    public String addGrammarInPersonal(@AuthenticationPrincipal User user,
                                       @RequestParam("grammarId") int grammarId,
                                       @RequestParam("hasLvl") boolean hasLvl,
                                       @RequestParam(value = "lvl", required = false) String lvl) {
        logger.info("\"addGrammarInPersonal\"");
        logger.info("Add a grammar in personal grammar list.");

        // find grammar and usersData
        Grammar grammar = grammarService.findById(grammarId);
        UsersData ud = usersDataService.findByUsername(user.getUsername());

        // add usersData
        grammar.addUsersData(ud);

        // update grammar
        grammarService.save(grammar);
        logger.info("Grammar was added in personal list!");

        // if it is a lvl page, redirect to lvl page
        if (hasLvl) return "redirect:/grammar/n" + lvl;

        logger.info("Ready to \"redirect:/grammar/\"");
        return "redirect:/grammar/";

    }

    @GetMapping("/deleteGrammarFromPersonal")
    public String deleteGrammarFromPersonal(@AuthenticationPrincipal User user,
                                            @RequestParam("grammarId") int grammarId,
                                            @RequestParam("hasLvl") boolean hasLvl,
                                            @RequestParam(value = "lvl", required = false) String lvl) {
        logger.info("\"addGrammarInPersonal\"");
        logger.info("Delete grammar from personal grammar.");

        // find grammar and usersData
        Grammar grammar = grammarService.findById(grammarId);
        UsersData ud = usersDataService.findByUsername(user.getUsername());

        // delete usersData
        grammar.deleteUsersData(ud);

        // update grammar
        grammarService.save(grammar);
        logger.info("Grammar was deleted from personal list!");

        if (hasLvl) return "redirect:/grammar/n" + lvl;

        logger.info("Ready to \"redirect:/grammar/\"");
        return "redirect:/grammar/";
    }
}
