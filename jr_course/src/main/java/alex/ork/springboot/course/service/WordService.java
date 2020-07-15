package alex.ork.springboot.course.service;

import alex.ork.springboot.course.entity.Word;

import java.util.List;

public interface WordService {

	public List<Word> findAll();

	public List<Word> findByLevelContains(String level);

	public Word findById(int theId);
	
	public void save(Word theEmployee);
	
	public void deleteById(int theId);

	public List<Word> searchBy(String theName);

}
