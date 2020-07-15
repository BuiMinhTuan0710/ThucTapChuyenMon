package com.example.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.ChiTietSanPham;
import com.example.thuctapchuyenmon.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllFoodAdapter  extends RecyclerView.Adapter<AllFoodAdapter.ViewHolder> {

    Context context;
    private int resource;
    private List<ChiTietSanPham> SanPhamList;


    public AllFoodAdapter(Context context, int resource, List<ChiTietSanPham> sanPhamList) {
        this.context = context;
        this.resource = resource;
        SanPhamList = sanPhamList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new AllFoodAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChiTietSanPham ct = SanPhamList.get(position);
        holder.txtMaSP.setText(ct.getMasp());
        holder.txtGia.setText(ct.getGiathuc()+"đ");
        holder.txtTenSP.setText(ct.getTensp());
        int giaBan = ct.getDongia();
        SpannableString spannableString = new SpannableString(giaBan+"đ");
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        char[]gia = (giaBan+"đ").toCharArray();
        spannableString.setSpan(strikethroughSpan,0,gia.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtGiaThuc.setText(spannableString);
        Picasso.get().load(ct.getHinhanh()).into(holder.imgHinh);
        if(ct.getDongia()==ct.getGiathuc())
        {
            holder.frameLayoutTitle.setVisibility(View.INVISIBLE);
            holder.txtGiaThuc.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return SanPhamList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        RoundedImageView imgHinh;
        TextView txtTenSP,txtGia,txtGiaThuc,txtMaSP,txtTitle;
        FrameLayout frameLayoutTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaSP = itemView.findViewById(R.id.txtMaSPSale);
            imgHinh = itemView.findViewById(R.id.imgFoodSale);
            txtTenSP = itemView.findViewById(R.id.txtNameFoodSale);
            txtGia = itemView.findViewById(R.id.txtGiaBanSale);
            txtGiaThuc = itemView.findViewById(R.id.txtGiamGiaSale);
            frameLayoutTitle = itemView.findViewById(R.id.frameAllFood);
            txtTitle = itemView.findViewById(R.id.txtTitleMar);
        }
    }
}
