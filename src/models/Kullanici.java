// Youssef AYOUBI - 230404906
package models;

import enums.Rol;
import java.time.LocalDateTime;

public abstract class Kullanici {
    protected int kullaniciId;
    protected String kullaniciAdi;
    protected String sifreHash;
    protected String ad;
    protected String soyad;
    protected String eposta;
    protected String telefon;
    protected Rol rol;
    protected boolean aktifMi;
    protected LocalDateTime olusturmaTarihi;
    protected LocalDateTime sonGirisTarihi;

    public Kullanici() {
    }

    public Kullanici(int kullaniciId, String kullaniciAdi, String sifreHash, String ad, String soyad,
                     String eposta, String telefon, Rol rol, boolean aktifMi,
                     LocalDateTime olusturmaTarihi, LocalDateTime sonGirisTarihi) {
        this.kullaniciId = kullaniciId;
        this.kullaniciAdi = kullaniciAdi;
        this.sifreHash = sifreHash;
        this.ad = ad;
        this.soyad = soyad;
        this.eposta = eposta;
        this.telefon = telefon;
        this.rol = rol;
        this.aktifMi = aktifMi;
        this.olusturmaTarihi = olusturmaTarihi;
        this.sonGirisTarihi = sonGirisTarihi;
    }

    public int getKullaniciId() {
        return kullaniciId;
    }

    public void setKullaniciId(int kullaniciId) {
        this.kullaniciId = kullaniciId;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getSifreHash() {
        return sifreHash;
    }

    public void setSifreHash(String sifreHash) {
        this.sifreHash = sifreHash;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getEposta() {
        return eposta;
    }

    public void setEposta(String eposta) {
        this.eposta = eposta;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public boolean isAktifMi() {
        return aktifMi;
    }

    public void setAktifMi(boolean aktifMi) {
        this.aktifMi = aktifMi;
    }

    public LocalDateTime getOlusturmaTarihi() {
        return olusturmaTarihi;
    }

    public void setOlusturmaTarihi(LocalDateTime olusturmaTarihi) {
        this.olusturmaTarihi = olusturmaTarihi;
    }

    public LocalDateTime getSonGirisTarihi() {
        return sonGirisTarihi;
    }

    public void setSonGirisTarihi(LocalDateTime sonGirisTarihi) {
        this.sonGirisTarihi = sonGirisTarihi;
    }

    // IK-01
    public boolean girisYap(String kullaniciAdi, String sifre) {
        // TODO: Implement business logic
        return false;
    }

    // IK-01
    public void cikisYap() {
        // TODO: Implement business logic
    }

    // IK-02
    public boolean sifreGuncelle(String eskiSifre, String yeniSifre) {
        // TODO: Implement business logic
        return false;
    }

    // IK-02
    public String profilBilgisiGetir() {
        // TODO: Implement business logic
        return null;
    }
}
