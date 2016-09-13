package com.codepath.simpletodo;

/**
 * A class representing a todo item
 *
 * Created by vmiha on 9/9/16.
 */
public class Todo {

    public String name;
    public Priority priority;
    public String date;

    public static enum Priority {
        LOW, MEDIUM, HIGH
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Priority getPriority() {
        return priority;
    }


    public Todo(String name, Priority priority, String date){
        this.name = name;
        this.priority = priority;
        this.date = date;
    }

    public Todo(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Todo)) return false;

        Todo todo = (Todo) o;

        if (!getName().equals(todo.getName())) return false;
        if (getPriority() != todo.getPriority()) return false;
        return getDate().equals(todo.getDate());

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getPriority().hashCode();
        result = 31 * result + getDate().hashCode();
        return result;
    }

}
