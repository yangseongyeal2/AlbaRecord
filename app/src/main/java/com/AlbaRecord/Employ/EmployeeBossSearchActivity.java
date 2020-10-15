package com.AlbaRecord.Employ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.AlbaRecord.R;

public class EmployeeBossSearchActivity extends AppCompatActivity implements View.OnClickListener {
    Button searchemployeebutton;
    EditText searchEmployeeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_boss_search);
        initViewId();
        searchemployeebutton.setOnClickListener(this);
    }
    private void initViewId() {
        searchemployeebutton=(Button)findViewById(R.id.searchemployeebutton);
        searchEmployeeEdit=(EditText)findViewById(R.id.searchEmployeeEdit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.searchemployeebutton:
                String brandName=searchEmployeeEdit.getText().toString().trim();
                if(brandName.isEmpty()){
                    Toast.makeText(this, "상호명을입력해주시오", Toast.LENGTH_SHORT).show();
                }else{
                    Intent showIntent=new Intent(getApplicationContext(), ShowBossActivity.class);
                    showIntent.putExtra("BrandName",brandName);
                    startActivity(showIntent);
                    finish();
                }
                break;
        }
    }
}