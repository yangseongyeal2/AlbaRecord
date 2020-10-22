package com.AlbaRecord.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AlbaRecord.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BossEvalute_detailAdapter extends RecyclerView.Adapter<BossEvalute_detailAdapter.BossEvalute_detailViewHolder> {
    private ArrayList<String> arrayList;

    public BossEvalute_detailAdapter(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public BossEvalute_detailAdapter.BossEvalute_detailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BossEvalute_detailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_boss_evaluate_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BossEvalute_detailAdapter.BossEvalute_detailViewHolder holder, int position) {
        String str=arrayList.get(position);
        holder.text.setText(str);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class BossEvalute_detailViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        public BossEvalute_detailViewHolder(@NonNull View itemView) {
            super(itemView);
            text=itemView.findViewById(R.id.text);

        }
    }
}
