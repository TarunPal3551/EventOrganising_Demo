package com.example.eventorganising_demo;

public class Event {
    String name;
    String date;
    String eventId;
    String time;
    Boolean isReminder;
    String imageUrl;

    public Event(String name, String date, String eventId, String time, Boolean isReminder, String imageUrl) {
        this.name = name;
        this.date = date;
        this.eventId = eventId;
        this.time = time;
        this.isReminder = isReminder;
        this.imageUrl = imageUrl;
    }

    public Event() {

    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getReminder() {
        return isReminder;
    }

    public void setReminder(Boolean reminder) {
        isReminder = reminder;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
