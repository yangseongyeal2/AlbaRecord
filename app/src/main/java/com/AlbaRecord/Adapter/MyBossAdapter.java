package com.AlbaRecord.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AlbaRecord.Boss.ShowEmployeeDetailActivity;
import com.AlbaRecord.Employ.ShowBossDetailActivity;
import com.AlbaRecord.Interface.OnItemClick;
import com.AlbaRecord.Map.NaverMapActivity;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.R;

import java.util.ArrayList;

public class MyBossAdapter extends RecyclerView.Adapter<MyBossAdapter.MyBossViewHolder> {
    private ArrayList<BossModel> bossModels;
    private Context mContext;
    private Activity mActivity;
    private OnItemClick mCallback;

    private CommonAdapter.OnItemClickListener mListener = null;
    public void setOnIemlClickListner(CommonAdapter.OnItemClickListener listner) {
        this.mListener = listner;
    }
    ////////////////////////////////


    public MyBossAdapter(ArrayList<BossModel> bossModels, Context mContext, Activity mActivity, OnItemClick mCallback) {
        this.bossModels = bossModels;
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public MyBossAdapter.MyBossViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyBossViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myboss, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyBossAdapter.MyBossViewHolder holder, int position) {
        BossModel bossModel=bossModels.get(position);
        holder.brandname.setText(bossModel.getBrand());
        holder.phonenum.setText(bossModel.getPhoneNumber());
        holder.address_result.setText(bossModel.getAddress());
        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(mContext, NaverMapActivity.class);
                intent.putExtra("위도",bossModel.getLatitude());
                Log.d("네이버로 보내는 위도",String.valueOf(bossModel.getLatitude()));
                intent.putExtra("경도",bossModel.getLongtitude());
                mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        holder.ex_evalute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mActivity.finish();
                Intent intent=new Intent(mContext, ShowBossDetailActivity.class);
                intent.putExtra("DocumentId",bossModel.getDocumentId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bossModels.size();
    }

    public class MyBossViewHolder extends RecyclerView.ViewHolder {
        private TextView brandname,phonenum,address_result;
        private Button map;
        private Button ex_evalute;

        public MyBossViewHolder(@NonNull View itemView) {
            super(itemView);
            brandname=itemView.findViewById(R.id.brandname);
            phonenum=itemView.findViewById(R.id.phonenum);
            address_result=itemView.findViewById(R.id.address_result);
            map=itemView.findViewById(R.id.map);
            ex_evalute=itemView.findViewById(R.id.ex_evalute);



            itemView.setOnClickListener(new View.OnClickListener() {//클릭했을때
                @Override
                public void onClick(View v) {//들어가는 기능 detail로
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onitemClick(v, pos);
                        }
                    }
                }
            });

        }
    }
}
