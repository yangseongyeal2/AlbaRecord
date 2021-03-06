package com.AlbaRecord.Notification;

import com.AlbaRecord.Accept.AcceptActivity;
import com.AlbaRecord.Accept.FireAcceptActivity;
import com.AlbaRecord.Boss.BossMainActivity;
import com.AlbaRecord.Model.NotiInfo;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.util.Date;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    static final String TAG = "파이어베이스 메세지 샘플";
    public String title;
    public String body;
    public String documentid;
    public String flag;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //    Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {//data형식
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            title = remoteMessage.getData().get("title");
            body = remoteMessage.getData().get("body");
            documentid = remoteMessage.getData().get("DocumentId");
            flag = remoteMessage.getData().get("flag");
            Date date = new Date();
            NotiInfo notiInfo = new NotiInfo(title, body, documentid, date, flag);
            mStore.collection("users")
                    .document(firebaseUser.getUid())
                    .collection("notification")
                    .document()
                    .set(notiInfo)
            ;


        }
        // 노티피케이션을 사용했을떄 데이터 가져오기
        if (remoteMessage.getNotification() != null) {//notification 형식
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();

        }
        //


        sendNotification();//자기폰에 올리는 작업
    }

    @Override
    public void onNewToken(@NonNull String s) {
        //Log.d(TAG, "Refreshed token: " + s);
        //서버에 토큰을 보내줘야함 만약 토큰이 새로 생겼다면(다시 다운로드 할때)
    }


    private void sendNotification() {
        if (flag.equals("3")) {
            mStore.collection("users")
                    .document(firebaseUser.getUid())
                    .update("myBoss", FieldValue.arrayRemove(documentid))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("해고메세지","마이보스에서 삭제완료");
                        }
                    });

           // Toast.makeText(getApplicationContext(), "해고메세지도착", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, FireAcceptActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("title", "Fire Message");
            intent.putExtra("body", "사장님이 해고 하셨습니다");
            intent.putExtra("DocumentId", documentid);
            intent.putExtra("flag", flag);


            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(FireAcceptActivity.class);
            stackBuilder.addNextIntent(intent);



            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);//이게 뒤로 눌렀을때 안꺼지는 방법
            String channelId = "채널 ID";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.main)
                            .setContentTitle("Fire Message")
                            .setContentText("사장님이 해고 하셨습니다")
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(channel);
            }
            assert notificationManager != null;
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());





        }else if(flag.equals("1")){
            mStore.collection("users").document(firebaseUser.getUid()).update("MyEmployee", FieldValue.arrayUnion(documentid));
            Intent intent = new Intent(this, BossMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("title", title);
            intent.putExtra("body", body);
            intent.putExtra("DocumentId", documentid);
            intent.putExtra("flag", flag);


            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(BossMainActivity.class);
            stackBuilder.addNextIntent(intent);
            Log.d("DocumentId", documentid);


            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);//이게 뒤로 눌렀을때 안꺼지는 방법
            String channelId = "채널 ID";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.main)
                            .setContentTitle(title)
                            .setContentText(body)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(channel);
            }
            assert notificationManager != null;
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        }

        else {

            Intent intent = new Intent(this, AcceptActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("title", title);
            intent.putExtra("body", body);
            intent.putExtra("DocumentId", documentid);
            intent.putExtra("flag", flag);


            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(AcceptActivity.class);
            stackBuilder.addNextIntent(intent);
            Log.d("DocumentId", documentid);


            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);//이게 뒤로 눌렀을때 안꺼지는 방법
            String channelId = "채널 ID";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.main)
                            .setContentTitle(title)
                            .setContentText(body)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(channel);
            }
            assert notificationManager != null;
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }


}
