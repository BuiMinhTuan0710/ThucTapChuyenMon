package com.example.thuctapchuyenmon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    Button btnTiepTuc;
    EditText edtSo1;
    Intent intent;
    String phoneNumber,code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        addViews();
        addEvents();
    }

    private void addEvents() {
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtSo1.getText().toString().equals("123456"))
                {
                    Intent intent = new Intent(OtpActivity.this,PasswordActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(OtpActivity.this, "Mã xác nhận không chính xác", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addViews() {
        btnTiepTuc = findViewById(R.id.btnTiepTucDK);
        edtSo1 = findViewById(R.id.editSo1);
        intent = getIntent();
        phoneNumber = intent.getStringExtra("sdt");
        //sendVerificationCode(phoneNumber);
    }
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                callbacks);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            code = phoneAuthCredential.getSmsCode();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.e("lỗi ", e.getMessage() );
        }
    };
}
