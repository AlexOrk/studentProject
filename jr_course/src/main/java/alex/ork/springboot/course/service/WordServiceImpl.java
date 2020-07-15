package alex.ork.springboot.course.service;

import alex.ork.springboot.course.controller.WordController;
import alex.ork.springboot.course.dao.WordRepository;
import alex.ork.springboot.course.entity.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		logger.info("Find all words in DB (\"wordRepository.findAll()\")");
		return wordRepository.findAll();
	}

	@Override
	public List<Word> findByLevelContains(String level) {
		logger.info("Find all words in DB depending on the \"" + level
				+ "\" level (\"wordRepository.findAllByLevelContains(level)\")");
		return wordRepository.findAllByLevelContains(level);
	}

	@Override
	public Word findById(int id) {
		Optional<Word> result = wordRepository.findById(id);
		
		Word word = null;
		
		if (result.isPresent()) {
			word = result.get();
		}
		else {
			logger.warn("Word not found.");
			throw new RuntimeException("Слово с id - " + id + " не было найдено.");
		}
		
		return word;
	}

	@Override
	public void save(Word word) {
		logger.info("Save a word \"" + word + "\" in DB (\"wordRepository.save(word)\")");
		wordRepository.save(word);
	}

	@Override
	public void deleteById(int id) {
		logger.info("Delete a word in DB by Id " + id + " (\"wordRepository.save(word)\")");
		wordRepository.deleteById(id);
	}

	@Override
	public List<Word> searchBy(String word) {

		List<Word> results = null;

		if (word != null && (word.trim().length() > 0)) {
			word = word.trim();
			logger.info("Search a word \"" + word + "\" in DB by jp or rus title (\"wordRepository.save(word)\")");
			results = wordRepository.findByJpKanjiContainsOrJpKanaContainsOrRuWordContainsAllIgnoreCase(
					word, word, word);
		}
//		else {
//			results = findAll();
//		}

		return results;
	}
}






