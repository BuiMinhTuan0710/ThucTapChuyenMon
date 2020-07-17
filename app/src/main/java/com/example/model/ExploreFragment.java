package com.example.model;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chabbal.slidingdotsplash.SlidingSplashView;
import com.example.adapter.SanPhamAdapter;
import com.example.adapter.SearchAdapter;
import com.example.adapter.ThongTinSanPhamAdapter;
import com.example.database.connect;
import com.example.thuctapchuyenmon.AllFoodMainActivity;
import com.example.thuctapchuyenmon.CartActivity;
import com.example.thuctapchuyenmon.ChonSanPhamActivity;
import com.example.thuctapchuyenmon.ChucNangActivity;
import com.example.thuctapchuyenmon.R;
import com.google.android.material.textfield.TextInputEditText;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment{
    private RecyclerView recyclerSale, recyclerMilkTea;
    private ViewFlipper ViewFlipper;
    private List<SanPham> productsSale, productsMilkTea;
    private GoogleProgressBar progressBarExplore;
    private static NotificationBadge badge;
    private TextInputEditText edtSearch;
    private TextView txtDanhMuc;
    private SearchAdapter searchAdapter;
    private List<ChiTietSanPham>ds_SanPhamSearch=new ArrayList<>();
    FrameLayout framBadge;
    public String MaKhachHang;
    private NestedScrollView nestedScrollMenu;
    TextView txtTenDanhMuc;
    String simpleFileName;
    ThongTinSanPhamAdapter adapter;
    SanPhamAdapter sanPhamAdapter;
    Button btnViewAllProduct;
    public static final int REQUEST_CODE=115;
    public ArrayList<String>ds_SanPhamChon = new ArrayList<>();
    private androidx.appcompat.widget.Toolbar toolbarExplore;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        addViews(view);
        onScrollListener();
        addEvents();
        return view;
    }


    private String readData() {
        try {
            FileInputStream in = getActivity().openFileInput(simpleFileName);

            BufferedReader br= new BufferedReader(new InputStreamReader(in));

            StringBuilder sb= new StringBuilder();
            String s= null;
            while((s= br.readLine())!= null)  {
                sb.append(s);
            }
            return sb.toString();
        } catch (Exception e) {
            return "0";
        }
    }
    private void addEvents() {

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    DialogLoading.LoadingGoogle(true, progressBarExplore);
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
                    recyclerSale.setAdapter(sanPhamAdapter);
                    txtDanhMuc.setText("Sale");
                    recyclerMilkTea.setVisibility(View.VISIBLE);
                    txtTenDanhMuc.setVisibility(View.VISIBLE);
                    btnViewAllProduct.setVisibility(View.VISIBLE);
                }
            }
        });
        recyclerMilkTea.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerMilkTea ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        final TextView txtMaSP = view.findViewById(R.id.txtMaSPFood);
                        masp = txtMaSP.getText().toString();
                        ThongTinChiTietSP thongTinChiTietSP = new ThongTinChiTietSP();
                        thongTinChiTietSP.execute(masp);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        recyclerSale.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerSale ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if(ds_SanPhamSearch.size()==0)
                        {
                            final TextView txtMaSP;
                            txtMaSP = view.findViewById(R.id.txtMaSPSale);
                            masp = txtMaSP.getText().toString();
                            ThongTinChiTietSP thongTinChiTietSP = new ThongTinChiTietSP();
                            thongTinChiTietSP.execute(masp);
                        }
                        else
                        {
                            masp = ds_SanPhamSearch.get(position).getMasp();
                            ThongTinChiTietSP thongTinChiTietSP = new ThongTinChiTietSP();
                            thongTinChiTietSP.execute(masp);
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        framBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                intent.putExtra("makh",MaKhachHang);
                startActivity(intent);
            }
        });

        btnViewAllProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllFoodMainActivity.class);
                intent.putExtra("makh",MaKhachHang);
                startActivityForResult(intent,110);
            }
        });
    }

    private void SearchItems() {
        if(edtSearch.getText().length()>0)
        {
            ds_SanPhamSearch = new ArrayList<>();
            searchAdapter = new SearchAdapter(ds_SanPhamSearch,R.layout.custom_item_search,getActivity());
            recyclerSale.setAdapter(searchAdapter);
            txtDanhMuc.setText("Sản Phẩm");
            recyclerMilkTea.setVisibility(View.GONE);
            txtTenDanhMuc.setVisibility(View.INVISIBLE);
            btnViewAllProduct.setVisibility(View.GONE);
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
            DialogLoading.LoadingGoogle(false,progressBarExplore);
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
                    String params = "masp="+item.masp;
                    connect connect1 = new connect("ds_ChiTiet");
                    NodeList nodeList1 = connect1.getDataParameter(params,"ChiTietSanPham");
                    Element element = (Element) nodeList1.item(0);
                    String size = element.getElementsByTagName("Size").item(0).getTextContent();
                    String gia = element.getElementsByTagName("DonGia").item(0).getTextContent();

                    String paramsgia = "masp="+item.masp+"&size="+size;
                    connect connect2 = new connect("getGiaThuc");
                    NodeList nodeList2 = connect2.getDataParameter(paramsgia,"int");
                    String giathuc = nodeList2.item(0).getTextContent();
                    ChiTietSanPham chiTietSanPham = new ChiTietSanPham(item.masp,item.tensp,size,item.hinhsp,Integer.parseInt(gia),Integer.parseInt(giathuc));
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE && resultCode== Activity.RESULT_OK)
        {
            ChiTietSanPham chiTietSanPham = (ChiTietSanPham) data.getSerializableExtra("ChiTietSP");
            int soluong = data.getIntExtra("soluong",0);
            if(soluong>0)
            {
                badge.setNumber(1);
                String sanpham = chiTietSanPham.getMasp().trim()+"-"+chiTietSanPham.getSize().trim()+"-"+soluong;
                ds_SanPhamChon.add(sanpham);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        simpleFileName = MaKhachHang.trim()+".txt";
        nestedScrollMenu = view.findViewById(R.id.nestedScrollMenu);
        toolbarExplore = view.findViewById(R.id.toolbarExplore);
        recyclerSale = view.findViewById(R.id.recyclerHot);
        ViewFlipper = view.findViewById(R.id.ViewFlipper);
        progressBarExplore = view.findViewById(R.id.progressExplore);
        recyclerMilkTea = view.findViewById(R.id.recyclerMilkTea);
        edtSearch = view.findViewById(R.id.edtSearch);
        txtTenDanhMuc = view.findViewById(R.id.txtTenDanhMuc);
        txtDanhMuc = view.findViewById(R.id.txtDanhMuc);

        acctionViewFlipper();
        TenLoai tenLoai = new TenLoai();
        tenLoai.execute();
        framBadge = view.findViewById(R.id.layout_Badge);
        badge = framBadge.findViewById(R.id.badge);

        int number = Integer.parseInt(readData());
        badge.setNumber(number);
        btnViewAllProduct = view.findViewById(R.id.btnViewAllProduct);

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

    private void acctionViewFlipper() {
        ArrayList<String>quangcao = new ArrayList<>();
        quangcao.add("https://intruongphu.com/images/attachment/2849poster-quang-cao-do-an.jpg");
        quangcao.add("https://i.pinimg.com/originals/3c/79/c6/3c79c6f54cf25a01b3dfefb0b90e7e07.jpg");
        quangcao.add("https://bizweb.dktcdn.net/100/060/439/files/poster-quang-cao-mon-an-020.jpg?v=1470871779199");
        quangcao.add("https://www.cukcuk.vn/wp-content/uploads/2019/06/41936420_2418111258213885_3981372350531633152_o.jpg");
        for (String item :
                quangcao) {

            ImageView imageView = new ImageView(getActivity());
            Picasso.get().load(item).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ViewFlipper.addView(imageView);
        }
        ViewFlipper.setFlipInterval(5000);
        ViewFlipper.setAutoStart(true);
        Animation animationInRight = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_right);
        Animation animationOutRight = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_right);
        ViewFlipper.setInAnimation(animationInRight);
        ViewFlipper.setOutAnimation(animationOutRight);
    }

    String masp ="";

    class ThongTinChiTietSP extends AsyncTask<String,Void, Integer>
    {
        @Override
        protected void onPreExecute() {
            DialogLoading.LoadingGoogle(true,progressBarExplore);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer chiTietSanPhams) {
            DialogLoading.LoadingGoogle(false,progressBarExplore);
            Intent intent = new Intent(getContext(), ChonSanPhamActivity.class);
            intent.putExtra("masp",masp);
            intent.putExtra("soluong",chiTietSanPhams);
            intent.putExtra("makh",MaKhachHang);
            startActivityForResult(intent,REQUEST_CODE);
            super.onPostExecute(chiTietSanPhams);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Integer chiTietSanPhams) {
            super.onCancelled(chiTietSanPhams);
        }

        @Override
        protected Integer doInBackground(String... strings) {
            List<ChiTietSanPham> ds = new ArrayList<>();
            String params = "masp="+strings[0];
            try {
                connect connect = new connect("ds_ChiTiet");
                NodeList nodeList = connect.getDataParameter(params,"ChiTietSanPham");
                for (int i=0;i<nodeList.getLength();i++) {
                    Element element = (Element) nodeList.item(i);
                    String size = element.getElementsByTagName("Size").item(0).getTextContent();
                    String gia = element.getElementsByTagName("DonGia").item(0).getTextContent();
                    ChiTietSanPham chiTietSanPham = new ChiTietSanPham(strings[0], size, Integer.parseInt(gia));
                    ds.add(chiTietSanPham);
                }
                return ds.size();
            }
            catch (Exception e)
            {
                return 0;
            }
        }
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
    @Override
    public void onResume() {
        int number = Integer.parseInt(readData());
        badge.setNumber(number);
        super.onResume();
    }
}
