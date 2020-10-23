package com.AlbaRecord.Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;

import com.AlbaRecord.Boss.EvaluateEmployeeActivity;
import com.AlbaRecord.Boss.MyEmployeeActivity;
import com.AlbaRecord.Boss.SearchEmployeeActivity;
import com.AlbaRecord.Boss.ShowEmployeeActivity;
import com.AlbaRecord.Boss.ShowEmployeeDetailActivity;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.R;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.CLIPBOARD_SERVICE;

public class EmplyeeInfoAdapter extends RecyclerView.Adapter<EmplyeeInfoAdapter.EmplyeeInfoViewHolder> {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private Activity activity;
    private BossModel bossModel;
    private final String URL = "https://fcm.googleapis.com/fcm/send";





    private List<EmployeeModel> employeeModels;
    private Context mContext;

    public EmplyeeInfoAdapter(List<EmployeeModel> employeeModels, Context mContext, Activity activity, BossModel bossModel) {
        this.employeeModels = employeeModels;
        this.mContext = mContext;
        this.activity = activity;
        this.bossModel=bossModel;
    }


    public class EmplyeeInfoViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView name, age, phone, workstart, AccountInfo;
        EditText position;
        Button setposition, evaluate, fire, call, account_save, evaluate_ex;
        RequestQueue mRequesQue;


