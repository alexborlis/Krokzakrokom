package com.example.krokzakrokom.data.model;


public class Item {
    private String id;
    private String title;
    private String description;
    private boolean isDone;
    private boolean isExpanded;

    public Item(String id, String title, String description, boolean isDone, boolean isExpanded) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isDone = isDone;
        this.isExpanded = isExpanded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        this.isDone = done;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
