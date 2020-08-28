package alex.ork.springboot.course.service;

import alex.ork.springboot.course.entity.Grammar;

import java.util.List;

public interface GrammarService {
    public List<Grammar> findAll();

    public List<Grammar> findByJlptLvlContains(int jlptLvl);

    public Grammar findById(int id);

    public void save(Grammar grammar);

    public void deleteById(int id);

    public List<Grammar> searchBy(String name);

    public List<Grammar> findAllByUsersDataCollection_Id(int uId);

    public List<Grammar> findAllByGrammarAndLevel(String grammar, String lvl);
}
