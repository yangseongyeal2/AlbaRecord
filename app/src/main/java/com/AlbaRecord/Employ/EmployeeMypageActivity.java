package com.AlbaRecord.Employ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AlbaRecord.Boss.BossMypageActivity;
import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.R;
import com.AlbaRecord.login.DaumWebViewActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.Objects;

public class EmployeeMypageActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    TextView name,age,address,account_text;
    Button setpassword,setaddress,seteducation,setphoto,setSelfInroduce,setAccount;
    EditText education,SelfIntroduce;
    private ProgressDialog progressDialog;
    String emailAddress="";
    ImageView photo;
    Uri downloadUri;
    String changedAdress="";
    String AccountInfo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_mypage);

        changedAdress=getIntent().getStringExtra("주소");
        try {
            if(!changedAdress.isEmpty()){
                db.collection("users").document(mAuth.getCurrentUser().getUid()).update("adress",changedAdress).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"주소변경이 완료되었습니다",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        initViewID();
        RetriveEmployeeInfo();
    }

    private void RetriveEmployeeInfo() {
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                EmployeeModel employeeModel=documentSnapshot.toObject(EmployeeModel.class);
                name.setText(employeeModel.getName());
                age.setText(employeeModel.getAge());
                address.setText(employeeModel.getAdress());
                education.setText(employeeModel.getDegree());
                Glide.with(photo).load(employeeModel.getPhoto()).into(photo);
                SelfIntroduce.setText(employeeModel.getSelf_introduce());
                AccountInfo=employeeModel.getAccount()+" "+employeeModel.getBank();
                account_text.setText(AccountInfo);


            }
        });
    }

    private void initViewID() {
        name=findViewById(R.id.name);
        age=findViewById(R.id.age);
        address=findViewById(R.id.address);
        education=findViewById(R.id.education);
        setpassword=findViewById(R.id.setpassword);
        setpassword.setOnClickListener(this);
        setaddress=findViewById(R.id.setaddress);
        setaddress.setOnClickListener(this);
        seteducation=findViewById(R.id.seteducation);
        seteducation.setOnClickListener(this);
        progressDialog= progressDialog = new ProgressDialog(this);
        setphoto=findViewById(R.id.setphoto);
        setphoto.setOnClickListener(this);
        photo=findViewById(R.id.photo);
        SelfIntroduce=findViewById(R.id.SelfIntroduce);
        setSelfInroduce=findViewById(R.id.setSelfInroduce);
        setSelfInroduce.setOnClickListener(this);
        account_text=findViewById(R.id.account_text);
        account_text.setOnClickListener(this);
        setAccount=findViewById(R.id.setAccount);
        setAccount.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.seteducation:
                if(!education.getText().toString().isEmpty()){
                    db.collection("users")
                            .document(mAuth.getCurrentUser().getUid())
                            .update("degree",education.getText().toString().trim())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"최종학력이 수정되었습니다",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                break;
            case R.id.setaddress:
                finish();
                Intent intent1 = new Intent(this, DaumWebViewActivity.class);
                intent1.putExtra("flag", "직원MyPage");
                startActivity(intent1); //추가해 줄 로그인 액티비티
                break;
            case R.id.setpassword:
                progressDialog.setMessage("처리중입니다. 잠시 기다려 주세요...");
                progressDialog.show();
                mAuth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
//                                    Toast.makeText(FindActivity.this, "이메일을 보냈습니다.", Toast.LENGTH_LONG).show();
                                    AlertDialog.Builder ad = new AlertDialog.Builder(EmployeeMypageActivity.this);
                                    ad.setIcon(R.mipmap.ic_launcher);
                                    ad.setTitle("비밀번호 재설정");
                                    ad.setMessage("본인의 Email에 비밀번호 재설정 Link를 보냈습니다");
                                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                        }
                                    });
                                    ad.show();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(EmployeeMypageActivity.this, "메일 보내기 실패!", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                break;
            case R.id.setphoto:
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                mIntent.setAction(Intent.ACTION_GET_CONTENT);
                mIntent.setType("image/*");
//                startActivity(mIntent);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(mIntent, 1);
                break;
            case R.id.setSelfInroduce:
                db.collection("users")
                        .document(mAuth.getCurrentUser().getUid())
                        .update("self_introduce",SelfIntroduce.getText().toString().trim())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"자기소개 수정이 완료되엇습니다",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.account_text:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Account",AccountInfo); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                clipboardManager.setPrimaryClip(clipData);
                break;
            case R.id.setAccount:
                Intent accountintent = new Intent(this, AccountPopUpActivity.class);
                accountintent.putExtra("data", "Test Popup");
                startActivityForResult(accountintent, 2);


                break;

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
//                    InputStream in = getContentResolver().openInputStream(data.getData());
//                    Bitmap img = BitmapFactory.decodeStream(in);
//                    in.close();
                    StorageReference ref = mStorageRef.child("profile/" + new Date() + ".jpg");
                    UploadTask uploadTask = ref.putFile(Objects.requireNonNull(data.getData()));
                    progressDialog.show();
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Log.d("스토리지업로드성공", task.getResult().toString());
                                throw task.getException();

                            }

                            // Continue with the task to get the download URL
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Log.d("스토리지업로드성공", task.getResult().toString());
                                downloadUri = task.getResult();
//                                Drawable mDrawble=loadImage(downloadUri.toString());
//                                image.setImageDrawable(mDrawble);
                                Glide.with(photo)
                                        .load(downloadUri)
                                        .into(photo);
                                db.collection("users")
                                        .document(mAuth.getCurrentUser().getUid())
                                        .update("photo",downloadUri.toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),"사진변경이 완료되었습니다",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                progressDialog.dismiss();
                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    });


                    // 이미지 표시
                    //imageView.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode==2){
            if (resultCode == RESULT_OK){
                String Bank = data.getStringExtra("Bank");
                String Account = data.getStringExtra("Account");
                AccountInfo=Account+" "+Bank;
                account_text.setText(AccountInfo);
                db.collection("users").document(mAuth.getCurrentUser().getUid()).update("bank",Bank);
                db.collection("users").document(mAuth.getCurrentUser().getUid()).update("account",Account);




            }

        }
    }
}