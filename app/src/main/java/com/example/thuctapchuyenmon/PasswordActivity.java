package com.example.thuctapchuyenmon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.database.connect;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import es.dmoral.toasty.Toasty;

public class PasswordActivity extends AppCompatActivity {

    Button btnXacNhanPass;
    TextInputEditText edtNewPass,edtConfirm;
    String phoneNuber;
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
                if(checkValid())
                {
                    insertKH insertKH = new insertKH();
                    insertKH.execute();
                }
            }
        });
    }
    class insertKH extends AsyncTask<Void,Void,Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean)
            {
                Toasty.success(PasswordActivity.this, "Đăng kí thành công!", Toasty.LENGTH_SHORT).show();
                Intent intent = new Intent(PasswordActivity.this,LoginActivity.class);
                startActivity(intent);
            }
            else
            {
                Toasty.error(PasswordActivity.this, "Đăng kí thất bại!", Toasty.LENGTH_SHORT).show();
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
                connect connect = new connect("insertKhachHang");
                String param = "tenkh=''&sdt="+phoneNuber+"&diachi=''&email=''";
                NodeList nodeList = connect.getDataParameter(param,"boolean");
                String kiemtra = nodeList.item(0).getTextContent();
                boolean kt = Boolean.parseBoolean(kiemtra);
                if(kt)
                {
                    connect connectKH = new connect("thongtinkh");
                    String param1 = "sdt="+phoneNuber;
                    NodeList nodeList1 = connectKH.getDataParameter(param1,"KhachHang");
                    Element element = (Element) nodeList1.item(0);
                    String makh = element.getElementsByTagName("MaKH").item(0).getTextContent();
                    connect connectTK = new connect("insertTaiKhoan");
                    String param2 = "makh="+makh+"&sdt="+phoneNuber+"&matkhau="+edtNewPass.getText();
                    NodeList nodeList2 = connectTK.getDataParameter(param2,"boolean");
                    String kiemtra2 = nodeList2.item(0).getTextContent();
                    boolean kt2 = Boolean.parseBoolean(kiemtra2);
                    return kt2;
                }
            } catch (Exception e){
                return false;
            }
            return false;
        }
    }
    private boolean checkValid(){
        String newPassword = edtNewPass.getText().toString().trim();
        String newPasswordAgain = edtConfirm.getText().toString().trim();
        if (newPassword.length() == 0) {
            edtNewPass.setError("New Password Invalid");
            edtNewPass.requestFocus();
            return false;
        }
        else if(newPasswordAgain.length()==0)
        {
            edtConfirm.setError("New Password Again Invalid");
            edtConfirm.requestFocus();
            return false;
        }
        else if(!newPassword.equals(newPasswordAgain))
        {
            edtConfirm.setError("password incorrect");
            edtConfirm.requestFocus();
            return false;
        }
        return  true;
    }
    private void addViews() {
        btnXacNhanPass = findViewById(R.id.btnXacNhanPass);
        edtConfirm = findViewById(R.id.edtNewPassConfirm);
        edtNewPass = findViewById(R.id.edtNewPass);

        Intent intent = getIntent();
        phoneNuber = intent.getStringExtra("sdt");
        Log.e("phone", phoneNuber );
    }
}