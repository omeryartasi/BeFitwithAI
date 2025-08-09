package com.example.fitmybody;

public class DailyProgramSummary {
    private int dayNumber;
    private String bodyPart;

    public DailyProgramSummary(int dayNumber, String bodyPart) {
        this.dayNumber = dayNumber;
        this.bodyPart = bodyPart;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public String getBodyPart() {
        return bodyPart;
    }
}