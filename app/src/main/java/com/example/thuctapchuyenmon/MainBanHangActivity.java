package com.example.thuctapchuyenmon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import com.example.model.SanPham;

import java.util.ArrayList;
import java.util.List;

public class MainBanHangActivity extends AppCompatActivity {

    RecyclerView rvSanPhamHot;
    TextView txtSoLuong,txtGia,txtTongTien;
    TabHost tabDanhMuc;
    ListView lvDanhSach;
    List<SanPham>ds_sanpham;
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
                if(tabId.equals("Trà sữa"))
                {
                    Toast.makeText(MainBanHangActivity.this, tabId, Toast.LENGTH_SHORT).show();
                    lvDanhSach.setAdapter(adapter);
                    txtSoLuong = lvDanhSach.findViewById(R.id.txtSoLuong);
                    txtGia = lvDanhSach.findViewById(R.id.txtGiaInfo);
                }

            }
        });
        lvDanhSach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainBanHangActivity.this,ChonSanPhamActivity.class);
                selectedSanPham = ds_sanpham.get(position);
                intent.putExtra("SanPham",selectedSanPham);
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
            soluong = data.getIntExtra("soluong",0);
            txtSoLuong.setVisibility(View.VISIBLE);
            txtSoLuong.setText("x"+soluong);
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
        ds_SanPham.add(new SanPham("Phở Hà Nội",R.drawable.pho,150000));
        ds_SanPham.add(new SanPham("Bánh Cuốn",R.drawable.banhcuon,200000));

        SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(ds_SanPham,getApplicationContext());
        rvSanPhamHot.setAdapter(sanPhamAdapter);
        addTab();
    }

    private void addTab() {
        tabDanhMuc = findViewById(R.id.tabDanhMuc);
        lvDanhSach = findViewById(R.id.lvDanhSach);
        ds_sanpham = new ArrayList<SanPham>();
        ds_sanpham.add(new SanPham("Phở Hà Nội",R.drawable.pho,150000));
        ds_sanpham.add(new SanPham("Bánh Cuốn",R.drawable.banhcuon,200000));
        adapter = new ThongTinSanPhamAdapter(ds_sanpham,MainBanHangActivity.this,R.layout.custom_item);
        tabDanhMuc.setup();
        setupTab(new TextView(this), "Trà sữa");
        tabDanhMuc.getTabWidget().setDividerDrawable(R.drawable.custom_divider);
        setupTab(new TextView(this), "Đồ ăn vặt");
        setupTab(new TextView(this), "Thức uống khác");

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
}
