// Youssef AYOUBI - 230404906
package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Departman {
    private int departmanId;
    private String departmanAdi;
    private int yoneticiId;
    private boolean aktifMi;
    private LocalDate olusturmaTarihi;
    private List<Calisan> calisanlar;
    private List<Gorev> gorevler;

    public Departman() {
        this.calisanlar = new ArrayList<>();
        this.gorevler = new ArrayList<>();
    }

    public Departman(int departmanId, String departmanAdi, int yoneticiId, boolean aktifMi, LocalDate olusturmaTarihi) {
        this.departmanId = departmanId;
        this.departmanAdi = departmanAdi;
        this.yoneticiId = yoneticiId;
        this.aktifMi = aktifMi;
        this.olusturmaTarihi = olusturmaTarihi;
        this.calisanlar = new ArrayList<>();
        this.gorevler = new ArrayList<>();
    }

    public int getDepartmanId() {
        return departmanId;
    }

    public void setDepartmanId(int departmanId) {
        this.departmanId = departmanId;
    }

    public String getDepartmanAdi() {
        return departmanAdi;
    }

    public void setDepartmanAdi(String departmanAdi) {
        this.departmanAdi = departmanAdi;
    }

    public int getYoneticiId() {
        return yoneticiId;
    }

    public void setYoneticiId(int yoneticiId) {
        this.yoneticiId = yoneticiId;
    }

    public boolean isAktifMi() {
        return aktifMi;
    }

    public void setAktifMi(boolean aktifMi) {
        this.aktifMi = aktifMi;
    }

    public LocalDate getOlusturmaTarihi() {
        return olusturmaTarihi;
    }

    public void setOlusturmaTarihi(LocalDate olusturmaTarihi) {
        this.olusturmaTarihi = olusturmaTarihi;
    }

    public List<Calisan> getCalisanlar() {
        return calisanlar;
    }

    public void setCalisanlar(List<Calisan> calisanlar) {
        this.calisanlar = calisanlar;
    }

    public List<Gorev> getGorevler() {
        return gorevler;
    }

    public void setGorevler(List<Gorev> gorevler) {
        this.gorevler = gorevler;
    }

    public void calisanEkle(Calisan calisan) {
        // TODO: Implement business logic
    }

    public void calisanCikar(int calisanId) {
        // TODO: Implement business logic
    }

    public List<Calisan> aktifCalisanlariGetir() {
        // TODO: Implement business logic
        return new ArrayList<>();
    }

    public List<Gorev> aktifGorevleriGetir() {
        // TODO: Implement business logic
        return new ArrayList<>();
    }
}
