package com.example.model;

import java.io.Serializable;

public class ChiTietSanPham implements Serializable {
    String masp;
    String tensp;
    String size;
    String hinhanh;
    int dongia;
    int giathuc;

    public ChiTietSanPham(String masp, String tensp, String size, String hinhanh, int dongia, int giathuc) {
        this.masp = masp;
        this.tensp = tensp;
        this.size = size;
        this.hinhanh = hinhanh;
        this.dongia = dongia;
        this.giathuc = giathuc;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public int getGiathuc() {
        return giathuc;
    }

    public void setGiathuc(int giathuc) {
        this.giathuc = giathuc;
    }

    public ChiTietSanPham() {
    }

    public ChiTietSanPham(String masp, String size, int dongia) {
        this.masp = masp;
        this.size = size;
        this.dongia = dongia;
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

    public int getDongia() {
        return dongia;
    }

    public void setDongia(int dongia) {
        this.dongia = dongia;
    }
}
