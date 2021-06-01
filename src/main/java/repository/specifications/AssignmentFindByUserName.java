package repository.specifications;

import model.Assignment;

public class AssignmentFindByUserName extends SpecificationBase<Assignment>{
    private final String username;

    public AssignmentFindByUserName(String username) {
        this.username = username;
    }

    public boolean match(Assignment t) {
        return t.getUserName().equals(username);
    }
}
