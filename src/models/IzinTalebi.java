// Adnan Derviş 230404935
package models;

import enums.BildirimTuru;
import enums.IzinDurumu;
import enums.IzinTuru;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class IzinTalebi {
    private int izinId;
    private int calisanId;
    private LocalDate baslangicTarihi;
    private LocalDate bitisTarihi;
    private IzinTuru izinTuru;
    private String aciklama;
    private IzinDurumu durum;
    private int onaylayanYoneticiId;
    private String retGerekcesi;
    private LocalDateTime olusturmaZamani;
    private LocalDateTime kararZamani;

    // Adnan Derviş 230404935
    public IzinTalebi() {
        // IK-04: Oluşturma zamanı Europe/Istanbul sunucu saatine göre atanır.
        this.olusturmaZamani = ZonedDateTime.now(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
    }

    // Adnan Derviş 230404935
    public IzinTalebi(int izinId, int calisanId, LocalDate baslangicTarihi, LocalDate bitisTarihi,
                      IzinTuru izinTuru, String aciklama, IzinDurumu durum, int onaylayanYoneticiId,
                      String retGerekcesi, LocalDateTime olusturmaZamani, LocalDateTime kararZamani) {
        this.izinId = izinId;
        this.calisanId = calisanId;
        this.baslangicTarihi = baslangicTarihi;
        this.bitisTarihi = bitisTarihi;
        this.izinTuru = izinTuru;
        this.aciklama = aciklama;
        this.durum = durum;
        this.onaylayanYoneticiId = onaylayanYoneticiId;
        this.retGerekcesi = retGerekcesi;
        // IK-04: Oluşturma zamanı boşsa Europe/Istanbul sunucu saatine göre atanır.
        this.olusturmaZamani = (olusturmaZamani != null)
                ? olusturmaZamani
                : ZonedDateTime.now(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
        this.kararZamani = kararZamani;
    }

    public int getIzinId() {
        return izinId;
    }

    public void setIzinId(int izinId) {
        this.izinId = izinId;
    }

    public int getCalisanId() {
        return calisanId;
    }

    public void setCalisanId(int calisanId) {
        this.calisanId = calisanId;
    }

    public LocalDate getBaslangicTarihi() {
        return baslangicTarihi;
    }

    public void setBaslangicTarihi(LocalDate baslangicTarihi) {
        this.baslangicTarihi = baslangicTarihi;
    }

    public LocalDate getBitisTarihi() {
        return bitisTarihi;
    }

    public void setBitisTarihi(LocalDate bitisTarihi) {
        this.bitisTarihi = bitisTarihi;
    }

    public IzinTuru getIzinTuru() {
        return izinTuru;
    }

    public void setIzinTuru(IzinTuru izinTuru) {
        this.izinTuru = izinTuru;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public IzinDurumu getDurum() {
        return durum;
    }

    public void setDurum(IzinDurumu durum) {
        this.durum = durum;
    }

    public int getOnaylayanYoneticiId() {
        return onaylayanYoneticiId;
    }

    public void setOnaylayanYoneticiId(int onaylayanYoneticiId) {
        this.onaylayanYoneticiId = onaylayanYoneticiId;
    }

    public String getRetGerekcesi() {
        return retGerekcesi;
    }

    public void setRetGerekcesi(String retGerekcesi) {
        this.retGerekcesi = retGerekcesi;
    }

    public LocalDateTime getOlusturmaZamani() {
        return olusturmaZamani;
    }

    public void setOlusturmaZamani(LocalDateTime olusturmaZamani) {
        this.olusturmaZamani = olusturmaZamani;
    }

    public LocalDateTime getKararZamani() {
        return kararZamani;
    }

    public void setKararZamani(LocalDateTime kararZamani) {
        this.kararZamani = kararZamani;
    }

    // Adnan Derviş 230404935
    public long gunSayisiHesapla() {
        if (!tarihAraligiGecerliMi()) {
            return 0L;
        }
        return ChronoUnit.DAYS.between(this.baslangicTarihi, this.bitisTarihi) + 1;
    }

    // Adnan Derviş 230404935
    public boolean tarihAraligiGecerliMi() {
        if (this.baslangicTarihi == null || this.bitisTarihi == null) {
            return false;
        }
        return !this.bitisTarihi.isBefore(this.baslangicTarihi);
    }

    // Adnan Derviş 230404935
    public void onayla(int yoneticiId) {
        // IK-09: Çalışan veya departman yöneticisi kendi izin talebini onaylayamaz.
        if (yoneticiId == this.calisanId) {
            System.out.println("Yetkisiz İşlem: Kendi izin talebinizi onaylayamazsınız! (IK-09)");
            return;
        }

        // IK-10: Yalnızca BEKLIYOR durumundaki izin talepleri onaylanabilir.
        if (this.durum != IzinDurumu.BEKLIYOR) {
            System.out.println("Hata: Yalnızca BEKLIYOR durumundaki izin talepleri onaylanabilir! Mevcut durum: " + this.durum + " (IK-10)");
            return;
        }

        this.durum = IzinDurumu.ONAYLANDI;
        this.onaylayanYoneticiId = yoneticiId;
        // IK-04: Karar zamanı Europe/Istanbul sunucu saatine göre kaydedilir.
        this.kararZamani = ZonedDateTime.now(ZoneId.of("Europe/Istanbul")).toLocalDateTime();

        Bildirim.bildirimGonder(
                this.calisanId,
                "İzin Kararı",
                "İzniniz Onaylandı",
                BildirimTuru.IZIN_KARARI,
                this.izinId
        );
    }

    // Adnan Derviş 230404935
    public boolean reddet(int yoneticiId, String retGerekcesi) {
        // IK-09: Çalışan veya departman yöneticisi kendi izin talebini reddedemez.
        if (yoneticiId == this.calisanId) {
            System.out.println("Yetkisiz İşlem: Kendi izin talebinizi reddedemezsiniz! (IK-09)");
            return false;
        }

        // IK-09: Ret işlemi için gerekçe girilmesi zorunludur.
        if (retGerekcesi == null || retGerekcesi.trim().isEmpty()) {
            System.out.println("Hata: Ret için gerekçe belirtilmesi zorunludur! (IK-09)");
            return false;
        }

        // IK-10: Yalnızca BEKLIYOR durumundaki izin talepleri reddedilebilir.
        if (this.durum != IzinDurumu.BEKLIYOR) {
            System.out.println("Hata: Yalnızca BEKLIYOR durumundaki izin talepleri reddedilebilir! Mevcut durum: " + this.durum + " (IK-10)");
            return false;
        }

        this.durum = IzinDurumu.REDDEDILDI;
        this.onaylayanYoneticiId = yoneticiId;
        this.retGerekcesi = retGerekcesi.trim();
        // IK-04: Karar zamanı Europe/Istanbul sunucu saatine göre kaydedilir.
        this.kararZamani = ZonedDateTime.now(ZoneId.of("Europe/Istanbul")).toLocalDateTime();

        Bildirim.bildirimGonder(
                this.calisanId,
                "İzin Kararı",
                "İzin Talebiniz Reddedildi: " + this.retGerekcesi,
                BildirimTuru.IZIN_KARARI,
                this.izinId
        );
        return true;
    }

    // Adnan Derviş 230404935
    public boolean iptalEt(boolean yoneticiOnayiVarMi) {
        return iptalEt(this.calisanId, yoneticiOnayiVarMi);
    }

    // Adnan Derviş 230404935
    public boolean iptalEt(int islemYapanId, boolean yoneticiOnayiVarMi) {
        // IK-10: Bekleyen izin talebi yalnızca talep sahibi çalışan tarafından doğrudan iptal edilebilir.
        if (this.durum == IzinDurumu.BEKLIYOR) {
            if (islemYapanId != this.calisanId) {
                System.out.println("Yetkisiz İşlem: Bekleyen izin talebini yalnızca talep sahibi çalışan iptal edebilir! (IK-10)");
                return false;
            }
            this.durum = IzinDurumu.IPTAL_EDILDI;
            // IK-04: İşlem zamanı Europe/Istanbul sunucu saatine göre kaydedilir.
            this.kararZamani = ZonedDateTime.now(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
            System.out.println("BAŞARILI (IK-10): Bekleyen izin talebi (ID: " + this.izinId + ") iptal edildi.");
            return true;
        }

        // IK-10: Onaylanmış izin talebi çalışan tarafından doğrudan iptal edilemez; yönetici onayı gerekir.
        if (this.durum == IzinDurumu.ONAYLANDI) {
            if (!yoneticiOnayiVarMi) {
                System.out.println("Hata: Onaylanmış izin talebi çalışan tarafından doğrudan iptal edilemez; resmi yönetici onayı gereklidir! (IK-10)");
                return false;
            }
            this.durum = IzinDurumu.IPTAL_EDILDI;
            // IK-04: İşlem zamanı Europe/Istanbul sunucu saatine göre kaydedilir.
            this.kararZamani = ZonedDateTime.now(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
            System.out.println("BAŞARILI (IK-10): Onaylanmış izin talebi (ID: " + this.izinId + ") yönetici onayı ile iptal edildi.");
            return true;
        }

        System.out.println("Hata: Mevcut durumda (" + this.durum + ") bulunan izin talebi iptal edilemez! (IK-10)");
        return false;
    }
}
