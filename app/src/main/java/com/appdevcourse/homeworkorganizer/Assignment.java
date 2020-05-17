package com.appdevcourse.homeworkorganizer;

import java.io.Serializable;

public class Assignment implements Serializable {
    String dueDate;
    String assignment;
    boolean isCompleted;

    public Assignment(String assignment, String dueDate) {
        this.dueDate=dueDate;
        this.assignment=assignment;
        isCompleted=false;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getAssignment() {
        return assignment;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
