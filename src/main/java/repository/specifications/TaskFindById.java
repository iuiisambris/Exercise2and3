package repository.specifications;

import model.Task;

public class TaskFindById extends SpecificationBase<Task>{
    private final String id;

    public TaskFindById(String id) {
        this.id = id;
    }

    public boolean match(Task t) {
        return t.getId().equals(id);
    }
}
