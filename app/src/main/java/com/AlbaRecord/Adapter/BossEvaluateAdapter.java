package com.AlbaRecord.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AlbaRecord.Model.BossEvaluateModel;
import com.AlbaRecord.R;

import java.util.ArrayList;

public class BossEvaluateAdapter extends RecyclerView.Adapter<BossEvaluateAdapter.BossEvaluateViewHolder> {
    private ArrayList<BossEvaluateModel> bossEvaluateModels;

    @NonNull
    @Override
    public BossEvaluateAdapter.BossEvaluateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BossEvaluateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_boss_evaluate, parent, false));
    }

    public BossEvaluateAdapter(ArrayList<BossEvaluateModel> bossEvaluateModels) {
        this.bossEvaluateModels = bossEvaluateModels;
    }

    @Override
    public void onBindViewHolder(@NonNull BossEvaluateAdapter.BossEvaluateViewHolder holder, int position) {
        BossEvaluateModel bossEvaluateModel=bossEvaluateModels.get(position);
        holder.date.setText(bossEvaluateModel.getDate_text());
        ArrayList<String>arrayList=bossEvaluateModel.getStrList();
        BossEvalute_detailAdapter bossEvalute_detailAdapter=new BossEvalute_detailAdapter(arrayList);
        holder.recyclerView.setAdapter(bossEvalute_detailAdapter);

    }

    @Override
    public int getItemCount() {
        return bossEvaluateModels.size();
    }

    public class BossEvaluateViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        RecyclerView recyclerView;
        public BossEvaluateViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            recyclerView=itemView.findViewById(R.id.recyclerView);
        }
    }
}
