package com.example.baith;

public class CauHoi {
    private String cauhoi;
    private String[] luachon;
    private String dapan;
    public CauHoi(String cauhoi, String[] luachon, String dapan)
    {
        this.cauhoi=cauhoi;
        this.dapan=dapan;
        this.luachon=new String[luachon.length];
        for (int i=0; i<luachon.length;i++)
            this.luachon[i]=luachon[i];
    }
    public String getCauhoi(){return cauhoi;}
    public String getDapan(){return dapan;}
    public String[] getLuachon(){return luachon;}
}
