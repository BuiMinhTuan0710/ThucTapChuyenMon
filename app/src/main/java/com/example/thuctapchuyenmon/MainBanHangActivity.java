package com.example.thuctapchuyenmon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.SanPhamAdapter;
import com.example.adapter.ThongTinSanPhamAdapter;
import com.example.model.LoaiSanPham;
import com.example.model.SanPham;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainBanHangActivity extends AppCompatActivity {

    RecyclerView rvSanPhamHot;
    TextView txtSoLuong,txtTongTien;
    TabHost tabDanhMuc;
    ListView lvDanhSach;
    List<SanPham>ds_sanpham;
    List<LoaiSanPham>ds_loaisp = new ArrayList<>();
    public static SanPham selectedSanPham;
    public static final int REQUEST_CODE=113;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ban_hang);
        addViews();
        addEvents();
    }
    ThongTinSanPhamAdapter adapter;
    private void addEvents() {
        tabDanhMuc.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                ds_sanpham.clear();
                for (LoaiSanPham item :
                        ds_loaisp) {
                    if(tabId.equals(item.getTenLoai()))
                    {
                        ThongTinSanPham thongTinSanPham = new ThongTinSanPham();
                        thongTinSanPham.execute(item.getMaLoai());
                    }
                }
            }
        });
        lvDanhSach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainBanHangActivity.this,ChonSanPhamActivity.class);
                selectedSanPham = ds_sanpham.get(position);
                intent.putExtra("SanPham",selectedSanPham);
                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                int soluong = sharedPreferences.getInt(selectedSanPham.getMasp(),0);
                intent.putExtra("soluong",soluong);
                txtSoLuong = view.findViewById(R.id.txtSoLuong);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }
    int soluong = 0;
    int tongtien = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE && resultCode== Activity.RESULT_OK)
        {
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            soluong = data.getIntExtra("soluong",0);
            if(soluong!=0)
            {
                txtSoLuong.setVisibility(View.VISIBLE);
                txtSoLuong.setText("x"+soluong);
                editor.putInt(selectedSanPham.getMasp(),soluong);
                editor.commit();
            }
            else
            {
                txtSoLuong.setVisibility(View.INVISIBLE);
                editor.remove(selectedSanPham.getMasp());
                editor.commit();
            }

            tongtien+=soluong*selectedSanPham.getGiasp();
            soluong = 0;
            txtTongTien.setText(tongtien+"đ");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void addViews() {
        txtTongTien = findViewById(R.id.txtTongTien);
        rvSanPhamHot = findViewById(R.id.rvSanPhamHot);
        rvSanPhamHot.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        rvSanPhamHot.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvSanPhamHot.getContext(),DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.custom_divider);
        dividerItemDecoration.setDrawable(drawable);
        ArrayList<SanPham> ds_SanPham = new ArrayList<>();
        SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(ds_SanPham,getApplicationContext());
        rvSanPhamHot.setAdapter(sanPhamAdapter);
        addTab();
    }

    private void addTab() {
        tabDanhMuc = findViewById(R.id.tabDanhMuc);
        lvDanhSach = findViewById(R.id.lvDanhSach);
        ds_sanpham = new ArrayList<SanPham>();
        ThongTinSanPham thongTinSanPham = new ThongTinSanPham();
        thongTinSanPham.execute("L01");
        adapter = new ThongTinSanPhamAdapter(ds_sanpham,MainBanHangActivity.this,R.layout.custom_item);
        tabDanhMuc.setup();
        LoaiSP loaiSP = new LoaiSP();
        loaiSP.execute();

    }
    private void setupTab(final View view, final String tag) {
        View tabview = createTabView(tabDanhMuc.getContext(), tag);
        final TabHost.TabSpec setContent = tabDanhMuc.newTabSpec(tag).setIndicator(tabview).setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return view;
            }
        });
        tabDanhMuc.addTab(setContent);
    }
    private static View createTabView(final Context context, final String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
        TextView tv = view.findViewById(R.id.tabsText);
        tv.setText(text);
        return view;
    }

    public void clickImage(View view) {
        Toast.makeText(this, "hot", Toast.LENGTH_SHORT).show();
    }
    public void clickTiepTuc(View view) {
        Intent intent = new Intent(MainBanHangActivity.this,MapsActivity.class);
        startActivity(intent);
    }
    class LoaiSP extends AsyncTask<Void,Void,List<LoaiSanPham>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<LoaiSanPham> loaiSanPhams) {
            super.onPostExecute(loaiSanPhams);
            for (LoaiSanPham item :
                    loaiSanPhams) {
                ds_loaisp.add(item);
                setupTab(new TextView(MainBanHangActivity.this),item.getTenLoai());
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<LoaiSanPham> doInBackground(Void... voids) {
            List<LoaiSanPham>ds_loai = new ArrayList<>();
            try {
                URL url = new URL("http://www.minhtuan.somee.com/myService.asmx/ds_loaisp");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(connection.getInputStream());
                NodeList nodeList = document.getElementsByTagName("LoaiSanPham");
                for (int i=0;i<nodeList.getLength();i++)
                {
                    Element element = (Element) nodeList.item(i);
                    String maloai = element.getElementsByTagName("MaLoai").item(0).getTextContent();
                    String tenLoai = element.getElementsByTagName("TenLoai").item(0).getTextContent();
                    LoaiSanPham loaiSanPham = new LoaiSanPham(maloai,tenLoai);
                    ds_loai.add(loaiSanPham);
                }
                return ds_loai;


            } catch (Exception e) {
                Log.e( "loi: ", e.toString());
                return null;
            }
        }
    }
    class ThongTinSanPham extends AsyncTask<String,Void,List<SanPham>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<SanPham> sanPhams) {
            super.onPostExecute(sanPhams);
            for (SanPham sp :
                    sanPhams) {
                ds_sanpham.add(sp);
            }
            lvDanhSach.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<SanPham> doInBackground(String... strings) {
            List<SanPham>ds_sp = new ArrayList<>();
            String params = "maloai="+strings[0];
            try {
                URL url = new URL("http://www.minhtuan.somee.com/myService.asmx/ds_SanPham");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                OutputStream os = connection.getOutputStream();
                OutputStreamWriter ws = new OutputStreamWriter(os,"UTF-8");
                ws.write(params);
                ws.flush();
                ws.close();

                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(connection.getInputStream());
                NodeList nodeList = document.getElementsByTagName("SanPham");
                for (int i=0;i<nodeList.getLength();i++)
                {
                    Element element = (Element) nodeList.item(i);
                    String masp = element.getElementsByTagName("MaSP").item(0).getTextContent();
                    String tensp = element.getElementsByTagName("TenSanPham").item(0).getTextContent();
                    String hinhanh = element.getElementsByTagName("hinhanh").item(0).getTextContent();
                    SanPham sanPham = new SanPham(masp,tensp,hinhanh,10000);
                    ds_sp.add(sanPham);
                }
                return  ds_sp;
            } catch (Exception e) {
                Log.e("loi ",e.getMessage() );
                return null;
            }
        }
    }
}
