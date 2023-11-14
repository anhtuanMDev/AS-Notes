package com.example.notes.Model;

public class NotesModel {
    private String title;
    private String content;
    private String backgroundColor;
    private String textColor;

    public NotesModel(String title, String content, String backgroundColor, String textColor) {
        this.title = title;
        this.content = content;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    public NotesModel(String backgroundColor, String textColor) {
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
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

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }
}
