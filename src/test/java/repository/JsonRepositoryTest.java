package repository;

import model.Assignment;
import model.Task;
import model.User;
import org.junit.jupiter.api.*;
import repository.specifications.UserFindByUsername;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JsonRepositoryTest {
    private List<User> users;
    private List<Task> tasks;
    private List<Assignment> assignments;

    @BeforeAll
    void setUp() {
        users = new ArrayList<User>();
        tasks = new ArrayList<Task>();
        assignments =new ArrayList<Assignment>();

        var u1 = new User("FirstName1", "LastName1", "UserName1");
        var u2 = new User("FirstName2", "LastName2", "UserName2");
        var u3 = new User("FirstName3", "LastName3", "UserName3");

        users.add(u1);
        users.add(u2);
        users.add(u3);

        var t1 = new Task("TaskTitle1", "TaskDescription1");
        var t2 = new Task("TaskTitle2", "TaskDescription2");
        var t3 = new Task("TaskTitle3", "TaskDescription3");

        tasks.add(t1);
        tasks.add(t2);
        tasks.add(t3);

        var a1 = new Assignment(u1.getUserName(), t1.getId());
        var a2 = new Assignment(u2.getUserName(), t2.getId()); // both user 2 and user 3 are assigned to task2
        var a3 = new Assignment(u3.getUserName(), t2.getId());

        assignments.add(a1);
        assignments.add(a2);
        assignments.add(a3);
    }

    @AfterAll
    void tearDown() {
        users = null;
        tasks = null;
        assignments = null;
    }

    @Test
    @DisplayName("Add ")
    void add() {
        var userRepo= new JsonRepository<>(User.class);
        userRepo.add(users.get(0));
        userRepo.add(users.get(1));
        userRepo.add(users.get(2));

        Assertions.assertEquals(3, userRepo.getAll().size());

        var taskRepo = new JsonRepository<Task>(Task.class);
        taskRepo.add(tasks.get(0));
        Assertions.assertEquals(1, taskRepo.getAll().size());
    }

    @Test
    void saveLoad(){
        var userRepo = new JsonRepository<>(User.class);
        userRepo.add(users.get(0));
        userRepo.add(users.get(1));
        userRepo.add(users.get(2));
        userRepo.commit();

        userRepo = new JsonRepository<>(User.class);
        userRepo.load();

        Assertions.assertEquals(3, userRepo.getAll().size());
    }

    @Test
    void update(){
        var userRepo = new JsonRepository<User>(User.class);
        userRepo.add(users.get(0));

        userRepo.update(users.get(0), users.get(1));

        var userOptional = userRepo.getAll().stream().findFirst();

        Assertions.assertTrue(userOptional.isPresent());
        var usr = userOptional.get();
        Assertions.assertSame(usr.getUserName(), users.get(1).getUserName());
    }

    @Test
    void delete(){
        var userRepo = new JsonRepository<User>(User.class);
        userRepo.add(users.get(0));
        userRepo.remove(users.get(0));

        Assertions.assertEquals(0, userRepo.getAll().size());
    }

    @Test
    void query(){
        var userRepo = new JsonRepository<User>(User.class);
        userRepo.add(users.get(0));
        userRepo.add(users.get(1));
        userRepo.add(users.get(2));

        var spec = new UserFindByUsername(users.get(1).getUserName());

        var res = userRepo.findBySpecification(spec);

        var optional = res.stream().findFirst();

        Assertions.assertTrue(optional.isPresent());

        var usr = optional.get();

        Assertions.assertEquals(users.get(1).getId(), usr.getId());
    }


}