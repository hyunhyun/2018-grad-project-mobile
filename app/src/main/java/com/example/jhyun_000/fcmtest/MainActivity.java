package com.example.jhyun_000.fcmtest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    //    private static final String TAG = "MainActivity";
    private static final String TAG = "Token";
    String token;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }

            //
            String num1 = getIntent().getExtras().get("num1").toString();
            TextView main_nt_textview = (TextView) findViewById(R.id.main_nt_textview);
            main_nt_textview.setText(num1);
        }
        // [END handle_data_extras]

        Button subscribeButton = findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("news2");
                // [END subscribe_topics]

                // Log and toast
                String msg = getString(R.string.msg_subscribed);
                Log.d(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        Button logTokenButton = findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get token
                token = FirebaseInstanceId.getInstance().getToken();

                // Log and toast
                String msg = getString(R.string.msg_token_fmt, token);
                Log.d(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                fecMOTOriSA:APA91bH8tVJb055m0g_TbmkNEUI9_CVFQ-jLC8henZQxFJMwEmCzGLHsy1ErAAXUtowvRqzj1-MQIIcqnzEMz3U39ezkeOu5G_AaP660dIqc9ns9c10Bz7sRQMLje37jcxNhblj9eUke

//                FirebaseMessaging fm = FirebaseMessaging.getInstance();
//                fm.send(new RemoteMessage.Builder(msg + "@gcm.googleapis.com")
////        fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
////                .setMessageId(Integer.toString(msgId.incrementAndGet()))
//                        .setMessageId(Integer.toString(0))
//                        .addData("my_message", "Hello World")
//                        .addData("my_action","SAY_HELLO")
//                        .build());

                new Thread() {
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
//                     RequestBody body = new FormBody.Builder()
//                             .add("Token", FirebaseInstanceId.getInstance().getToken())
//                             .build();

                        RequestBody body = RequestBody.create(JSON, "{\"token\": \"" + token + "\"}");
                        Log.d(TAG, "Body : " + body);

                        Request request = new Request.Builder()
                                .url("https://pure-depths-50816.herokuapp.com/user/emergency")
                                .post(body)
                                .build();

                        try {
//                         Log.d(TAG, "before execute");
                            client.newCall(request).execute();
//                         Log.d(TAG, "after execute");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }


}
