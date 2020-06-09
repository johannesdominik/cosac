package swe4.model.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import swe4.model.entities.User;
import swe4.util.PasswordUtil;

import static swe4.model.entities.User.Role.ADMIN;
import static swe4.model.entities.User.Role.CUSTOMER;

public class UserRepository {
  private static ObservableList<User> users = FXCollections.observableArrayList();

  public static void loadMockUsers() {
    users.setAll(
      new User("Bill", "Yard", "yard", PasswordUtil.generateHash("yard123"), false, ADMIN),
      new User("admin", "admin", "admin", PasswordUtil.generateHash("admin"), false, ADMIN),
      new User("Claire", "Waßer", "wasser", PasswordUtil.generateHash("wasser123"), false, CUSTOMER),
      new User("Rainer", "Zufall", "zufall", PasswordUtil.generateHash("zufall123"), false, CUSTOMER),
      new User("Martha", "Pfahl", "pfahl", PasswordUtil.generateHash("pfahl123"), false, CUSTOMER),
      new User("Marie", "Huana", "huana", PasswordUtil.generateHash("huana123"), false, CUSTOMER)
    );
  }

  public static ObservableList<User> getUsers() {
    return users;
  }

  public static User getUser(String userName) {
    for (User user : users) {
      if (user.getUserName().equals(userName)) {
        return user;
      }
    }
    return null;
  }

  public static void deleteUser(String userName) {
    for (User user : users) {
      if (user.getUserName().equals(userName)) {
        users.remove(user);
      }
    }
  }

  public static void addUser(String firstName, String lastName, String userName, String password) {
    users.add(new User(firstName, lastName, userName, PasswordUtil.generateHash(password), false, CUSTOMER));
  }

  public static boolean isValidUser(String userName, String password) {
    for (User user : users) {
      if (user.getUserName().equals(userName) && PasswordUtil.isValid(password, user.getPasswordHash())) {
        return true;
      }
    }
    return false;
  }

  public static void receiveUsers(Object[] userObjectArray) {
      users.clear();
      for (int i = 0; i < userObjectArray.length; ++i) {
        User user = (User) userObjectArray[i];
        users.add(new User(user.getFirstName(), user.getLastName(), user.getUserName(), user.getPasswordHash(), user.isLocked(), user.getRole()));
      }
    System.out.println("client, received users: " + users);
  }
}