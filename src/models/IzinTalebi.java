// Adnan Derviş - 230404935
package models;

import enums.IzinDurumu;
import enums.IzinTuru;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public IzinTalebi() {
    }

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
        this.olusturmaZamani = olusturmaZamani;
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

    public long gunSayisiHesapla() {
        // TODO: Implement business logic
        return 0L;
    }

    public boolean tarihAraligiGecerliMi() {
        // TODO: Implement business logic
        return false;
    }

    public void onayla(int yoneticiId) {
        // TODO: Implement business logic
    }

    public boolean reddet(int yoneticiId, String retGerekcesi) {
        // TODO: Implement business logic
        return false;
    }

    public boolean iptalEt(boolean yoneticiOnayiVarMi) {
        // TODO: Implement business logic
        return false;
    }
}
