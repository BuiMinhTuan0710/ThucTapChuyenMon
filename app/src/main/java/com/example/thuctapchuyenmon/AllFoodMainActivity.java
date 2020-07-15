package com.example.thuctapchuyenmon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.adapter.AllFoodAdapter;
import com.example.adapter.SearchAdapter;
import com.example.database.connect;
import com.example.model.ChiTietSanPham;
import com.example.model.DialogLoading;
import com.example.model.ExploreFragment;
import com.example.model.RecyclerItemClickListener;
import com.example.model.SanPham;
import com.google.android.material.textfield.TextInputEditText;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class AllFoodMainActivity extends AppCompatActivity {

    List<ChiTietSanPham>ds_SanPham;
    RecyclerView recyclerViewAllFoodPaging;
    AllFoodAdapter allFoodAdapter;
    GoogleProgressBar progressAllFood;
    public String makh;
    private TextInputEditText edtSearch;
    private SearchAdapter searchAdapter;
    private List<ChiTietSanPham>ds_SanPhamSearch=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_food_main);
        addViews();
        addEvents();
    }

    private void addEvents() {
        recyclerViewAllFoodPaging.addOnItemTouchListener(new RecyclerItemClickListener(AllFoodMainActivity.this, recyclerViewAllFoodPaging, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView txtMaSP = view.findViewById(R.id.txtMaSPSale);
                Intent intent = new Intent(AllFoodMainActivity.this,ChonSanPhamActivity.class);
                intent.putExtra("makh",makh);
                intent.putExtra("masp",txtMaSP.getText());
                startActivityForResult(intent,118);
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    DialogLoading.LoadingGoogle(true, progressAllFood);
                    SearchItems();
                }
                return true;
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0)
                {
                    ds_SanPhamSearch = new ArrayList<>();
                    recyclerViewAllFoodPaging.setAdapter(allFoodAdapter);
                }
            }
        });
    }

    private void SearchItems() {
        if(edtSearch.getText().length()>0)
        {
            ds_SanPhamSearch = new ArrayList<>();
            searchAdapter = new SearchAdapter(ds_SanPhamSearch,R.layout.custom_item_search,this);
            recyclerViewAllFoodPaging.setAdapter(searchAdapter);
            SearchSanPham searchSanPham = new SearchSanPham();
            searchSanPham.execute(edtSearch.getText().toString());

        }
    }

    class SearchSanPham extends AsyncTask<String,Void,List<ChiTietSanPham>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<ChiTietSanPham> chiTietSanPhams) {
            DialogLoading.LoadingGoogle(false,progressAllFood);
            for (ChiTietSanPham item : chiTietSanPhams)
            {
                ds_SanPhamSearch.add(item);
            }
            searchAdapter.notifyDataSetChanged();
            super.onPostExecute(chiTietSanPhams);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<ChiTietSanPham> doInBackground(String... strings) {
            List<ChiTietSanPham> ds = new ArrayList<>();
            List<SanPham>ds_SanPham = new ArrayList<>();
            try {
                String paramsp = "tensp="+strings[0];
                connect connect = new connect("TimKiemSanPham");
                NodeList nodeList = connect.getDataParameter(paramsp,"SanPham");
                for (int i=0;i<nodeList.getLength();i++) {
                    Element element = (Element) nodeList.item(i);
                    String masp = element.getElementsByTagName("MaSP").item(0).getTextContent();
                    String tensp = element.getElementsByTagName("TenSanPham").item(0).getTextContent();
                    String hinhanh = element.getElementsByTagName("hinhanh").item(0).getTextContent();
                    SanPham sp = new SanPham(masp,tensp,hinhanh,0);
                    ds_SanPham.add(sp);
                }
                for(SanPham item : ds_SanPham)
                {
                    String params = "masp="+item.getMasp();
                    connect connect1 = new connect("ds_ChiTiet");
                    NodeList nodeList1 = connect1.getDataParameter(params,"ChiTietSanPham");
                    Element element = (Element) nodeList1.item(0);
                    String size = element.getElementsByTagName("Size").item(0).getTextContent();
                    String gia = element.getElementsByTagName("DonGia").item(0).getTextContent();

                    String paramsgia = "masp="+item.getMasp()+"&size="+size;
                    connect connect2 = new connect("getGiaThuc");
                    NodeList nodeList2 = connect2.getDataParameter(paramsgia,"int");
                    String giathuc = nodeList2.item(0).getTextContent();
                    ChiTietSanPham chiTietSanPham = new ChiTietSanPham(item.getMasp(),item.getTensp(),size,item.getHinhsp(),Integer.parseInt(gia),Integer.parseInt(giathuc));
                    ds.add(chiTietSanPham);
                }
                return ds;
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addViews() {

        Intent intent = getIntent();
        makh = intent.getStringExtra("makh");
        ds_SanPham = new ArrayList<>();

        progressAllFood = findViewById(R.id.progressAllFood);
        edtSearch = findViewById(R.id.edtSearchAll);
        allFoodAdapter = new AllFoodAdapter(this,R.layout.custom_itemsphot,ds_SanPham);
        recyclerViewAllFoodPaging = findViewById(R.id.recyclerViewAllFoodPaging);
        recyclerViewAllFoodPaging.setLayoutManager(new GridLayoutManager(this,2));
        recyclerViewAllFoodPaging.setAdapter(allFoodAdapter);
        DanhSachSanPham danhSachSanPham = new DanhSachSanPham();
        danhSachSanPham.execute();
    }
    class DanhSachSanPham extends AsyncTask<Void,Void,List<ChiTietSanPham>>
    {
        @Override
        protected void onPreExecute() {
            DialogLoading.LoadingGoogle(true,progressAllFood);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<ChiTietSanPham> chiTietSanPhams) {
            DialogLoading.LoadingGoogle(false,progressAllFood);
            for (ChiTietSanPham item : chiTietSanPhams)
            {
                ds_SanPham.add(item);
            }
            allFoodAdapter.notifyDataSetChanged();
            super.onPostExecute(chiTietSanPhams);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<ChiTietSanPham> doInBackground(Void... strings) {
            List<ChiTietSanPham> ds = new ArrayList<>();
            List<SanPham>ds_SanPham = new ArrayList<>();
            try {
                connect connect = new connect("DanhSachAllFood");
                NodeList nodeList = connect.getData("SanPham");
                for (int i=0;i<nodeList.getLength();i++) {
                    Element element = (Element) nodeList.item(i);
                    String masp = element.getElementsByTagName("MaSP").item(0).getTextContent();
                    String tensp = element.getElementsByTagName("TenSanPham").item(0).getTextContent();
                    String hinhanh = element.getElementsByTagName("hinhanh").item(0).getTextContent();
                    SanPham sp = new SanPham(masp,tensp,hinhanh,0);
                    ds_SanPham.add(sp);
                }
                for(SanPham item : ds_SanPham)
                {
                    String params = "masp="+item.getMasp();
                    connect connect1 = new connect("ds_ChiTiet");
                    NodeList nodeList1 = connect1.getDataParameter(params,"ChiTietSanPham");
                    Element element = (Element) nodeList1.item(0);
                    String size = element.getElementsByTagName("Size").item(0).getTextContent();
                    String gia = element.getElementsByTagName("DonGia").item(0).getTextContent();

                    String paramsgia = "masp="+item.getMasp()+"&size="+size;
                    connect connect2 = new connect("getGiaThuc");
                    NodeList nodeList2 = connect2.getDataParameter(paramsgia,"int");
                    String giathuc = nodeList2.item(0).getTextContent();
                    ChiTietSanPham chiTietSanPham = new ChiTietSanPham(item.getMasp(),item.getTensp(),size,item.getHinhsp(),Integer.parseInt(gia),Integer.parseInt(giathuc));
                    ds.add(chiTietSanPham);
                }
                return ds;
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }
}