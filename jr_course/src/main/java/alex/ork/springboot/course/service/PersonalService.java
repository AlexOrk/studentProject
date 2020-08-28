package alex.ork.springboot.course.service;

import alex.ork.springboot.course.entity.Grammar;
import alex.ork.springboot.course.entity.Word;

import java.util.List;

public interface PersonalService {
    public List<Word> findAllWordsInPersonalWordsList(List<Word> words, String word);

    public List<Grammar> findAllGrammarInPersonalWordsList(List<Grammar> grammarList, String gramar);
}
