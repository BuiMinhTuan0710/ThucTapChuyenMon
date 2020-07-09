package com.example.thuctapchuyenmon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.model.SanPham;
import com.squareup.picasso.Picasso;

public class ChonSanPhamActivity extends AppCompatActivity {

    ImageView imgChonSP;
    TextView txtSL;
    int soluong;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_san_pham);
        addViews();
        showData();
    }

    private void showData() {
        intent = getIntent();
        SanPham sp = (SanPham) intent.getSerializableExtra("SanPham");
        soluong = intent.getIntExtra("soluong",0);
        txtSL.setText(soluong+"");
        String image = sp.getHinhsp()+"?type=large";
        Picasso.get().load(image).into(imgChonSP);
    }

    private void addViews() {
        imgChonSP = findViewById(R.id.imgImageChonSP);
        txtSL = findViewById(R.id.txtSL);
    }

    public void clickDatMon(View view) {
        intent.putExtra("soluong",soluong);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    public void clickTang(View view) {
        soluong+=1;
        txtSL.setText(soluong+"");
    }

    public void clickGiam(View view) {
        soluong-=1;
        txtSL.setText(soluong+"");
    }
}
