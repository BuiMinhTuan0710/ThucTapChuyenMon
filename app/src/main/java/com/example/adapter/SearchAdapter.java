package com.example.adapter;

import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.ChiTietSanPham;
import com.example.thuctapchuyenmon.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    List<ChiTietSanPham>ds_SanPhamSearch;
    private int resource;
    Activity context;

    public SearchAdapter(List<ChiTietSanPham> ds_SanPhamSearch, int resource, Activity context) {
        this.ds_SanPhamSearch = ds_SanPhamSearch;
        this.resource = resource;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChiTietSanPham ct = ds_SanPhamSearch.get(position);
        holder.txtMaSearch.setText(ct.getMasp());
        holder.txtGiaBanSearch.setText(ct.getGiathuc()+"đ");
        holder.txtNameSearch.setText(ct.getTensp());
        int giaBan = ct.getDongia();
        SpannableString spannableString = new SpannableString(giaBan+"đ");
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        char[]gia = (giaBan+"đ").toCharArray();
        spannableString.setSpan(strikethroughSpan,0,gia.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtGiaThucSearch.setText(spannableString);
        Picasso.get().load(ct.getHinhanh()).into(holder.imgHinhSearch);
        if(ct.getDongia()==ct.getGiathuc())
        {
            holder.txtTitleSearch.setVisibility(View.INVISIBLE);
            holder.txtGiaThucSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return ds_SanPhamSearch.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView imgHinhSearch;
        FrameLayout txtTitleSearch;
        TextView txtMaSearch,txtNameSearch,txtGiaBanSearch,txtGiaThucSearch;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSearch = itemView.findViewById(R.id.imgFoodSearch);
            txtMaSearch = itemView.findViewById(R.id.txtMaSanPhamSearch);
            txtGiaBanSearch = itemView.findViewById(R.id.txtGiaBanSearch);
            txtNameSearch = itemView.findViewById(R.id.txtNameFoodSearch);
            txtGiaThucSearch = itemView.findViewById(R.id.txtGiamGiaSearch);
            txtTitleSearch = itemView.findViewById(R.id.txtTitleMarSearch);
        }
    }
}
