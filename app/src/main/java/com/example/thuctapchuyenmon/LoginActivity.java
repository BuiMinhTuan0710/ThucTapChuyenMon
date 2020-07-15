package com.example.thuctapchuyenmon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chabbal.slidingdotsplash.SlidingSplashView;
import com.example.database.connect;
import com.example.model.DialogLoading;
import com.google.android.material.textfield.TextInputEditText;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private TextInputEditText edtPhoneLogin,edtPasswordLogin;
    private GoogleProgressBar googleProgressBar;
    public static String makh="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addViews();
        addEvents();
    }

    private void addViews() {
        btnLogin = findViewById(R.id.btnLogin);
        edtPhoneLogin = findViewById(R.id.edtphoneLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        googleProgressBar = findViewById(R.id.google_progress);
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValid())
                {
                    KiemTraDangNhap kiemTraDangNhap = new KiemTraDangNhap();
                    kiemTraDangNhap.execute();
                }

            }
        });
    }
    private boolean checkValid(){
        String email = edtPhoneLogin.getText().toString().trim();
        String password = edtPasswordLogin.getText().toString().trim();
        if(email.length() == 0){
            edtPhoneLogin.setError("Phone number Invalid");
            edtPhoneLogin.requestFocus();
            return false;
        }else if (password.length() == 0) {
            edtPasswordLogin.setError("Password Invalid");
            edtPasswordLogin.requestFocus();
            return false;
        }
        return  true;
    }

    public void clickSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }


    class KiemTraDangNhap extends AsyncTask<Void,Void,String>
    {
        @Override
        protected void onPreExecute() {
            DialogLoading.LoadingGoogle(true,googleProgressBar);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String b) {
            if(!b.equals(""))
            {
                DialogLoading.LoadingGoogle(false,googleProgressBar);
                Toasty.success(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT, true).show();
                Intent intent = new Intent(LoginActivity.this,ChucNangActivity.class);
                Log.e("makh",b );
                intent.putExtra("makh",b);
                startActivity(intent);
                finish();
            }
            else
            {
                DialogLoading.LoadingGoogle(false,googleProgressBar);
                Toasty.error(LoginActivity.this, "Login fail!", Toast.LENGTH_SHORT, true).show();
            }
            super.onPostExecute(b);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String b) {
            super.onCancelled(b);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                connect connect = new connect("kiemtraDangNhap");
                String param = "tk="+edtPhoneLogin.getText()+"&mk="+edtPasswordLogin.getText();
                NodeList nodeList = connect.getDataParameter(param,"boolean");
                String kiemtra = nodeList.item(0).getTextContent();
                boolean kt = Boolean.parseBoolean(kiemtra);
                if(kt)
                {
                    connect connect1 = new connect("thongtinkh");
                    String param1 = "sdt="+edtPhoneLogin.getText();
                    NodeList nodeList1 = connect1.getDataParameter(param1,"KhachHang");
                    Element element = (Element) nodeList1.item(0);
                    String makh = element.getElementsByTagName("MaKH").item(0).getTextContent();
                    return makh;
                }
                else
                    return "";
            }
            catch (Exception e)
            {
                return "";
            }
        }
    }
}