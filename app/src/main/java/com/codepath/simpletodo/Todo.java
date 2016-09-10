package com.codepath.simpletodo;

/**
 * A class representing a todo item
 *
 * Created by vmiha on 9/9/16.
 */
public class Todo {

    public String name;

    public Integer getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer priority;

    public Todo(String name, Integer priority){
        this.name = name;
        this.priority = priority;
    }

    public Todo(){}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Todo)) return false;

        Todo todo = (Todo) o;

        if (!getName().equals(todo.getName())) return false;
        return getPriority() != null ? getPriority().equals(todo.getPriority()) : todo.getPriority() == null;

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + (getPriority() != null ? getPriority().hashCode() : 0);
        return result;
    }
}
