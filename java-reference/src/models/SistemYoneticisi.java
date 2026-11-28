// Ortak Sinif - Gorev Dagilimi:
// - Youssef AYOUBI - 230404906 (Sinif yapisi, kullaniciHesabiOlustur, kullaniciDurumGuncelle, rolAta, departmanOlustur, sistemAyarlariniGuncelle)
// - Mohamad BIRAHJAKLI - 230404939 (devamDuzeltmeTalebiDegerlendir, erkenGirisOnayla)
// - Adnan Dervis - 230404935 (yoneticiIzinTalebiDegerlendir, onayliIzinIptalOnayla)
package models;

import enums.BildirimTuru;
import enums.DevamDurumu;
import enums.IzinDurumu;
import enums.Rol;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SistemYoneticisi extends Kullanici {
    private String yetkiSeviyesi;
    private SistemAyarlari devamAyarlari = DevamDuzeltmeTalebi.varsayilanAyarlar();

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
        // IK-01 & IK-02: aktif Sistem Yoneticisi oturumu gereklidir
        if (!this.aktifMi || !this.oturumAcikMi || this.rol != Rol.SISTEM_YONETICISI) {
            System.out.println("Uyari: Kullanici hesabi olusturmak icin yetkili Sistem Yoneticisi oturumu gerekir.");
            return false;
        }
        if (kullanici == null || kullanici.getKullaniciAdi() == null || kullanici.getKullaniciAdi().isBlank()) {
            System.out.println("Uyari: Gecersiz kullanici bilgisi, kullanici adi bos olamaz.");
            return false;
        }
        // IK-04: olusturma tarihi Europe/Istanbul saatine gore atanir
        if (kullanici.getOlusturmaTarihi() == null) {
            kullanici.setOlusturmaTarihi(LocalDateTime.now(ZoneId.of("Europe/Istanbul")));
        }
        System.out.println("Bilgi: Kullanici hesabi olusturuldu -> " +
                kullanici.getAd() + " " + kullanici.getSoyad() + " (" + kullanici.getRol() + ")");
        return true;
    }

    // Youssef AYOUBI - 230404906
    public boolean kullaniciDurumGuncelle(Kullanici kullanici, boolean aktifMi) {
        // IK-01 & IK-02: hesap durumu sadece Sistem Yoneticisi tarafindan degistirilebilir
        if (!this.aktifMi || !this.oturumAcikMi || this.rol != Rol.SISTEM_YONETICISI) {
            System.out.println("Uyari: Hesap durumunu sadece Sistem Yoneticisi guncelleyebilir.");
            return false;
        }
        if (kullanici == null) {
            System.out.println("Uyari: Hedef kullanici bulunamadi.");
            return false;
        }
        kullanici.setAktifMi(aktifMi);
        if (!aktifMi) {
            kullanici.setOturumAcikMi(false);
        }
        System.out.println("Bilgi: Kullanici durumu guncellendi -> " + kullanici.getKullaniciAdi() +
                " (" + (aktifMi ? "AKTIF" : "PASIF") + ")");
        return true;
    }

    // Youssef AYOUBI - 230404906
    public boolean rolAta(Kullanici kullanici, Rol yeniRol, IslemGecmisiLog log) {
        // IK-02: rol degistirme yetkisi Sistem Yoneticisine aittir
        if (!this.aktifMi || !this.oturumAcikMi || this.rol != Rol.SISTEM_YONETICISI) {
            System.out.println("Uyari: Rol atama islemi icin Sistem Yoneticisi yetkisi gerekir.");
            return false;
        }
        if (kullanici == null || yeniRol == null) {
            System.out.println("Uyari: Gecersiz kullanici veya rol bilgisi.");
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
                "Sistem yoneticisi tarafindan rol guncellendi."
        );
        System.out.println("Bilgi: Kullanici rolu degistirildi -> " + kullanici.getKullaniciAdi() +
                " (" + eskiRol + " -> " + yeniRol + ")");
        return true;
    }

    // Youssef AYOUBI - 230404906
    public boolean departmanOlustur(Departman departman) {
        // IK-02: departman olusturma yetkisi Sistem Yoneticisine aittir
        if (!this.aktifMi || !this.oturumAcikMi || this.rol != Rol.SISTEM_YONETICISI) {
            System.out.println("Uyari: Departman olusturmak icin Sistem Yoneticisi yetkisi gerekir.");
            return false;
        }
        if (departman == null || departman.getDepartmanAdi() == null || departman.getDepartmanAdi().isBlank()) {
            System.out.println("Uyari: Departman adi bos olamaz.");
            return false;
        }
        if (departman.getOlusturmaTarihi() == null) {
            departman.setOlusturmaTarihi(LocalDate.now(ZoneId.of("Europe/Istanbul")));
        }
        departman.setAktifMi(true);
        System.out.println("Bilgi: Yeni departman olusturuldu -> " + departman.getDepartmanAdi() +
                " (ID: " + departman.getDepartmanId() + ")");
        return true;
    }

    // Youssef AYOUBI - 230404906
    public boolean sistemAyarlariniGuncelle(SistemAyarlari ayarlar) {
        // IK-02: sistem ayarlarini guncelleme yetkisi Sistem Yoneticisine aittir
        if (!this.aktifMi || !this.oturumAcikMi || this.rol != Rol.SISTEM_YONETICISI) {
            System.out.println("Uyari: Sistem ayarlarini guncellemek icin Sistem Yoneticisi yetkisi gerekir.");
            return false;
        }
        if (ayarlar == null) {
            System.out.println("Uyari: Gecersiz sistem ayarlari nesnesi.");
            return false;
        }
        System.out.println("Bilgi: Sistem ayarlari guncellendi -> Kabul yaricapi: " +
                ayarlar.getKabulYaricapiMetre() + "m, Zaman dilimi: " + ayarlar.getZamanDilimi());
        this.devamAyarlari = ayarlar;
        return true;
    }

    // Mohamad BIRAHJAKLI - 230404939
    public boolean devamDuzeltmeTalebiDegerlendir(DevamDuzeltmeTalebi talep, boolean onay,
                                                  LocalDateTime duzeltilmisGiris, LocalDateTime duzeltilmisCikis,
                                                  String gerekce, IslemGecmisiLog log) {
        return devamDuzeltmeTalebiDegerlendir(talep, onay, duzeltilmisGiris, duzeltilmisCikis,
                gerekce, log, devamAyarlari);
    }

    // Mohamad BIRAHJAKLI - 230404939
    public boolean devamDuzeltmeTalebiDegerlendir(DevamDuzeltmeTalebi talep, boolean onay,
                                                  LocalDateTime duzeltilmisGiris, LocalDateTime duzeltilmisCikis,
                                                  String gerekce, IslemGecmisiLog log, SistemAyarlari ayarlar) {
        // IK-01, IK-02, IK-07: duzeltme talebine sadece aktif Sistem Yoneticisi karar verir
        if (talep == null) {
            System.out.println("Uyari: Duzeltme talebi bulunamadi.");
            return false;
        }
        return talep.karariUygula(this, onay, duzeltilmisGiris, duzeltilmisCikis,
                gerekce, log, ayarlar);
    }

    // Mohamad BIRAHJAKLI - 230404939
    public boolean erkenGirisOnayla(DevamKaydi kayit, boolean onay) {
        // IK-13: erken giris aday puani yonetici karariyla kesinlesir
        if (!devamKarariYetkisiVarMi() || kayit == null || kayit.getDurum() != DevamDurumu.ERKEN
                || kayit.getAdayYesilPuan() <= 0) {
            System.out.println("Uyari: Yetkili oturum ve erken giris aday puani gereklidir.");
            return false;
        }
        int oncekiPuan = kayit.getKesinlesenYesilPuan();
        int yeniPuan = onay ? kayit.getAdayYesilPuan() : 0;
        kayit.setErkenGirisOnaylandiMi(onay);
        kayit.setKesinlesenYesilPuan(yeniPuan);
        if (oncekiPuan != yeniPuan) {
            String calisan = "Calisan=" + kayit.getCalisanId() + ", Kesinlesen yesil=";
            IslemGecmisiLog.logKaydet(getKullaniciId(), "ERKEN_GIRIS_KARARI", kayit.getKayitId(),
                    calisan + oncekiPuan, calisan + yeniPuan, onay ? "Onaylandi" : "Onaylanmadi");
        }
        return true;
    }

    final boolean devamKarariYetkisiVarMi() {
        return isAktifMi() && isOturumAcikMi() && getRol() == Rol.SISTEM_YONETICISI && getKullaniciId() > 0;
    }

    // Adnan Dervis - 230404935
    public boolean yoneticiIzinTalebiDegerlendir(IzinTalebi talep, boolean onaylandiMi, String retGerekcesi) {
        if (talep == null) {
            System.out.println("Uyari: Degerlendirilecek izin talebi bulunamadi.");
            return false;
        }

        // IK-09: kendi izin talebini onaylama engeli
        if (talep.getCalisanId() == this.kullaniciId) {
            System.out.println("Uyari: Kendi izin talebinizi onaylayamazsiniz.");
            return false;
        }

        // IK-10: sadece BEKLIYOR durumundaki talepler degerlendirilebilir
        if (talep.getDurum() != IzinDurumu.BEKLIYOR) {
            System.out.println("Uyari: Sadece BEKLIYOR durumundaki talepler degerlendirilebilir. Mevcut: "
                    + talep.getDurum());
            return false;
        }

        // IK-04: sunucu saati Europe/Istanbul
        ZonedDateTime kararZamani = ZonedDateTime.now(ZoneId.of("Europe/Istanbul"));

        // IK-09: sistem yoneticisi yonlendirilen talebi onaylar veya gerekceli reddeder
        if (onaylandiMi) {
            talep.onayla(this.kullaniciId);
            if (talep.getDurum() == IzinDurumu.ONAYLANDI) {
                System.out.println("Bilgi: Yonetici izin talebi onaylandi -> ID: " + talep.getIzinId() +
                        " (" + kararZamani.toLocalDateTime() + ")");
                return true;
            }
            return false;
        } else {
            if (retGerekcesi == null || retGerekcesi.trim().isEmpty()) {
                System.out.println("Uyari: Ret icin gerekce belirtilmelidir.");
                return false;
            }
            return talep.reddet(this.kullaniciId, retGerekcesi);
        }
    }

    // Adnan Dervis - 230404935
    public boolean onayliIzinIptalOnayla(IzinTalebi talep, String iptalGerekcesi) {
        if (talep == null) {
            System.out.println("Uyari: Iptal onayi verilecek izin talebi bulunamadi.");
            return false;
        }

        // IK-10: sadece ONAYLANDI durumundaki izinler icin iptal onayi verilebilir
        if (talep.getDurum() != IzinDurumu.ONAYLANDI) {
            System.out.println("Uyari: Sadece ONAYLANDI durumundaki talepler iptal onayina uygundur. Mevcut: "
                    + talep.getDurum());
            return false;
        }

        boolean iptalBasarili = talep.iptalEt(this.kullaniciId, true);
        if (iptalBasarili) {
            // IK-04: Islem zamani Europe/Istanbul sunucu saatine gore alinir.
            ZonedDateTime islemZamani = ZonedDateTime.now(ZoneId.of("Europe/Istanbul"));
            String gerekceMetni = (iptalGerekcesi != null && !iptalGerekcesi.trim().isEmpty())
                    ? iptalGerekcesi.trim()
                    : "Sistem yoneticisi onayi ile iptal edildi";

            Bildirim.bildirimGonder(
                    talep.getCalisanId(),
                    "Izin Iptal Onayi",
                    "Onaylanmis izin talebiniz (ID: " + talep.getIzinId() + ") Sistem Yoneticisi tarafindan iptal edildi. Gerekce: "
                            + gerekceMetni + " (" + islemZamani.toLocalDateTime() + ")",
                    BildirimTuru.IZIN_KARARI,
                    talep.getIzinId()
            );
            return true;
        }
        return false;
    }
}
