package com.example.madencigozlugu.ui.dashboard;

public class DegerClass {

    Float temizHava,metan,butan,propan,co;
    String tarih;
    int userid,did;

    public DegerClass(int did, Float metan, Float butan, Float propan,  Float temizHava, Float co,   int userid, String tarih) {
        this.temizHava = temizHava;
        this.metan = metan;
        this.butan = butan;
        this.propan = propan;
        this.co = co;
        this.tarih = tarih;
        this.userid = userid;
        this.did = did;
    }
    public DegerClass( ) {

    }

    public Float getTemizHava() {
        return temizHava;
    }

    public void setTemizHava(Float temizHava) {
        this.temizHava = temizHava;
    }

    public Float getMetan() {
        return metan;
    }

    public void setMetan(Float metan) {
        this.metan = metan;
    }

    public Float getButan() {
        return butan;
    }

    public void setButan(Float butan) {
        this.butan = butan;
    }

    public Float getPropan() {
        return propan;
    }

    public void setPropan(Float propan) {
        this.propan = propan;
    }

    public Float getCo() {
        return co;
    }

    public void setCo(Float co) {
        this.co = co;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }
}
