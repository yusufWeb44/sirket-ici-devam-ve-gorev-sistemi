// Ahmad Alamin - 230404933
package models;

import enums.DevamDurumu;
import services.KonumServisi;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    public LocalDateTime getGirisZamani() {
        return girisZamani;
    }

    public LocalDateTime getCikisZamani() {
        return cikisZamani;
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

    // Mohamad BIRAHJAKLI - 230404939
    // IK-07: Kayitli giris ve cikis saatleri yalnizca yonetici onayli taleple duzeltilir.
    void onayliDuzeltmeyiUygula(DevamDuzeltmeTalebi.OnayliSaatDuzeltmesi duzeltme, SistemAyarlari ayarlar) {
        if (duzeltme.giris != null && !duzeltme.giris.equals(girisZamani)) {
            girisZamani = duzeltme.giris;
            erkenGirisOnaylandiMi = false;
            kesinlesenYesilPuan = 0;
            durumuVePuanlariHesapla(ayarlar);
        }
        if (duzeltme.cikis != null) {
            cikisZamani = duzeltme.cikis;
        }
    }

    // IK-03, IK-04, IK-05: gunluk giris kaydi ve konum/zaman kontrolu
    public void girisKaydet(LocalDateTime zaman, KonumBilgisi konum, SistemAyarlari ayarlar) {
        if (this.girisZamani != null) {
            System.out.println("Uyari: Bugun icin zaten giris kaydi var.");
            return;
        }

        if (zaman == null) {
            System.out.println("Uyari: Giris zamani bos olamaz.");
            return;
        }

        if (ayarlar == null) {
            System.out.println("Uyari: Sistem ayarlari bulunamadi.");
            return;
        }

        if (!KonumServisi.konumDogrula(konum, ayarlar)) {
            System.out.println("Uyari: Konum dogrulanamadi, giris kaydedilmedi.");
            return;
        }

        this.girisZamani = zaman;
        this.tarih = zaman.toLocalDate();
        this.girisKonumu = konum;
        durumuVePuanlariHesapla(ayarlar);
        System.out.println("Bilgi: Ise giris kaydedildi -> Tarih=" + this.tarih
                + ", Saat=" + this.girisZamani.toLocalTime() + ", Durum=" + this.durum);
    }

    // IK-05: giris kaydi varsa cikis saati ve konumu kaydedilir
    public boolean cikisKaydet(LocalDateTime zaman, KonumBilgisi konum) {
        if (this.girisZamani == null) {
            System.out.println("Uyari: Giris kaydi olmadan cikis yapilamaz.");
            return false;
        }

        if (this.cikisZamani != null) {
            System.out.println("Uyari: Bugun icin zaten cikis yapilmis.");
            return false;
        }

        if (zaman == null) {
            System.out.println("Uyari: Cikis zamani bos olamaz.");
            return false;
        }

        if (zaman.isBefore(this.girisZamani)) {
            System.out.println("Uyari: Cikis zamani giristen once olamaz.");
            return false;
        }

        if (konum == null || !konum.gecerliKoordinatMi()) {
            System.out.println("Uyari: Cikis konum bilgisi gecersiz.");
            return false;
        }

        this.cikisZamani = zaman;
        this.cikisKonumu = konum;
        System.out.println("Bilgi: Ise cikis kaydedildi -> Saat=" + this.cikisZamani.toLocalTime());
        return true;
    }

    // IK-13: mesai saatine gore erken/gec durumu ve aday puanlari hesapla
    public void durumuVePuanlariHesapla(SistemAyarlari ayarlar) {
        if (this.girisZamani == null) {
            return;
        }
        if (ayarlar == null || ayarlar.getMesaiBaslangic() == null) {
            System.out.println("Uyari: Mesai baslangic saati tanimli degil.");
            return;
        }

        LocalTime mesaiBaslangic = ayarlar.getMesaiBaslangic();
        LocalTime girisSaati = this.girisZamani.toLocalTime();

        // Pozitif deger: gec kalinan dakika; negatif deger: erken gelinen dakika
        long farkDakika = Duration.between(mesaiBaslangic, girisSaati).toMinutes();

        this.adayYesilPuan = 0;
        this.kirmiziPuan = 0;

        int erkenEsik = ayarlar.getYesilPuanEsikDakika() > 0 ? ayarlar.getYesilPuanEsikDakika() : 60;
        int kirmiziAlt = ayarlar.getKirmiziPuanAltDakika() > 0 ? ayarlar.getKirmiziPuanAltDakika() : 60;
        int asiriGecEsik = ayarlar.getAsiriGecEsikDakika() > 0 ? ayarlar.getAsiriGecEsikDakika() : 120;

        if (farkDakika <= -erkenEsik) {
            this.durum = DevamDurumu.ERKEN;
            this.adayYesilPuan = 1;
            if (this.erkenGirisOnaylandiMi) {
                this.kesinlesenYesilPuan = 1;
            }
        } else if (farkDakika < kirmiziAlt) {
            this.durum = (farkDakika <= 0) ? DevamDurumu.ZAMANINDA : DevamDurumu.GEC;
        } else if (farkDakika < asiriGecEsik) {
            this.durum = DevamDurumu.GEC;
            this.kirmiziPuan = 1;
        } else {
            this.durum = DevamDurumu.ASIRI_GEC;
        }
    }

    
    // IK-06: giris yapilmamis gunu devamsiz olarak isaretle
    public void devamsizOlarakIsaretle(LocalDate tarih) {
        if (tarih == null) {
            System.out.println("Uyari: Tarih bos olamaz.");
            return;
        }
        if (this.girisZamani != null) {
            System.out.println("Uyari: Giris kaydi olan gun DEVAMSIZ yapilamaz.");
            return;
        }
        this.tarih = tarih;
        this.durum = DevamDurumu.DEVAMSIZ;
        this.adayYesilPuan = 0;
        this.kesinlesenYesilPuan = 0;
        System.out.println("Bilgi: " + tarih + " tarihi devamsiz olarak isaretlendi.");
    }
}
