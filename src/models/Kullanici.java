// Youssef AYOUBI - 230404906
package models;

import enums.Rol;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
    protected boolean oturumAcikMi;

    public Kullanici() {
        this.oturumAcikMi = false;
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
        this.oturumAcikMi = false;
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

    public boolean isOturumAcikMi() {
        return oturumAcikMi;
    }

    public void setOturumAcikMi(boolean oturumAcikMi) {
        this.oturumAcikMi = oturumAcikMi;
    }

    // IK-01: kullanici adi, sifre ve aktiflik kontrolu
    public boolean girisYap(String kullaniciAdi, String sifre) {
        if (!this.aktifMi) {
            System.out.println("Uyari: Hesap pasif durumda oldugu icin giris yapilamadi -> " + this.kullaniciAdi);
            return false;
        }
        if (kullaniciAdi == null || sifre == null ||
                !this.kullaniciAdi.equals(kullaniciAdi) || !this.sifreHash.equals(sifre)) {
            System.out.println("Uyari: Kullanici adi veya sifre hatali.");
            return false;
        }
        // IK-04: sunucu saati Europe/Istanbul
        this.sonGirisTarihi = LocalDateTime.now(ZoneId.of("Europe/Istanbul"));
        this.oturumAcikMi = true;
        System.out.println("Bilgi: Oturum acildi -> " + this.ad + " " + this.soyad + " (" + this.rol + ")");
        return true;
    }

    // IK-01: oturumu kapat
    public void cikisYap() {
        this.oturumAcikMi = false;
        System.out.println("Bilgi: Oturum kapatildi -> " + this.ad + " " + this.soyad);
    }

    // IK-01, IK-02: sifre degistirmek icin acik oturum ve dogru eski sifre gerekir
    public boolean sifreGuncelle(String eskiSifre, String yeniSifre) {
        if (!this.aktifMi || !this.oturumAcikMi) {
            System.out.println("Uyari: Sifre guncellemek icin once oturum acmalisiniz.");
            return false;
        }
        if (eskiSifre == null || !this.sifreHash.equals(eskiSifre)) {
            System.out.println("Uyari: Mevcut sifre yanlis.");
            return false;
        }
        if (yeniSifre == null || yeniSifre.trim().length() < 4) {
            System.out.println("Uyari: Yeni sifre en az 4 karakter olmali.");
            return false;
        }
        this.sifreHash = yeniSifre;
        System.out.println("Bilgi: Sifre guncellendi -> " + this.kullaniciAdi);
        return true;
    }

    // IK-02: sadece oturum aciksa kendi profilini gorebilir
    public String profilBilgisiGetir() {
        if (!this.oturumAcikMi) {
            System.out.println("Uyari: Profili gormek icin oturum acik olmali.");
            return null;
        }
        return "Kullanici ID: " + kullaniciId +
            " | Ad Soyad: " + ad + " " + soyad +
            " | Kullanici Adi: " + kullaniciAdi +
            " | Rol: " + rol +
            " | Aktif: " + (aktifMi ? "Evet" : "Hayir");
    }
}