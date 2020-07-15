package com.example.thuctapchuyenmon;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.database.connect;
import com.example.model.ChiTietSanPham;
import com.example.model.DialogLoading;
import com.example.model.SanPham;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ChonSanPhamActivity extends AppCompatActivity {

    ImageView imgChonSP;
    TextView txtSL,txtSize,txtDa,txtNgot;
    int soluong;
    Intent intent;
    LinearLayout lnSize,lnDa,lnDoNgot;
    Button btnOrder;
    CheckBox cbSizeM,cbSizeL;
    SanPham sanPhamSelected;
    public String makh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_san_pham);
        addViews();
        showData();
    }

    private void showData() {
        intent = getIntent();
        String masp = intent.getStringExtra("masp");
        makh = intent.getStringExtra("makh");
        ThongTinSanPham thongTinSanPham = new ThongTinSanPham();
        thongTinSanPham.execute(masp);
        SoLuongSize soLuongSize = new SoLuongSize();
        soLuongSize.execute(masp);
    }
    private void addViews() {
        imgChonSP = findViewById(R.id.imgImageChonSP);
        txtSL = findViewById(R.id.txtSL);
        lnDa = findViewById(R.id.lnDa);
        lnSize = findViewById(R.id.lnSize);
        lnDoNgot = findViewById(R.id.lnDoNgot);
        txtDa = findViewById(R.id.txtDa);
        txtSize = findViewById(R.id.txtSize);
        txtNgot = findViewById(R.id.txtNgot);
        btnOrder = findViewById(R.id.btnDatMon);
        cbSizeM = findViewById(R.id.cbSizeM);
        cbSizeL = findViewById(R.id.cbSizeL);
    }

    class SoLuongSize extends AsyncTask<String,Void,Integer>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
        if(integer<=1)
        {
            lnSize.setVisibility(View.INVISIBLE);
            lnDoNgot.setVisibility(View.INVISIBLE);
            lnDa.setVisibility(View.INVISIBLE);
            txtDa.setVisibility(View.INVISIBLE);
            txtNgot.setVisibility(View.INVISIBLE);
            txtSize.setVisibility(View.INVISIBLE);
        }
            super.onPostExecute(integer);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Integer integer) {
            super.onCancelled(integer);
        }

        @Override
        protected Integer doInBackground(String... strings) {
            try {
                String params = "masp="+strings[0];
                connect connect = new connect("ds_ChiTiet");
                NodeList nodeList = connect.getDataParameter(params,"ChiTietSanPham");
                return nodeList.getLength();
            }
            catch (Exception e)
            {
                return 0;
            }
        }
    }
    class ThongTinSanPham extends AsyncTask<String,Void, SanPham>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(SanPham sanPham) {
            sanPhamSelected = sanPham;
            String image = sanPham.getHinhsp()+"?type=large";
            Picasso.get().load(image).into(imgChonSP);
            super.onPostExecute(sanPham);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(SanPham sanPham) {
            super.onCancelled(sanPham);
        }

        @Override
        protected SanPham doInBackground(String... strings) {
            try {
                String params = "masp="+strings[0];
                connect connect = new connect("get_SanPham");
                NodeList nodeList = connect.getDataParameter(params,"SanPham");
                Element element = (Element) nodeList.item(0);
                String masp = element.getElementsByTagName("MaSP").item(0).getTextContent();
                String tensp = element.getElementsByTagName("TenSanPham").item(0).getTextContent();
                String hinhanh = element.getElementsByTagName("hinhanh").item(0).getTextContent();
                SanPham sp = new SanPham(masp,tensp,hinhanh,0);
                return sp;
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }
    public void clickDatMon(View view) {
        ChiTietSanPham chiTietSanPham = new ChiTietSanPham();
        chiTietSanPham.setMasp(sanPhamSelected.getMasp());
        if(lnSize.getVisibility()==View.INVISIBLE)
        {
            chiTietSanPham.setSize("Không");
        }
        else if(cbSizeM.isChecked())
            chiTietSanPham.setSize("M");
        else if(cbSizeL.isChecked())
            chiTietSanPham.setSize("M");

//        intent.putExtra("makh",makh);
//        intent.putExtra("ChiTietSP",chiTietSanPham);
//        intent.putExtra("soluong",soluong);
//        setResult(Activity.RESULT_OK,intent);
        Toasty.success(ChonSanPhamActivity.this,"Đã thêm zô giỏ hàng.",Toasty.LENGTH_LONG).show();

        Intent intent1 = new Intent(ChonSanPhamActivity.this,CartActivity.class);
        intent1.putExtra("makh",makh);
        intent1.putExtra("ChiTietSP",chiTietSanPham);
        intent1.putExtra("soluong",txtSL.getText());
        startActivityForResult(intent1,119);
        finish();
    }

    public void clickTang(View view) {
        soluong+=1;
        txtSL.setText(soluong+"");
    }

    public void clickGiam(View view) {
        soluong-=1;
        if(soluong<0)
            soluong=0;
        txtSL.setText(soluong+"");
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }
}
