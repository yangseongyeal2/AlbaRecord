package com.AlbaRecord.Board;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.AlbaRecord.Adapter.CommonAdapter;
import com.AlbaRecord.Interface.OnItemClick;
import com.AlbaRecord.Model.BoardInfo;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnItemClick {
    private List<BoardInfo> mBoardList;
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private TextInputEditText mTextInputEditText;
    private TextInputLayout mTextInputLayout;
    private ProgressDialog loadingbar;
    private CommonAdapter mBoardAdapter;
    // private PhotoboardAdapter mPhotoAdapter;
    private RecyclerView mRecyclerView;
    private String mBoardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        loadingbar = new ProgressDialog(this);
        mRecyclerView = findViewById(R.id.serch_RecyclerView);
        mBoardName = getIntent().getStringExtra("BoardName");
        mTextInputEditText = findViewById(R.id.serch_TextInputEditText);
        mTextInputLayout = findViewById(R.id.search_TextInputLayout);
        mTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mTextInputEditText.getText().toString();
                retriveSearch(content, mBoardName);
                mTextInputEditText.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(mTextInputEditText.getWindowToken(), 0);
            }
        });
    }

    private void retriveSearch(String content, final String mBoardName) {
        FirebaseFirestore mStore = FirebaseFirestore.getInstance();
        loadingbar.setTitle("Serching");
        loadingbar.setMessage("Serching");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        mBoardList = new ArrayList<>();
        mStore.collection("Board")
                .whereEqualTo("title", content)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.getResult() != null) {
                            for (QueryDocumentSnapshot data : task.getResult()) {
                                BoardInfo boardInfo = data.toObject(BoardInfo.class);
                                Log.d("서치", boardInfo.getContent());
                                mBoardList.add(boardInfo);
                                Log.d("서치", String.valueOf(mBoardList.size()));
                            }
                            if (mBoardList.size() == 0) {//찾는 데이터가 없을떄
                                Toast.makeText(getApplicationContext(), "데이터가없습니다", Toast.LENGTH_SHORT).show();
                            }

                            mBoardAdapter = new CommonAdapter(mBoardList, SearchActivity.this, firebaseUser, SearchActivity.this);
                            mBoardAdapter.setOnIemlClickListner(new CommonAdapter.OnItemClickListener() {//Detail 액티비티로 이동
                                @Override
                                public void onitemClick(View v, int pos) {
                                    Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                                    intent.putExtra("DocumentId", mBoardList.get(pos).getDocumentId());
                                   // intent.putExtra("BoardName", mBoardName);

                                    startActivity(intent);
                                }
                            });
                            mRecyclerView.setAdapter(mBoardAdapter);


                        } else {
                            Log.d("서치", "데이터가 없다");
                        }

                        loadingbar.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("서치", "불러오기 실패");
            }
        });
    }

    @Override
    public void onClick(String value) {

    }
}
