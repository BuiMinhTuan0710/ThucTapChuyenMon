package com.example.model;

import java.io.Serializable;

public class HoaDon implements Serializable {
    String mahd;
    String NguoiNhan;
    String sdt;
    String diachi;
    String trangthai;
    public HoaDon() {
    }

    public HoaDon(String mahd, String nguoiNhan, String sdt, String diachi, String trangthai) {
        this.mahd = mahd;
        NguoiNhan = nguoiNhan;
        this.sdt = sdt;
        this.diachi = diachi;
        this.trangthai = trangthai;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getMahd() {
        return mahd;
    }

    public void setMahd(String mahd) {
        this.mahd = mahd;
    }

    public String getNguoiNhan() {
        return NguoiNhan;
    }

    public void setNguoiNhan(String nguoiNhan) {
        NguoiNhan = nguoiNhan;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }
}
