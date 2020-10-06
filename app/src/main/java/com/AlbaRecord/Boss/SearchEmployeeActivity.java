package com.AlbaRecord.Boss;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.AlbaRecord.R;

public class SearchEmployeeActivity extends AppCompatActivity implements View.OnClickListener {
    Button searchemployeebutton;
    EditText searchEmployeeEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_employee);
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
                String email=searchEmployeeEdit.getText().toString().trim();
                if(email.isEmpty()){
                    Toast.makeText(this, "이메일을입력해주시오", Toast.LENGTH_SHORT).show();
                }else{
                    Intent showIntent=new Intent(getApplicationContext(), ShowEmployeeActivity.class);
                    showIntent.putExtra("Email",email);
                    startActivity(showIntent);
                    finish();
                }
                break;
        }
    }
}