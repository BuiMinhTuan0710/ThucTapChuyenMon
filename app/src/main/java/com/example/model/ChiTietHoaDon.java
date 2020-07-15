package com.example.model;

import java.io.Serializable;

public class ChiTietHoaDon implements Serializable {
    String tensp;
    String size;
    String hinhsp;
    int giasp;
    int soluong;

    public ChiTietHoaDon(String tensp, String size, String hinhsp, int giasp, int soluong) {
        this.tensp = tensp;
        this.size = size;
        this.hinhsp = hinhsp;
        this.giasp = giasp;
        this.soluong = soluong;
    }

    public ChiTietHoaDon() {
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
