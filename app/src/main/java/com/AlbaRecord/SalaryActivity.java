package com.AlbaRecord;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.widget.Toast;


import java.util.Calendar;
import java.util.GregorianCalendar;

public class SalaryActivity extends AppCompatActivity implements View.OnClickListener {
    private Button lowestpay, start_worktimebtn, end_worktimebtn, insertbtn,logout;
    private EditText HourlyWage_edit;
    private InputMethodManager imm;
    int starthour, startminute, endhour, endminute, HourlyWage;
    int sumhour, summinute;
    private TextView starttime, endtime, salary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary);
        initViewId();
        initToolBar();
        lowestpay.setOnClickListener(this);
        start_worktimebtn.setOnClickListener(this);
        end_worktimebtn.setOnClickListener(this);
        insertbtn.setOnClickListener(this);

        Log.d("SalaryActivity", starttime.getText().toString().trim());
        if (!starttime.getText().toString().equals("시간:분") && !endtime.getText().toString().equals("시간:분")) {
            Log.d("SalaryActivity:if문안", starttime.getText().toString().trim());
        }


    }
    private void initToolBar() {
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(this);
        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initViewId() {
        lowestpay = (Button) findViewById(R.id.lowestpay);
        HourlyWage_edit = (EditText) findViewById(R.id.HourlyWage_edit);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);//키보드 올리기 내리기 할때 사용

        start_worktimebtn = (Button) findViewById(R.id.start_worktimebtn);
        starttime = (TextView) findViewById(R.id.starttime);

        end_worktimebtn = (Button) findViewById(R.id.end_worktimebtn);
        endtime = (TextView) findViewById(R.id.endtime);

        Calendar cal = new GregorianCalendar();
        //Calendar cal = Calendar.getInstance();
        starthour = cal.get(Calendar.HOUR_OF_DAY);
        startminute = cal.get(Calendar.MINUTE);
        endhour = cal.get(Calendar.HOUR_OF_DAY);
        endminute = cal.get(Calendar.MINUTE);

        salary = (TextView) findViewById(R.id.salary);
        insertbtn = (Button) findViewById(R.id.insertbtn);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lowestpay://최저시급눌렀을때
                HourlyWage_edit.setText("8590");
                HourlyWage_edit.clearFocus();
                //imm.showSoftInput(HourlyWage_edit,0);//키보드 보이기
                imm.hideSoftInputFromWindow(HourlyWage_edit.getWindowToken(), 0);//키보드 내리기
                break;
            case R.id.start_worktimebtn:
                //리스너 적용
                new TimePickerDialog(SalaryActivity.this, mTimeSetListener, starthour,
                        startminute, false).show();
                break;
            case R.id.end_worktimebtn:
                //리스너적용
                new TimePickerDialog(SalaryActivity.this, mTimeSetListener2, endhour,
                        endminute, false).show();
                break;
            case R.id.insertbtn:
                getsalary();
                break;
        }
    }

    //리스너 선언
    TimePickerDialog.OnTimeSetListener mTimeSetListener =//starttime
            new TimePickerDialog.OnTimeSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                    HourlyWage=Integer.parseInt(HourlyWage_edit.getText().toString());
                    //사용자가 입력한 값을 가져온뒤
                    starthour = hourOfDay;

                    if (minute >= 30) {
                        startminute = 30;
                    } else {
                        startminute = 0;
                    }
                    //startminute = minute;
                    //텍스트뷰의 값을 업데이트함
                    starttime.setText(String.valueOf(starthour) + ":" + String.valueOf(startminute));
                   // getsalary();

                }


            };

    private void getsalary() {
        if (!starttime.getText().toString().equals("시간:분") && !endtime.getText().toString().equals("시간:분")) {
            try {
                HourlyWage = Integer.parseInt(HourlyWage_edit.getText().toString());
                if(HourlyWage<8590){
                    HourlyWage_edit.setText("");
                    imm.showSoftInput(HourlyWage_edit,0);
                    Toast.makeText(SalaryActivity.this,"최저임금보다 낮은 임금입니다",Toast.LENGTH_SHORT).show();
                    return;
                }
                //Log.d("SalaryActivity:if문안",starttime.getText().toString().trim());
                int sumhour = endhour - starthour;
                if(sumhour<0){
                    sumhour+=24;
                }
                int summinute = endminute - startminute;
                int resultm = sumhour * 60 + summinute;
                sumhour = resultm / 60;
                summinute = resultm % 60;
                if (summinute == 30) {
                    String reslut = String.valueOf(sumhour * HourlyWage + HourlyWage / 2);
                    salary.setText(reslut);
                } else if (summinute == 0) {
                    String reslut = String.valueOf(sumhour * HourlyWage);
                    salary.setText(reslut);
                } else {
                    String reslut = String.valueOf(sumhour * HourlyWage - HourlyWage / 2);
                    salary.setText(reslut);
                }

            } catch (NumberFormatException e) {
                Toast.makeText(SalaryActivity.this, "시급을 입력하시오", Toast.LENGTH_SHORT).show();
            }


        }
    }

    //리스너 선언
    TimePickerDialog.OnTimeSetListener mTimeSetListener2 =//endtime
            new TimePickerDialog.OnTimeSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    //사용자가 입력한 값을 가져온뒤
                    endhour = hourOfDay;
                    if (minute >= 30) {
                        endminute = 30;
                    } else {
                        endminute = 0;
                    }
                    //endminute = minute;
                    //텍스트뷰의 값을 업데이트함
                    endtime.setText(String.valueOf(endhour) + ":" + String.valueOf(endminute));
                   // getsalary();

                }
            };


}