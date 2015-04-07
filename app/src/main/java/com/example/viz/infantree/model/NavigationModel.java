package com.example.viz.infantree.model;

/**
 * Created by chayongbin on 15. 4. 8..
 */
public class NavigationModel {
    private String titles[];
    private int icons[];
    private int profile;

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public int[] getIcons() {
        return icons;
    }

    public void setIcons(int[] icons) {
        this.icons = icons;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }
}


//String TITLES[] = {"타임라인", "필터", "설정"};
//int ICONS[] = {R.drawable.ic_access_time_grey600_24dp,
//        R.drawable.ic_filter_list_grey600_24dp,
//        R.drawable.ic_settings_grey600_24dp};
//
//String NAME = "차민우";
//String AGE = "185일, 6개월";
//int PROFILE = R.drawable.aka;