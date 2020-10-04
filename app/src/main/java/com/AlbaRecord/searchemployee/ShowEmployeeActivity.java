package com.AlbaRecord.searchemployee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.AlbaRecord.R;

public class ShowEmployeeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button more;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_employee);
        initViewId();

        more.setOnClickListener(this);
    }

    private void initViewId() {
        more=findViewById(R.id.more);
        // map_view=(MapView) findViewById(R.id.map_view);

    }


    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more:
                startActivity(new Intent(getApplicationContext(), ShowEmployeeActivity.class));
                break;
        }
    }
}