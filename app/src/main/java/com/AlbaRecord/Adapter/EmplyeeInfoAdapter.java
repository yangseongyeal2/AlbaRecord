package com.AlbaRecord.Adapter;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;

import com.AlbaRecord.Boss.EvaluateEmployeeActivity;
import com.AlbaRecord.Boss.SearchEmployeeActivity;
import com.AlbaRecord.Boss.ShowEmployeeActivity;
import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.Model.UserModel;
import com.AlbaRecord.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.CLIPBOARD_SERVICE;

public class EmplyeeInfoAdapter extends RecyclerView.Adapter<EmplyeeInfoAdapter.EmplyeeInfoViewHolder> {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatePickerDialog.OnDateSetListener callbackMethod;


    private List<EmployeeModel> employeeModels;
    private Context mContext;

    public EmplyeeInfoAdapter(List<EmployeeModel> employeeModels, Context mContext) {
        this.employeeModels = employeeModels;
        this.mContext = mContext;
    }


    public class EmplyeeInfoViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView name, age, phone, workstart,AccountInfo;
        EditText position;
        Button setposition, evaluate, fire,call,account_save;

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
            call=(Button)itemView.findViewById(R.id.call);
            AccountInfo=(TextView)itemView.findViewById(R.id.AccountInfo);
            account_save=(Button)itemView.findViewById(R.id.account_save);

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
                            }catch (NullPointerException e){
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
                        holder.workstart.setText(year + "년" + monthOfYear + "월" + dayOfMonth + "일");
                        Map<String, Object> docData = new HashMap<>();
                        docData.put("근무시작시간", year + "년" + monthOfYear + "월" + dayOfMonth + "일");
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
                DatePickerDialog dialog = new DatePickerDialog(mContext, callbackMethod, 2019, 5, 24);
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
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel="tel:"+employeeModel.getPhonenumber();
                mContext.startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
            }
        });
        String AccountInfo=employeeModel.getAccount()+" "+employeeModel.getBank();
        holder.AccountInfo.setText(AccountInfo);

        holder.account_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클립보드 사용 코드
                ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Account",AccountInfo); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                clipboardManager.setPrimaryClip(clipData);
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
