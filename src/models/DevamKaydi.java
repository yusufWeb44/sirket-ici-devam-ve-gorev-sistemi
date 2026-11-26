// Ahmad Alamin - 230404933
package models;

import enums.DevamDurumu;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DevamKaydi {
    private int kayitId;
    private int calisanId;
    private LocalDate tarih;
    private LocalDateTime girisZamani;
    private LocalDateTime cikisZamani;
    private KonumBilgisi girisKonumu;
    private KonumBilgisi cikisKonumu;
    private DevamDurumu durum;
    private int adayYesilPuan;
    private int kesinlesenYesilPuan;
    private int kirmiziPuan;
    private boolean erkenGirisOnaylandiMi;

    public DevamKaydi() {
    }

    public DevamKaydi(int kayitId, int calisanId, LocalDate tarih, LocalDateTime girisZamani,
                      LocalDateTime cikisZamani, KonumBilgisi girisKonumu, KonumBilgisi cikisKonumu,
                      DevamDurumu durum, int adayYesilPuan, int kesinlesenYesilPuan, int kirmiziPuan,
                      boolean erkenGirisOnaylandiMi) {
        this.kayitId = kayitId;
        this.calisanId = calisanId;
        this.tarih = tarih;
        this.girisZamani = girisZamani;
        this.cikisZamani = cikisZamani;
        this.girisKonumu = girisKonumu;
        this.cikisKonumu = cikisKonumu;
        this.durum = durum;
        this.adayYesilPuan = adayYesilPuan;
        this.kesinlesenYesilPuan = kesinlesenYesilPuan;
        this.kirmiziPuan = kirmiziPuan;
        this.erkenGirisOnaylandiMi = erkenGirisOnaylandiMi;
    }

    public int getKayitId() {
        return kayitId;
    }

    public void setKayitId(int kayitId) {
        this.kayitId = kayitId;
    }

    public int getCalisanId() {
        return calisanId;
    }

    public void setCalisanId(int calisanId) {
        this.calisanId = calisanId;
    }

    public LocalDate getTarih() {
        return tarih;
    }

    public void setTarih(LocalDate tarih) {
        this.tarih = tarih;
    }

    public LocalDateTime getGirisZamani() {
        return girisZamani;
    }

    public void setGirisZamani(LocalDateTime girisZamani) {
        this.girisZamani = girisZamani;
    }

    public LocalDateTime getCikisZamani() {
        return cikisZamani;
    }

    public void setCikisZamani(LocalDateTime cikisZamani) {
        this.cikisZamani = cikisZamani;
    }

    public KonumBilgisi getGirisKonumu() {
        return girisKonumu;
    }

    public void setGirisKonumu(KonumBilgisi girisKonumu) {
        this.girisKonumu = girisKonumu;
    }

    public KonumBilgisi getCikisKonumu() {
        return cikisKonumu;
    }

    public void setCikisKonumu(KonumBilgisi cikisKonumu) {
        this.cikisKonumu = cikisKonumu;
    }

    public DevamDurumu getDurum() {
        return durum;
    }

    public void setDurum(DevamDurumu durum) {
        this.durum = durum;
    }

    public int getAdayYesilPuan() {
        return adayYesilPuan;
    }

    public void setAdayYesilPuan(int adayYesilPuan) {
        this.adayYesilPuan = adayYesilPuan;
    }

    public int getKesinlesenYesilPuan() {
        return kesinlesenYesilPuan;
    }

    public void setKesinlesenYesilPuan(int kesinlesenYesilPuan) {
        this.kesinlesenYesilPuan = kesinlesenYesilPuan;
    }

    public int getKirmiziPuan() {
        return kirmiziPuan;
    }

    public void setKirmiziPuan(int kirmiziPuan) {
        this.kirmiziPuan = kirmiziPuan;
    }

    public boolean isErkenGirisOnaylandiMi() {
        return erkenGirisOnaylandiMi;
    }

    public void setErkenGirisOnaylandiMi(boolean erkenGirisOnaylandiMi) {
        this.erkenGirisOnaylandiMi = erkenGirisOnaylandiMi;
    }

    public void girisKaydet(LocalDateTime zaman, KonumBilgisi konum, SistemAyarlari ayarlar) {
        // TODO: Implement business logic
    }

    public boolean cikisKaydet(LocalDateTime zaman, KonumBilgisi konum) {
        // TODO: Implement business logic
        return false;
    }

    public void durumuVePuanlariHesapla(SistemAyarlari ayarlar) {
        // TODO: Implement business logic
    }

    public void devamsizOlarakIsaretle(LocalDate tarih) {
        // TODO: Implement business logic
    }
}
