package com.example.viz.infantree.model;

/**
 * Created by chayongbin on 15. 4. 8..
 */
public class UserModel {
    private String name;
    private String age;
    private int profile;
    private int backgroundImg;

    public UserModel (String name, String age, int profile, int backgroundImg) {
        this.name = name;
        this.age = age;
        this.profile = profile;
        this.backgroundImg = backgroundImg;
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

    public int getBackgroundImg() {
        return backgroundImg;
    }
}
