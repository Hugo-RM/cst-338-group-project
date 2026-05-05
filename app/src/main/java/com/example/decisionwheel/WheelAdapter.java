package com.example.decisionwheel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.decisionwheel.wheel.WheelEntity;

import java.util.ArrayList;
import java.util.List;

public class WheelAdapter extends RecyclerView.Adapter<WheelAdapter.ViewHolder> {

    public interface OnWheelClickListener {
        void onWheelClick(WheelEntity wheel);
    }

    private List<WheelEntity> wheels = new ArrayList<>();
    private final OnWheelClickListener listener;

    public WheelAdapter(OnWheelClickListener listener) {
        this.listener = listener;
    }

    public void setWheels(List<WheelEntity> wheels) {
        this.wheels = wheels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wheel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WheelEntity wheel = wheels.get(position);
        holder.nameText.setText(wheel.getName());
        holder.itemView.setOnClickListener(v -> listener.onWheelClick(wheel));
    }

    @Override
    public int getItemCount() {
        return wheels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.wheelItemName);
        }
    }
}
