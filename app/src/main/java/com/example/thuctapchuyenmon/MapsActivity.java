package com.example.thuctapchuyenmon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.database.connect;
import com.example.model.DataParser;
import com.example.model.DialogLoading;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleMap mMap;
    EditText edtAddress,edtNguoiNhan,edtSdtNhan;
    Button btnMyOrder;
    String mahd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        addViews();
        String apiKey="AIzaSyDNI_ZWPqvdS6r6gPVO50I4TlYkfkZdXh8";
        if(!Places.isInitialized())
        {
            Places.initialize(getApplicationContext(),apiKey);
        }
        final AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.e("address", place.getAddress() );
                Log.e("lat_lng", place.getLatLng().latitude+"-"+place.getLatLng().longitude );

                TimKiemDiaChi(place.getAddress());
                edtAddress.setText(place.getAddress());
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                ViTriHienTai();
                addEvents();
            }
        });
    }
    private void addViews() {
        edtAddress = findViewById(R.id.edtAddress);
        edtNguoiNhan = findViewById(R.id.edtNguoiNhan);
        edtSdtNhan = findViewById(R.id.edtsdtNhan);
        btnMyOrder = findViewById(R.id.btnMyOrder);
        Intent intent = getIntent();
        mahd = intent.getStringExtra("mahd");
        Log.e("mahoadon", mahd );
    }

    private void addEvents() {

//        edtSearchPlace.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    TimKiemDiaChi(edtSearchPlace.getText().toString());
//                }
//                return true;
//            }
//        });
//        edtSearchPlace.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(s.length()==0)
//                {
//                    mMap.clear();
//                    ViTriHienTai();
//                }
//            }
//        });

        btnMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("btn", "onClick: " );
                UpdateHoaDon update = new UpdateHoaDon();
                update.execute(mahd);
            }
        });
    }

    public class UpdateHoaDon extends AsyncTask<String,Void,Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean)
            {
                Toasty.success(MapsActivity.this,"Order thành công!").show();
                finish();
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
            String params = "mahd="+mahd+"&sdt="+edtSdtNhan.getText()+"&nguoinhan="+edtNguoiNhan.getText()+"&diachi="+edtAddress.getText();
            try {
                connect connect = new connect("updateHoaDon");
                NodeList nodeList = connect.getDataParameter(params, "boolean");
                String kiemtra = nodeList.item(0).getTextContent();
                boolean b = Boolean.parseBoolean(kiemtra);
                return b;
            }
            catch (Exception e)
            {
                return false;
            }

        }
    }
    private void ViTriHienTai() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (lastLocation != null)
        {
            LatLng latLng = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 15));
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
            String diachi = lastLocation.getLatitude()+","+lastLocation.getLongitude();
            ThongTinViTri(diachi);
        }
    }

    public void TimKiemDiaChi(String diachi)
    {
        mMap.clear();
        String point = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+diachi+"&key=AIzaSyDNI_ZWPqvdS6r6gPVO50I4TlYkfkZdXh8";
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, point, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String results =jsonObject.getString("results");
                    JSONArray array = new JSONArray(results);

                    for (int i=0;i<array.length();i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        Log.e("response",object.getString("formatted_address"));

                        edtAddress.setText(object.getString("formatted_address"));
                        String object1 = object.getString("geometry");
                        JSONObject object2 = new JSONObject(object1);

                        String location = object2.getString("location");

                        JSONObject object3 = new JSONObject(location);
                        String lat = object3.getString("lat");
                        String lng = object3.getString("lng");

                        LatLng latLng = new LatLng(Float.parseFloat(lat),Float.parseFloat(lng));
                        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("lỗi",error.getMessage() );
            }
        }
        );
        requestQueue.add(stringRequest);
    }
    public void ThongTinViTri(String diachi)
    {
        String point = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+diachi+"&key=AIzaSyDNI_ZWPqvdS6r6gPVO50I4TlYkfkZdXh8";
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, point, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String results =jsonObject.getString("results");
                    JSONArray array = new JSONArray(results);

                    for (int i=0;i<array.length();i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        edtAddress.setText(object.getString("formatted_address"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("lỗi",error.getMessage() );
            }
        }
        );
        requestQueue.add(stringRequest);
    }
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
