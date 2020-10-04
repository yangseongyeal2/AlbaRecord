package com.AlbaRecord.searchemployee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.AlbaRecord.R;

public class SearchEmployeeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button showAlba;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_employee);
        initViewId();

        showAlba.setOnClickListener(this);
    }

    private void initViewId() {
        showAlba=findViewById(R.id.searchemployeebutton);
        // map_view=(MapView) findViewById(R.id.map_view);

    }


    public void onClick(View v) {
        switch (v.getId()){
            case R.id.searchemployeebutton:
                startActivity(new Intent(getApplicationContext(), ShowEmployeeActivity.class));
                break;
        }
    }
}