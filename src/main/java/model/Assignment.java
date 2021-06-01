package model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "ASSIGNMENTS")
public class Assignment {
    private String userName;
    String taskId;
    String assignmentId;

    public Assignment(){
        this.assignmentId = UUID.randomUUID().toString();
    }

    public Assignment(String userName, String taskId) {
        this.assignmentId = UUID.randomUUID().toString();
        this.userName = userName;
        this.taskId = taskId;
    }

    @Id
    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
