package com.example.daandroid2;

import java.util.Arrays;

public class book {
   public int ma,gia,sotrang;
   public stylebook maloai;
    public String ten;
   public byte[] hinhanh;

//    public book(int id, String ten, int maloai, byte[] anh) {
//    }

    public book(int ma, String ten ,int gia, stylebook maloai, byte[] hinhanh, int sotrang) {
        this.ma = ma;
        this.gia = gia;
        this.maloai = maloai;
        this.ten = ten;

        this.hinhanh = hinhanh;
        this.sotrang = sotrang;
    }

    public int getMa() {
        return ma;
    }

    public void setMa(int ma) {
        this.ma = ma;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getSotrang() {
        return sotrang;
    }

    public void setSotrang(int sotrang) {
        this.sotrang = sotrang;
    }

    public stylebook getMaloai() {
        return maloai;
    }

    public void setMaloai(stylebook maloai) {
        this.maloai = maloai;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public byte[] getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(byte[] hinhanh) {
        this.hinhanh = hinhanh;
    }

    public book(int ma, String ten, stylebook maloai, byte[] hinhanh) {
        this.ma = ma;
        this.ten = ten;
        this.maloai = maloai;
        this.hinhanh = hinhanh;
    }



    @Override
    public String toString() {
        return
                "ma:" + ma +"\n"+
                "ten='" + ten +"\n"+
                "maloai=" + maloai+"\n"+
                 "hinhanh:" + Arrays.toString(hinhanh) ;
    }
}
