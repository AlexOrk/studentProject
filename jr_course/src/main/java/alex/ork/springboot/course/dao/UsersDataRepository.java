package alex.ork.springboot.course.dao;

import alex.ork.springboot.course.entity.UsersData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UsersDataRepository extends JpaRepository<UsersData, Integer> {
    public List<UsersData> findAll();

    public List<UsersData> findByUsernameContainsOrFirstnameContainsOrLastnameContainsAllIgnoreCase(
            String username, String firstName, String lastName);

    public UsersData findByUsername(String username);

    // delete all usersData except admin
    @Transactional
    @Modifying
    @Query(value = "delete from UsersData ud where not ud.id = :id")
    public void deleteAllExceptAdmin(int id);


}
