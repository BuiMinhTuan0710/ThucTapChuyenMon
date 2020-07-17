package com.example.thuctapchuyenmon;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.database.connect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;


public class ForgotPassMainActivity extends AppCompatActivity {

    Button btnTiepTucForgot;
    EditText edtSDTForgot;
    String phoneNumber;
    String code;
    TextView edtOTP;
    PhoneAuthProvider.ForceResendingToken token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_main);
        btnTiepTucForgot = findViewById(R.id.btnSubmitForgot);
        edtSDTForgot = findViewById(R.id.edtSDTForgot);
        addEvents();

    }

    private class KiemTraSDT extends AsyncTask<String,Void,Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if(s)
            {
                clickForgot();
            }
            else
            {
                Toasty.error(ForgotPassMainActivity.this,"Phone number does not exit");
            }
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Boolean s) {
            super.onCancelled(s);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                connect connect = new connect("thongtinkh");
                String param = "sdt="+strings[0];
                NodeList nodeList = connect.getDataParameter(param,"KhachHang");
                if(nodeList.getLength()>0)
                    return true;
                return false;
            } catch (Exception e){
                return false;
            }
        }
    }
    private void addEvents() {
        btnTiepTucForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone;
                if(edtSDTForgot.getText().length()==9)
                {
                    phone = "0"+edtSDTForgot.getText().toString();
                }
                else
                {
                    phone = edtSDTForgot.getText().toString();
                }
                KiemTraSDT kiemTraSDT = new KiemTraSDT();
                kiemTraSDT.execute(phone);
            }
        });
        edtSDTForgot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==9||s.length()==10)
                {
                    btnTiepTucForgot.setEnabled(true);
                    btnTiepTucForgot.setBackground(getDrawable(R.drawable.custom_btn));
                    btnTiepTucForgot.setTextColor(getResources().getColor(R.color.colorWhite));
                }
                else
                {
                    btnTiepTucForgot.setEnabled(false);
                    btnTiepTucForgot.setBackground(getDrawable(R.drawable.custom_verify));
                    btnTiepTucForgot.setTextColor(getResources().getColor(R.color.colorVerify));
                }
            }
        });
    }

    private void clickForgot() {
        final Dialog dialog = new Dialog(ForgotPassMainActivity.this);
        dialog.setContentView(R.layout.alertdialog_register);
        final Button btnYes = dialog.findViewById(R.id.btnRegister);
        TextView edtOTP = dialog.findViewById(R.id.edtOTPRegister);
        edtOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==6)
                {
                    btnYes.setEnabled(true);
                    btnYes.setBackground(getDrawable(R.drawable.custom_btn));
                    btnYes.setTextColor(getResources().getColor(R.color.colorWhite));
                }
                else
                {
                    btnYes.setEnabled(false);
                    btnYes.setBackground(getDrawable(R.drawable.custom_verify));
                    btnYes.setTextColor(getResources().getColor(R.color.colorVerify));
                }
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KiemTraCode();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        String phone;
        if(edtSDTForgot.getText().length()==9)
        {
            phone = "+84"+edtSDTForgot.getText().toString();
            phoneNumber = "0"+edtSDTForgot.getText().toString();
        }
        else
        {
            String sdt = edtSDTForgot.getText().toString().substring(1,10);
            phone = "+84"+sdt;
            phoneNumber = "0"+sdt;
        }
        sendVerificationCode(phone);
    }
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                callbacks);
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            code = s;
            token = forceResendingToken;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.e("lỗi ", e.getMessage() );
        }
    };
    private void KiemTraCode() {
        PhoneAuthCredential authCredential = PhoneAuthProvider.getCredential(code,edtOTP.getText().toString());
        FirebaseAuth.getInstance().signInWithCredential(authCredential).addOnCompleteListener(ForgotPassMainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toasty.success(ForgotPassMainActivity.this,"Verified Successful !",Toasty.LENGTH_LONG).show();
                Intent intent = new Intent(ForgotPassMainActivity.this,PasswordActivity.class);
                intent.putExtra("sdt",phoneNumber);
                Log.e("phonenumber",phoneNumber );
                startActivityForResult(intent,123);
            }
        }).addOnFailureListener(ForgotPassMainActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(ForgotPassMainActivity.this,"Verified Failed !",Toasty.LENGTH_LONG).show();
            }
        });
    }

}