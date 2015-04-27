package com.connection.next.infantree.model;

/**
 * Created by chayongbin on 15. 4. 8..
 */
public class UserModel {
    private String Baby_Id;
    private String name;
    private String age;
    private int profile;
    private int backgroundImg;

    public UserModel (String Baby_Id, String name, String age, int profile, int backgroundImg) {
        this.Baby_Id = Baby_Id;
        this.name = name;
        this.age = age;
        this.profile = profile;
        this.backgroundImg = backgroundImg;
    }

    public String getBaby_Id() {
        return Baby_Id;
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
