package com.example.expenseplanner;

public class UserGuideItem {

    private String mTitle;
    private String mDescription;

    public UserGuideItem( String title, String description) {

        mTitle = title;
        mDescription = description;
    }



    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }
}
