package com.example.thuctapchuyenmon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class ThongTinActivity extends AppCompatActivity {
    Button btnLuu,btnSua;
    EditText edtHoten,edtDiachi,edtsdt,edtEmail;
    TextView txtTen;
    ImageView imgHinh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin);
        addViews();
        addEvents();
    }

    private void addEvents() {
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDiachi.setEnabled(true);
                edtEmail.setEnabled(true);
                edtHoten.setEnabled(true);
                edtsdt.setEnabled(true);
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void addViews() {
        edtHoten = findViewById(R.id.edtHoTen);
        txtTen = findViewById(R.id.txtTenKH);
        edtDiachi = findViewById(R.id.edtDiaChi);
        edtsdt = findViewById(R.id.edtSDT);
        edtEmail = findViewById(R.id.edtEmail);
        imgHinh = findViewById(R.id.imageHinhKH);
        btnLuu = findViewById(R.id.btnLuu);
        btnSua = findViewById(R.id.btnSua);

        Intent intent = getIntent();
        String hoten = intent.getStringExtra("hoten");
        String email = intent.getStringExtra("email");
        String hinhanh = intent.getStringExtra("hinhanh");
        txtTen.setText(hoten);
        edtHoten.setText(hoten);
        edtEmail.setText(email);

        if(hinhanh!=null)
        {
            Log.e("hình ảnh :",hinhanh);
            String image = hinhanh+"?type=large";
            Picasso.get().load(image).into(imgHinh);
        }
        else
        {
            Log.e("hình ảnh :","null");
        }
    }
}
