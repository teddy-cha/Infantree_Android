package com.connection.next.infantree.model;

/**
 * Created by viz on 15. 5. 23..
 */
public class DiaryModel {

    private String Diary_Id;
    private String Photo_Id;
    private String Mom_Diary;
    private String Dad_Diary;

    public DiaryModel() {
    }

    public DiaryModel(String diary_Id, String photo_Id, String mom_Diary, String dad_Diary) {
        Diary_Id = diary_Id;
        Photo_Id = photo_Id;
        Mom_Diary = mom_Diary;
        Dad_Diary = dad_Diary;
    }

    public String getDiary_Id() {
        return Diary_Id;
    }

    public String getPhoto_Id() {
        return Photo_Id;
    }

    public String getMom_Diary() {
        return Mom_Diary;
    }

    public String getDad_Diary() {
        return Dad_Diary;
    }

    public void setDiary_Id(String diary_Id) {
        Diary_Id = diary_Id;
    }

    public void setPhoto_Id(String photo_Id) {
        Photo_Id = photo_Id;
    }

    public void setMom_Diary(String mom_Diary) {
        Mom_Diary = mom_Diary;
    }

    public void setDad_Diary(String dad_Diary) {
        Dad_Diary = dad_Diary;
    }
}
