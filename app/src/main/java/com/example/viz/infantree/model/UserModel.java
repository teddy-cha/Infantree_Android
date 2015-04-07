package com.example.viz.infantree.model;

/**
 * Created by chayongbin on 15. 4. 8..
 */
public class UserModel {
    private String name;
    private String age;
    private int profile;

    public UserModel (String name, String age, int profile) {
        this.name = name;
        this.age = age;
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public int getProfile() {
        return profile;
    }
}
