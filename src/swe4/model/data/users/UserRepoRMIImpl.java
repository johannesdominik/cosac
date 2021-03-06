package swe4.model.data.users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import swe4.model.entities.User;
import swe4.server.RMIInterface;
import swe4.util.PasswordUtil;

import java.rmi.Naming;

public class UserRepoRMIImpl implements UserRepo {
  private static final String SERVER_URL = "rmi://127.0.0.1/RMIServer";
  private ObservableList<User> users = FXCollections.observableArrayList();

  public UserRepoRMIImpl() {
    updateUsers();
  }

  @Override
  public ObservableList<User> getUsers() {
    return users;
  }

  @Override
  public User getUserByUsername(String username) {
    for (User user : users) {
      if (user.getUsername().equals(username)) {
        return user;
      }
    }
    return null;
  }

  @Override
  public boolean addUser(User user) {
    users.add(user);
    return true;
  }

  @Override
  public boolean deleteUser(String username) {
    for (User user : users) {
      if (user.getUsername().equals(username)) {
        users.remove(user);
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isValidUser(String username, String password) {
    for (User user : users) {
      if (user.getUsername().equals(username) && PasswordUtil.isValid(password, user.getPasswordHash())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean updateUsers() {
    RMIInterface server;
    try {
      server = (RMIInterface) Naming.lookup(SERVER_URL);
      users.clear();
      Object[] rawUsers = server.loadUsers();
      for (int i = 0; i < rawUsers.length; ++i) {
        users.add((User) rawUsers[i]);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean saveUsers() {
    RMIInterface server;
    try {
      server = (RMIInterface) Naming.lookup(SERVER_URL);
      Object[] userData = new Object[getUsers().size()];
      for (int i = 0; i < userData.length; ++i) {
        userData[i] = getUsers().get(i);
      }
      server.saveUsers(userData);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
