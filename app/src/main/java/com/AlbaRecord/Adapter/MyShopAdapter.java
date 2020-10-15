package com.AlbaRecord.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.R;

import java.util.ArrayList;

public class MyShopAdapter extends RecyclerView.Adapter<MyShopAdapter.MyShopViewHolder> {
    private ArrayList<BossModel> bossModels;
    private Context mContext;



    public class MyShopViewHolder extends RecyclerView.ViewHolder {
        TextView name, phonenum, email,brand;

        public MyShopViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phonenum = itemView.findViewById(R.id.phonenum);
            email = itemView.findViewById(R.id.email);
            brand=itemView.findViewById(R.id.brandName);

        }
    }

    public MyShopAdapter(ArrayList<BossModel> bossModels, Context mContext) {
        this.bossModels = bossModels;
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(@NonNull MyShopViewHolder holder, int position) {
        BossModel bossModel = bossModels.get(position);
        holder.name.setText(bossModel.getName());
        holder.phonenum.setText(bossModel.getPhoneNumber());
        holder.email.setText(bossModel.getEmail());
        holder.brand.setText(bossModel.getBrand());

    }

    @Override
    public int getItemCount() {
        return bossModels.size();
    }

    @NonNull
    @Override
    public MyShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyShopViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myshop, parent, false));
    }


}
