package com.example.model;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.adapter.HoaDonAdapter;
import com.example.database.connect;
import com.example.thuctapchuyenmon.ChiTietHoaDonActivity;
import com.example.thuctapchuyenmon.R;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;

public class MyOrderFlagment extends Fragment {
    RecyclerView recyclerViewMyOrder;
    List<HoaDon>ds_HoaDon;
    HoaDonAdapter hoaDonAdapter;
    public String MaKhachHang;
    GoogleProgressBar progressOrder;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_myorder, container, false);
        addViews(view);
        addEvents();
        return view;
    }

    private void addEvents() {
        recyclerViewMyOrder.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerViewMyOrder ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        final TextView txtMaHoaDon = view.findViewById(R.id.txtMaHD);
                        Intent intent = new Intent(getContext(), ChiTietHoaDonActivity.class);
                        intent.putExtra("mahd",txtMaHoaDon.getText().toString());
                        startActivity(intent);
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    private void addViews(View view) {
        ds_HoaDon = new ArrayList<>();
        progressOrder = view.findViewById(R.id.progressOrder);
        recyclerViewMyOrder = view.findViewById(R.id.recyclerMyorder);
        recyclerViewMyOrder.setLayoutManager(new GridLayoutManager(getActivity(),1));
        hoaDonAdapter = new HoaDonAdapter(ds_HoaDon,R.layout.custom_item_myorder,getActivity());
        DanhSachHoaDon danhSachHoaDon = new DanhSachHoaDon();
        danhSachHoaDon.execute();
    }
    class DanhSachHoaDon extends AsyncTask<Void,Void,List<HoaDon>>
    {
        @Override
        protected void onPreExecute() {
            DialogLoading.LoadingGoogle(true,progressOrder);
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(List<HoaDon> hoaDons) {
            DialogLoading.LoadingGoogle(false,progressOrder);
            for (HoaDon item :
                    hoaDons) {
                ds_HoaDon.add(item);
            }
            recyclerViewMyOrder.setAdapter(hoaDonAdapter);
            hoaDonAdapter.notifyDataSetChanged();
            super.onPostExecute(hoaDons);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(List<HoaDon> hoaDons) {
            super.onCancelled(hoaDons);
        }

        @Override
        protected List<HoaDon> doInBackground(Void... voids) {
            List<HoaDon> ds_hoadon = new ArrayList<>();
            try {
                String paramsp = "makh="+MaKhachHang;
                connect connect = new connect("DanhSachHoaDon");
                NodeList nodeList = connect.getDataParameter(paramsp,"HoaDon");
                for (int i=0;i<nodeList.getLength();i++) {
                    Element element = (Element) nodeList.item(i);
                    String mahd = element.getElementsByTagName("MaHD").item(0).getTextContent();
                    String NguoiNhan = element.getElementsByTagName("nguoinhan").item(0).getTextContent();
                    String SDT = element.getElementsByTagName("sdt").item(0).getTextContent();
                    String diachi = element.getElementsByTagName("diachinhan").item(0).getTextContent();
                    String trangthai = element.getElementsByTagName("trangthai").item(0).getTextContent();
                    HoaDon hd = new HoaDon(mahd,NguoiNhan,SDT,diachi,trangthai);
                    ds_hoadon.add(hd);
                }
                return ds_hoadon;
            }
            catch (Exception e) {
                return null;
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}
