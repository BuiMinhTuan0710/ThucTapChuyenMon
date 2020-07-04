package com.example.thuctapchuyenmon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    Button btnTiepTuc;
    EditText edtSDT;
    String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        addViews();
        addEvents();
    }

    private void addEvents() {
        edtSDT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==9)
                {
                    phoneNumber = s.toString();
                    btnTiepTuc.setEnabled(true);
                }
                else if(s.length()==10)
                {
                    phoneNumber = s.toString();
                    btnTiepTuc.setEnabled(true);
                }
                else {
                    btnTiepTuc.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addViews() {
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        edtSDT = findViewById(R.id.edtSDT);
        btnTiepTuc.setEnabled(false);
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chuyenMaOTP();
            }
        });
    }

    private void chuyenMaOTP() {
        Intent intent = new Intent(RegisterActivity.this,OtpActivity.class);
        intent.putExtra("sdt",phoneNumber);
        startActivity(intent);
    }
}
