package com.example.thuctapchuyenmon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.database.connect;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.NodeList;

import es.dmoral.toasty.Toasty;

public class DoiMatKhauActivity extends AppCompatActivity {

    TextInputEditText edtOldPass,edtNewPass,edtNewPassAgain;
    TextView txtCloseDialogChangePassword;
    Button btnChangePass;
    public String makh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);
        addViews();
        addEvents();
    }

    private void addEvents() {
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValid())
                {
                    KiemTraMatKhau kiemTraMatKhau = new KiemTraMatKhau();
                    kiemTraMatKhau.execute();
                }
            }
        });
        txtCloseDialogChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class ChangePass extends AsyncTask<Void,Void,Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean)
            {
                Toasty.success(DoiMatKhauActivity.this,"Change Password success!").show();
                finish();
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
        protected Boolean doInBackground(Void... voids) {
            try {
                connect connect = new connect("ChangePassword");
                String param = "makh="+makh+"&matkhau="+edtNewPass.getText().toString();
                NodeList nodeList = connect.getDataParameter(param,"boolean");
                String kiemtra = nodeList.item(0).getTextContent();

                boolean kt = Boolean.parseBoolean(kiemtra);
                return kt;
            }
            catch (Exception e)
            {
                return  false;
            }
        }
    }
    class KiemTraMatKhau extends AsyncTask<Void,Void,Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(!aBoolean)
            {
                edtOldPass.setError("Old Password incorrect");
                edtOldPass.requestFocus();
            }
            else
            {
                ChangePass changePass = new ChangePass();
                changePass.execute();
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
        protected Boolean doInBackground(Void... voids) {
            try {
                connect connect = new connect("kiemtramatkhau");
                String param = "makh="+makh+"&matkhau="+edtOldPass.getText().toString();
                NodeList nodeList = connect.getDataParameter(param,"boolean");
                String kiemtra = nodeList.item(0).getTextContent();

                boolean kt = Boolean.parseBoolean(kiemtra);
                return kt;
            }
            catch (Exception e)
            {
                return  false;
            }
        }
    }
    private boolean checkValid(){
        String oldPass = edtOldPass.getText().toString().trim();
        String newPassword = edtNewPass.getText().toString().trim();
        String newPasswordAgain = edtNewPassAgain.getText().toString().trim();
        if(oldPass.length() == 0){
            edtOldPass.setError("Old Password Invalid");
            edtOldPass.requestFocus();
            return false;
        }else if (newPassword.length() == 0) {
            edtNewPass.setError("New Password Invalid");
            edtNewPass.requestFocus();
            return false;
        }
        else if(newPasswordAgain.length()==0)
        {
            edtNewPassAgain.setError("New Password Again Invalid");
            edtNewPassAgain.requestFocus();
            return false;
        }
        else if(!newPassword.equals(newPasswordAgain))
        {
            edtNewPassAgain.setError("password incorrect");
            edtNewPassAgain.requestFocus();
            return false;
        }
        return  true;
    }

    private void addViews() {
        Intent intent = getIntent();
        makh = intent.getStringExtra("makh");
        edtOldPass = findViewById(R.id.edtOldPassword);
        edtNewPass = findViewById(R.id.edtNewPassword);
        edtNewPassAgain = findViewById(R.id.edtNewPasswordAgain);
        btnChangePass = findViewById(R.id.btnChangePassword);
        txtCloseDialogChangePassword = findViewById(R.id.txtCloseDialogChangePassword);
    }
}