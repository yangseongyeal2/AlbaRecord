package com.AlbaRecord.Board;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.AlbaRecord.Adapter.CommonAdapter;
import com.AlbaRecord.Interface.OnItemClick;
import com.AlbaRecord.Model.BoardInfo;
import com.AlbaRecord.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity implements OnItemClick {
    private FloatingActionButton mWiteBitton;
    private ProgressDialog loadingbar;
    private List<BoardInfo> mBoardList;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private CommonAdapter mBoardAdapter;
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private RecyclerView mRecyclerView;
    private String mBoardName;
    private ImageView mSerch;
    private TextView mBoardTitleName;
    private SwipeRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        mBoardName = getIntent().getStringExtra("BoardName");
        loadingbar = new ProgressDialog(BoardActivity.this);//로딩바
        mRecyclerView = findViewById(R.id.normal_recyclerview);//리사이클러뷰 선언
        //Toast.makeText(getApplicationContext(),mBoardName,Toast.LENGTH_SHORT).show();
        mWiteBitton = findViewById(R.id.normal_write_Button);
        mWiteBitton.setOnClickListener(new View.OnClickListener() {//글쓰기
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                intent.putExtra("BoardName", mBoardName);
                startActivityForResult(intent, 99);
            }
        });
        mSerch = findViewById(R.id.normal_serch);
        mSerch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("BoardName", mBoardName);
                startActivity(intent);
            }
        });
        mBoardTitleName=findViewById(R.id.normal_title_constraint1);
        mBoardTitleName.setText(mBoardName);
        refresh=findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                startActivity(new Intent(getApplicationContext(),BoardActivity.class));
            }
        });


        retreive_Testing(mBoardName);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 99 && requestCode == 99) {//write 액티비티실행후 나온 결과 받아오기.
            Log.d("양성열", "리절트 함수 실행");
            retreive_Testing(mBoardName);//업로드 화면 끝났을때
        }
//        else if(resultCode == RESULT_CANCELED) {
//            Log.d("양성열","실패");
//        }
    }
    //
    public void retreive_Testing(String boardname) {
        loadingbar.setTitle("Set profile image");
        loadingbar.setMessage("pleas wait업로딩중");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        mBoardList = new ArrayList<>();
        mStore.collection("Board")
                .orderBy("date", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //        for (QueryDocumentSnapshot document : task.getResult()) {

                        BoardInfo boardInfo = document.toObject(BoardInfo.class);
                        if (boardInfo.getDeleted_at().equals("0")) {
                            // Log.d("yangseongyeal",boardInfo.getContent());
                            mBoardList.add(boardInfo);
                        } else {
                            Log.d("양성열", "삭제됬었음");
                        }
                    }
                    mBoardAdapter = new CommonAdapter(mBoardList, BoardActivity.this, firebaseUser, BoardActivity.this, mBoardName);
                    mBoardAdapter.setOnIemlClickListner(new CommonAdapter.OnItemClickListener() {//Detail 액티비티로 이동
                        @Override
                        public void onitemClick(View v, int pos) {
                            Intent intent = new Intent(BoardActivity.this, DetailActivity.class);
                            intent.putExtra("DocumentId", mBoardList.get(pos).getDocumentId());
                            intent.putExtra("BoardName", mBoardName);
                            startActivity(intent);
                        }
                    });
                    mRecyclerView.setAdapter(mBoardAdapter);
                    loadingbar.dismiss();
                } else {
                    Log.d("양성열", "Error getting documents: ", task.getException());
                    loadingbar.dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(String value) {
        if (value.equals("실시간 데이터 삭제")) {
            retreive_Testing(mBoardName);
        }
    }
}