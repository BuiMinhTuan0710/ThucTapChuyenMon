package com.example.thuctapchuyenmon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.adapter.ChiTietSanPhamAdapter;
import com.example.database.connect;
import com.example.model.ChiTietHoaDon;
import com.example.model.DialogLoading;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonActivity extends AppCompatActivity {

    RecyclerView recyclerMyorderCT;
    GoogleProgressBar progressOrderCT;
    ChiTietSanPhamAdapter adapter;
    List<ChiTietHoaDon>ds_HoaDon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_hoa_don);
        addViews();
    }

    private void addViews() {

        Intent intent = getIntent();
        recyclerMyorderCT = findViewById(R.id.recyclerMyorderCT);
        progressOrderCT = findViewById(R.id.progressOrderCT);
        ds_HoaDon = new ArrayList<>();

        String mahd = intent.getStringExtra("mahd");
        adapter = new ChiTietSanPhamAdapter(ds_HoaDon,R.layout.custom_item_lvorder,this);
        recyclerMyorderCT.setLayoutManager(new GridLayoutManager(this,1));

        DanhSachSanPham danhSachSanPham = new DanhSachSanPham();
        danhSachSanPham.execute(mahd);
    }

    class DanhSachSanPham extends AsyncTask<String,Void, List<ChiTietHoaDon>>
    {
        @Override
        protected List<ChiTietHoaDon> doInBackground(String... strings) {
            try {
                List<ChiTietHoaDon>ds = new ArrayList<>();
                String params = "mahd="+strings[0];
                connect connect = new connect("DanhSachChiTietHD");
                NodeList nodeList = connect.getDataParameter(params,"ChiTietHoaDon");
                for (int i=0;i<nodeList.getLength();i++) {
                    Element element = (Element) nodeList.item(i);
                    String masp = element.getElementsByTagName("MaSP").item(0).getTextContent();
                    String size = element.getElementsByTagName("Size").item(0).getTextContent();
                    String SoLuong = element.getElementsByTagName("SoLuong").item(0).getTextContent();
                    String GiaBan = element.getElementsByTagName("GiaBan").item(0).getTextContent();

                    String paramSP = "masp="+masp;
                    connect connect1 = new connect("get_SanPham");
                    NodeList nodeList1 = connect1.getDataParameter(paramSP,"SanPham");

                    Element element1  = (Element) nodeList1.item(0);
                    String tensp = element1.getElementsByTagName("TenSanPham").item(0).getTextContent();
                    String hinhanh = element1.getElementsByTagName("hinhanh").item(0).getTextContent();

                    ChiTietHoaDon ct = new ChiTietHoaDon(tensp,size,hinhanh,Integer.parseInt(GiaBan),Integer.parseInt(SoLuong));
                    ds.add(ct);
                }
                return ds;
            }
            catch (Exception e)
            {
                return null;
            }
        }
        @Override
        protected void onPreExecute() {
            DialogLoading.LoadingGoogle(false,progressOrderCT);
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(List<ChiTietHoaDon> chiTietHoaDons) {
            DialogLoading.LoadingGoogle(false,progressOrderCT);
            for (ChiTietHoaDon ct :
                    chiTietHoaDons) {
                ds_HoaDon.add(ct);
            }
            recyclerMyorderCT.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            super.onPostExecute(chiTietHoaDons);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(List<ChiTietHoaDon> chiTietHoaDons) {
            super.onCancelled(chiTietHoaDons);
        }
    }
}