package com.example.decisionwheel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.decisionwheel.wheel.WheelEntity;

import java.util.ArrayList;
import java.util.List;

public class WheelAdapter extends RecyclerView.Adapter<WheelAdapter.WheelViewHolder> {

    public interface OnWheelActionListener {
        void onUse(WheelEntity wheel);
        void onEdit(WheelEntity wheel);
        void onDelete(WheelEntity wheel);
    }

    private List<WheelEntity> wheels = new ArrayList<>();
    private final OnWheelActionListener listener;

    public WheelAdapter(OnWheelActionListener listener) {
        this.listener = listener;
    }

    public void setWheels(List<WheelEntity> wheels) {
        this.wheels = wheels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WheelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wheel_item, parent, false);
        return new WheelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WheelViewHolder holder, int position) {
        WheelEntity wheel = wheels.get(position);
        holder.nameText.setText(wheel.getName());
        holder.btnUse.setOnClickListener(v -> listener.onUse(wheel));
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(wheel));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(wheel));
    }

    @Override
    public int getItemCount() {
        return wheels.size();
    }

    static class WheelViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        ImageButton btnUse, btnEdit, btnDelete;

        WheelViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.text_wheel_category);
            btnUse = itemView.findViewById(R.id.btn_use_wheel);
            btnEdit = itemView.findViewById(R.id.btn_edit_wheel);
            btnDelete = itemView.findViewById(R.id.btn_delete_wheel);
        }
    }
}
