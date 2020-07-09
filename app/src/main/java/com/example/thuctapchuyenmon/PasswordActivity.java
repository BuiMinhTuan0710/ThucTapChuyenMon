package com.example.thuctapchuyenmon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PasswordActivity extends AppCompatActivity {

    Button btnXacNhanPass;
    EditText edtNewPass,edtConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        addViews();
        addEvents();
    }

    private void addEvents() {
        btnXacNhanPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtConfirm.getText().toString().equals(edtNewPass.getText().toString()))
                {
                    Intent intent = new Intent(PasswordActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void addViews() {
        btnXacNhanPass = findViewById(R.id.btnXacNhanPass);
        edtConfirm = findViewById(R.id.edtNewPassConfirm);
        edtNewPass = findViewById(R.id.edtNewPass);
    }
}