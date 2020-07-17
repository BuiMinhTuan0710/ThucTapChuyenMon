package com.example.thuctapchuyenmon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.adapter.ChiTietSanPhamAdapter;
import com.example.database.connect;
import com.example.model.ChiTietHoaDon;
import com.example.model.DataParser;
import com.example.model.DialogLoading;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChiTietHoaDonActivity extends AppCompatActivity {

    RecyclerView recyclerMyorderCT;
    GoogleProgressBar progressOrderCT;
    ChiTietSanPhamAdapter adapter;
    List<ChiTietHoaDon>ds_HoaDon;
    LinearLayout bodyChiTiet;
    FrameLayout frameLayout;
    private GoogleMap mMap;
    LatLng origin,dest;
    Button btnHuyOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_hoa_don);
        addViews();
        addEvents();
    }

    private void addEvents() {
        Intent intent = getIntent();
        String mahd = intent.getStringExtra("mahd");
        DanhSachSanPham danhSachSanPham = new DanhSachSanPham();
        danhSachSanPham.execute("HD06");

        GetTrangThai getTrangThai = new GetTrangThai();
        getTrangThai.execute("HD06");

        GetViTriGiaoHang getViTriGiaoHang = new GetViTriGiaoHang();
        getViTriGiaoHang.execute("HD06");

        GetViTriNhanVien getViTriNhanVien = new GetViTriNhanVien();
        getViTriNhanVien.execute("HD06");
    }

    private void addViews() {
        recyclerMyorderCT = findViewById(R.id.recyclerMyorderCT);
        progressOrderCT = findViewById(R.id.progressOrderCT);
        ds_HoaDon = new ArrayList<>();
        bodyChiTiet = findViewById(R.id.bodyChiTiet);
        frameLayout = findViewById(R.id.mapThongTin);
        adapter = new ChiTietSanPhamAdapter(ds_HoaDon,R.layout.custom_item_lvorder,this);
        recyclerMyorderCT.setLayoutManager(new GridLayoutManager(this,1));

        btnHuyOrder = findViewById(R.id.btnHuyOrder);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapThongTin);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
            }
        });
    }
    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+"&key=AIzaSyDNI_ZWPqvdS6r6gPVO50I4TlYkfkZdXh8";
        Log.e("url",url);
        return url;
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
                Log.d("onPostExecute","onPostExecute lineoptions decoded");
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }
    class GetViTriNhanVien extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            if(!s.isEmpty())
            {
                String[] vitri = s.split(",");
                float lat = Float.parseFloat(vitri[0]);
                float lng = Float.parseFloat(vitri[1]);
                origin = new LatLng(lat,lng);
                // Getting URL to the Google Directions API
                String url = getUrl(origin, dest);
                Log.d("onMapClick", url.toString());
                FetchUrl FetchUrl = new FetchUrl();
                // Start downloading json data from Google Directions API
                FetchUrl.execute(url);
                ViTriGiaoHang(origin);
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
            try {
                String params = "mahd="+strings[0];
                connect connect = new connect("getViTriNhanVien");
                NodeList nodeList = connect.getDataParameter(params,"string");
                return nodeList.item(0).getTextContent();
            }
            catch (Exception e)
            {
                return "";
            }
        }
    }
    class GetViTriGiaoHang extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if(!s.isEmpty())
            {
                String[] vitri = s.split(",");
                float lat = Float.parseFloat(vitri[0]);
                float lng = Float.parseFloat(vitri[1]);
                dest = new LatLng(lat,lng);
                ViTriGiaoHang(dest);
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
            try {
                String params = "mahd="+strings[0];
                connect connect = new connect("getViTriGiaoHang");
                NodeList nodeList = connect.getDataParameter(params,"string");
                return nodeList.item(0).getTextContent();
            }
            catch (Exception e)
            {
                return "";
            }
        }
    }

    private void ViTriGiaoHang(LatLng latLng) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
    }
    class GetTrangThai extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("Đang giao"))
            {
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                bodyChiTiet.setLayoutParams(layoutParams);
                frameLayout.setVisibility(View.VISIBLE);
                btnHuyOrder.setVisibility(View.INVISIBLE);
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
            try {
                String params = "mahd="+strings[0];
                connect connect = new connect("getTrangThaiHoaDon");
                NodeList nodeList = connect.getDataParameter(params,"string");
                return nodeList.item(0).getTextContent();
            }
            catch (Exception e)
            {
                return "";
            }
        }
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