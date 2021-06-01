package com.company;

import model.Assignment;
import model.Task;
import model.User;
import repository.RepositoryFactory;
import repository.specifications.AssignmentFindByUserName;
import repository.specifications.TaskFindById;
import repository.specifications.UserFindByUsername;

import java.util.Arrays;
import java.util.Formatter;

//-createUser -fn='FirstName' - ln='LastName' -un='UserName'
//-showAllUsers
//-addTask -un='userName' -tt='Task Title' -td='Task Description'
//-showTasks -un='userName'
public class Main {
    private static final String createUser = "-createUser";
    private static final String showAllUsers = "-showAllUsers";
    private static final String addTask = "-addTask";
    private static final String showTasks = "-showTasks";
    private static final String firstName = "-fn";
    private static final String lastName = "-ln";
    private static final String userName = "-un";
    private static final String taskTitle = "-tt";
    private static final String taskDescription = "-td";

    public static void main(String[] args) {
        try
        {
            switch (args[0]) {
                case "-createUser": {
                    createUser(args);
                    break;
                }
                case "-showAllUsers": {
                    showAllUsers(args);
                    break;
                }
                case "-addTask": {
                    addTask(args);
                    break;
                }
                case "-showTasks":
                    showTasks(args);
                    break;
                default:
                    System.out.println("Unrecognized option: " + args[0]);
                    break;
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void showTasks(String[] args) {
        if (args.length != 2)
            throw new IllegalArgumentException("Option -showTasks expects 1 parameter: -un='userName'");

        var un = getParamValue(args, userName);

        var factory = new RepositoryFactory<Assignment>(Assignment.class);
        var assignmentRepository = factory.create();
        var taskFactory = new RepositoryFactory<Task>(Task.class);
        var taskRepo = taskFactory.create();

        assignmentRepository.load();
        taskRepo.load();

        var spec = new AssignmentFindByUserName(un);
        spec.addNameValuePair("userName", un);
        var userAssignments = assignmentRepository.findBySpecification(spec);

        Formatter f = new Formatter();
        for (var a : userAssignments){
            var taskSpec = new TaskFindById(a.getTaskId());
            taskSpec.addNameValuePair("id", a.getTaskId());
            var res = taskRepo.findBySpecification(taskSpec).stream().findFirst();

            if (res.isPresent()){
                f.format("%15s %15s %15s\n", res.get().getId(), res.get().getTitle(), res.get().getDescription());
            }
        }
        System.out.println(f);
    }

    private static void addTask(String[] args) {
        if (args.length != 4)
            throw new IllegalArgumentException("Option -addTask expects 3 parameters: -un='userName' -tt='Task Title' -td='Task Description'");

        Task task = new Task();
        task.setTitle(getParamValue(args, taskTitle));
        task.setDescription(getParamValue(args, taskDescription));

        var factory = new RepositoryFactory<Task>(Task.class);
        var repo = factory.create();

        repo.load();
        repo.add(task);
        repo.commit();

        var assignment = new Assignment();
        assignment.setUserName(getParamValue(args, userName));
        assignment.setTaskId(task.getId());

        var assignmentRepositoryFactory = new RepositoryFactory<Assignment>(Assignment.class);
        var assignmentRepo = assignmentRepositoryFactory.create();

        assignmentRepo.load();
        assignmentRepo.add(assignment);
        assignmentRepo.commit();

        System.out.println("Task added successfully");
    }

    private static void showAllUsers(String[] args) {
        if (args.length != 1)
            throw new IllegalArgumentException("Option -showAllUsers allows no additional parameters");

        var factory = new RepositoryFactory<User>(User.class);
        var repo = factory.create();
        repo.load();

        var users = repo.getAll();
        Formatter f = new Formatter();

        for (var u : users) {
            f.format("%5s %15s %15s %15s\n", u.getId(), u.getFirstName(), u.getLastName(), u.getUserName());
        }

        System.out.println(f);
    }

    private static void createUser(String[] args) throws Exception {
        if (args.length != 4)
            throw new IllegalArgumentException("Option -createUser expects 3 parameters: -fn='FirstName' - ln='LastName' -un='UserName'");

        User user = new User();
        user.setFirstName(getParamValue(args, firstName));
        user.setLastName(getParamValue(args, lastName));
        user.setUserName(getParamValue(args, userName));

        var factory = new RepositoryFactory<User>(User.class);
        var repo = factory.create();

        var spec = new UserFindByUsername(user.getUserName());
        spec.addNameValuePair("userName", user.getUserName());

        repo.load();
        if (repo.findBySpecification(spec).size() > 0){
            throw new Exception("Username already exists");
        }

        repo.add(user);
        repo.commit();

        System.out.println("User added successfully");
    }

    private static String getParamValue(String [] args, String toFind){
        var option = Arrays.stream(args).filter(a -> a.contains(toFind)).findFirst();
        if (! option.isPresent()){
            throw new IllegalArgumentException("Argument missing: " + toFind);
        }
        var parser = new NameValueArgumentParser(option.get());
        return parser.getValue();
    }

}

/*
     JsonRepository<Settings> settings=  new JsonRepository<Settings>(Settings.class);
        settings.add(new Settings("repoType", "inMemory"));
        settings.commit();
 */
