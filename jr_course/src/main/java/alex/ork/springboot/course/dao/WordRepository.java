package alex.ork.springboot.course.dao;

import alex.ork.springboot.course.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Integer> {


	public List<Word> findAll();
	public List<Word> findAllByLevelContains(String level);

	public List<Word> findByJpKanjiContainsOrJpKanaContainsOrRuWordContainsAllIgnoreCase(
								String kanji, String kana, String ru);

	public List<Word> findAllByUsersDataCollection_Id(int ud);
}
