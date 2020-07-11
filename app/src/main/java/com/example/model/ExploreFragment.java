package com.example.model;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chabbal.slidingdotsplash.SlidingSplashView;
import com.example.adapter.SanPhamAdapter;
import com.example.adapter.ThongTinSanPhamAdapter;
import com.example.database.connect;
import com.example.thuctapchuyenmon.ChucNangActivity;
import com.example.thuctapchuyenmon.R;
import com.google.android.material.textfield.TextInputEditText;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.nex3z.notificationbadge.NotificationBadge;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {
    private RecyclerView recyclerSale, recyclerMilkTea;
    private SlidingSplashView splashExplore;
    private List<SanPham> productsSale, productsMilkTea;
    private GoogleProgressBar progressBarExplore;
    private static NotificationBadge badge;
    private TextInputEditText edtSearch;
    private NestedScrollView nestedScrollMenu;
    TextView txtTenDanhMuc;
    ThongTinSanPhamAdapter adapter;
    SanPhamAdapter sanPhamAdapter;
    private androidx.appcompat.widget.Toolbar toolbarExplore;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        addViews(view);
        onScrollListener();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onScrollListener() {
        nestedScrollMenu.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == 0) {
                    toolbarExplore.setVisibility(View.GONE);
                }
                if (scrollY > oldScrollY) {
                    toolbarExplore.setVisibility(View.VISIBLE);
                    toolbarExplore.setTitle("UTC2 Food");
                    ChucNangActivity.navigation.setVisibility(View.GONE);

                    TranslateAnimation animate = new TranslateAnimation(
                            0,
                            0,
                            ChucNangActivity.navigation.getHeight(),
                            0);
                    animate.setDuration(200);
                    animate.setFillAfter(true);
                    ChucNangActivity.navigation.startAnimation(animate);
                } else if (scrollY < oldScrollY) {
                    toolbarExplore.setVisibility(View.VISIBLE);

                    toolbarExplore.setTitle("UTC2 Food");
                    ChucNangActivity.navigation.setVisibility(View.VISIBLE);
                    TranslateAnimation animate = new TranslateAnimation(
                            0,
                            0,
                            ChucNangActivity.navigation.getHeight(),
                            0);
                    animate.setDuration(200);
                    animate.setFillAfter(true);
                    ChucNangActivity.navigation.startAnimation(animate);
                }
            }
        });
    }

    private void addViews(View view) {
        nestedScrollMenu = view.findViewById(R.id.nestedScrollMenu);
        toolbarExplore = view.findViewById(R.id.toolbarExplore);
        recyclerSale = view.findViewById(R.id.recyclerHot);
        splashExplore = view.findViewById(R.id.splashExplore);
        progressBarExplore = view.findViewById(R.id.progressExplore);
        recyclerMilkTea = view.findViewById(R.id.recyclerMilkTea);
        edtSearch = view.findViewById(R.id.edtSearch);
        txtTenDanhMuc = view.findViewById(R.id.txtTenDanhMuc);
        TenLoai tenLoai = new TenLoai();
        tenLoai.execute();
        FrameLayout framBadge = view.findViewById(R.id.layout_Badge);
        badge = framBadge.findViewById(R.id.badge);
        Button btnViewAllProduct = view.findViewById(R.id.btnViewAllProduct);


        productsSale = new ArrayList<>();
        productsMilkTea = new ArrayList<>();

        recyclerSale.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        sanPhamAdapter = new SanPhamAdapter(productsSale,R.layout.custom_itemsphot,getActivity());
        ThongTinSanPhamSale thongTinSanPhamSale = new ThongTinSanPhamSale();
        thongTinSanPhamSale.execute();

        recyclerMilkTea.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        adapter = new ThongTinSanPhamAdapter(productsMilkTea,R.layout.custom_item_food,getActivity());
        ThongTinSanPham thongTinSanPham = new ThongTinSanPham();
        thongTinSanPham.execute();
    }

    class TenLoai extends AsyncTask<Void,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            txtTenDanhMuc.setText(s);
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                connect connect = new connect("ds_loaisp");
                NodeList nodeList = connect.getData("LoaiSanPham");
                Element element = (Element) nodeList.item(0);
                String tenloai = element.getElementsByTagName("TenLoai").item(0).getTextContent();
                return tenloai;
            }
            catch (Exception e1)
            {
                Log.e("loi ",e1.getMessage() );
                return null;
            }
        }
    }
    class ThongTinSanPhamSale extends AsyncTask<Void,Void,List<SanPham>>
    {
        @Override
        protected void onPreExecute() {
            DialogLoading.LoadingGoogle(true,progressBarExplore);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<SanPham> sanPhams) {
            for (SanPham sp :
                    sanPhams) {
                productsSale.add(sp);
            }
            Log.e("san pham",productsSale.size()+"" );
            recyclerSale.setAdapter(sanPhamAdapter);
            sanPhamAdapter.notifyDataSetChanged();
            DialogLoading.LoadingGoogle(true,progressBarExplore);
            super.onPostExecute(sanPhams);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(List<SanPham> sanPhams) {
            super.onCancelled(sanPhams);
        }

        @Override
        protected List<SanPham> doInBackground(Void... voids) {
            List<SanPham>ds_sp = new ArrayList<>();
            List<GiamGia>ds_giamgia = new ArrayList<>();

            //lấy danh sách sản phẩm giảm giá
            try {
                connect connect = new connect("ds_giamgia");
                NodeList nodeList = connect.getData("GiamGia");
                for (int i=0;i<nodeList.getLength();i++)
                {
                    Element element = (Element) nodeList.item(i);
                    String magg = element.getElementsByTagName("MaGG").item(0).getTextContent();
                    String masp = element.getElementsByTagName("MaSP").item(0).getTextContent();
                    String giamgia = element.getElementsByTagName("GiamGia1").item(0).getTextContent();
                    GiamGia giamGia = new GiamGia(magg,masp, Integer.parseInt(giamgia));
                    ds_giamgia.add(giamGia);
                }
            }
            catch (Exception e1)
            {
                Log.e("loi ",e1.getMessage() );
                return null;
            }
            try {
                for(int i = 0;i<ds_giamgia.size();i++)
                {
                    //lấy sản phẩm
                    String params = "masp="+ds_giamgia.get(i).getMasp();
                    connect connect = new connect("get_SanPham");
                    NodeList nodeList = connect.getDataParameter(params,"SanPham");
                    Element element = (Element) nodeList.item(0);
                    String masp = element.getElementsByTagName("MaSP").item(0).getTextContent();
                    String tensp = element.getElementsByTagName("TenSanPham").item(0).getTextContent();
                    String hinhanh = element.getElementsByTagName("hinhanh").item(0).getTextContent();

                    //lấy chi tiết sản phẩm
                    connect connect1 = new connect("ds_ChiTiet");
                    NodeList nodeList1 = connect1.getDataParameter(params,"ChiTietSanPham");
                    Element element1 = (Element) nodeList1.item(0);
                    String gia = element1.getElementsByTagName("DonGia").item(0).getTextContent();

                    SanPham sp = new SanPham(masp,tensp,hinhanh,Integer.parseInt(gia),ds_giamgia.get(i).getGiamgia());
                    ds_sp.add(sp);
                }
                return  ds_sp;
            } catch (Exception e) {
                Log.e("loi ",e.getMessage() );
                return null;
            }
        }
    }
    class ThongTinSanPham extends AsyncTask<Void,Void,List<SanPham>>
    {
        @Override
        protected void onPreExecute() {
            DialogLoading.LoadingGoogle(true,progressBarExplore);
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(List<SanPham> sanPhams) {
            super.onPostExecute(sanPhams);
            for (SanPham sp :
                    sanPhams) {
                productsMilkTea.add(sp);
            }
            recyclerMilkTea.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            DialogLoading.LoadingGoogle(false,progressBarExplore);
            Log.e(productsMilkTea.size()+"","Sản Phẩm");

        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected List<SanPham> doInBackground(Void... strings) {
            String maloai = "";
            try {
                connect connect = new connect("ds_loaisp");
                NodeList nodeList = connect.getData("LoaiSanPham");
                Element element = (Element) nodeList.item(0);
                maloai = element.getElementsByTagName("MaLoai").item(0).getTextContent();
            }
            catch (Exception e1)
            {
                Log.e("loi ",e1.getMessage() );
                return null;
            }
            List<SanPham>ds_sp = new ArrayList<>();
            String params = "maloai="+maloai.trim();
            try {

                connect connect = new connect("ds_SanPham");
                NodeList nodeList = connect.getDataParameter(params,"SanPham");
                for (int i=0;i<nodeList.getLength();i++)
                {
                    Element element = (Element) nodeList.item(i);
                    String masp = element.getElementsByTagName("MaSP").item(0).getTextContent();
                    String tensp = element.getElementsByTagName("TenSanPham").item(0).getTextContent();
                    String hinhanh = element.getElementsByTagName("hinhanh").item(0).getTextContent();

                    String params1 = "masp="+masp;
                    connect connect1 = new connect("ds_ChiTiet");
                    NodeList nodeList1 = connect1.getDataParameter(params1,"ChiTietSanPham");
                    Element element1 = (Element) nodeList1.item(0);
                    String gia = element1.getElementsByTagName("DonGia").item(0).getTextContent();
                    SanPham sanPham = new SanPham(masp,tensp,hinhanh,Integer.parseInt(gia));
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
