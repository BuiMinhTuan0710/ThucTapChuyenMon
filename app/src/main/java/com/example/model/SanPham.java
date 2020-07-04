package com.example.model;

import java.io.Serializable;

public class SanPham implements Serializable {
    String tensp;
    int hinhsp;
    int giasp;

    public SanPham(String tensp, int hinhsp, int giasp) {
        this.tensp = tensp;
        this.hinhsp = hinhsp;
        this.giasp = giasp;
    }

    public SanPham() {
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public int getHinhsp() {
        return hinhsp;
    }

    public void setHinhsp(int hinhsp) {
        this.hinhsp = hinhsp;
    }

    public int getGiasp() {
        return giasp;
    }

    public void setGiasp(int giasp) {
        this.giasp = giasp;
    }
}
