package repository.specifications;

import model.User;

import java.util.Map;

public class UserFindByUsername extends SpecificationBase<User> {

    private String username;

    public UserFindByUsername(String username) {
        this.username = username;
    }

    public boolean match(User t) {
        return t.getUserName().equals(username);
    }

}
