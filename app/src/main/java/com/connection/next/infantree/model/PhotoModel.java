package com.connection.next.infantree.model;

/**
 * Created by chayongbin on 15. 4. 16..
 */
public class PhotoModel {

    private String _id;
    private String date;

    public PhotoModel(String _id, String date) {
        this._id = _id;
        this.date = date;
    }

    public String get_id() {
        return _id;
    }

    public String getDate() {
        return date;
    }
}