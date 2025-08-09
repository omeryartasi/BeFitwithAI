package com.example.fitmybody; // Kendi paket adınızla değiştirin

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private Context context;
    private List<WorkoutDay> workoutDays;

    public WorkoutAdapter(Context context, List<WorkoutDay> workoutDays) {
        this.context = context;
        this.workoutDays = workoutDays;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_workout_day, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        WorkoutDay workoutDay = workoutDays.get(position);

        // Gün başlığı varsa göster
        if (workoutDay.getDayTitle() != null && !workoutDay.getDayTitle().isEmpty()) {
            holder.dayTitle.setText(workoutDay.getDayTitle());
            holder.dayTitle.setVisibility(View.VISIBLE);
        } else {
            holder.dayTitle.setVisibility(View.GONE);
        }

        // Eğer egzersizler varsa, iç içe RecyclerView'ı ayarla
        if (workoutDay.getExercises() != null && !workoutDay.getExercises().isEmpty()) {
            holder.exercisesRecyclerView.setVisibility(View.VISIBLE);
            ExerciseAdapter exerciseAdapter = new ExerciseAdapter(context, workoutDay.getExercises());
            holder.exercisesRecyclerView.setAdapter(exerciseAdapter);
        } else {
            holder.exercisesRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return workoutDays.size();
    }

    // MainActivity'den veri güncellemesi için kullanılan metot
    public void setWorkoutDays(List<WorkoutDay> newWorkoutDays) {
        this.workoutDays = newWorkoutDays;
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView dayTitle;
        RecyclerView exercisesRecyclerView; // İç içe RecyclerView

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTitle = itemView.findViewById(R.id.dayTitle);
            exercisesRecyclerView = itemView.findViewById(R.id.exercisesRecyclerView); // ID'yi kendi XML'inize göre ayarlayın
            exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}