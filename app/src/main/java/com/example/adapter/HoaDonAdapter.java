package com.example.adapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database.connect;
import com.example.model.ChiTietHoaDon;
import com.example.model.HoaDon;
import com.example.thuctapchuyenmon.R;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class HoaDonAdapter extends RecyclerView.Adapter<HoaDonAdapter.ViewHolder>{
    List<HoaDon> ds_HoaDon;
    private int resource;
    Activity context;
    public HoaDonAdapter(List<HoaDon> ds_HoaDon, int resource, Activity context) {
        this.ds_HoaDon = ds_HoaDon;
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new HoaDonAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HoaDon hd = ds_HoaDon.get(position);
        Log.e("HoaDon",hd.getMahd());
        holder.txtMaHD.setText(hd.getMahd());
        holder.txtNguoiNhan.setText(hd.getNguoiNhan());
        holder.txtSDT.setText(hd.getSdt());
        holder.txtTrangThai.setText(hd.getTrangthai());
        holder.txtDiaChi.setText(hd.getDiachi());
    }
    @Override
    public int getItemCount() {
        return ds_HoaDon.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtMaHD,txtNguoiNhan,txtSDT,txtTrangThai,txtDiaChi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaHD = itemView.findViewById(R.id.txtMaHD);
            txtNguoiNhan = itemView.findViewById(R.id.txtNguoiNhan);
            txtSDT = itemView.findViewById(R.id.txtSDT);
            txtTrangThai = itemView.findViewById(R.id.txtTrangThai);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChi);
        }
    }
}
