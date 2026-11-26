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
        if (this.girisZamani != null) {
            System.out.println("RED (IK-05): Bugun icin zaten ise giris kaydi bulunmaktadir!");
            return;
        }

        if (zaman == null) {
            System.out.println("RED (IK-04): Giris zamani bos (null) olamaz!");
            return;
        }

        if (ayarlar == null) {
            System.out.println("RED (IK-03): Sistem ayarlari bulunamadi!");
            return;
        }

        if (!KonumServisi.konumDogrula(konum, ayarlar)) {
            System.out.println("RED (IK-03): Konum dogrulamasi basarisiz, ise giris kaydi olusturulmadi!");
            return;
        }

        this.girisZamani = zaman;
        this.tarih = zaman.toLocalDate();
        this.girisKonumu = konum;
        durumuVePuanlariHesapla(ayarlar);
        System.out.println("BASARILI (IK-03/IK-04/IK-05): Ise giris kaydedildi -> Tarih: " + this.tarih
                + ", Saat: " + this.girisZamani.toLocalTime() + ", Durum: " + this.durum);
    }

    public boolean cikisKaydet(LocalDateTime zaman, KonumBilgisi konum) {
        if (this.girisZamani == null) {
            System.out.println("RED (IK-05): Giris kaydi olmadan cikis yapilamaz!");
            return false;
        }

        if (this.cikisZamani != null) {
            System.out.println("RED (IK-05): Bugun icin zaten cikis kaydi olusturulmus!");
            return false;
        }

        if (zaman == null) {
            System.out.println("RED (IK-04): Cikis zamani bos (null) olamaz!");
            return false;
        }

        if (zaman.isBefore(this.girisZamani)) {
            System.out.println("RED (IK-04): Cikis zamani giris zamanindan once olamaz!");
            return false;
        }

        if (konum == null || !konum.gecerliKoordinatMi()) {
            System.out.println("RED (IK-03): Cikis konum bilgisi gecersiz!");
            return false;
        }

        this.cikisZamani = zaman;
        this.cikisKonumu = konum;
        System.out.println("BASARILI (IK-05): Ise cikis kaydedildi -> Saat: " + this.cikisZamani.toLocalTime());
        return true;
    }

    public void durumuVePuanlariHesapla(SistemAyarlari ayarlar) {
        if (this.girisZamani == null) {
            return;
        }
        if (ayarlar == null || ayarlar.getMesaiBaslangic() == null) {
            System.out.println("RED (IK-13): Mesai baslangic saati tanimli degil!");
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

    
    public void devamsizOlarakIsaretle(LocalDate tarih) {
        if (tarih == null) {
            System.out.println("RED (IK-06): Devamsizlik isaretlemesi icin tarih bos olamaz!");
            return;
        }
        if (this.girisZamani != null) {
            System.out.println("RED (IK-06): Gecerli giris kaydi bulunan gun DEVAMSIZ yapilamaz!");
            return;
        }
        this.tarih = tarih;
        this.durum = DevamDurumu.DEVAMSIZ;
        this.adayYesilPuan = 0;
        this.kesinlesenYesilPuan = 0;
        System.out.println("KAYIT (IK-06): " + tarih + " tarihi icin durum DEVAMSIZ olarak isaretlendi.");
    }
}