package com.example.model;

import java.io.Serializable;

public class GiamGia implements Serializable {
    private String maGG;
    private String masp;
    private int giamgia;

    public GiamGia(String maGG, String masp, int giamgia) {
        this.maGG = maGG;
        this.masp = masp;
        this.giamgia = giamgia;
    }

    public GiamGia() {
    }

    public String getMaGG() {
        return maGG;
    }

    public void setMaGG(String maGG) {
        this.maGG = maGG;
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public int getGiamgia() {
        return giamgia;
    }

    public void setGiamgia(int giamgia) {
        this.giamgia = giamgia;
    }
}
