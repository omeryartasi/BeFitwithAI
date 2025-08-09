package com.example.fitmybody; // Kendi paket adınızla değiştirin

import java.util.List;

public class WorkoutDay {

    private int dayNumber;
    private String dayTitle;
    private List<Exercise> exercises; // Bu listeyi WorkoutDay modelinize eklediğinizden emin olun.

    // Firestore'dan veri çekerken gerekli olan boş constructor
    public WorkoutDay() {
    }

    // Parametreli constructor (isteğe bağlı, verileri manuel oluşturmak için)
    public WorkoutDay(int dayNumber, String dayTitle, List<Exercise> exercises) {
        this.dayNumber = dayNumber;
        this.dayTitle = dayTitle;
        this.exercises = exercises;
    }

    // Getter metotları (Firestore'un verileri nesneye dönüştürmesi için gerekli)
    public int getDayNumber() {
        return dayNumber;
    }

    public String getDayTitle() {
        return dayTitle;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    // Setter metotları (Eğer Firestore'dan gelen verileri manuel olarak set etmek isterseniz veya
    // alt koleksiyonlardan gelen veriyi ana nesneye eklemek için gerekli)
    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public void setDayTitle(String dayTitle) {
        this.dayTitle = dayTitle;
    }

    // setExercises metodu, 'Cannot resolve method 'setExercises'' hatasını çözmek için eklenmelidir.
    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }
}