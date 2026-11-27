// Adnan Dervis - 230404935
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

    // Adnan Dervis - 230404935
    public IzinTalebi() {
        // IK-04: sunucu saati Europe/Istanbul
        this.olusturmaZamani = ZonedDateTime.now(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
    }

    // Adnan Dervis - 230404935
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
        // IK-04: bos gelirse Europe/Istanbul sunucu saati atanir
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

    // Adnan Dervis - 230404935
    public long gunSayisiHesapla() {
        if (!tarihAraligiGecerliMi()) {
            return 0L;
        }
        return ChronoUnit.DAYS.between(this.baslangicTarihi, this.bitisTarihi) + 1;
    }

    // Adnan Dervis - 230404935
    public boolean tarihAraligiGecerliMi() {
        if (this.baslangicTarihi == null || this.bitisTarihi == null) {
            return false;
        }
        return !this.bitisTarihi.isBefore(this.baslangicTarihi);
    }

    // Adnan Dervis - 230404935
    public void onayla(int yoneticiId) {
        // IK-09: kullanici kendi izin talebini onaylayamaz
        if (yoneticiId == this.calisanId) {
            System.out.println("Uyari: Kendi izin talebinizi onaylayamazsiniz.");
            return;
        }

        // IK-10: sadece BEKLIYOR durumundaki talepler onaylanabilir
        if (this.durum != IzinDurumu.BEKLIYOR) {
            System.out.println("Uyari: Sadece BEKLIYOR durumundaki izin talepleri onaylanabilir. Mevcut durum: " + this.durum);
            return;
        }

        this.durum = IzinDurumu.ONAYLANDI;
        this.onaylayanYoneticiId = yoneticiId;
        // IK-04: karar zamani Europe/Istanbul saatine gore alinir
        this.kararZamani = ZonedDateTime.now(ZoneId.of("Europe/Istanbul")).toLocalDateTime();

        Bildirim.bildirimGonder(
                this.calisanId,
                "Izin Karari",
                "Izniniz Onaylandi",
                BildirimTuru.IZIN_KARARI,
                this.izinId
        );
    }

    // Adnan Dervis - 230404935
    public boolean reddet(int yoneticiId, String retGerekcesi) {
        // IK-09: kullanici kendi izin talebini reddedemez
        if (yoneticiId == this.calisanId) {
            System.out.println("Uyari: Kendi izin talebinizi reddedemezsiniz.");
            return false;
        }

        // IK-09: ret isleminde gerekce zorunludur
        if (retGerekcesi == null || retGerekcesi.trim().isEmpty()) {
            System.out.println("Uyari: Ret islemi icin gerekce yazilmalidir.");
            return false;
        }

        // IK-10: sadece BEKLIYOR durumundaki talepler reddedilebilir
        if (this.durum != IzinDurumu.BEKLIYOR) {
            System.out.println("Uyari: Sadece BEKLIYOR durumundaki izin talepleri reddedilebilir. Mevcut durum: " + this.durum);
            return false;
        }

        this.durum = IzinDurumu.REDDEDILDI;
        this.onaylayanYoneticiId = yoneticiId;
        this.retGerekcesi = retGerekcesi.trim();
        // IK-04: karar zamani Europe/Istanbul saatine gore alinir
        this.kararZamani = ZonedDateTime.now(ZoneId.of("Europe/Istanbul")).toLocalDateTime();

        Bildirim.bildirimGonder(
                this.calisanId,
                "Izin Karari",
                "Izin Talebiniz Reddedildi: " + this.retGerekcesi,
                BildirimTuru.IZIN_KARARI,
                this.izinId
        );
        return true;
    }

    // Adnan Dervis - 230404935
    public boolean iptalEt(boolean yoneticiOnayiVarMi) {
        return iptalEt(this.calisanId, yoneticiOnayiVarMi);
    }

    // Adnan Dervis - 230404935
    public boolean iptalEt(int islemYapanId, boolean yoneticiOnayiVarMi) {
        // IK-10: bekleyen izin sadece talep sahibi calisan tarafindan iptal edilebilir
        if (this.durum == IzinDurumu.BEKLIYOR) {
            if (islemYapanId != this.calisanId) {
                System.out.println("Uyari: Bekleyen izin talebini sadece talep sahibi iptal edebilir.");
                return false;
            }
            this.durum = IzinDurumu.IPTAL_EDILDI;
            // IK-04: islem zamani Europe/Istanbul saatine gore alinir
            this.kararZamani = ZonedDateTime.now(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
            System.out.println("Bilgi: Bekleyen izin talebi iptal edildi -> ID: " + this.izinId);
            return true;
        }

        // IK-10: onaylanmis izin calisan tarafindan direkt iptal edilemez, yonetici onayi gerekir
        if (this.durum == IzinDurumu.ONAYLANDI) {
            if (!yoneticiOnayiVarMi) {
                System.out.println("Uyari: Onaylanmis izin talebi yonetici onayi olmadan iptal edilemez.");
                return false;
            }
            this.durum = IzinDurumu.IPTAL_EDILDI;
            // IK-04: islem zamani Europe/Istanbul saatine gore alinir
            this.kararZamani = ZonedDateTime.now(ZoneId.of("Europe/Istanbul")).toLocalDateTime();
            System.out.println("Bilgi: Onaylanmis izin talebi yonetici onayi ile iptal edildi -> ID: " + this.izinId);
            return true;
        }

        System.out.println("Uyari: Bu durumdaki (" + this.durum + ") izin talebi iptal edilemez.");
        return false;
    }
}
