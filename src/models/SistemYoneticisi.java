// Ortak Sınıf (Shared Class) - Görev Dağılımı:
// - Youssef AYOUBI - 230404906 (Sınıf yapısı, kullaniciHesabiOlustur, kullaniciDurumGuncelle, rolAta, departmanOlustur, sistemAyarlariniGuncelle)
// - Mohamad BIRAHJAKLI - 230404939 (devamDuzeltmeTalebiDegerlendir, erkenGirisOnayla)
// - Adnan Derviş - 230404935 (yoneticiIzinTalebiDegerlendir)
package models;

import enums.Rol;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
        // IK-01 & IK-02: Sistem yöneticisi aktif ve oturum açmış olmalıdır.
        if (!this.aktifMi || !this.oturumAcikMi || this.rol != Rol.SISTEM_YONETICISI) {
            System.out.println("HATA (IK-01 / IK-02): Kullanıcı hesabı oluşturmak için yetkili Sistem Yöneticisi oturumu gereklidir.");
            return false;
        }
        if (kullanici == null || kullanici.getKullaniciAdi() == null || kullanici.getKullaniciAdi().isBlank()) {
            System.out.println("HATA: Geçersiz kullanıcı bilgisi! Kullanıcı adı boş olamaz.");
            return false;
        }
        // IK-04: Oluşturma tarihi sunucu saatine (Europe/Istanbul) göre atanır.
        if (kullanici.getOlusturmaTarihi() == null) {
            kullanici.setOlusturmaTarihi(LocalDateTime.now(ZoneId.of("Europe/Istanbul")));
        }
        System.out.println("BAŞARILI (IK-02): Kullanıcı hesabı oluşturuldu -> " +
                kullanici.getAd() + " " + kullanici.getSoyad() + " | Rol: " + kullanici.getRol());
        return true;
    }

    // Youssef AYOUBI - 230404906
    public boolean kullaniciDurumGuncelle(Kullanici kullanici, boolean aktifMi) {
        // IK-01 & IK-02: Hesap aktif/pasif durumu yalnızca Sistem Yöneticisi tarafından değiştirilebilir.
        if (!this.aktifMi || !this.oturumAcikMi || this.rol != Rol.SISTEM_YONETICISI) {
            System.out.println("HATA (IK-01 / IK-02): Yetkisiz işlem! Hesap durumunu yalnızca Sistem Yöneticisi güncelleyebilir.");
            return false;
        }
        if (kullanici == null) {
            System.out.println("HATA: Hedef kullanıcı bulunamadı.");
            return false;
        }
        kullanici.setAktifMi(aktifMi);
        if (!aktifMi) {
            kullanici.setOturumAcikMi(false);
        }
        System.out.println("BAŞARILI (IK-01): Kullanıcı (" + kullanici.getKullaniciAdi() +
                ") durumu güncellendi -> " + (aktifMi ? "AKTİF" : "PASİF"));
        return true;
    }

    // Youssef AYOUBI - 230404906
    public boolean rolAta(Kullanici kullanici, Rol yeniRol, IslemGecmisiLog log) {
        // IK-02: Rol değişikliği yetkisi Sistem Yöneticisine aittir.
        if (!this.aktifMi || !this.oturumAcikMi || this.rol != Rol.SISTEM_YONETICISI) {
            System.out.println("HATA (IK-02): Rol atama işlemi için Sistem Yöneticisi yetkisi gereklidir.");
            return false;
        }
        if (kullanici == null || yeniRol == null) {
            System.out.println("HATA: Geçersiz kullanıcı veya rol bilgisi.");
            return false;
        }
        Rol eskiRol = kullanici.getRol();
        kullanici.setRol(yeniRol);

        IslemGecmisiLog.logKaydet(
                this.kullaniciId,
                "ROL_ATAMA",
                kullanici.getKullaniciId(),
                String.valueOf(eskiRol),
                String.valueOf(yeniRol),
                "Sistem yöneticisi tarafından rol güncellendi."
        );
        System.out.println("BAŞARILI (IK-02): Kullanıcı (" + kullanici.getKullaniciAdi() +
                ") rolü değiştirildi: " + eskiRol + " -> " + yeniRol);
        return true;
    }

    // Youssef AYOUBI - 230404906
    public boolean departmanOlustur(Departman departman) {
        // IK-02: Departman oluşturma yetkisi Sistem Yöneticisine aittir.
        if (!this.aktifMi || !this.oturumAcikMi || this.rol != Rol.SISTEM_YONETICISI) {
            System.out.println("HATA (IK-02): Departman oluşturmak için Sistem Yöneticisi yetkisi gereklidir.");
            return false;
        }
        if (departman == null || departman.getDepartmanAdi() == null || departman.getDepartmanAdi().isBlank()) {
            System.out.println("HATA: Departman adı boş olamaz.");
            return false;
        }
        if (departman.getOlusturmaTarihi() == null) {
            departman.setOlusturmaTarihi(LocalDate.now(ZoneId.of("Europe/Istanbul")));
        }
        departman.setAktifMi(true);
        System.out.println("BAŞARILI (IK-02): Yeni departman oluşturuldu -> " + departman.getDepartmanAdi() +
                " (ID: " + departman.getDepartmanId() + ")");
        return true;
    }

    // Youssef AYOUBI - 230404906
    public boolean sistemAyarlariniGuncelle(SistemAyarlari ayarlar) {
        // IK-02: Sistem parametrelerini güncelleme yetkisi Sistem Yöneticisine aittir.
        if (!this.aktifMi || !this.oturumAcikMi || this.rol != Rol.SISTEM_YONETICISI) {
            System.out.println("HATA (IK-02): Sistem ayarlarını güncellemek için Sistem Yöneticisi yetkisi gereklidir.");
            return false;
        }
        if (ayarlar == null) {
            System.out.println("HATA: Geçersiz sistem ayarları nesnesi.");
            return false;
        }
        System.out.println("BAŞARILI (IK-02): Sistem ayarları güncellendi (Kabul Yarıçapı: " +
                ayarlar.getKabulYaricapiMetre() + "m, Zaman Dilimi: " + ayarlar.getZamanDilimi() + ").");
        return true;
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