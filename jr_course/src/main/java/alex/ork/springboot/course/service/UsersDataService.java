package alex.ork.springboot.course.service;

import alex.ork.springboot.course.entity.UsersData;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface UsersDataService {
    public List<UsersData> findAll(User admin);

    public List<UsersData> findUser(String name);

    public void deleteById(int id);

    public void deleteAllExceptAdmin(int id);

    public UsersData findByUsername(String username);

    public void save(UsersData usersData);
}
