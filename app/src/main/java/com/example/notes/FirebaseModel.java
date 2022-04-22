package com.example.notes;

public class FirebaseModel {
    private String title; //same in CreateNote.java
    private String content;
    private String dateTime;
    private String subtitle;
    private String imagePath; // để lưu hình ảnh
    private String color;

    public FirebaseModel() {
    }

    public FirebaseModel(String title, String content, String subtitle, String imagePath, String color, String dateTime) {
        this.title = title;
        this.content = content;
        this.subtitle = subtitle;
        this.imagePath = imagePath;
        this.color = color;
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubtitle() { return subtitle; }

    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }

    public String getDateTime() { return dateTime; }

    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
}
