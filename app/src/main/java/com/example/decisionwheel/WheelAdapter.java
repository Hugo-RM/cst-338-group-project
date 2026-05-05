package com.example.decisionwheel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.decisionwheel.wheel.Wheel;

import java.util.ArrayList;
import java.util.List;

public class WheelAdapter extends RecyclerView.Adapter<WheelAdapter.WheelViewHolder> {

    private List<Wheel> wheels = new ArrayList<>();
    private final OnWheelActionListener listener;

    public interface OnWheelActionListener {
        void onUse(Wheel wheel);
        void onEdit(Wheel wheel);
        void onDelete(Wheel wheel);
    }

    public WheelAdapter(OnWheelActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public WheelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wheel_item, parent, false);
        return new WheelViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WheelViewHolder holder, int position) {
        Wheel currentWheel = wheels.get(position);
        holder.textViewCategory.setText(currentWheel.getCategory());

        holder.btnUse.setOnClickListener(v -> listener.onUse(currentWheel));
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(currentWheel));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(currentWheel));
    }

    @Override
    public int getItemCount() {
        return wheels.size();
    }

    public void setWheels(List<Wheel> wheels) {
        this.wheels = wheels;
        notifyDataSetChanged();
    }

    static class WheelViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewCategory;
        private final ImageButton btnUse;
        private final ImageButton btnEdit;
        private final ImageButton btnDelete;

        public WheelViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategory = itemView.findViewById(R.id.text_wheel_category);
            btnUse = itemView.findViewById(R.id.btn_use_wheel);
            btnEdit = itemView.findViewById(R.id.btn_edit_wheel);
            btnDelete = itemView.findViewById(R.id.btn_delete_wheel);
        }
    }
}
