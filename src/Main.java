// Youssef AYOUBI - 230404906
// Ahmad Alamin - 230404933
// Adnan Derviş - 230404935
// Mohamad BIRAHJAKLI - 230404939

import enums.*;
import models.*;
import services.*;
// Ahmad Alamin - 230404933
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Şirket İçi Devam Takibi ve Görev Yönetimi Sistemi - Faz 1 İskeleti");

        // Ahmad Alamin - 230404933
        // Ortak test ayarlari (Istanbul merkez ve 09:00 mesai baslangici)
        SistemAyarlari ayarlar = new SistemAyarlari(
                1,
                41.0082,
                28.9784,
                100.0,
                50.0,
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
                60,
                60,
                120,
                500.0,
                100.0,
                "Europe/Istanbul"
        );

        // ==================== IK-01 ====================
        // Başarılı:
        // TODO: IK-01 Başarılı test senaryosunu ekle

        // Reddedilen:
        // TODO: IK-01 Reddedilen test senaryosunu ekle

        // ==================== IK-02 ====================
        // Başarılı:
        // TODO: IK-02 Başarılı test senaryosunu ekle

        // Reddedilen:
        // TODO: IK-02 Reddedilen test senaryosunu ekle

        // ==================== IK-03 ====================
        // Ahmad Alamin - 230404933
        // IK-03: Konum ve dogruluk payi kontrolu
        System.out.println("\n--- [IK-03] KONUM VE DOGRULUK KONTROLU ---");
        // Basarili:
        KonumBilgisi gecerliKonum = new KonumBilgisi(41.0085, 28.9788, 15.0, LocalDateTime.now());
        boolean konumGecerli = KonumServisi.konumDogrula(gecerliKonum, ayarlar);
        System.out.println("[IK-03 - BASARILI] konumDogrula = " + konumGecerli);

        // Reddedilen:
        KonumBilgisi uzakKonum = new KonumBilgisi(41.0200, 28.9950, 20.0, LocalDateTime.now());
        boolean konumRed = KonumServisi.konumDogrula(uzakKonum, ayarlar);
        System.out.println("[IK-03 - REDDEDILEN] konumDogrula = " + konumRed);

        // ==================== IK-04 ====================
        // Ahmad Alamin - 230404933
        // IK-04: Sunucu zaman dilimi ve zaman mantigi kontrolu
        System.out.println("\n--- [IK-04] SUNUCU ZAMANI (EUROPE/ISTANBUL) ---");
        // Basarili:
        ZonedDateTime sunucuZamani = ayarlar.sunucuZamaniGetir();
        System.out.println("[IK-04 - BASARILI] Zaman (" + sunucuZamani.getZone() + ") = " + sunucuZamani);

        // Reddedilen:
        DevamKaydi zamanTestKayit = new DevamKaydi();
        LocalDateTime girisSaatiZamanTest = ayarlar.sunucuZamaniGetir().toLocalDateTime();
        zamanTestKayit.girisKaydet(girisSaatiZamanTest, gecerliKonum, ayarlar);
        boolean erkenCikisSonucu = zamanTestKayit.cikisKaydet(girisSaatiZamanTest.minusMinutes(10), gecerliKonum);
        System.out.println("[IK-04 - REDDEDILEN] Gecersiz cikis zamani = " + erkenCikisSonucu);

        // ==================== IK-05 ====================
        // Ahmad Alamin - 230404933
        // IK-05: Gunluk tek giris ve cikis kaydi kontrolu
        System.out.println("\n--- [IK-05] GUNLUK TEK GIRIS VE CIKIS ---");
        // Basarili:
        DevamKaydi devamKaydi = new DevamKaydi();
        devamKaydi.setKayitId(101);
        devamKaydi.setCalisanId(230404933);
        devamKaydi.girisKaydet(LocalDate.now().atTime(9, 0), gecerliKonum, ayarlar);
        boolean cikisBasarili = devamKaydi.cikisKaydet(LocalDate.now().atTime(17, 0), gecerliKonum);
        System.out.println("[IK-05 - BASARILI] Cikis kaydi = " + cikisBasarili);

        // Reddedilen:
        devamKaydi.girisKaydet(LocalDate.now().atTime(9, 15), gecerliKonum, ayarlar);
        boolean ikinciCikis = devamKaydi.cikisKaydet(LocalDate.now().atTime(17, 5), gecerliKonum);
        System.out.println("[IK-05 - REDDEDILEN] Ikinci cikis kaydi = " + ikinciCikis);

        // ==================== IK-06 ====================
        // Ahmad Alamin - 230404933
        // IK-06: Devamsiz kaydi ve calisma gunu kontrolu
        System.out.println("\n--- [IK-06] DEVAMSIZ VE CALISMA GUNU KONTROLU ---");
        // Basarili:
        DevamKaydi devamsizKayit = new DevamKaydi();
        devamsizKayit.setKayitId(102);
        devamsizKayit.setCalisanId(230404933);
        LocalDate calismaGunu = LocalDate.of(2026, 11, 26); // Persembe
        if (ayarlar.calismaGunuMu(calismaGunu)) {
            devamsizKayit.devamsizOlarakIsaretle(calismaGunu);
            System.out.println("[IK-06 - BASARILI] Durum = " + devamsizKayit.getDurum());
        }

        // Reddedilen:
        LocalDate haftaSonu = LocalDate.of(2026, 11, 29); // Pazar
        System.out.println("[IK-06 - REDDEDILEN] Pazar calisma gunu mu = " + ayarlar.calismaGunuMu(haftaSonu));
        devamsizKayit.devamsizOlarakIsaretle(LocalDate.now());

        // ==================== IK-07 ====================
        // Başarılı:
        // TODO: IK-07 Başarılı test senaryosunu ekle

        // Reddedilen:
        // TODO: IK-07 Reddedilen test senaryosunu ekle

        // ==================== IK-08 ====================
        // Başarılı:
        // TODO: IK-08 Başarılı test senaryosunu ekle

        // Reddedilen:
        // TODO: IK-08 Reddedilen test senaryosunu ekle

        // ==================== IK-09 ====================
        // Başarılı:
        // TODO: IK-09 Başarılı test senaryosunu ekle

        // Reddedilen:
        // TODO: IK-09 Reddedilen test senaryosunu ekle

        // ==================== IK-10 ====================
        // Başarılı:
        // TODO: IK-10 Başarılı test senaryosunu ekle

        // Reddedilen:
        // TODO: IK-10 Reddedilen test senaryosunu ekle

        // ==================== IK-11 ====================
        // Başarılı:
        // TODO: IK-11 Başarılı test senaryosunu ekle

        // Reddedilen:
        // TODO: IK-11 Reddedilen test senaryosunu ekle

        // ==================== IK-12 ====================
        // Başarılı:
        // TODO: IK-12 Başarılı test senaryosunu ekle

        // Reddedilen:
        // TODO: IK-12 Reddedilen test senaryosunu ekle

        // ==================== IK-13 ====================
        // Ahmad Alamin - 230404933
        // IK-13: Erken giris ve gecikme puanlama kontrolu
        System.out.println("\n--- [IK-13] PUANLAMA VE GECIKME KONTROLU ---");
        // Basarili:
        DevamKaydi erkenKayit = new DevamKaydi();
        erkenKayit.setErkenGirisOnaylandiMi(true);
