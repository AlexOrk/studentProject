package alex.ork.springboot.course.service;

import alex.ork.springboot.course.entity.UsersData;
import alex.ork.springboot.course.entity.Word;

import java.util.List;

public interface WordService {

    public List<Word> findAll();

    public List<Word> findByLevelContains(String level);

    public Word findById(int id);

    public void save(Word employee);

    public void deleteById(int id);

    public List<Word> searchBy(String name);

    public List<Word> findAllByUsersDataCollection_Id(int ud);

    public List<Word> findAllByWordAndLevel(String word, String level);
}
