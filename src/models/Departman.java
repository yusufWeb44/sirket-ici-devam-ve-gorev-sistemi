// Youssef AYOUBI - 230404906
package models;

import enums.GorevDurumu;
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
        if (calisan == null) {
            System.out.println("Uyari: Gecersiz calisan departmana eklenemez.");
            return;
        }
        // ayni calisani iki kez eklememek icin kontrol
        for (Calisan mevcut : this.calisanlar) {
            if (mevcut.getKullaniciId() == calisan.getKullaniciId()) {
                System.out.println("Bilgi: Calisan (ID: " + calisan.getKullaniciId() + ") zaten " + this.departmanAdi + " departmaninda kayitli.");
                return;
            }
        }
        this.calisanlar.add(calisan);
        calisan.setDepartmanId(this.departmanId);
        calisan.setDepartman(this);
        System.out.println("Bilgi: " + calisan.getAd() + " " + calisan.getSoyad() + " -> " + this.departmanAdi + " departmanina eklendi.");
    }

    public void calisanCikar(int calisanId) {
        boolean silindi = this.calisanlar.removeIf(c -> c.getKullaniciId() == calisanId);
        if (silindi) {
            System.out.println("Bilgi: Calisan (ID: " + calisanId + ") " + this.departmanAdi + " departmanindan cikarildi.");
        } else {
            System.out.println("Uyari: Calisan (ID: " + calisanId + ") bu departmanda bulunamadi.");
        }
    }

    public List<Calisan> aktifCalisanlariGetir() {
        // IK-11: sadece departmandaki aktif calisanlar dondurulur
        List<Calisan> aktifListe = new ArrayList<>();
        for (Calisan c : this.calisanlar) {
            if (c.isAktifMi()) {
                aktifListe.add(c);
            }
        }
        return aktifListe;
    }

    public List<Gorev> aktifGorevleriGetir() {
        List<Gorev> aktifGorevListe = new ArrayList<>();
        for (Gorev g : this.gorevler) {
            if (g.getDurum() != GorevDurumu.TAMAMLANDI) {
                aktifGorevListe.add(g);
            }
        }
        return aktifGorevListe;
    }
}
