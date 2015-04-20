package com.connection.next.infantree.model;

/**
 * Created by chayongbin on 15. 4. 16..
 */
public class PhotoModel {

    private int Photo_Id;
    private String Photo_Path;
    private String date;

    public PhotoModel(int Photo_Id, String Photo_Path, String date) {
        this.Photo_Id = Photo_Id;
        this.Photo_Path = Photo_Path;
        this.date = date;
    }

    public int getPhoto_Id() {
        return Photo_Id;
    }

    public String getPhoto_Path() {
        return Photo_Path;
    }

    public String getDate() {
        return date;
    }
}
