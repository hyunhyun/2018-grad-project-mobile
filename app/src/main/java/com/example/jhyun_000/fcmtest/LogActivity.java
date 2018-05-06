package com.example.jhyun_000.fcmtest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Response;

import static com.example.jhyun_000.fcmtest.EmailPasswordActivity.user_email;

/**
 * Created by jhyun_000 on 2018-04-12.
 */


public class LogActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    GridView log_gridView;

    Response response;
    String resp_string;
    Callback callback;
    boolean response_get = false;

    int length_jarray;
    String uuid[];
    String timestamp[];
    String urls[];
    int image_location[];

    HttpThread okhttpThread;

    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        log_gridView = (GridView) findViewById(R.id.log_gridView);

        callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                resp_string = response.body().string();
                Log.i("resp_string", resp_string);
//                response_get = true;
                try {
                    parseResponse(resp_string);
                    Log.i("afterParse", uuid[0]);

//                    Message message = null;
//                    message.what= 1;
//                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

//        try {
//            sendLogHttp(user_email);
////            this.onStart();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        try {
//            okhttpThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        Log.d("beforeRequest", uuid[0]);
//        requestS3Log(uuid, timestamp, image_location);

        MyAsyncTask task = new MyAsyncTask();
        task.execute("", "", "");

    }

    @Override
    protected void onStart() {
        super.onStart();

//        while(handler.hasMessages(1)){
//            Log.i("onStartUuid", uuid[0]);
//
//            handler.removeMessages(1);
//            requestS3Log(uuid, timestamp, image_location);
//        }

//        synchronized (callback){
//            try{
//                callback.wait();
//                Log.i("onStartUuid", uuid[0]);
//
//            requestS3Log(uuid, timestamp, image_location);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    void sendLogHttp(final String email) throws IOException {

//         new Thread() {
//            public void run() {
//                OkHttpClient client = new OkHttpClient();
////                     RequestBody body = new FormBody.Builder()
////                             .add("Token", FirebaseInstanceId.getInstance().getToken())
////                             .build();
//
//                RequestBody body = RequestBody.create(JSON, "{\"email\": \"" + email + "\", \"duration\": "+0+"}");
//                Log.d("sendLogHttp", "Body : " + body);
//
//                Request request = new Request.Builder()
//                        .url("https://grad-project-app.herokuapp.com/user/logs")
//                        .post(body)
//                        .build();
//
//
////                    response = client.newCall(request).execute();
//                    client.newCall(request).enqueue(callback);
////                    returnresponse[0] = client.newCall(request).execute();
//
////                    responsestring[0] = new String(response.body().string());
////                    resp_string = new String(response.body().string());
////                    Log.i("ReturnResponse[0]", returnresponse[0].body().string());
////                    Log.i("insideThread-response", response.body().string());
////                    Log.i("Thread_respstring", responsestring[0]);
////                    parseResponse(response);
//
//            }

        okhttpThread = new HttpThread(callback, user_email);
        okhttpThread.start();
//        Log.i("Response[0]-2", response[0].body().string());
//        return response[0];

//        Log.i("beforereturn_respstring", responsestring[0]);
//        Log.i("beforereturn_respstring", resp_string);
//        return responsestring[0];
    }

    //    void parseResponse(Response response) throws JSONException, IOException {
    void parseResponse(String responsestring) throws JSONException, IOException {
        //Json parsing
//        StringBuffer sb = new StringBuffer();

//        JSONArray jarray = new JSONArray();

        Log.i("parseResponse", "parseResponse started");
        Log.i("parseResponse-responsesstring", responsestring);
//        if (response == null) return;

//        response.body().string()을 한번만 호출할수 있음
//        Log.i("Response-string", response.body().string());
//        Log.i("Response-tostring", response.body().toString());
//        String jsonData = response.body().string();
        String jsonData = responsestring;


        Log.i("jsonData", jsonData);
        JSONObject Jobject = new JSONObject(jsonData);
//        JSONObject Jobject = new JSONObject(response.body().string());


//        JsonObject Jobject = new JsonParser().parse(jsonData).getAsJsonObject();

//        JSONArray Jarray = Jobject.getAsJsonArray("result");
        JSONArray Jarray = Jobject.getJSONArray("result");
//        JSONArray Jarray = Jobject.getJSONArray( )

        Log.i("Jarraylength", String.valueOf(Jarray.length()));
        length_jarray = Jarray.length();

        uuid = new String[length_jarray];
        timestamp = new String[length_jarray];
        image_location = new int[length_jarray];


        String urls[] = new String[Jarray.length()];

        for (int i = 0; i < length_jarray; i++) {
            JSONObject object = Jarray.getJSONObject(i);

            uuid[i] = object.getString("key");
//            timestamp[i] = object.getString("timestamp");
            timestamp[i] = String.valueOf(object.getLong("timestamp"));
//            if(object.getString("result").equals("unknown")){
//                image_location[i] = 1;
//            }
//            else{
//                image_location[i] = 0;
//            }
            Log.i("uuid", uuid[i]);
            Log.i("timestamp", timestamp[i]);
//            Log.i("image_location", String.valueOf(image_location[i]));

            urls[i] = "https://s3.amazonaws.com/androidprojectapp-userfiles-mobilehub-1711223959/" + uuid[i] + ".jpg";
            Log.i("urls", urls[i]);
        }

//        requestS3Log(uuid, timestamp);


    }


    public void requestS3Log(String[] imageUuids, String[] timestamp, int[] image_location) {
        Log.i("requestS3Log", "requestS3Log started");

        log_gridView = (GridView) findViewById(R.id.log_gridView);
//        gridView.setAdapter(new ImageAdapter(this, urls));

        Log.i("requestLog_imageUuid", imageUuids[0]);
        Log.i("requestLog_timestamp", timestamp[0]);
        Log.i("requestLog_location", String.valueOf(image_location[0]));

        log_gridView.setAdapter(new ImageAdapter(this, imageUuids, timestamp, image_location));
//        @NonNull Context context, @NonNull String[] imageUuids, @Nullable String[] timestamp, @NonNull int[] uuid_location
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            okhttpThread = new HttpThread(callback, user_email);
            okhttpThread.start();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("onStartUuid", uuid[0]);

//            handler.removeMessages(1);
            requestS3Log(uuid, timestamp, image_location);
        }

    }
}