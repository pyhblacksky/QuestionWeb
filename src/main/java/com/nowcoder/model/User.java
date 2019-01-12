package com.nowcoder.model;

public class User {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String name){
        this.name = name;
    }

    public String Description(){
        return "This is description " + name + " HI!";
    }
}
