package repository;

import model.Assignment;
import model.Task;
import model.User;
import org.junit.jupiter.api.*;
import repository.specifications.SpecificationBase;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DBRepositoryTest {
    private List<User> users;
    private List<Task> tasks;
    private List<Assignment> assignments;

    @BeforeAll
    void setUp() {
        users = new ArrayList<User>();
        tasks = new ArrayList<Task>();
        assignments =new ArrayList<Assignment>();

        var u1 = new User(0, "FirstName1", "LastName1", "UserName1");
        var u2 = new User(0, "FirstName2", "LastName2", "UserName2");
        var u3 = new User(0, "FirstName3", "LastName3", "UserName3");

        users.add(u1);
        users.add(u2);
        users.add(u3);

        var t1 = new Task(0, "TaskTitle1", "TaskDescription1");
        var t2 = new Task(0, "TaskTitle2", "TaskDescription2");
        var t3 = new Task(0, "TaskTitle3", "TaskDescription3");

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
    void add() {
        var userRepo = new DBRepository<User>(User.class);

        var user = users.get(0);

        userRepo.add(user);

        Assertions.assertTrue(user.getId() != 0);

        userRepo.closeConnections();
    }

    @Test
    @DisplayName("add, remove then assert is not present")
    void remove() {
        var userRepo = new DBRepository<User>(User.class);
        userRepo.deleteAll();

        var user = users.get(0);

        userRepo.add(user);

        userRepo.remove(user);

        var optional = userRepo.getAll().stream().filter(x -> x.getUserName().equals(user.getUserName())).findFirst();

        Assertions.assertFalse(optional.isPresent());
    }

    @Test
    @DisplayName("Test add entity and update a field")
    void update(){
        var userRepo = new DBRepository<User>(User.class);

        var user = users.get(0);
        userRepo.add(user);
        user.setLastName("UpdatedLastName");
        userRepo.update(user, null);

        var lastName = user.getLastName();

        Assertions.assertEquals(lastName, "UpdatedLastName");
    }

    @Test
    @DisplayName("query by specification")
    void query(){
        var userRepo = new DBRepository<User>(User.class);
        userRepo.add(users.get(0));
        userRepo.add(users.get(1));
        userRepo.add(users.get(2));

        /// query by specification userName='username1'
        SpecificationBase<User> spec = new SpecificationBase<>();
        spec.addNameValuePair("userName", "username1");

        var res = userRepo.findBySpecification(spec);

        spec = new SpecificationBase<User>();
        spec.addNameValuePair("firstName", "firstName1");
    }

}