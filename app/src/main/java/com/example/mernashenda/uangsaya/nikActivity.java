package com.example.mernashenda.uangsaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

public class nikActivity extends AppCompatActivity {
    private AppCompatButton buttonnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nik);

        buttonnext = (AppCompatButton) findViewById(R.id.btnnext);

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(nikActivity.this,KodeUnikActivity.class));
            }
        });
    }
}
