package alex.ork.springboot.course.dao;

import alex.ork.springboot.course.entity.Grammar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrammarRepository extends JpaRepository<Grammar, Integer> {
    public List<Grammar> findAll();

    public List<Grammar> findAllByJlptLvlContains(int jlptlvl);

    public List<Grammar> findByFormulaContainsOrExampleContainsOrDescriptionContainsAllIgnoreCase(
            String formula, String example, String description
    );

    public List<Grammar> findAllByUsersDataCollection_Id(int ud);

}
