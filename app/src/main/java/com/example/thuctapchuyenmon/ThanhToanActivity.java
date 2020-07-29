package com.example.thuctapchuyenmon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ThanhToanActivity extends AppCompatActivity {

    ImageView imgQR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        imgQR = findViewById(R.id.imgQR);
        Intent intent = getIntent();
        String mahd = intent.getStringExtra("mahd");
        String url = "https://api.qrserver.com/v1/create-qr-code/?data="+mahd+"&size=220x220&margin=0";
        Picasso.get().load(url).into(imgQR);
    }
}