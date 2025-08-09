package com.example.fitmybody;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WeeklyProgramAdapter extends RecyclerView.Adapter<WeeklyProgramAdapter.WeeklyProgramViewHolder> {

    private List<DailyProgramSummary> weeklyProgramList;

    public WeeklyProgramAdapter(List<DailyProgramSummary> weeklyProgramList) {
        this.weeklyProgramList = weeklyProgramList;
    }

    @NonNull
    @Override
    public WeeklyProgramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weekly_program_summary, parent, false);
        return new WeeklyProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyProgramViewHolder holder, int position) {
        DailyProgramSummary program = weeklyProgramList.get(position);
        holder.dayNumberTextView.setText(String.format("%d. Gün", program.getDayNumber()));
        holder.bodyPartTextView.setText(program.getBodyPart());
    }

    @Override
    public int getItemCount() {
        return weeklyProgramList.size();
    }

    public void updateData(List<DailyProgramSummary> newProgramList) {
        this.weeklyProgramList.clear();
        this.weeklyProgramList.addAll(newProgramList);
        notifyDataSetChanged();
    }

    static class WeeklyProgramViewHolder extends RecyclerView.ViewHolder {
        TextView dayNumberTextView;
        TextView bodyPartTextView;

        public WeeklyProgramViewHolder(@NonNull View itemView) {
            super(itemView);
            dayNumberTextView = itemView.findViewById(R.id.dayNumberTextView);
            bodyPartTextView = itemView.findViewById(R.id.bodyPartTextView);
        }
    }
}