package com.AlbaRecord.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ColleagueAdapter extends RecyclerView.Adapter<ColleagueAdapter.ColleagueViewHolder> {
    List<EmployeeModel> employeeModels=new ArrayList<>();
    private Context mContext;



    @Override
    public void onBindViewHolder(@NonNull ColleagueAdapter.ColleagueViewHolder holder, int position) {
        EmployeeModel employeeModel=employeeModels.get(position);
        holder.colleaguename1.setText(employeeModel.getName());
        holder.colleagueage1.setText(employeeModel.getAge());
        holder.colleaguephone1.setText(employeeModel.getPhonenumber());
        Glide.with(holder.Photo1)
                .load(employeeModel.getPhoto())
                .into(holder.Photo1);
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel="tel:"+employeeModel.getPhonenumber();
                mContext.startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
            }
        });


    }

    public ColleagueAdapter(List<EmployeeModel> employeeModels, Context mContext) {
        this.employeeModels = employeeModels;
        this.mContext = mContext;
    }

    public class ColleagueViewHolder extends RecyclerView.ViewHolder {
        private TextView colleaguename1,colleagueage1,colleaguephone1;
        private ImageView Photo1;
        private Button call;

        public ColleagueViewHolder(@NonNull View itemView) {
            super(itemView);
            colleaguename1=itemView.findViewById(R.id.colleaguename1);
            colleagueage1=itemView.findViewById(R.id.colleagueage1);
            colleaguephone1=itemView.findViewById(R.id.colleaguephone1);
            Photo1=itemView.findViewById(R.id.Photo1);
            call=itemView.findViewById(R.id.call);


        }
    }


    @NonNull
    @Override
    public ColleagueAdapter.ColleagueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ColleagueViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_colleague, parent, false));
    }
    @Override
    public int getItemCount() {
        return employeeModels.size();
    }
}
