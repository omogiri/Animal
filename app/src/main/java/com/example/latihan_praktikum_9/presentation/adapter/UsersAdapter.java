package com.example.latihan_praktikum_9.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.latihan_praktikum_9.R;
import com.example.latihan_praktikum_9.data.entity.Users;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private List<Users> usersList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Users country);
    }

    public UsersAdapter(List<Users> usersList, OnItemClickListener listener) {
        this.usersList = usersList;
        this.listener = listener;
    }

    public void setUsers(List<Users> users) {
        this.usersList = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users country = usersList.get(position);
        holder.tvName.setText("Nama: " + country.getName());
        holder.tvCapital.setText("Nama panggilan: " + country.getUsername());
        holder.tvCurrency.setText("Email: " + country.getEmail());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(country);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvCapital, tvCurrency;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvCapital = itemView.findViewById(R.id.tv_capital);
            tvCurrency = itemView.findViewById(R.id.tv_currency);
        }
    }
}
