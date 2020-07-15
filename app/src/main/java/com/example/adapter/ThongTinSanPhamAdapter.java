package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thuctapchuyenmon.ChonSanPhamActivity;
import com.example.thuctapchuyenmon.MainBanHangActivity;
import com.example.thuctapchuyenmon.R;
import com.example.model.SanPham;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ThongTinSanPhamAdapter extends RecyclerView.Adapter<ThongTinSanPhamAdapter.ViewHolder> {
    Context context;
    private int resource;
    private List<SanPham> SanPhamList;

    public ThongTinSanPhamAdapter(List<SanPham> SanPhamList, int resource, Context context) {
        this.SanPhamList = SanPhamList;
        this.resource = resource;
        this.context = context;
    }


    @NonNull
    @Override
    public ThongTinSanPhamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ThongTinSanPhamAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtMaSP.setText(SanPhamList.get(position).getMasp());
        holder.txtTenSP.setText(SanPhamList.get(position).getTensp());
        holder.txtGia.setText("  "+SanPhamList.get(position).getGiasp()+"đ");
        Picasso.get().load(SanPhamList.get(position).getHinhsp()).into(holder.imgHinh);
        holder.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChonSanPhamActivity.class);
                intent.putExtra("sanpham",SanPhamList.get(position));
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return SanPhamList.size();
    }

    @Override
    public int getItemCount()
    {
        return SanPhamList == null ? 0 : SanPhamList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView imgHinh;
        Button btnOrder;
        TextView txtTenSP,txtGia,txtMaSP;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinh = itemView.findViewById(R.id.imgFoodNew);
            txtGia = itemView.findViewById(R.id.txtGiaSanPham);
            txtTenSP = itemView.findViewById(R.id.txtNameFoodNew);
            txtMaSP = itemView.findViewById(R.id.txtMaSPFood);
            btnOrder = itemView.findViewById(R.id.btnOrder);
        }
    }
}
