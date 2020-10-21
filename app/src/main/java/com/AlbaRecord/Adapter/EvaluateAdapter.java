package com.AlbaRecord.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AlbaRecord.Boss.ShowEmployeeDetailActivity;
import com.AlbaRecord.Model.EvaluateModel;
import com.AlbaRecord.R;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Radar;

import java.util.ArrayList;
import java.util.List;

public class EvaluateAdapter  extends RecyclerView.Adapter<EvaluateAdapter.EvaluateViewHolder> {
    private ArrayList<EvaluateModel> evaluateModels=new ArrayList<>();
    private Context mContext;

    public class EvaluateViewHolder extends RecyclerView.ViewHolder {
        TextView carrer,brandname,carrerlong,careerthing;
        AnyChartView anyChartView;
        Button evaluate_detail;
        public EvaluateViewHolder(@NonNull View itemView) {
            super(itemView);
            carrer=(TextView) itemView.findViewById(R.id.carrer);
            brandname=(TextView) itemView.findViewById(R.id.brandname);
            carrerlong=(TextView) itemView.findViewById(R.id.carrerlong);
            careerthing=(TextView)itemView.findViewById(R.id.careerthing);
            anyChartView=(AnyChartView)itemView.findViewById(R.id.carrerpentagon1);
            evaluate_detail=(Button)itemView.findViewById(R.id.evaluate_detail);
        }
    }

    public EvaluateAdapter(ArrayList<EvaluateModel> evaluateModels, Context mContext) {
        this.evaluateModels = evaluateModels;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public EvaluateAdapter.EvaluateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EvaluateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evaluate, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EvaluateAdapter.EvaluateViewHolder holder, int position) {
        EvaluateModel evaluateModel=evaluateModels.get(position);
        String carrer="경력"+ position;
        holder.carrer.setText(carrer);
        holder.brandname.setText(evaluateModel.getBrandname());
        holder.carrerlong.setText(evaluateModel.getDate_text());
        holder.careerthing.setText(evaluateModel.getCareerthing());
        Radar radar= AnyChart.radar();
       // radar.maxHeight("100");


        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("communication", evaluateModel.getCommunication()));
        data.add(new ValueDataEntry("diligence", evaluateModel.getDiligence()));
        data.add(new ValueDataEntry("flexibility", evaluateModel.getFlexibility()));
        data.add(new ValueDataEntry("mastery", evaluateModel.getMastery()));
        data.add(new ValueDataEntry("attitude", evaluateModel.getAttitude()));
        //radar.area(data);
        radar.data(data);
        holder.anyChartView.setChart(radar);

        holder.evaluate_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mContext.startActivity(new Intent(mContext, ShowEmployeeDetailActivity.class));
                Toast.makeText(mContext,"아직 기능구현이 안된 버튼입니다",Toast.LENGTH_SHORT).show();
            }
        });





    }

    @Override
    public int getItemCount() {
        return evaluateModels.size();
    }


}
