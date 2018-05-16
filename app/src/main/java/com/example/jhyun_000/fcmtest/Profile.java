package com.example.jhyun_000.fcmtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by jhyun_000 on 2018-05-10.
 */

public class Profile extends AppCompatActivity {
    EditText edit_phone;
    Button button_profile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }

    void init() {
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        button_profile = (Button) findViewById(R.id.button_profile);

        button_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edit_phone.getText().toString();
            }
        });
    }

}
