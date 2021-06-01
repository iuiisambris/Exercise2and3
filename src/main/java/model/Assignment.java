package model;

import javax.persistence.*;

@Entity
@Table(name = "ASSIGNMENTS")
public class Assignment {
    private String userName;
    long taskId;
    long assignmentId;

    public Assignment(){

    }

    public Assignment(String userName, long taskId) {
        this.userName = userName;
        this.taskId = taskId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
