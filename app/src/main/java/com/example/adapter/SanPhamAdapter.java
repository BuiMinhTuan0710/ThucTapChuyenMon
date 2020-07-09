package com.example.adapter;

import android.content.Context;
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
import com.example.model.SanPham;
import com.example.thuctapchuyenmon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {
    ArrayList<SanPham> ds_SanPham;
    Context context;

    public SanPhamAdapter(ArrayList<SanPham> ds_SanPham, Context context) {
        this.ds_SanPham = ds_SanPham;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View customView = inflater.inflate(R.layout.custom_itemsphot,null);
        return new ViewHolder(customView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtMaSP.setText(ds_SanPham.get(position).getMasp());
       // holder.imgHinh.setImageResource(ds_SanPham.get(position).getHinhsp());
        String image = ds_SanPham.get(position).getHinhsp()+"?type=large";
        Log.e("URL ",image );
        Picasso.get().load(image).into(holder.imgHinh);
        holder.txtTenSP.setText(ds_SanPham.get(position).getTensp());
        holder.txtGia.setText(ds_SanPham.get(position).getGiasp()+"đ");
        int giathuc = (int) (ds_SanPham.get(position).getGiasp()*1.5);
        SpannableString spannableString = new SpannableString(giathuc+"đ");
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        char[]gia = (giathuc+"đ").toCharArray();
        spannableString.setSpan(strikethroughSpan,0,gia.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtGiaThuc.setText(spannableString);
    }


    @Override
    public int getItemCount() {
        return ds_SanPham.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgHinh;
        TextView txtMaSP,txtTenSP,txtGia,txtGiaThuc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaSP = itemView.findViewById(R.id.txtMaSP);
            imgHinh = itemView.findViewById(R.id.imgHinhAnh);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtGia = itemView.findViewById(R.id.txtGiaBan);
            txtGiaThuc = itemView.findViewById(R.id.txtGiaThuc);
        }
    }
}
