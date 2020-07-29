package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.model.ChiTietHoaDon;
import com.example.thuctapchuyenmon.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ChiTietSanPhamAdapter extends RecyclerView.Adapter<ChiTietSanPhamAdapter.ViewHolder>{

    List<ChiTietHoaDon> ds_cthd;
    private int resource;
    Context context;

    public ChiTietSanPhamAdapter(List<ChiTietHoaDon> ds_cthd, int resource, Context context) {
        this.ds_cthd = ds_cthd;
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ChiTietSanPhamAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChiTietHoaDon ct = ds_cthd.get(position);
        holder.txtSizeLV.setText("Size "+ct.getSize());
        if(holder.txtSizeLV.getText().toString().trim().equals("Size Không"))
        {
            holder.txtSizeLV.setVisibility(View.GONE);
        }
        holder.txtNameLV.setText(ct.getTensp());
        holder.txtGiaLV.setText("   "+ct.getGiasp()+"đ");
        holder.txtSoLuongLV.setText("x "+ct.getSoluong());
    }
    @Override
    public int getItemCount() {
        return ds_cthd.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtNameLV,txtSizeLV,txtGiaLV,txtSoLuongLV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameLV = itemView.findViewById(R.id.txtNameItemLV);
            txtSizeLV = itemView.findViewById(R.id.txtSizeItemLV);
            txtGiaLV = itemView.findViewById(R.id.txtGiaItemLV);
            txtSoLuongLV = itemView.findViewById(R.id.txtSoLuongItemLV);
        }
    }
}
