package com.example.finalproject;

public class DataCourseClass {

    private String courseTitle;
    private int courseDesc;
    private  String courseLang;
    private int courseImage;
    public String getCourseTitle(){
        return courseTitle;
    }
    public int getCourseDesc() {
        return courseDesc;
    }

    public String getCourseLang() {
        return courseLang;
    }

    public int getCourseImage() {
        return courseImage;
    }
    public DataCourseClass (String courseTitle, int courseDesc, String courseLang, int courseImage){
        this.courseTitle = courseTitle;
        this.courseDesc = courseDesc;
        this.courseLang = courseLang;
        this.courseImage = courseImage;
    }
}