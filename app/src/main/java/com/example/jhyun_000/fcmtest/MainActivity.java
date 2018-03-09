package com.example.jhyun_000.fcmtest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//import com.google.firebase.iid.FirebaseInstanceId;

//import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Token";
    String token;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    int REQUEST_FINE = 1;
    int REQUEST_COARSE = 2;
    int REQUEST_INTERNET = 3;

    Button button_gps;
    Button button_timer_start;
    Button button_timer_end;
    Button button_emergecy;
    TextView textView;

    double longitude;
    double latitude;

    boolean isEmergency = false;
    MyDBHandler myDBHandler;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        findviews();

        myDBHandler = new MyDBHandler(this, null, null, 1);
        db = myDBHandler.getWritableDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    void init() {
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
    }

    void findviews() {
        button_gps = (Button) findViewById(R.id.button_gps);
        button_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GPS_Service.class);
                startService(i);
            }
        });

        Button logTokenButton = findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTokenHttp();
            }
        });

        button_timer_start = (Button) findViewById(R.id.button_timer_start);
        button_timer_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Timer.class);
                startService(intent);
            }
        });

        button_timer_end = (Button) findViewById(R.id.button_timer_end);
        button_timer_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Timer.class);
                stopService(intent);

                MyDBHandler myDBHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
            }
        });

        button_emergecy = (Button) findViewById(R.id.button_emergency);
        button_emergecy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GPS_Service.class);
                startService(intent);

                isEmergency = true;
//                sendEmergency("ddd@gmail.com");
            }
        });

        textView = (TextView) findViewById(R.id.textView);

    }

    void sendTokenHttp() {
        // Get token
        token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        String msg = getString(R.string.msg_token_fmt, token);
        Log.d(TAG, msg);
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

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
                    client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    void sendEmergency(final String email) {
        Toast.makeText(MainActivity.this, "Emerg longitude : " + String.valueOf(longitude) + "latitude : " + String.valueOf(latitude), Toast.LENGTH_SHORT).show();

        new Thread() {
            public void run() {
                OkHttpClient client = new OkHttpClient();

                RequestBody body = RequestBody.create(JSON, "{\"email\": \"" + email + "\"," +
                        "\"locations\":[ " +
                        "{\"longitude\": \": \"" + longitude + "\", \"latitude\": \"" + latitude + "\"}]}");
                Log.d(TAG, "Emergency Body : " + body);

                Request request = new Request.Builder()
                        .url("https://pure-depths-50816.herokuapp.com/user/emergency")
                        .post(body)
                        .build();

                try {
                    client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

//        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Cursor cursor = myDBHandler.findAll();
        String Jsonarray = "";
        if (cursor.moveToFirst()) {
            String longitude = cursor.getString(1);
            String latitude = cursor.getString(2);

            Jsonarray += "{\"locations\":[ " + "{\"longitude\": \": \"" + longitude + "\", \"latitude\": \"" + latitude + "\"}}";
        }

        while (cursor.moveToNext()) {
//            cursor.getString(0);
            String longitude = cursor.getString(1);
            String latitude = cursor.getString(2);

            Jsonarray += ", {\"longitude\": \": \"" + longitude + "\", \"latitude\": \"" + latitude + "\"}}";
        }
        Jsonarray += "]}";


        //이걸 server로 보내야함
        final String finalJsonarray = Jsonarray;
        new Thread() {
            public void run() {
                OkHttpClient client = new OkHttpClient();

                RequestBody body = RequestBody.create(JSON, finalJsonarray);
                Log.d(TAG, "Emergency Body : " + body);

                Request request = new Request.Builder()
                        .url("https://pure-depths-50816.herokuapp.com/user/emergency")
                        .post(body)
                        .build();

                try {
                    client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Log.d("Response", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();


        Log.d("Jsonarray", Jsonarray);
    }

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onResume() {
        PermissionCheck();

        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    textView.append("\n" + intent.getExtras().get("longitude") + " " + intent.getExtras().get("latitude"));
                    Log.d("broadcast: longitude", String.valueOf(intent.getExtras().get("longitude")));
                    Log.d("broadcast: latitude", String.valueOf(intent.getExtras().get("latitude")));

                    longitude = (double) intent.getExtras().get("longitude");
                    latitude = (double) intent.getExtras().get("latitude");

                    Toast.makeText(MainActivity.this, "BC longitude : " + String.valueOf(longitude) + "latitude : " + String.valueOf(latitude), Toast.LENGTH_SHORT).show();

                    if (isEmergency) {
                        isEmergency = false;
                        sendEmergency("sss@gmail.com");
                        Intent i = new Intent(MainActivity.this, GPS_Service.class);
                        stopService(i);
                    }
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    private void PermissionCheck() {
        int permission_fine_location = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permission_coarse_location = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int permission_internet = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET);
        if (permission_fine_location == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE);
        }

        if (permission_coarse_location == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE);
        }

        if (permission_internet == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_INTERNET);
        }
    }
}
