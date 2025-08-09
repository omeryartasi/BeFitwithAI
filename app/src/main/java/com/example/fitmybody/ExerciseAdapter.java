// ExerciseAdapter.java
package com.example.fitmybody;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private Context context;
    private List<Exercise> exercises;

    public ExerciseAdapter(Context context, List<Exercise> exercises) {
        this.context = context;
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_exercise.xml layoutunu kullanın
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);

        holder.name.setText(exercise.getName());
        holder.setsReps.setText(exercise.getSetsReps());

        String imageName = exercise.getImageName();
        if (imageName != null && !imageName.isEmpty()) {
            int imageResourceId = context.getResources().getIdentifier(
                    imageName, "drawable", context.getPackageName());

            if (imageResourceId != 0) {
                Glide.with(context)
                        .load(imageResourceId)
                        .into(holder.imageView);
            } else {
                holder.imageView.setImageResource(R.drawable.error_image);
            }
        } else {
            holder.imageView.setImageResource(R.drawable.ic_kullanici_profil); // Varsayılan resim
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        TextView setsReps;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.exerciseImage);
            name = itemView.findViewById(R.id.exerciseName);
            setsReps = itemView.findViewById(R.id.setsReps);
        }
    }
}