package alex.ork.springboot.course.service;

import alex.ork.springboot.course.dao.GrammarRepository;
import alex.ork.springboot.course.entity.Grammar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GrammarServiceImpl implements GrammarService {

    private Logger logger = LoggerFactory.getLogger(GrammarServiceImpl.class);
    private GrammarRepository grammarRepository;

    @Autowired
    public GrammarServiceImpl(GrammarRepository grammarRepository) {
        this.grammarRepository = grammarRepository;
    }

    @Override
    public List<Grammar> findAll() {
        logger.info("\"findAll()\"");
        logger.info("Find all grammar in DB.");
        return grammarRepository.findAll();
    }

    @Override
    public List<Grammar> findByJlptLvlContains(int jlptLvl) {
        logger.info("\"findAllByLevelContains(jlptLvl)\"");
        logger.info("Find all words in DB depending on the \"" + jlptLvl + "\" jlptLvl.");
        return grammarRepository.findAllByJlptLvlContains(jlptLvl);
    }

    @Override
    public Grammar findById(int id) {
        logger.info("\"findById(id)\"");
        logger.info("Find grammar by id.");
        Optional<Grammar> result = grammarRepository.findById(id);

        Grammar grammar = null;

        if (result.isPresent()) {
            grammar = result.get();
        } else {
            logger.warn("Grammar not found.");
            throw new RuntimeException("Грамматика с id - " + id + " не была найдена.");
        }

        logger.info("Return grammar.");
        return grammar;
    }

    @Override
    public void save(Grammar grammar) {
        logger.info("\"save(grammar)\"");
        logger.info("Save a grammar in DB.");
        grammarRepository.save(grammar);
    }

    @Override
    public void deleteById(int id) {
        logger.info("\"deleteById(id)\"");
        logger.info("Delete a grammar in DB by Id.");
        grammarRepository.deleteById(id);
    }

    @Override
    public List<Grammar> searchBy(String grammar) {
        logger.info("\"searchBy(grammar)\"");
        logger.info("Search a grammar in DB.");
        List<Grammar> results = null;

        if (grammar != null && (grammar.trim().length() > 0)) {
            grammar = grammar.trim();
            logger.info("Search a grammar \"" + grammar + "\" in DB by jp or rus title (\"grammarRepository.save(grammar)\")");
            results = grammarRepository.findByFormulaContainsOrExampleContainsOrDescriptionContainsAllIgnoreCase(
                    grammar, grammar, grammar);
        }

        if (results.isEmpty()) {
            results = null;
            logger.info("results = null");
        }

        logger.info("Return grammar list.");
        return results;
    }

    @Override
    public List<Grammar> findAllByUsersDataCollection_Id(int uId) {
        logger.info("\"findAllByUsersDataCollection_Id(id)\"");
        logger.info("Find all grammar by UsersData Id.");
        return grammarRepository.findAllByUsersDataCollection_Id(uId);
    }

    @Override
    public List<Grammar> findAllByGrammarAndLevel(String grammar, String lvl) {
        logger.info("\"findAllByGrammarAndLevel(grammar, lvl)\"");
        logger.info("Find all grammar by grammar and lvl.");

        // searching by grammar
        List<Grammar> grammarList = searchBy(grammar);
        List<Grammar> grListByGrammarAndLevel = new ArrayList<>();

        // searching by lvl
        for (Grammar g : grammarList) {
            if (g.getJlptLvl().equals(lvl))
                grListByGrammarAndLevel.add(g);
        }

        if (grListByGrammarAndLevel.isEmpty()) {
            grListByGrammarAndLevel = null;
            logger.info("grammarList = null");
        }

        logger.info("Return grammar list.");
        return grListByGrammarAndLevel;
    }
}
