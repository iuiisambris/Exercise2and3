package model;

import javax.persistence.*;

@Entity
@Table(name = "TASKS")
public class Task {
    private long id;
    private String title;
    private String description;

    public Task() {

    }

    public Task(String title, String description) {
        this.id = 0;
        this.title = title;
        this.description = description;
    }

    public Task(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
