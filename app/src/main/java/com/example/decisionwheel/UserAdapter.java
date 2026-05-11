package com.example.decisionwheel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.decisionwheel.database.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<UserEntity> users = new ArrayList<>();
    private OnUserActionListener listener;

    public interface OnUserActionListener {
        void onUserClick(UserEntity user);
        void onEditUsername(UserEntity user);
        void onEditPassword(UserEntity user);
        void onDeleteUser(UserEntity user);
        void onToggleAdmin(UserEntity user);
    }

    public void setOnUserActionListener(OnUserActionListener listener) {
        this.listener = listener;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserEntity user = users.get(position);
        holder.usernameText.setText(user.getUsername());
        holder.adminBadge.setVisibility(user.isAdmin() ? View.VISIBLE : View.GONE);

        holder.usernameText.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserClick(user);
            }
        });

        holder.moreButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenu().add("Edit Username");
            popup.getMenu().add("Edit Password");
            popup.getMenu().add(user.isAdmin() ? "Remove Admin" : "Make Admin");
            popup.getMenu().add("Delete User");

            popup.setOnMenuItemClickListener(item -> {
                if (listener == null) return false;
                String title = item.getTitle().toString();
                if (title.equals("Edit Username")) {
                    listener.onEditUsername(user);
                } else if (title.equals("Edit Password")) {
                    listener.onEditPassword(user);
                } else if (title.equals("Make Admin") || title.equals("Remove Admin")) {
                    listener.onToggleAdmin(user);
                } else if (title.equals("Delete User")) {
                    listener.onDeleteUser(user);
                }
                return true;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        TextView adminBadge;
        ImageButton moreButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.userItemUsername);
            adminBadge = itemView.findViewById(R.id.userItemAdminBadge);
            moreButton = itemView.findViewById(R.id.userItemMoreButton);
        }
    }
}
