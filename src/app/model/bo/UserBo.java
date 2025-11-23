package app.model.bo;

import app.model.bean.User;
import app.model.dao.UserDao;

import java.util.Optional;

public class UserBo {
    private final UserDao userDao = new UserDao();

    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public Optional<User> authenticate(String username, String password) {
        Optional<User> userOpt = userDao.findByUsername(username);
        if (userOpt.isPresent() && password.equals(userOpt.get().getPassword())) {
            return userOpt;
        }
        return Optional.empty();
    }

    public boolean register(String username, String password) {
        if (userDao.findByUsername(username).isPresent()) {
            return false;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole("user");
        userDao.save(user);
        return true;
    }
}

