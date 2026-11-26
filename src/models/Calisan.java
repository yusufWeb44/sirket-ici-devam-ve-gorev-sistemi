// Ortak Sınıf - Görev Dağılımı:
// - Youssef AYOUBI - 230404906 (Sınıf yapısı ve gorevDurumuGuncelle)
// - Ahmad Alamin - 230404933 (iseGirisYap, iseCikisYap, gunlukGirisKontrolu)
// - Adnan Derviş - 230404935 (izinTalebiOlustur, izinCakismasiVarMi, izinTalebiIptalEt)
// - Mohamad BIRAHJAKLI - 230404939 (devamDuzeltmeTalebiOlustur, kendiRaporunuGoruntule)
package models;

import enums.DuzeltmeTuru;
import enums.GorevDurumu;
import enums.IzinTuru;
import enums.Rol;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Calisan extends Kullanici {
    protected int departmanId;
    protected Departman departman;
    protected String unvan;
    protected double temelUcret;
    protected List<DevamKaydi> devamKayitlari;
    protected List<IzinTalebi> izinTalepleri;
    protected List<Gorev> atananGorevler;
    protected List<DevamDuzeltmeTalebi> duzeltmeTalepleri;
    protected List<Bildirim> bildirimler;

    public Calisan() {
        this.devamKayitlari = new ArrayList<>();
        this.izinTalepleri = new ArrayList<>();
        this.atananGorevler = new ArrayList<>();
        this.duzeltmeTalepleri = new ArrayList<>();
        this.bildirimler = new ArrayList<>();
    }

    public Calisan(int kullaniciId, String kullaniciAdi, String sifreHash, String ad, String soyad,
                   String eposta, String telefon, Rol rol, boolean aktifMi,
                   LocalDateTime olusturmaTarihi, LocalDateTime sonGirisTarihi,
                   int departmanId, Departman departman, String unvan, double temelUcret) {
        super(kullaniciId, kullaniciAdi, sifreHash, ad, soyad, eposta, telefon, rol, aktifMi,
                olusturmaTarihi, sonGirisTarihi);
        this.departmanId = departmanId;
        this.departman = departman;
        this.unvan = unvan;
        this.temelUcret = temelUcret;
        this.devamKayitlari = new ArrayList<>();
        this.izinTalepleri = new ArrayList<>();
        this.atananGorevler = new ArrayList<>();
        this.duzeltmeTalepleri = new ArrayList<>();
        this.bildirimler = new ArrayList<>();
    }

    public int getDepartmanId() {
        return departmanId;
    }

    public void setDepartmanId(int departmanId) {
        this.departmanId = departmanId;
    }

    public Departman getDepartman() {
        return departman;
    }

    public void setDepartman(Departman departman) {
        this.departman = departman;
    }

    public String getUnvan() {
        return unvan;
    }

    public void setUnvan(String unvan) {
        this.unvan = unvan;
    }

    public double getTemelUcret() {
        return temelUcret;
    }

    public void setTemelUcret(double temelUcret) {
        this.temelUcret = temelUcret;
    }

    public List<DevamKaydi> getDevamKayitlari() {
        return devamKayitlari;
    }

    public void setDevamKayitlari(List<DevamKaydi> devamKayitlari) {
        this.devamKayitlari = devamKayitlari;
    }

    public List<IzinTalebi> getIzinTalepleri() {
        return izinTalepleri;
    }

    public void setIzinTalepleri(List<IzinTalebi> izinTalepleri) {
        this.izinTalepleri = izinTalepleri;
    }

    public List<Gorev> getAtananGorevler() {
        return atananGorevler;
    }

    public void setAtananGorevler(List<Gorev> atananGorevler) {
        this.atananGorevler = atananGorevler;
    }

    public List<DevamDuzeltmeTalebi> getDuzeltmeTalepleri() {
        return duzeltmeTalepleri;
    }

    public void setDuzeltmeTalepleri(List<DevamDuzeltmeTalebi> duzeltmeTalepleri) {
        this.duzeltmeTalepleri = duzeltmeTalepleri;
    }

    public List<Bildirim> getBildirimler() {
        return bildirimler;
    }

    public void setBildirimler(List<Bildirim> bildirimler) {
        this.bildirimler = bildirimler;
    }

    // Ahmad Alamin - 230404933
    public DevamKaydi iseGirisYap(KonumBilgisi konum, SistemAyarlari ayarlar) {
        // TODO: Implement business logic
        return null;
    }

    // Ahmad Alamin - 230404933
    public DevamKaydi iseCikisYap(KonumBilgisi konum, SistemAyarlari ayarlar) {
        // TODO: Implement business logic
        return null;
    }

    // Ahmad Alamin - 230404933
    public boolean gunlukGirisKontrolu(LocalDate tarih) {
        // TODO: Implement business logic
        return false;
    }

    // Adnan Derviş - 230404935
    public IzinTalebi izinTalebiOlustur(LocalDate baslangic, LocalDate bitis, IzinTuru tur, String aciklama) {
        // TODO: Implement business logic
        return null;
    }

    // Adnan Derviş - 230404935
    public boolean izinCakismasiVarMi(LocalDate baslangic, LocalDate bitis) {
        // TODO: Implement business logic
        return false;
    }

    // Adnan Derviş - 230404935
    public boolean izinTalebiIptalEt(int izinId) {
        // TODO: Implement business logic
        return false;
    }

    // Youssef AYOUBI - 230404906
    public boolean gorevDurumuGuncelle(int gorevId, GorevDurumu yeniDurum, String aciklama) {
        // IK-01: Oturum açmamış veya pasif çalışan işlem yapamaz.
        if (!this.aktifMi || !this.oturumAcikMi) {
            System.out.println("HATA (IK-01): Görev durumu güncellemek için aktif hesap ve açık oturum gereklidir.");
            return false;
        }

        // IK-12: Çalışan görevi doğrudan TAMAMLANDI yapamaz; inceleme onayı gerekir.
        if (yeniDurum == GorevDurumu.TAMAMLANDI) {
            System.out.println("HATA (IK-12): Yetkisiz İşlem! Çalışan görevi doğrudan TAMAMLANDI yapamaz, incelemeye göndermelidir.");
            return false;
        }

        // İlgili görevi çalışanın atanan görevleri arasında bulma
        Gorev hedefGorev = null;
        for (Gorev g : this.atananGorevler) {
            if (g.getGorevId() == gorevId) {
                hedefGorev = g;
                break;
            }
        }

        if (hedefGorev == null) {
            System.out.println("HATA (IK-02): Bu görev (ID: " + gorevId + ") size atanmamış veya bulunamadı.");
            return false;
        }

        // IK-12: Atanan çalışan görevi DEVAM_EDIYOR veya INCELEME_BEKLIYOR durumlarına taşır.
        boolean guncellendi = hedefGorev.durumaGecis(yeniDurum, this.kullaniciId, aciklama);
        if (guncellendi && yeniDurum == GorevDurumu.INCELEME_BEKLIYOR) {
            Bildirim.bildirimGonder(
                    hedefGorev.getIncelemeSorumlusuId(),
                    "Görev İnceleme Bekliyor",
                    this.ad + " " + this.soyad + " '" + hedefGorev.getBaslik() + "' görevini incelemeye gönderdi.",
                    enums.BildirimTuru.GOREV_INCELEME,
                    hedefGorev.getGorevId()
            );
            System.out.println("BAŞARILI (IK-12): Görev inceleme için sorumluya iletildi ve bildirim oluşturuldu.");
        }
        return guncellendi;
    }

    // Mohamad BIRAHJAKLI - 230404939
    public DevamDuzeltmeTalebi devamDuzeltmeTalebiOlustur(LocalDate tarih, DuzeltmeTuru tur, String gerekce) {
        // TODO: Implement business logic
        return null;
    }

    // Mohamad BIRAHJAKLI - 230404939
    public String kendiRaporunuGoruntule(int yil, int ay) {
        // TODO: Implement business logic
        return null;
    }
}
