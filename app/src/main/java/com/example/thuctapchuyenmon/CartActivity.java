package com.example.thuctapchuyenmon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.adapter.CartAdapter;
import com.example.database.GioHang;
import com.example.database.connect;
import com.example.model.Cart;
import com.example.model.ChiTietSanPham;
import com.example.model.ExploreFragment;
import com.example.model.RecyclerItemClickListener;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private Intent intent;
    private RecyclerView recyclerCart;
    public ArrayList<String>ds_SanPham;
    public GioHang gioHang;
    public static final int REQUEST_CODE=116;
    String simpleFileName;
    String databaseName;
    String makh;
    CartAdapter cartAdapter;
    Button btnOrderNow;
    public TextView txtTongTienCart;
    List<Cart>ds_Cart = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        addViews();
        addEvents();
    }

    private void addEvents() {
        Cursor cursor = gioHang.getData("select * from Carts");
        int soluong = 0;
        String SanPham;
        while (cursor.moveToNext())
        {
            SanPham=cursor.getString(1);
            String[] sp = SanPham.split("-");
            soluong+= Integer.parseInt(sp[2]);
        }
        outPut(soluong+"");
    }

    private void prepareDB() {
        gioHang = new GioHang(this,databaseName,null,1);
        gioHang.QueryData("CREATE TABLE IF NOT EXISTS Carts(Id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "SanPham varchar(200))");
    }
    public  void openDiaLogDelete(final String name)
    {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        builder.setTitle("Confirm delete Carts");
        builder.setMessage("Are you sure you want to delete this Carts ?");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                gioHang.QueryData("delete from Carts where SanPham ='"+name+"'");
                dialog.dismiss();
                reset();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    public void reset()
    {
        Cursor cursor = gioHang.getData("select * from Carts");
        ds_SanPham.clear();
        String SanPham;
        while (cursor.moveToNext())
        {
            SanPham=cursor.getString(1);
            ds_SanPham.add(SanPham);

        }
        cartAdapter.notifyDataSetChanged();
    }
    private void data() {
        intent = getIntent();
        makh = intent.getStringExtra("makh");
        simpleFileName = makh.trim()+".txt";
        databaseName = makh.trim()+".sqlite";
        prepareDB();
        ChiTietSanPham chiTietSanPham = (ChiTietSanPham) intent.getSerializableExtra("ChiTietSP");
        if(chiTietSanPham!=null)
        {
            String sl = intent.getStringExtra("soluong");
            String item = chiTietSanPham.getMasp().trim()+"-"+chiTietSanPham.getSize().trim()+"-"+sl;
            String kt = chiTietSanPham.getMasp().trim()+"-"+chiTietSanPham.getSize().trim();
            if(KiemTraTonTai(kt))
                gioHang.QueryData("insert into Carts values(null,'"+item+"')");
            else
            {
                String querry = "select * from Carts where SanPham like '%"+kt+"%'";
                Cursor cursor = gioHang.getData(querry);
                while (cursor.moveToNext())
                {
                    int magio = cursor.getInt(0);
                    String sanpham = cursor.getString(1);
                    String[] sp = sanpham.split("-");
                    int soluong = Integer.parseInt(sp[2]);
                    soluong += Integer.parseInt(sl);
                    sanpham = sp[0]+"-"+sp[1]+"-"+soluong;
                    String qr = "update Carts set SanPham='"+sanpham+"' where id="+magio;
                    gioHang.QueryData(qr);
                }

            }
        }
        Cursor cursor = gioHang.getData("select * from Carts");
        ds_SanPham = new ArrayList<>();
        String SanPham;
        while (cursor.moveToNext())
        {
            SanPham=cursor.getString(1);
            ds_SanPham.add(SanPham);
        }
    }
    public boolean KiemTraTonTai(String item)
    {
        String querry = "select * from Carts where SanPham like '%"+item+"%'";
        Cursor cursor = gioHang.getData(querry);
        if(cursor.getCount()>0)
            return false;
        return true;
    }

    public void outPut(String s)
    {
        try {
            FileOutputStream out = openFileOutput(simpleFileName, MODE_PRIVATE);
            out.write((s).getBytes());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addViews() {
        data();
        btnOrderNow = findViewById(R.id.btnOrderCart);
        recyclerCart = findViewById(R.id.recyclerCart);
        txtTongTienCart = findViewById(R.id.txtTongTienCart);
        recyclerCart.setLayoutManager(new GridLayoutManager(this, 1));
        getGiaSanPham getGiaSanPham = new getGiaSanPham();
        getGiaSanPham.execute(ds_SanPham);
        btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMaHoaDon getMaHoaDon = new GetMaHoaDon();
                getMaHoaDon.execute(makh);
            }
        });


    }
    class GetMaHoaDon extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if(!s.equals(""))
            {
                gioHang.QueryData("delete from Carts");
                outPut("0");
                Intent intent = new Intent(CartActivity.this,MapsActivity.class);
                Log.e("s=", s );
                intent.putExtra("mahd",s);
                startActivityForResult(intent,REQUEST_CODE);
                finish();
            }
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
        protected String doInBackground(String... strings) {
            String params = "makh="+strings[0];
            try {
                connect connect = new connect("getMaHD");
                NodeList nodeList = connect.getDataParameter(params,"string");
                String MaHD = nodeList.item(0).getTextContent();

                for (String item:
                     ds_SanPham) {
                    String [] sanpham = item.split("-");
                    String masp = sanpham[0];
                    String size = sanpham[1];
                    int soluong = Integer.parseInt(sanpham[2]);

                    String paramsCT = "mahd="+MaHD+"&masp="+masp+"&soluong="+soluong+"&size="+size;
                    connect connectCT = new connect("insertCTHD");
                    NodeList nodeListCT = connectCT.getDataParameter(paramsCT,"boolean");

                   boolean kt = Boolean.parseBoolean(nodeListCT.item(0).getTextContent());
                   if(kt)
                   {
                       Log.e("thêm thành công",masp );
                   }
                }
                return MaHD;
            }
            catch (Exception e)
            {
                return "";
            }
        }
    }
    class getGiaSanPham extends AsyncTask<ArrayList<String>,Void,List<Cart>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Cart> carts) {
            cartAdapter = new CartAdapter(carts,R.layout.custom_item_order,CartActivity.this);
            recyclerCart.setAdapter(cartAdapter);
            Integer tongtien = 0;
            for (Cart item :
            carts) {
                tongtien +=item.getGia()*item.getSoluong();
            }
            txtTongTienCart.setText(tongtien+"đ");
            super.onPostExecute(carts);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(List<Cart> carts) {
            super.onCancelled(carts);
        }

        @Override
        protected List<Cart> doInBackground(ArrayList<String>... arrayLists) {
            List<Cart> ds_cart = new ArrayList<>();
            ArrayList<String>ds = arrayLists[0];
            for (String item:
                 ds) {
                String[] sanpham = item.split("-");
                String masp = sanpham[0];
                String size = sanpham[1];
                String soluong = sanpham[2];
                try {
                    String params = "masp="+masp+"&size="+size;
                    connect connect = new connect("giasanpham");
                    NodeList nodeList = connect.getDataParameter(params,"int");
                    String gia = nodeList.item(0).getTextContent();

                    String params1 = "masp="+masp;
                    connect connect1 = new connect("get_SanPham");
                    NodeList nodeList1 = connect1.getDataParameter(params1,"SanPham");
                    Element element = (Element) nodeList1.item(0);
                    String tensp = element.getElementsByTagName("TenSanPham").item(0).getTextContent();
                    String hinhanh = element.getElementsByTagName("hinhanh").item(0).getTextContent();
                    Cart cart = new Cart(masp,tensp,hinhanh,size,Integer.parseInt(gia),Integer.parseInt(soluong));
                    ds_cart.add(cart);
                }
                catch (Exception e)
                {
                    return null;
                }
            }
            return ds_cart;
        }
    }
    @Override
    public void onBackPressed() {
        Cursor cursor = gioHang.getData("select * from Carts");
        int soluong = 0;
        String SanPham;
        while (cursor.moveToNext())
        {
            SanPham=cursor.getString(1);
            String[] sp = SanPham.split("-");
            soluong+= Integer.parseInt(sp[2]);
        }
        outPut(soluong+"");
        super.onBackPressed();
    }
}