erkenKayit.durumuVePuanlariHesapla(ayarlar);
        System.out.println("[IK-13 - BASARILI ERKEN] Durum = " + erkenKayit.getDurum()
                + ", Aday Yesil = " + erkenKayit.getAdayYesilPuan()
                + ", Kesinlesen Yesil = " + erkenKayit.getKesinlesenYesilPuan());

        DevamKaydi asiriGecKayit = new DevamKaydi();
        asiriGecKayit.girisKaydet(LocalDate.now().atTime(11, 15), gecerliKonum, ayarlar);
        System.out.println("[IK-13 - BASARILI ASIRI GEC] Durum = " + asiriGecKayit.getDurum()
                + ", Kirmizi Puan = " + asiriGecKayit.getKirmiziPuan());

        // Reddedilen:
        SistemAyarlari eksikAyar = new SistemAyarlari();
        DevamKaydi puanRedKayit = new DevamKaydi();
        puanRedKayit.setGirisZamani(LocalDate.now().atTime(9, 30));
        puanRedKayit.durumuVePuanlariHesapla(eksikAyar);
        System.out.println("[IK-13 - REDDEDILEN] Eksik ayar sonrasi durum = " + puanRedKayit.getDurum());

        // ==================== IK-14 ====================
        // Başarılı:
        // TODO: IK-14 Başarılı test senaryosunu ekle

        // Reddedilen:
        // TODO: IK-14 Reddedilen test senaryosunu ekle
    }
}