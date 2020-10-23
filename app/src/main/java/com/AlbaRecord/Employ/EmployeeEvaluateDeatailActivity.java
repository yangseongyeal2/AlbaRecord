package com.AlbaRecord.Employ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.AlbaRecord.Map.NaverMapActivity;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.Model.EvaluateModel;
import com.AlbaRecord.R;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Radar;
import com.google.firebase.firestore.FirebaseFirestore;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class EmployeeEvaluateDeatailActivity extends AppCompatActivity {
    TextView carrer, brandname, carrerlong, careerthing,attitude,communication,diligence,flexibility,mastery,detail;
    AnyChartView carrerpentagon1;
    Button evaluate_detail, brandnavermap1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_evaluate_deatail);
        EvaluateModel evaluateModel = (EvaluateModel) getIntent().getParcelableExtra("EvaluateModel");
        BossModel bossModel = (BossModel) getIntent().getParcelableExtra("BossModel");

        initViewId();


        brandname.setText(evaluateModel.getBrandname());
        carrerlong.setText(evaluateModel.getDate_text());
        careerthing.setText(evaluateModel.getCareerthing());
        Radar radar = AnyChart.radar();
        // radar.maxHeight("100");


        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("communication", evaluateModel.getCommunication()));
        data.add(new ValueDataEntry("diligence", evaluateModel.getDiligence()));
        data.add(new ValueDataEntry("flexibility", evaluateModel.getFlexibility()));
        data.add(new ValueDataEntry("mastery", evaluateModel.getMastery()));
        data.add(new ValueDataEntry("attitude", evaluateModel.getAttitude()));
        //radar.area(data);
        radar.data(data);
        carrerpentagon1.setChart(radar);

        diligence.setText(String.valueOf(evaluateModel.getDiligence()));
        flexibility.setText(String.valueOf(evaluateModel.getFlexibility()));
        mastery.setText(String.valueOf(evaluateModel.getMastery()));
        attitude.setText(String.valueOf(evaluateModel.getAttitude()));
        communication.setText(String.valueOf(evaluateModel.getCommunication()));
        detail.setText(evaluateModel.getHashtagdetail());










        brandnavermap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NaverMapActivity.class);
                intent.putExtra("위도", bossModel.getLatitude());
                Log.d("네이버로 보내는 위도", String.valueOf(bossModel.getLatitude()));
                intent.putExtra("경도", bossModel.getLongtitude());
                startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });

    }

    private void initViewId() {

        carrer = findViewById(R.id.carrer);
        brandname = findViewById(R.id.brandname);
        carrerlong = findViewById(R.id.carrerlong);
        careerthing = findViewById(R.id.careerthing);
        carrerpentagon1 = findViewById(R.id.carrerpentagon1);
        brandnavermap1 = findViewById(R.id.brandnavermap1);
        attitude=findViewById(R.id.attitude);
        communication=findViewById(R.id.communication);
        diligence=findViewById(R.id.diligence);
        flexibility=findViewById(R.id.flexibility);
        mastery=findViewById(R.id.mastery);
        detail=findViewById(R.id.detail);





    }

}