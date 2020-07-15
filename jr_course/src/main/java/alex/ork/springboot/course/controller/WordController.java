package alex.ork.springboot.course.controller;

import alex.ork.springboot.course.entity.Word;
import alex.ork.springboot.course.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/words")
public class WordController {

    private Logger logger = LoggerFactory.getLogger(WordController.class);
    private WordService wordService;

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

//    @GetMapping("/words")
//    public String level(@RequestParam(value = "lvl", required = false) String lvl, Model model) {
//        String level = "";
//
//        if (lvl == null) return "vocabulary/vocabulary";
//
//        else if (lvl.equals("easy")) level = "easy";
//        else if (lvl.equals("normal")) level = "normal";
//        else if (lvl.equals("hard")) level = "hard";
//
//        List<Word> words = wordService.findByLevelContains(level);
//        for (Word word : words) {
//            String text = word.getDescription().replaceAll("\n", "separator");
//            word.setDescription(text);
//        }
//        model.addAttribute("words", words);
//        return "vocabulary/words";
//    }

    @GetMapping("/")
    public String words(Model model) {
        logger.info("\"/words/\"");

        List<Word> words = wordService.findAll();

        for (Word word : words) {
            String text = word.getDescription().replaceAll("\n", "separator");
            word.setDescription(text);
        }
        model.addAttribute("words", words);

        logger.info("Ready to return \"vocabulary/words\"");
        return "vocabulary/words";
    }

    @GetMapping("/easy")
    public String showEasy(Model model) {
        logger.info("\"/words/easy\"");

        List<Word> words = wordService.findByLevelContains("easy");
        for (Word word : words) {
            String text = word.getDescription().replaceAll("\n", "separator");
            word.setDescription(text);
        }
        model.addAttribute("words", words);

        logger.info("Ready to return \"vocabulary/words\"");
        return "vocabulary/words";
    }

    @GetMapping("/normal")
    public String showNormal(Model model) {
        logger.info("\"/words/normal\"");

        List<Word> words = wordService.findByLevelContains("normal");
        for (Word word : words) {
            String text = word.getDescription().replaceAll("\n", "separator");
            word.setDescription(text);
        }
        model.addAttribute("words", words);

        logger.info("Ready to return \"vocabulary/words\"");
        return "vocabulary/words";
    }

    @GetMapping("/hard")
    public String showHard(Model model) {
        logger.info("\"/words/hard\"");

        List<Word> words = wordService.findByLevelContains("hard");
        for (Word word : words) {
            String text = word.getDescription().replaceAll("\n", "separator");
            word.setDescription(text);
        }
        model.addAttribute("words", words);

        logger.info("Ready to return \"vocabulary/words\"");
        return "vocabulary/words";
    }

    @GetMapping("/showFormForAddWord")
    public String showFormForAddWord(@RequestParam("lvl") String lvl, Model model) {
        logger.info("\"/words/showFormForAddWord\"");
        Word word = new Word();
        word.setLevel(lvl);
        model.addAttribute("word", word);

        logger.info("Ready to return \"vocabulary/word-form\"");
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

        logger.info("Ready to \"redirect:/words/\"");
        return "redirect:/words/";
    }

    @GetMapping("/deleteWord")
    public String deleteWord(@RequestParam("wordId") int wordId) {
        logger.info("\"/words/deleteWord\"");

        wordService.deleteById(wordId);

        logger.info("Ready to \"redirect:/words/\"");
        return "redirect:/words/";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "word", required = false) String word,
                         Model model) {
        logger.info("\"/words/search\"");

        if (word == null || word.trim().isEmpty()) {
            logger.info("Ready to \"redirect:/words/\"");
            return "redirect:/words/";
        }

        List<Word> words = wordService.searchBy(word);

        if (words.isEmpty()) {
            words = null;
            logger.info("words = null");
        }

        model.addAttribute("words", words);

        logger.info("Ready to return \"vocabulary/words\"");
        return "/vocabulary/words";

    }

    @GetMapping("/search/easy")
    public String searchEasy(@RequestParam(value = "word", required = false) String word,
                             Model model) {
        logger.info("\"/words/search/easy\"");

        if (word == null || word.trim().isEmpty()) {
            logger.info("Ready to \"redirect:/words/\"");
            return "redirect:/words/easy";
        }
        List<Word> words = wordService.searchBy(word);
        List<Word> foundedListByWordAndLevel = new ArrayList<>(words.size());
        for (Word w : words) {
            if (w.getLevel().equals("easy"))
                foundedListByWordAndLevel.add(w);
        }
        if (foundedListByWordAndLevel.isEmpty()) {
            words = null;
            logger.info("words = null");
        } else {
            words = foundedListByWordAndLevel;
            logger.info("words = foundedListByWordAndLevel");
        }
        model.addAttribute("words", words);

        logger.info("Ready to return \"vocabulary/words\"");
        return "/vocabulary/words";
    }

    @GetMapping("/search/normal")
    public String searchNormal(@RequestParam(value = "word", required = false) String word,
                               Model model) {
        logger.info("\"/words/search/normal\"");

        if (word == null || word.trim().isEmpty()) {
            logger.info("Ready to \"redirect:/words/\"");
            return "redirect:/words/normal";
        }
        List<Word> words = wordService.searchBy(word);
        List<Word> foundedListByWordAndLevel = new ArrayList<>(words.size());
        for (Word w : words) {
            if (w.getLevel().equals("normal"))
                foundedListByWordAndLevel.add(w);
        }
        if (foundedListByWordAndLevel.isEmpty()) {
            words = null;
            logger.info("words = null");
        } else {
            words = foundedListByWordAndLevel;
            logger.info("words = foundedListByWordAndLevel");
        }
        model.addAttribute("words", words);

        logger.info("Ready to return \"vocabulary/words\"");
        return "/vocabulary/words";
    }

    @GetMapping("/search/hard")
    public String searchHard(@RequestParam(value = "word", required = false) String word,
                             Model model) {
        logger.info("\"/words/search/hard\"");

        if (word == null || word.trim().isEmpty()) {
            logger.info("Ready to \"redirect:/words/\"");
            return "redirect:/words/hard";
        }
        List<Word> words = wordService.searchBy(word);
        List<Word> foundedListByWordAndLevel = new ArrayList<>(words.size());
        for (Word w : words) {
            if (w.getLevel().equals("hard"))
                foundedListByWordAndLevel.add(w);
        }
        if (foundedListByWordAndLevel.isEmpty()) {
            words = null;
            logger.info("words = null");
        } else {
            words = foundedListByWordAndLevel;
            logger.info("words = foundedListByWordAndLevel");
        }
        model.addAttribute("words", words);

        logger.info("Ready to return \"vocabulary/words\"");
        return "/vocabulary/words";
    }
}
