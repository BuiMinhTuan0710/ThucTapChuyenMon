package com.example.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.Cart;
import com.example.thuctapchuyenmon.CartActivity;
import com.example.thuctapchuyenmon.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    List<Cart> ds_Cart;
    private int resource;
    CartActivity context;
    int soluong;

    public CartAdapter(List<Cart> ds_Cart, int resource, CartActivity context) {
        this.ds_Cart = ds_Cart;
        this.resource = resource;
        this.context = context;
    }
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.ViewHolder holder, final int position) {
        holder.txtMaCart.setText(ds_Cart.get(position).getMasp());
        holder.txtSizeCart.setText("Size "+ds_Cart.get(position).getSize());
        holder.txtPriceCart.setText("   "+ds_Cart.get(position).getGia()+"đ");
        holder.txtNameCart.setText(ds_Cart.get(position).getTensp()+"");
        holder.numberCart.setText(ds_Cart.get(position).getSoluong()+"");
        Picasso.get().load(ds_Cart.get(position).getHinh()).into(holder.imgHinh);
        if(holder.txtSizeCart.getText().toString().equals("Size Không"))
        {
            holder.txtSizeCart.setVisibility(View.GONE);
        }

        soluong = ds_Cart.get(position).getSoluong();
        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soluong+=1;
                holder.numberCart.setText(soluong+"");
                String sanpham = ds_Cart.get(position).getMasp()+"-"+ds_Cart.get(position).getSize()+"-"+soluong;
                String sp = ds_Cart.get(position).getMasp()+"-"+ds_Cart.get(position).getSize()+"-"+ds_Cart.get(position).getSoluong();
                context.gioHang.QueryData("update Carts set SanPham = '"+sanpham+"' where SanPham ='"+sp+"'");
            }
        });
        holder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soluong-=1;
                if (soluong<=1)
                    soluong = 1;
                holder.numberCart.setText(soluong+"");
                String sanpham = ds_Cart.get(position).getMasp()+"-"+ds_Cart.get(position).getSize()+"-"+soluong;
                String sp = ds_Cart.get(position).getMasp()+"-"+ds_Cart.get(position).getSize()+"-"+ds_Cart.get(position).getSoluong();
                Log.e("SanPham",sanpham );
                context.gioHang.QueryData("update Carts set SanPham = '"+sanpham+"' where SanPham ='"+sp+"'");
            }
        });
        holder.txtDeleteFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sanpham = ds_Cart.get(position).getMasp()+"-"+ds_Cart.get(position).getSize()+"-"+soluong;
                context.openDiaLogDelete(sanpham);
                ds_Cart.remove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ds_Cart == null ? 0 : ds_Cart.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView imgHinh;
        TextView txtMaCart,txtNameCart,txtPriceCart,numberCart,txtSizeCart,increase,reduce,txtDeleteFood;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaCart = itemView.findViewById(R.id.txtMaCart);
            imgHinh = itemView.findViewById(R.id.imgCart);
            txtNameCart = itemView.findViewById(R.id.txtNameCart);
            txtPriceCart = itemView.findViewById(R.id.txtPriceCart);
            txtSizeCart = itemView.findViewById(R.id.txtSizeCart);
            numberCart = itemView.findViewById(R.id.numberCart);
            increase = itemView.findViewById(R.id.increase);
            reduce = itemView.findViewById(R.id.reduce);
            txtDeleteFood = itemView.findViewById(R.id.txtDeleteFood);
        }
    }
}
