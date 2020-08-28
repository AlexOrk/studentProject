package alex.ork.springboot.course.service;

import alex.ork.springboot.course.dao.WordRepository;
import alex.ork.springboot.course.entity.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WordServiceImpl implements WordService {

	private Logger logger = LoggerFactory.getLogger(WordServiceImpl.class);
	private WordRepository wordRepository;
	
	@Autowired
	public WordServiceImpl(WordRepository wordRepository) {
		this.wordRepository = wordRepository;
	}
	
	@Override
	public List<Word> findAll() {
		logger.info("\"findAll()\"");
		logger.info("Find all words in DB.");
		return wordRepository.findAll();
	}

	@Override
	public List<Word> findByLevelContains(String level) {
		logger.info("\"findAllByLevelContains(level)\"");
		logger.info("Find all words in DB depending on the \"" + level + "\" level.");
		return wordRepository.findAllByLevelContains(level);
	}

	@Override
	public Word findById(int id) {
		logger.info("\"findById(id)\"");
		logger.info("Find word by id.");
		Optional<Word> result = wordRepository.findById(id);
		
		Word word = null;
		
		if (result.isPresent()) {
			word = result.get();
		}
		else {
			logger.warn("Word not found.");
			throw new RuntimeException("Слово с id - " + id + " не было найдено.");
		}

		logger.info("Return word.");
		return word;
	}

	@Override
	public void save(Word word) {
		logger.info("\"save(word)\"");
		logger.info("Save a word \"" + word + ".");
		wordRepository.save(word);
	}

	@Override
	public void deleteById(int id) {
		logger.info("\"deleteById(id)\"");
		logger.info("Delete a word in DB by Id.");
		wordRepository.deleteById(id);
	}

	@Override
	public List<Word> searchBy(String word) {
		logger.info("\"searchBy(word)\"");
		logger.info("Search word by id.");

		List<Word> words = null;

		if (word != null && (word.trim().length() > 0)) {
			word = word.trim();
			words = wordRepository.findByJpKanjiContainsOrJpKanaContainsOrRuWordContainsAllIgnoreCase(
					word, word, word);
		}

		if (words.isEmpty()) {
			words = null;
			logger.info("words = null");
		}

		logger.info("Return words.");
		return words;
	}

	@Override
	public List<Word> findAllByUsersDataCollection_Id(int uId) {
		logger.info("\"findAllByUsersDataCollection_Id(uId)\" method.");
		logger.info("Find all words by UsersData Id.");
		return wordRepository.findAllByUsersDataCollection_Id(uId);
	}

	@Override
	public List<Word> findAllByWordAndLevel(String word, String level) {
		logger.info("\"findAllByWordAndLevel(word, level)\"");
		logger.info("Find all words by word and level.");

		// Find all words
		List<Word> words = searchBy(word);

		// search words by level
		List<Word> foundedListByWordAndLevel = new ArrayList<>();
		for (Word w : words) {
			if (w.getLevel().equals(level))
				foundedListByWordAndLevel.add(w);
		}
		if (foundedListByWordAndLevel.isEmpty()) {
			foundedListByWordAndLevel = null;
			logger.info("words = null");
		}

		logger.info("Return words.");
		return foundedListByWordAndLevel;
	}
}






