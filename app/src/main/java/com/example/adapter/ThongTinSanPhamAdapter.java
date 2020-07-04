package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thuctapchuyenmon.MainBanHangActivity;
import com.example.thuctapchuyenmon.R;
import com.example.model.SanPham;

import java.util.List;

public class ThongTinSanPhamAdapter extends BaseAdapter {
    private MainBanHangActivity context;
    private int layout;
    private List<SanPham> SanPhamList;

    public ThongTinSanPhamAdapter(List<SanPham> SanPhamList, Context context,int layout) {
        this.SanPhamList = SanPhamList;
        this.context = (MainBanHangActivity) context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return SanPhamList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.imgHinh = view.findViewById(R.id.imgHinhSP);
            holder.txtGia = view.findViewById(R.id.txtGiaInfo);
            holder.txtTenSP = view.findViewById(R.id.txtTenInfo);
            holder.txtSoLuong = view.findViewById(R.id.txtSoLuong);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        final SanPham sp = SanPhamList.get(position);
        holder.txtTenSP.setText(sp.getTensp());
        holder.txtGia.setText(sp.getGiasp()+"đ");
        holder.imgHinh.setImageResource(sp.getHinhsp());
        holder.txtSoLuong.setVisibility(View.INVISIBLE);
        return view;
    }

    class ViewHolder  {
        ImageView imgHinh;
        TextView txtTenSP,txtGia,txtSoLuong;
    }
}
