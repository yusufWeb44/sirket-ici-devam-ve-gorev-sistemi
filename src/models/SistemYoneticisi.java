// Youssef AYOUBI - 230404906
package models;

import enums.Rol;
import java.time.LocalDateTime;

public class SistemYoneticisi extends Kullanici {
    private String yetkiSeviyesi;

    public SistemYoneticisi() {
        super();
    }

    public SistemYoneticisi(int kullaniciId, String kullaniciAdi, String sifreHash, String ad, String soyad,
                            String eposta, String telefon, Rol rol, boolean aktifMi,
                            LocalDateTime olusturmaTarihi, LocalDateTime sonGirisTarihi,
                            String yetkiSeviyesi) {
        super(kullaniciId, kullaniciAdi, sifreHash, ad, soyad, eposta, telefon, rol, aktifMi,
                olusturmaTarihi, sonGirisTarihi);
        this.yetkiSeviyesi = yetkiSeviyesi;
    }

    public String getYetkiSeviyesi() {
        return yetkiSeviyesi;
    }

    public void setYetkiSeviyesi(String yetkiSeviyesi) {
        this.yetkiSeviyesi = yetkiSeviyesi;
    }

    // Youssef AYOUBI - 230404906
    public boolean kullaniciHesabiOlustur(Kullanici kullanici) {
        // TODO: Implement business logic
        return false;
    }

    // Youssef AYOUBI - 230404906
    public boolean kullaniciDurumGuncelle(Kullanici kullanici, boolean aktifMi) {
        // TODO: Implement business logic
        return false;
    }

    // Youssef AYOUBI - 230404906
    public boolean rolAta(Kullanici kullanici, Rol yeniRol, IslemGecmisiLog log) {
        // TODO: Implement business logic
        return false;
    }

    // Youssef AYOUBI - 230404906
    public boolean departmanOlustur(Departman departman) {
        // TODO: Implement business logic
        return false;
    }

    // Youssef AYOUBI - 230404906
    public boolean sistemAyarlariniGuncelle(SistemAyarlari ayarlar) {
        // TODO: Implement business logic
        return false;
    }

    // Mohamad BIRAHJAKLI - 230404939
    public boolean devamDuzeltmeTalebiDegerlendir(DevamDuzeltmeTalebi talep, boolean onay,
                                                  LocalDateTime duzeltilmisGiris, LocalDateTime duzeltilmisCikis,
                                                  String gerekce, IslemGecmisiLog log) {
        // TODO: Implement business logic
        return false;
    }

    // Mohamad BIRAHJAKLI - 230404939
    public boolean erkenGirisOnayla(DevamKaydi kayit, boolean onay) {
        // TODO: Implement business logic
        return false;
    }

    // Adnan Derviş - 230404935
    public boolean yoneticiIzinTalebiDegerlendir(IzinTalebi talep, boolean onay, String gerekce) {
        // TODO: Implement business logic
        return false;
    }
}
