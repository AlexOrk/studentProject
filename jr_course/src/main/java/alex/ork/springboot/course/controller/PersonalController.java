package alex.ork.springboot.course.controller;

import alex.ork.springboot.course.entity.Grammar;
import alex.ork.springboot.course.entity.UsersData;
import alex.ork.springboot.course.entity.Word;
import alex.ork.springboot.course.service.GrammarService;
import alex.ork.springboot.course.service.PersonalService;
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
@RequestMapping("/personal")
public class PersonalController {
    // Controller for personal users section

    private Logger logger = LoggerFactory.getLogger(PersonalController.class);
    private UsersDataService usersDataService;
    private WordService wordService;
    private GrammarService grammarService;
    private PersonalService personalService;

    @Autowired
    public PersonalController(UsersDataService usersDataService, WordService wordService,
                              GrammarService grammarService, PersonalService personalService) {
        this.usersDataService = usersDataService;
        this.wordService = wordService;
        this.grammarService = grammarService;
        this.personalService = personalService;
    }

    @GetMapping("/")
    public String personal() {
        logger.info("\"/personal/\"");
        return "personal/personal";
    }

    @GetMapping("/words")
    public String showWords(@AuthenticationPrincipal User user, Model model) {
        logger.info("\"/personal/words\"");

        int uId = usersDataService.findByUsername(user.getUsername()).getId();
        List<Word> wordList = wordService.findAllByUsersDataCollection_Id(uId);
        model.addAttribute("wordList", wordList);

        logger.info("Return \"personal/personal\"");
        return "personal/personal";
    }

    @GetMapping("/grammar")
    public String showGrammar(@AuthenticationPrincipal User user, Model model) {
        logger.info("\"/personal/grammar\"");

        int uId = usersDataService.findByUsername(user.getUsername()).getId();
        logger.info("Searching for grammar by users Id");
        List<Grammar> grammarList = grammarService.findAllByUsersDataCollection_Id(uId);
        model.addAttribute("grammarList", grammarList);

        logger.info("Return \"personal/personal\"");
        return "personal/personal";
    }

    @GetMapping("/search/words")
    public String searchWords(@AuthenticationPrincipal User user,
                         @RequestParam(value = "word", required = false) String word,
                         Model model) {
        logger.info("\"/personal/search/words\"");

        if (word == null || word.trim().isEmpty()) {
            logger.info("Ready to \"redirect:/personal/words\"");
            return "redirect:/personal/words";
        }

        // Search all words by users Id
        int uId = usersDataService.findByUsername(user.getUsername()).getId();
        logger.info("Searching for words by users Id");
        List<Word> personalWordList = wordService.findAllByUsersDataCollection_Id(uId);

        List<Word> wordList =
                personalService.findAllWordsInPersonalWordsList(personalWordList, word);

        model.addAttribute("wordList", wordList);

        logger.info("Return \"personal/personal\"");
        return "personal/personal";

    }

    @GetMapping("/search/grammar")
    public String searchGrammar(@AuthenticationPrincipal User user,
                         @RequestParam(value = "grammar", required = false) String grammar,
                         Model model) {
        logger.info("\"/personal/search/grammar\"");

        if (grammar == null || grammar.trim().isEmpty()) {
            logger.info("Ready to \"redirect:/personal/grammar\"");
            return "redirect:/personal/grammar";
        }

        // Search all grammar by users Id
        int uId = usersDataService.findByUsername(user.getUsername()).getId();
        logger.info("Searching for grammar by users Id");
        List<Grammar> personalGrammarList = grammarService.findAllByUsersDataCollection_Id(uId);
        List<Grammar> grammarList =
                personalService.findAllGrammarInPersonalWordsList(personalGrammarList, grammar);

        model.addAttribute("grammarList", grammarList);

        logger.info("Return \"personal/personal\"");
        return "personal/personal";
    }

    @GetMapping("/deleteWordFromPersonal")
    public String deleteWordFromPersonal(@AuthenticationPrincipal User user,
                                         @RequestParam("wordId") int wordId) {
        logger.info("\"deleteWordFromPersonal\"");
        logger.info("Delete word from personal vocabulary.");

        // find word and usersData
        Word word = wordService.findById(wordId);
        UsersData ud = usersDataService.findByUsername(user.getUsername());

        // delete usersData
        word.deleteUsersData(ud);

        // update word
        wordService.save(word);

        logger.info("Word was deleted from personal list!");
        logger.info("Ready to \"redirect:/personal/words\"");
        return "redirect:/personal/words";
    }

    @GetMapping("/deleteGrammarFromPersonal")
    public String deleteGrammarFromPersonal(@AuthenticationPrincipal User user,
                                            @RequestParam("grammarId") int grammarId) {
        logger.info("\"deleteGrammarFromPersonal\"");
        logger.info("Delete grammar from personal grammar list.");

        // find grammar and usersData
        Grammar grammar = grammarService.findById(grammarId);
        UsersData ud = usersDataService.findByUsername(user.getUsername());

        // delete usersData
        grammar.deleteUsersData(ud);

        // update grammar
        grammarService.save(grammar);

        logger.info("Grammar was deleted from personal list!");
        logger.info("Ready to \"redirect:/personal/grammar\"");
        return "redirect:/personal/grammar";
    }
}
