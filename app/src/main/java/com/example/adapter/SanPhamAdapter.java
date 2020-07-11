package com.example.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database.connect;
import com.example.model.SanPham;
import com.example.thuctapchuyenmon.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {
    List<SanPham> ds_SanPham;
    private int resource;
    Context context;

    public SanPhamAdapter(List<SanPham> ds_SanPham,int resource, Context context) {
        this.ds_SanPham = ds_SanPham;
        this.resource = resource;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtMaSP.setText(ds_SanPham.get(position).getMasp());
       String image = ds_SanPham.get(position).getHinhsp()+"?type=large";
        Picasso.get().load(image).into(holder.imgHinh);
        holder.txtTenSP.setText(ds_SanPham.get(position).getTensp());
        int giathuc = (int) (ds_SanPham.get(position).getGiasp()*(100-ds_SanPham.get(position).getGiamgia())/100);
        holder.txtGia.setText(giathuc+"đ");
        int giaBan = ds_SanPham.get(position).getGiasp();
        SpannableString spannableString = new SpannableString(giaBan+"đ");
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        char[]gia = (giaBan+"đ").toCharArray();
        spannableString.setSpan(strikethroughSpan,0,gia.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtGiaThuc.setText(spannableString);
    }
    @Override
    public int getItemCount() {
        return ds_SanPham == null ? 0 : ds_SanPham.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView imgHinh;
        TextView txtMaSP,txtTenSP,txtGia,txtGiaThuc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaSP = itemView.findViewById(R.id.txtMaSPSale);
            imgHinh = itemView.findViewById(R.id.imgFoodSale);
            txtTenSP = itemView.findViewById(R.id.txtNameFoodSale);
            txtGia = itemView.findViewById(R.id.txtGiaBanSale);
            txtGiaThuc = itemView.findViewById(R.id.txtGiamGiaSale);
        }
    }
}
