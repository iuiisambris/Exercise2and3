package com.company;

import model.Task;
import model.User;
import repository.RepositoryFactory;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Optional;

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
                    if (args.length != 4)
                        throw new IllegalArgumentException("Option -createUser expects 3 parameters: -fn='FirstName' - ln='LastName' -un='UserName'");

                    User user = new User();
                    user.setFirstName(getParamValue(args, firstName));
                    user.setLastName(getParamValue(args, lastName));
                    user.setUserName(getParamValue(args, lastName));

                    var factory = new RepositoryFactory<User>(User.class);
                    var repo = factory.create();

                    repo.load();
                    repo.add(user);
                    repo.commit();

                    System.out.println("User added successfully");
                    break;
                }
                case "-showAllUsers": {
                    if (args.length != 1)
                        throw new IllegalArgumentException("Option -showAllUsers allows no additional parameters");

                    var factory = new RepositoryFactory<User>(User.class);
                    var repo = factory.create();
                    repo.load();

                    var users = repo.getAll();
                    Formatter f = new Formatter();

                    for (var u : users) {
                        f.format("%5d %15s %15s %15s\n", u.getId(), u.getFirstName(), u.getLastName(), u.getUserName());
                    }

                    System.out.println(f);
                    break;
                }
                case "-addTask": {
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

                    System.out.println("Task added successfully");

                    break;
                }
                case "-showTasks":
                    if (args.length != 2)
                        throw new IllegalArgumentException("Option -showTasks expects 1 parameter: -un='userName'");

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
