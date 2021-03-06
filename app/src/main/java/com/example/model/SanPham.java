package com.example.model;

import java.io.Serializable;

public class SanPham implements Serializable {
    String masp;
    String tensp;
    String hinhsp;
    int giasp;
    int giamgia;

    public SanPham(String masp,String tensp, String hinhsp, int giasp) {
        this.masp = masp;
        this.tensp = tensp;
        this.hinhsp = hinhsp;
        this.giasp = giasp;
    }
    public SanPham(String masp,String tensp, String hinhsp, int giasp,int giamgia) {
        this.masp = masp;
        this.tensp = tensp;
        this.hinhsp = hinhsp;
        this.giasp = giasp;
        this.giamgia = giamgia;
    }

    public int getGiamgia() {
        return giamgia;
    }

    public void setGiamgia(int giamgia) {
        this.giamgia = giamgia;
    }

    public SanPham() {
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getHinhsp() {
        return hinhsp;
    }

    public void setHinhsp(String hinhsp) {
        this.hinhsp = hinhsp;
    }

    public int getGiasp() {
        return giasp;
    }

    public void setGiasp(int giasp) {
        this.giasp = giasp;
    }
}
