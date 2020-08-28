package alex.ork.springboot.course.controller;

import alex.ork.springboot.course.entity.UsersData;
import alex.ork.springboot.course.entity.Word;
import alex.ork.springboot.course.service.UsersDataService;
import alex.ork.springboot.course.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/words")
public class WordController {
    // Controller for words section

    private Logger logger = LoggerFactory.getLogger(WordController.class);
    private WordService wordService;
    private UsersDataService usersDataService;

    @Autowired
    public WordController(WordService wordService, UsersDataService usersDataService) {
        this.wordService = wordService;
        this.usersDataService = usersDataService;
    }

    @GetMapping("/")
    public String words(@AuthenticationPrincipal User user, Model model) {
        logger.info("\"/words/\"");

        // check rolls
        if (user != null && user.getAuthorities().toString().contains("USER")) {
            UsersData ud = usersDataService.findByUsername(user.getUsername());
            model.addAttribute("ud", ud);
        }
        List<Word> words = wordService.findAll();
        model.addAttribute("words", words);
        model.addAttribute("hasLvl", false);

        logger.info("Return \"vocabulary/words\"");
        return "vocabulary/words";
    }


    @GetMapping("/lvl/{lvl}")
    public String showEasy(@AuthenticationPrincipal User user,
                           @PathVariable String lvl,
                           Model model) {
        logger.info("\"/words/lvl/" + lvl + "\"");

        // check rolls
        if (user != null && user.getAuthorities().toString().contains("USER")) {
            UsersData ud = usersDataService.findByUsername(user.getUsername());
            model.addAttribute("ud", ud);
        }
        List<Word> words = wordService.findByLevelContains(lvl);
        model.addAttribute("words", words);
        model.addAttribute("hasLvl", true);

        logger.info("Return \"vocabulary/words\"");
        return "vocabulary/words";
    }

    @GetMapping("/showFormForAddWord")
    public String showFormForAddWord(@RequestParam("lvl") String lvl, Model model) {
        logger.info("\"/words/showFormForAddWord\"");

        Word word = new Word();
        word.setLevel(lvl);
        model.addAttribute("word", word);

        logger.info("Return \"vocabulary/word-form\"");
        return "vocabulary/word-form";
    }

    @GetMapping("/showFormForUpdateWord")
    public String showFormForUpdateWord(@RequestParam("wordId") int wordId,
                                        @RequestParam("lvl") String lvl, Model model) {
        logger.info("\"/words/showFormForUpdateWord\"");

        Word word = wordService.findById(wordId);
        word.setLevel(lvl);
        model.addAttribute("word", word);

        logger.info("Ready to return \"vocabulary/word-form\"");
        return "vocabulary/word-form";
    }

    @PostMapping("/saveWord")
    public String saveWord(@ModelAttribute("word") Word word) {
        logger.info("\"/words/saveWord\"");

        wordService.save(word);

        logger.info("Word was saved!");
        logger.info("Ready to \"redirect:/words/\"");
        return "redirect:/words/";
    }

    @GetMapping("/deleteWord")
    public String deleteWord(@RequestParam("wordId") int wordId) {
        logger.info("\"/words/deleteWord\"");

        wordService.deleteById(wordId);

        logger.info("Word was deleted!");
        logger.info("Ready to \"redirect:/words/\"");
        return "redirect:/words/";
    }

    @GetMapping("/search")
    public String search(@AuthenticationPrincipal User user,
                         @RequestParam(value = "word", required = false) String word,
                         Model model) {
        logger.info("\"/words/search\"");

        if (word == null || word.trim().isEmpty()) {
            logger.info("Ready to \"redirect:/words/\"");
            return "redirect:/words/";
        }

        // check rolls
        if (user != null && user.getAuthorities().toString().contains("USER")) {
            UsersData ud = usersDataService.findByUsername(user.getUsername());
            model.addAttribute("ud", ud);
        }

        List<Word> words = wordService.searchBy(word);
        model.addAttribute("words", words);
        model.addAttribute("hasLvl", false);

        logger.info("Ready to return \"vocabulary/words\"");
        return "vocabulary/words";
    }


    @GetMapping("/search/lvl/{lvl}")
    public String searchEasy(@AuthenticationPrincipal User user,
                             @RequestParam(value = "word", required = false) String word,
                             @PathVariable String lvl,
                             Model model) {
        logger.info("\"/words/search/lvl/" + lvl + "\"");

        if (word == null || word.trim().isEmpty()) {
            logger.info("Ready to \"redirect:/words/lvl/" + lvl + "\"");
            return "redirect:/words/lvl/" + lvl;
        }

        // check rolls
        if (user != null && user.getAuthorities().toString().contains("USER")) {
            UsersData ud = usersDataService.findByUsername(user.getUsername());
            model.addAttribute("ud", ud);
        }

        List<Word> words = wordService.findAllByWordAndLevel(word, lvl);
        model.addAttribute("words", words);
        model.addAttribute("hasLvl", true);

        logger.info("Ready to return \"vocabulary/words\"");
        return "vocabulary/words";
    }

    @GetMapping("/addWordInPersonal")
    public String addWordInPersonal(@AuthenticationPrincipal User user,
                                    @RequestParam("wordId") int wordId,
                                    @RequestParam("hasLvl") boolean hasLvl,
                                    @RequestParam("lvl") String lvl) {
        logger.info("\"addWordInPersonal\"");
        logger.info("Add a word in personal vocabulary.");

        // find word and usersData
        Word word = wordService.findById(wordId);
        UsersData ud = usersDataService.findByUsername(user.getUsername());

        // add usersData
        word.addUsersData(ud);

        // update word
        wordService.save(word);
        logger.info("Word was added in personal list!");

        // if it is a lvl page, redirect to lvl page
        if (hasLvl) return "redirect:/words/lvl/" + lvl;

        logger.info("Ready to \"redirect:/words/\"");
        return "redirect:/words/";
    }

    @GetMapping("/deleteWordFromPersonal")
    public String deleteWordFromPersonal(@AuthenticationPrincipal User user,
                                         @RequestParam("wordId") int wordId,
                                         @RequestParam("hasLvl") boolean hasLvl,
                                         @RequestParam("lvl") String lvl) {
        logger.info("\"addWordInPersonal\"");
        logger.info("Delete word from personal vocabulary into.");

        // find word and usersData
        Word word = wordService.findById(wordId);
        UsersData ud = usersDataService.findByUsername(user.getUsername());

        // delete usersData
        word.deleteUsersData(ud);

        // update word
        wordService.save(word);
        logger.info("Word was deleted from personal list!");

        // if it is a lvl page, redirect to lvl page
        if (hasLvl) return "redirect:/words/lvl/" + lvl;

        logger.info("Ready to \"redirect:/words/\"");
        return "redirect:/words/";
    }
}
