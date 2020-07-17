package com.example.thuctapchuyenmon;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {
    Button btnSubmitRegister;
    EditText edtSDT;
    String phoneNumber;
    String code;
    TextView edtOTP;
    PhoneAuthProvider.ForceResendingToken token;
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

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==9||s.length()==10)
                {
                    btnSubmitRegister.setEnabled(true);
                    btnSubmitRegister.setBackground(getDrawable(R.drawable.custom_btn));
                    btnSubmitRegister.setTextColor(getResources().getColor(R.color.colorWhite));
                }
                else
                {
                    btnSubmitRegister.setEnabled(false);
                    btnSubmitRegister.setBackground(getDrawable(R.drawable.custom_verify));
                    btnSubmitRegister.setTextColor(getResources().getColor(R.color.colorVerify));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addViews() {
        btnSubmitRegister = findViewById(R.id.btnSubmitRegister);
        edtSDT = findViewById(R.id.edtSDT);
        btnSubmitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KiemTra kiemTra = new KiemTra();
                kiemTra.execute(phoneNumber);
            }
        });
    }
    private void clickRegister() {
        final Dialog dialog = new Dialog(RegisterActivity.this);
        dialog.setContentView(R.layout.alertdialog_register);
        final Button btnYes = dialog.findViewById(R.id.btnRegister);
        edtOTP = dialog.findViewById(R.id.edtOTPRegister);
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
        if(edtSDT.getText().length()==9)
        {
            phone = "+84"+edtSDT.getText().toString();
            phoneNumber = "0"+edtSDT.getText().toString();
        }
        else
        {
            String sdt = edtSDT.getText().toString().substring(1,10);
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
        PhoneAuthCredential authCredential =PhoneAuthProvider.getCredential(code,edtOTP.getText().toString());
        FirebaseAuth.getInstance().signInWithCredential(authCredential).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toasty.success(RegisterActivity.this,"Verified Successful !",Toasty.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this,PasswordActivity.class);
                intent.putExtra("sdt",phoneNumber);
                Log.e("phonenumber",phoneNumber );
                startActivityForResult(intent,123);
            }
        }).addOnFailureListener(RegisterActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(RegisterActivity.this,"Verified Failed !",Toasty.LENGTH_LONG).show();
            }
        });
    }

    class KiemTra extends AsyncTask<String,Void,Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean)
            {
                clickRegister();
            }
            else
            {
                Toasty.error(RegisterActivity.this, "SDT đã tồn tại", Toasty.LENGTH_SHORT).show();
            }
            super.onPostExecute(aBoolean);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                URL url = new URL("http://www.minhtuan.somee.com/myService.asmx/kiemtrasdt");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                String params = "sdt="+strings[0];
                OutputStream os = connection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os,"UTF-8");
                osw.write(params);
                osw.flush();
                osw.close();

                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(connection.getInputStream());
                NodeList nodeList = document.getElementsByTagName("boolean");
                String kq = nodeList.item(0).getTextContent();
                boolean kt = Boolean.parseBoolean(kq);
                return kt;
            } catch (Exception e) {
                Log.e( "loi: ", e.toString());
            }
            return false;
        }
    }
}