        public EmplyeeInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.photo);
            name = (TextView) itemView.findViewById(R.id.name);
            age = (TextView) itemView.findViewById(R.id.age);
            phone = (TextView) itemView.findViewById(R.id.phone);
            position = (EditText) itemView.findViewById(R.id.position);
            setposition = (Button) itemView.findViewById(R.id.setposition);
            evaluate = (Button) itemView.findViewById(R.id.evaluate);
            fire = (Button) itemView.findViewById(R.id.fire);
            workstart = (TextView) itemView.findViewById(R.id.workstart);
            call = (Button) itemView.findViewById(R.id.call);
            AccountInfo = (TextView) itemView.findViewById(R.id.AccountInfo);
            account_save = (Button) itemView.findViewById(R.id.account_save);
            evaluate_ex = (Button) itemView.findViewById(R.id.evaluate_ex);
            mRequesQue = Volley.newRequestQueue(mContext);


        }

    }

    @Override
    public void onBindViewHolder(@NonNull EmplyeeInfoAdapter.EmplyeeInfoViewHolder holder, int position) {
        EmployeeModel employeeModel = employeeModels.get(position);

        //저장되있던 정보 불러오기
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("MyEmployeeInfo")
                .document(employeeModel.getDocumentId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> data = documentSnapshot.getData();

                            Log.d("어댑터", data.toString());
                            try {
                                holder.workstart.setText(data.get("근무시작시간").toString());
                                holder.position.setText(data.get("직급").toString());
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                });


        //employeeModel에서 가져올수 있는 정보
        Glide.with(holder.photo)
                .load(employeeModel.getPhoto())
                .into(holder.photo);
        holder.name.setText(employeeModel.getName());
        holder.age.setText(employeeModel.getAge());
        holder.phone.setText(employeeModel.getPhonenumber());
        //새로 작성해줘야 하는 정보
        holder.setposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.position.getText().toString().trim().isEmpty()) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(mContext);
                    ad.setIcon(R.mipmap.ic_launcher);
                    ad.setTitle("직원의 직급");
                    ad.setMessage("직원의 직급을 적어주십시오");
                    ad.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                } else {
                    String pos = holder.position.getText().toString().trim();
                    Map<String, Object> docData = new HashMap<>();
                    docData.put("직급", pos);
                    db.collection("users")
                            .document(mAuth.getCurrentUser().getUid())
                            .collection("MyEmployeeInfo")
                            .document(employeeModel.getDocumentId()).set(docData, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("업데이트옵션", "성공");
                        }
                    });
                    holder.position.clearFocus();
                }
            }
        });
        //근무 시간 시간
        //달력 꺼내기 ,저장
        holder.workstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackMethod = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        holder.workstart.setText(year + "년" + (monthOfYear + 1) + "월" + dayOfMonth + "일");
                        Map<String, Object> docData = new HashMap<>();
                        docData.put("근무시작시간", year + "년" + (monthOfYear + 1) + "월" + dayOfMonth + "일");
                        db.collection("users")
                                .document(mAuth.getCurrentUser().getUid())
                                .collection("MyEmployeeInfo")
                                .document(employeeModel.getDocumentId()).set(docData, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("업데이트옵션", "성공");
                            }
                        });

                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(mContext, callbackMethod, 2020, 9, 25);
                dialog.show();
            }
        });
        //평가하기 눌렀을때
        holder.evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EvaluateEmployeeActivity.class);
                intent.putExtra("DocumentId", employeeModel.getDocumentId());
                mContext.startActivity(intent);
                activity.finish();
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = "tel:" + employeeModel.getPhonenumber();
                mContext.startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
            }
        });
        String AccountInfo = employeeModel.getAccount() + " " + employeeModel.getBank();
        holder.AccountInfo.setText(AccountInfo);

        holder.account_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클립보드 사용 코드
                ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Account", AccountInfo); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                clipboardManager.setPrimaryClip(clipData);
            }
        });

        holder.evaluate_ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_ex = new Intent(mContext, ShowEmployeeDetailActivity.class);
                intent_ex.putExtra("DocumentId", employeeModel.getDocumentId());
                mContext.startActivity(intent_ex);
                activity.finish();
            }
        });

        holder.fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(mContext);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("직원 해고");
                ad.setMessage("이 직원을 해고 하시겠습니까?");
                ad.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("users")
                                .document(mAuth.getCurrentUser().getUid())
                                .update("MyEmployee", FieldValue.arrayRemove(employeeModel.getDocumentId()))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        db.collection("users")
                                                .document(mAuth.getCurrentUser().getUid())
                                                .collection("MyEmployeeInfo")
                                                .document(employeeModel.getDocumentId())
                                                .delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        //메세지보내기

                                                        JSONObject mainObj = new JSONObject();
                                                        try {
                                                            mainObj.put("to", "/topics/" + employeeModel.getDocumentId() );
                                                            JSONObject notificationObj = new JSONObject();
                                                            notificationObj.put("title", bossModel.getBrand());//사장이 보냄
                                                            notificationObj.put("body", bossModel.getBrand()+" 님이 고용을 원합니다 수락하시겠습니까?");//브랜드네임
                                                            notificationObj.put("DocumentId", mAuth.getCurrentUser().getUid());//직원 도큐먼트ID
                                                            notificationObj.put("flag", "3");//사장이해고 메세지 보냄
                                                            // mainObj.put("notification",notificationObj);
                                                            mainObj.put("data", notificationObj);


                                                            JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL,
                                                                    mainObj,
                                                                    new com.android.volley.Response.Listener<JSONObject>() {
                                                                        @Override
                                                                        public void onResponse(JSONObject response) {
                                                                            Log.d("양성열",response.toString());

                                                                        }
                                                                    }, new com.android.volley.Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    Log.d("양성열","onErrorResponse");
                                                                }
                                                            }) {
                                                                @Override
                                                                public Map<String, String> getHeaders() throws AuthFailureError {
                                                                    Map<String, String> header = new HashMap<>();
                                                                    header.put("content-type", "application/json");
                                                                    header.put("authorization", "key=AAAARXzwbpI:APA91bGeGR-WB_570H5C2xeoal97SKyuO1Bj1aUgehYdHlH4Us5bbgsBrPsoQW7IzFItjSv1PJ2KT-TbPJb-ZCCoCD4DsnY5TCn9D6pkOijllp3qOqhQu1-YO3SccAHUpDaDpGJXDrOq");
                                                                    Log.d("양성열","getHeaders");
                                                                    return header;
                                                                }
                                                            };
                                                            Log.d("양성열","큐 전");
                                                            holder.mRequesQue.add(request);
                                                            Log.d("양성열","큐 후");
                                                        } catch (JSONException e) {
                                                            Log.d("양성열","캐치");
                                                            e.printStackTrace();
                                                        }
                                                        mContext.startActivity(new Intent(mContext, MyEmployeeActivity.class));
                                                        activity.finish();
                                                        dialog.dismiss();
                                                    }
                                                });
                                    }
                                });

                    }
                });
                ad.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();


//                mContext.notify();


            }
        });
    }
    @Override
    public int getItemCount() {
        return employeeModels.size();
    }
    @NonNull
    @Override
    public EmplyeeInfoAdapter.EmplyeeInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmplyeeInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employ_info, parent, false));
    }
}
