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

    public boolean girisYap(String kullaniciAdi, String sifre) {
        // IK-01: Pasife alınmış hesaplar sisteme giriş yapamaz.
        if (!this.aktifMi) {
            System.out.println("HATA (IK-01): Hesap pasif durumda olduğu için giriş reddedildi! Kullanıcı: " + this.kullaniciAdi);
            return false;
        }

        // IK-01: Kullanıcı, geçerli kullanıcı adı ve şifreyle oturum açmadan işlem yapamaz.
        if (kullaniciAdi == null || sifre == null ||
                !this.kullaniciAdi.equals(kullaniciAdi) || !this.sifreHash.equals(sifre)) {
            System.out.println("HATA (IK-01): Hatalı kullanıcı adı veya şifre! Giriş reddedildi.");
            return false;
        }

        // IK-04: Sunucu zamanı (Europe/Istanbul) kullanılır.
        this.sonGirisTarihi = LocalDateTime.now(ZoneId.of("Europe/Istanbul"));
        this.oturumAcikMi = true;
        System.out.println("BAŞARILI (IK-01): Oturum açıldı -> " + this.ad + " " + this.soyad + " (" + this.rol + ")");
        return true;
    }

    public void cikisYap() {
        // IK-01: Oturum sonlandırma
        this.oturumAcikMi = false;
        System.out.println("BAŞARILI: Oturum kapatıldı -> " + this.ad + " " + this.soyad);
    }

    public boolean sifreGuncelle(String eskiSifre, String yeniSifre) {
        // IK-01 & IK-02: Oturum ve hesap aktiflik kontrolü
        if (!this.aktifMi || !this.oturumAcikMi) {
            System.out.println("HATA (IK-01): Şifre güncellemek için aktif hesap ve açık oturum gereklidir.");
            return false;
        }
        if (eskiSifre == null || !this.sifreHash.equals(eskiSifre)) {
            System.out.println("HATA (IK-02): Mevcut şifre hatalı! Şifre güncellenemedi.");
            return false;
        }
        if (yeniSifre == null || yeniSifre.trim().length() < 4) {
            System.out.println("HATA: Yeni şifre en az 4 karakter olmalıdır.");
            return false;
        }
        this.sifreHash = yeniSifre;
        System.out.println("BAŞARILI (IK-02): Şifre başarıyla güncellendi -> " + this.kullaniciAdi);
        return true;
    }

    public String profilBilgisiGetir() {
        // IK-02: Kullanıcı kendi profil bilgilerini görüntüler.
        if (!this.oturumAcikMi) {
            System.out.println("HATA (IK-01): Profil bilgisi görüntülemek için oturum açılmalıdır.");
            return null;
        }
        return "Kullanıcı ID: " + kullaniciId +
            " | Ad Soyad: " + ad + " " + soyad +
            " | Kullanıcı Adı: " + kullaniciAdi +
            " | Rol: " + rol +
            " | Aktif: " + (aktifMi ? "Evet" : "Hayır");
    }
}