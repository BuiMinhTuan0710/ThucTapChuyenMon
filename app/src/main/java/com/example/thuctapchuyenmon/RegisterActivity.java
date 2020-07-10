package com.example.thuctapchuyenmon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.database.connect;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
                KiemTra kiemTra = new KiemTra();
                kiemTra.execute(phoneNumber);
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
                chuyenMaOTP();
            }
            else
            {
                Toast.makeText(RegisterActivity.this, "SDT đã tồn tại", Toast.LENGTH_SHORT).show();
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
    private void chuyenMaOTP() {
        Intent intent = new Intent(RegisterActivity.this,OtpActivity.class);
        intent.putExtra("sdt",phoneNumber);
        startActivity(intent);
    }
}
