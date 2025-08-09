package com.example.fitmybody;

public class Exercise {
    private String name;
    private String setsReps;
    private String imageName;

    public Exercise() {
        // Firestore için boş kurucu metot gerekli
    }

    public Exercise(String name, String setsReps, String imageName) {
        this.name = name;
        this.setsReps = setsReps;
        this.imageName = imageName;
    }

    // Getter metotları
    public String getName() {
        return name;
    }

    public String getSetsReps() {
        return setsReps;
    }

    public String getImageName() {
        return imageName;
    }
}