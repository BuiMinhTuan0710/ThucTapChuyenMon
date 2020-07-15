package com.example.model;

import java.io.Serializable;

public class Cart implements Serializable {
    String masp;
    String tensp;
    String size;
    String hinh;
    int gia;
    int soluong;

    public Cart() {
    }

    public Cart(String masp,String tensp,String hinh, String size, int gia, int soluong) {
        this.masp = masp;
        this.tensp = tensp;
        this.hinh = hinh;
        this.size = size;
        this.gia = gia;
        this.soluong = soluong;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
