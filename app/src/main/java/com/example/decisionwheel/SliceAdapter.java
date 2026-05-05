package com.example.decisionwheel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.decisionwheel.wheel.Slice;

import java.util.ArrayList;
import java.util.List;

public class SliceAdapter extends RecyclerView.Adapter<SliceAdapter.ViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(Slice slice);
    }

    private List<Slice> slices = new ArrayList<>();
    private final OnDeleteClickListener listener;

    public SliceAdapter(OnDeleteClickListener listener) {
        this.listener = listener;
    }

    public void setSlices(List<Slice> slices) {
        this.slices = slices;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Slice slice = slices.get(position);
        holder.objectiveText.setText(slice.getObjective());
        holder.colorDot.setBackgroundColor(slice.getColor());
        holder.deleteBtn.setOnClickListener(v -> listener.onDeleteClick(slice));
    }

    @Override
    public int getItemCount() {
        return slices.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View colorDot;
        TextView objectiveText;
        ImageButton deleteBtn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorDot = itemView.findViewById(R.id.sliceColorDot);
            objectiveText = itemView.findViewById(R.id.sliceObjectiveText);
            deleteBtn = itemView.findViewById(R.id.sliceDeleteBtn);
        }
    }
}
