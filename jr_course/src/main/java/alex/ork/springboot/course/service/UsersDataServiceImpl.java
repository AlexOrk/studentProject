package alex.ork.springboot.course.service;

import alex.ork.springboot.course.dao.UsersDataRepository;
import alex.ork.springboot.course.entity.UsersData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersDataServiceImpl implements UsersDataService {

    private Logger logger = LoggerFactory.getLogger(UsersDataServiceImpl.class);
    private UsersDataRepository usersDataRepository;

    @Autowired
    public UsersDataServiceImpl(UsersDataRepository usersDataRepository) {
        this.usersDataRepository = usersDataRepository;
    }

    @Override
    public List<UsersData> findAll(User user) {
        logger.info("\"findAll(user)\"");
        logger.info("Find all information about users in DB.");
        List<UsersData> users = usersDataRepository.findAll();

        String adminUsername = user.getUsername();

        // delete admin from list of users
        users.removeIf(nextUser -> nextUser.getUsername().equals(adminUsername));
        logger.info("Return all information about users from DB except admin.");
        return users;
    }

    @Override
    public List<UsersData> findUser(String name) {
        logger.info("\"findUser(name)\"");
        logger.info("Find usersData by username or first name or last name in DB.");

        List<UsersData> usersData = null;
        if (name != null && (name.trim().length() > 0)) {
            name = name.trim();
            usersData = usersDataRepository.findByUsernameContainsOrFirstnameContainsOrLastnameContainsAllIgnoreCase(
                    name, name, name);
        }
        if (usersData.isEmpty()) {
            usersData = null;
            logger.info("users = null");
        }
        logger.info("Return usersData list.");
        return usersData;
    }

    @Override
    public UsersData findByUsername(String username) {
        logger.info("\"findByUsername(username)\"");
        logger.info("Find usersData in DB by Id.");
        return usersDataRepository.findByUsername(username);
    }

    @Override
    public void deleteById(int id) {
        logger.info("\"deleteById(id)\"");
        logger.info("Delete usersData in DB by Id (\"deleteById(id)\")");
        usersDataRepository.deleteById(id);
    }

    @Override
    public void deleteAllExceptAdmin(int id) {
        logger.info("\"deleteAllExceptAdmin(id)\"");
        logger.info("Delete all usersData except admin.");
        usersDataRepository.deleteAllExceptAdmin(id);
    }

    @Override
    public void save(UsersData usersData) {
        logger.info("\"save(usersData)\"");
        logger.info("Save a usersData \"" + usersData + ".");
        usersDataRepository.save(usersData);
    }
}
