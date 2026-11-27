// Youssef AYOUBI - 230404906
// Ahmad Alamin - 230404933
// Adnan Dervis - 230404935
// Mohamad BIRAHJAKLI - 230404939

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import enums.DevamDurumu;
import enums.DuzeltmeTuru;
import enums.GorevDurumu;
import enums.IzinDurumu;
import enums.IzinTuru;
import enums.Oncelik;
import enums.Rol;
import enums.TalepDurumu;
import models.*;
import services.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Sirket Ici Devam Takibi ve Gorev Yonetimi Sistemi");
        System.out.println("Entegrasyon Testleri (IK-01 - IK-14)");
        System.out.println("========================================");

        // Ortak ayarlar: Istanbul merkez konum, 09:00-17:00, Pts-Cum, 100m yaricap
        SistemAyarlari ayarlar = new SistemAyarlari(
                1,
                41.0082, 28.9784,
                100.0, 50.0,
                LocalTime.of(9, 0), LocalTime.of(17, 0),
                List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                        DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
                60, 60, 120,
                500.0, 100.0,
                "Europe/Istanbul"
        );

        testIK01(ayarlar);
        testIK02(ayarlar);
        testIK03(ayarlar);
        testIK04(ayarlar);
        testIK05(ayarlar);
        testIK06(ayarlar);
        testIK07(ayarlar);
        testIK08(ayarlar);
        testIK09(ayarlar);
        testIK10(ayarlar);
        testIK11(ayarlar);
        testIK12(ayarlar);
        testIK13(ayarlar);
        testIK14(ayarlar);

        System.out.println("\n========================================");
        System.out.println("Tum testler tamamlandi.");
        System.out.println("========================================");
    }

    // ==================== IK-01 ====================
    // Ahmad Alamin - 230404933
    private static void testIK01(SistemAyarlari ayarlar) {
        System.out.println("\n--- IK-01: Kimlik Dogrulama ve Aktif Hesap Kontrolu ---");

        // Gecerli kullanici olustur
        Calisan c1 = new Calisan();
        c1.setKullaniciId(101);
        c1.setKullaniciAdi("ali.yilmaz");
        c1.setSifreHash("sifre123");
        c1.setAd("Ali"); c1.setSoyad("Yilmaz");
        c1.setAktifMi(true);
        c1.setRol(Rol.CALISAN);

        // Dogru bilgilerle giris
        System.out.println("1) Gecerli kullanici girisi kontrolu:");
        boolean giris1 = c1.girisYap("ali.yilmaz", "sifre123");
        System.out.println("  Sonuc: " + giris1 + " | Oturum acik: " + c1.isOturumAcikMi());
        if (!giris1 || !c1.isOturumAcikMi())
            System.out.println("  Uyari: Aktif kullanici giris yapamadi.");

        // Yanlis sifre ile giris denemesi
        c1.cikisYap();
        System.out.println("2) Yanlis sifre ile giris engeli:");
        boolean girisYanlisSifre = c1.girisYap("ali.yilmaz", "yanlis");
        System.out.println("  Sonuc: " + girisYanlisSifre + " | Oturum acik: " + c1.isOturumAcikMi());
        if (girisYanlisSifre) System.out.println("  Uyari: Yanlis sifre kabul edilmemeli.");

        // Pasif hesap giris denemesi
        Calisan pasifCalisan = new Calisan();
        pasifCalisan.setKullaniciId(102);
        pasifCalisan.setKullaniciAdi("pasif.kullanici");
        pasifCalisan.setSifreHash("sifre456");
        pasifCalisan.setAktifMi(false);
        pasifCalisan.setRol(Rol.CALISAN);
        System.out.println("3) Pasif hesap giris engeli:");
        boolean girisPasif = pasifCalisan.girisYap("pasif.kullanici", "sifre456");
        System.out.println("  Sonuc: " + girisPasif);
        if (girisPasif) System.out.println("  Uyari: Pasif hesap giris yapamamali.");

        // Oturum kapali iken sifre guncelleme denemesi
        System.out.println("4) Oturum kapaliyken sifre guncelleme engeli:");
        boolean sifreGuncelle = c1.sifreGuncelle("sifre123", "yeniSifre");
        System.out.println("  Sonuc: " + sifreGuncelle);
        if (sifreGuncelle) System.out.println("  Uyari: Oturum olmadan sifre degistirilememeli.");

        // Oturum actiktan sonra dogru eski sifre ile guncelleme
        c1.girisYap("ali.yilmaz", "sifre123");
        System.out.println("5) Oturum actiktan sonra sifre guncelleme:");
        boolean sifreGuncelleOk = c1.sifreGuncelle("sifre123", "yeniSifre999");
        System.out.println("  Sonuc: " + sifreGuncelleOk);
        if (!sifreGuncelleOk) System.out.println("  Uyari: Sifre guncellenemedi.");
    }

    // ==================== IK-02 ====================
    // Youssef AYOUBI - 230404906
    private static void testIK02(SistemAyarlari ayarlar) {
        System.out.println("\n--- IK-02: Rol Bazli Erisim Kontrolu ---");

        // SistemYoneticisi olustur ve oturum ac
        SistemYoneticisi sy = denemeOturumuAc(new SistemYoneticisi(), 201, Rol.SISTEM_YONETICISI);
        sy.setYetkiSeviyesi("SUPER");

        // Oturum acik kullanici profil bilgisi alabilmeli
        System.out.println("1) Oturum acik kullanicinin profil goruntulemesi:");
        String profil = sy.profilBilgisiGetir();
        System.out.println("  Profil: " + profil);
        if (profil == null) System.out.println("  Uyari: Profil bilgisi bos dondu.");

        // Sistem yoneticisi yeni kullanici olusturuyor
        System.out.println("2) Sistem yoneticisi ile yeni kullanici olusturma:");
        Calisan yeniCalisan = new Calisan();
        yeniCalisan.setKullaniciId(202);
        yeniCalisan.setKullaniciAdi("yeni.calisan");
        yeniCalisan.setAd("Yeni"); yeniCalisan.setSoyad("Calisan");
        yeniCalisan.setRol(Rol.CALISAN);
        boolean hesapOlusturuldu = sy.kullaniciHesabiOlustur(yeniCalisan);
        System.out.println("  Sonuc: " + hesapOlusturuldu);
        if (!hesapOlusturuldu) System.out.println("  Uyari: Hesap olusturulamadi.");

        // Yanlis rolle kullanici olusturma denemesi
        System.out.println("3) Yetkisiz roldeki kullanicinin hesap olusturma engeli:");
        SistemYoneticisi yetkisizSY = new SistemYoneticisi();
        yetkisizSY.setKullaniciId(203);
        yetkisizSY.setAktifMi(true);
        yetkisizSY.setOturumAcikMi(true);
        yetkisizSY.setRol(Rol.CALISAN); // yanlis rol, SISTEM_YONETICISI olmali
        Calisan denemeCalisan = new Calisan();
        denemeCalisan.setKullaniciId(204);
        denemeCalisan.setKullaniciAdi("deneme");
        boolean yetkisizHesap = yetkisizSY.kullaniciHesabiOlustur(denemeCalisan);
        System.out.println("  Sonuc: " + yetkisizHesap);
        if (yetkisizHesap) System.out.println("  Uyari: Bu rol hesap olusturmamaliydi.");

        // Rol degistirme
        System.out.println("4) Sistem yoneticisi ile rol atama:");
        boolean rolDegisti = sy.rolAta(yeniCalisan, Rol.DEPARTMAN_YONETICISI, new IslemGecmisiLog());
        System.out.println("  Sonuc: " + rolDegisti + " | Yeni rol: " + yeniCalisan.getRol());
        if (!rolDegisti) System.out.println("  Uyari: Rol atanamadi.");

        // Oturum kapali iken profil null donmeli
        System.out.println("5) Oturum kapaliyken profil goruntuleme engeli:");
        sy.cikisYap();
        String profilKapali = sy.profilBilgisiGetir();
        System.out.println("  Profil: " + profilKapali);
        if (profilKapali != null) System.out.println("  Uyari: Oturum kapaliyken profil gorunmemeli.");
    }

    // ==================== IK-03 ====================
    // Ahmad Alamin - 230404933
    private static void testIK03(SistemAyarlari ayarlar) {
        System.out.println("\n--- IK-03: Konum Dogrulama ve Dogruluk Payi ---");

        // Sirket yakinindaki gecerli konum
        System.out.println("1) Sirket siniri ici gecerli konum:");
        KonumBilgisi gecerliKonum = new KonumBilgisi(41.0085, 28.9788, 15.0, LocalDateTime.now());
        boolean konumGecerli = KonumServisi.konumDogrula(gecerliKonum, ayarlar);
        System.out.println("  konumDogrula: " + konumGecerli);
        if (!konumGecerli) System.out.println("  Uyari: Gecerli konum reddedildi.");

        // Uzak konum reddedilmeli
        System.out.println("2) Sirket disi uzak konum engeli:");
        KonumBilgisi uzakKonum = new KonumBilgisi(41.0200, 28.9950, 20.0, LocalDateTime.now());
        boolean konumRed = KonumServisi.konumDogrula(uzakKonum, ayarlar);
        System.out.println("  konumDogrula: " + konumRed);
        if (konumRed) System.out.println("  Uyari: Uzak konum kabul edilmemeli.");

        // null konum reddedilmeli
        System.out.println("3) Bos (null) konum kontrolu:");
        boolean konumNull = KonumServisi.konumDogrula(null, ayarlar);
        System.out.println("  konumDogrula: " + konumNull);
        if (konumNull) System.out.println("  Uyari: null konum kabul edilmemeli.");

        // Buyuk dogruluk payi: mesafe + pay > yaricap
        System.out.println("4) Dogruluk payi yuksek konum engeli:");
        KonumBilgisi buyukPayKonum = new KonumBilgisi(41.0085, 28.9788, 90.0, LocalDateTime.now());
        boolean buyukPay = KonumServisi.konumDogrula(buyukPayKonum, ayarlar);
        System.out.println("  konumDogrula: " + buyukPay);

        // Mesafe hesabi kontrolu
        double mesafe = KonumServisi.mesafeHesapla(
                new KonumBilgisi(41.0082, 28.9784, 5.0, LocalDateTime.now()),
                new KonumBilgisi(41.0085, 28.9788, 5.0, LocalDateTime.now()));
        System.out.println("5) Iki nokta arasi mesafe hesabi: " + String.format("%.2f", mesafe) + " m");
    }

    // ==================== IK-04 ====================
    // Ahmad Alamin - 230404933
    private static void testIK04(SistemAyarlari ayarlar) {
        System.out.println("\n--- IK-04: Sunucu Zaman Dilimi (Europe/Istanbul) ---");

        // Sunucu zamani Istanbul diliminde olmali
        System.out.println("1) Sunucu zaman dilimi kontrolu:");
        ZonedDateTime sunucuZamani = ayarlar.sunucuZamaniGetir();
        boolean istanbulDilimi = "Europe/Istanbul".equals(sunucuZamani.getZone().getId());
        System.out.println("  Zaman dilimi: " + sunucuZamani.getZone() + " | Istanbul: " + istanbulDilimi);
        if (!istanbulDilimi) System.out.println("  Uyari: Zaman dilimi Europe/Istanbul olmali.");

        // Giris saatinden once cikis reddedilmeli
        System.out.println("2) Giristen once cikis zamani engeli:");
        KonumBilgisi konum = new KonumBilgisi(41.0085, 28.9788, 10.0, LocalDateTime.now());
        DevamKaydi testKayit = new DevamKaydi();
        LocalDateTime girisSaati = sunucuZamani.toLocalDateTime();
        testKayit.girisKaydet(girisSaati, konum, ayarlar);
        boolean erkenCikis = testKayit.cikisKaydet(girisSaati.minusMinutes(10), konum);
        System.out.println("  Sonuc: " + erkenCikis);
        if (erkenCikis) System.out.println("  Uyari: Cikis giris saatinden once olamaz.");

        // Gecerli cikis zamani
        System.out.println("3) Gecerli cikis zamani (8 saat sonra):");
        boolean gecerliCikis = testKayit.cikisKaydet(girisSaati.plusHours(8), konum);
        System.out.println("  Sonuc: " + gecerliCikis);
        if (!gecerliCikis) System.out.println("  Uyari: Gecerli cikis reddedildi.");
    }

    // ==================== IK-05 ====================
    // Ahmad Alamin - 230404933
    private static void testIK05(SistemAyarlari ayarlar) {
        System.out.println("\n--- IK-05: Gunluk Tek Giris ve Cikis ---");

        KonumBilgisi konum = new KonumBilgisi(41.0085, 28.9788, 10.0, LocalDateTime.now());
        Calisan calisan = denemeOturumuAc(new Calisan(), 501, Rol.CALISAN);

        // Ilk giris kaydedilmeli
        System.out.println("1) Gunun ilk giris kaydi:");
        DevamKaydi kayit1 = calisan.iseGirisYap(konum, ayarlar);
        System.out.println("  Kayit olusturuldu mu: " + (kayit1 != null));
        if (kayit1 == null) System.out.println("  Uyari: Ilk giris kaydi olusturulamadi.");

        // Ayni gun ikinci giris engellenmeli
        System.out.println("2) Ayni gun ikinci giris engeli:");
        DevamKaydi kayit2 = calisan.iseGirisYap(konum, ayarlar);
        System.out.println("  Ikinci giris engellendi mi: " + (kayit2 == null));
        if (kayit2 != null) System.out.println("  Uyari: Ayni gun iki kez giris yapilamaliydi.");

        // Cikis kaydedilmeli
        System.out.println("3) Gunun cikis kaydi:");
        DevamKaydi cikisKaydi = calisan.iseCikisYap(konum, ayarlar);
        System.out.println("  Cikis kaydi olusturuldu mu: " + (cikisKaydi != null));
        if (cikisKaydi == null) System.out.println("  Uyari: Cikis kaydi olusturulamadi.");

        // Ayni gun ikinci cikis engellenmeli
        System.out.println("4) Ayni gun ikinci cikis engeli:");
        DevamKaydi cikis2 = calisan.iseCikisYap(konum, ayarlar);
        System.out.println("  Ikinci cikis engellendi mi: " + (cikis2 == null));
        if (cikis2 != null) System.out.println("  Uyari: Ayni gun iki kez cikis yapilamaliydi.");
    }

    // ==================== IK-06 ====================
    // Ahmad Alamin - 230404933
    private static void testIK06(SistemAyarlari ayarlar) {
        System.out.println("\n--- IK-06: Devamsiz Kaydi ve Calisma Gunu ---");

        // Persembe calisma gunu olmali
        System.out.println("1) Calisma gunu (Persembe) kontrolu:");
        LocalDate persembe = LocalDate.of(2026, 11, 26);
        boolean calismaGunuMu = ayarlar.calismaGunuMu(persembe);
        System.out.println("  Sonuc: " + calismaGunuMu);
        if (!calismaGunuMu) System.out.println("  Uyari: Persembe calisma gunu olmali.");

        // Pazar calisma gunu degil
        System.out.println("2) Tatil gunu (Pazar) kontrolu:");
        LocalDate pazar = LocalDate.of(2026, 11, 29);
        boolean pazarCalisma = ayarlar.calismaGunuMu(pazar);
        System.out.println("  Sonuc: " + pazarCalisma);
        if (pazarCalisma) System.out.println("  Uyari: Pazar tatil gunu sayilmali.");

        // Devamsiz kaydi olusturma
        System.out.println("3) Devamsiz olarak isaretleme:");
        DevamKaydi devamsizKayit = new DevamKaydi();
        devamsizKayit.setKayitId(601);
        devamsizKayit.setCalisanId(601);
        devamsizKayit.devamsizOlarakIsaretle(persembe);
        System.out.println("  Durum: " + devamsizKayit.getDurum());
        if (devamsizKayit.getDurum() != DevamDurumu.DEVAMSIZ)
            System.out.println("  Uyari: Durum DEVAMSIZ olmaliydi.");

        // Giris yapilmis gunde devamsiz isaretlenemez
        System.out.println("4) Giris kaydi olan gunu devamsiz yapma engeli:");
        DevamKaydi girisliKayit = new DevamKaydi();
        girisliKayit.setKayitId(602);
        girisliKayit.setCalisanId(602);
        KonumBilgisi konum = new KonumBilgisi(41.0085, 28.9788, 10.0, LocalDateTime.now());
        girisliKayit.girisKaydet(persembe.atTime(9, 0), konum, ayarlar);
        DevamDurumu oncekiDurum = girisliKayit.getDurum();
        girisliKayit.devamsizOlarakIsaretle(persembe);
        System.out.println("  Durum korundu mu: " + (girisliKayit.getDurum() == oncekiDurum));
        if (girisliKayit.getDurum() == DevamDurumu.DEVAMSIZ)
            System.out.println("  Uyari: Girisli kayit devamsiz yapilmamaliydi.");
    }

    // ==================== IK-07 ====================
    // Mohamad BIRAHJAKLI - 230404939
    private static void testIK07(SistemAyarlari ayarlar) {
        System.out.println("\n--- IK-07: Devam Duzeltme ve Islem Gecmisi ---");
        ik07DevamDuzeltmeSenaryolari(ayarlar);
    }

    // ==================== IK-08 ====================
    // Adnan Dervis - 230404935
    private static void testIK08(SistemAyarlari ayarlar) {
        System.out.println("\n--- IK-08: Izin Talebi ve Cakisma Kontrolu ---");

        Departman dept = new Departman(8, "Teknoloji", 801, true, LocalDate.now());
        Calisan calisan = denemeOturumuAc(new Calisan(), 802, Rol.CALISAN);
        calisan.setDepartman(dept);
        calisan.setDepartmanId(8);
        dept.getCalisanlar().add(calisan);

        LocalDate bas1 = LocalDate.of(2027, 1, 10);
        LocalDate bit1 = LocalDate.of(2027, 1, 15);

        // Gecerli tarih araliginda ilk izin talebi
        System.out.println("1) Gecerli tarih araliginda izin talebi:");
        IzinTalebi talep1 = calisan.izinTalebiOlustur(bas1, bit1, IzinTuru.YILLIK, "Yillik izin");
        System.out.println("  Talep olustu mu: " + (talep1 != null) + " | Durum: " + (talep1 != null ? talep1.getDurum() : "-"));
        if (talep1 == null) System.out.println("  Uyari: Izin talebi olusturulamadi.");
        if (talep1 != null && talep1.getDurum() != IzinDurumu.BEKLIYOR)
            System.out.println("  Uyari: Baslangic durumu BEKLIYOR olmali.");

        // Cakisan tarih araliginda ikinci talep engellenmeli
        System.out.println("2) Cakisan tarih araliginda talep engeli:");
        IzinTalebi talep2 = calisan.izinTalebiOlustur(LocalDate.of(2027, 1, 12), LocalDate.of(2027, 1, 20), IzinTuru.MAZERET, "Cakisan");
        System.out.println("  Engellendi mi: " + (talep2 == null));
        if (talep2 != null) System.out.println("  Uyari: Cakisan izin kabul edilmemeli.");

        // Bitis tarihi baslangictan once - reddedilmeli
        System.out.println("3) Bitis tarihi baslangictan once kontrolu:");
        IzinTalebi talep3 = calisan.izinTalebiOlustur(LocalDate.of(2027, 2, 20), LocalDate.of(2027, 2, 10), IzinTuru.SAGLIK, "Yanlis tarih");
        System.out.println("  Engellendi mi: " + (talep3 == null));
        if (talep3 != null) System.out.println("  Uyari: Ters tarih kabul edilmemeli.");

        // Cakismayan farkli tarihte talep
        System.out.println("4) Cakismayan farkli tarih araligi:");
        IzinTalebi talep4 = calisan.izinTalebiOlustur(LocalDate.of(2027, 2, 1), LocalDate.of(2027, 2, 5), IzinTuru.UCRETSIZ, "Diger izin");
        System.out.println("  Talep olustu mu: " + (talep4 != null));
        if (talep4 == null) System.out.println("  Uyari: Gecerli ikinci talep olusturulamadi.");
    }

    // ==================== IK-09 ====================
    // Adnan Dervis - 230404935
    private static void testIK09(SistemAyarlari ayarlar) {
        System.out.println("\n--- IK-09: Izin Degerlendirme ve Oz-Onay Engeli ---");

        Departman dept = new Departman(9, "Finans", 901, true, LocalDate.now());
        Calisan calisan = denemeOturumuAc(new Calisan(), 902, Rol.CALISAN);
        calisan.setDepartmanId(9); calisan.setDepartman(dept);
        DepartmanYoneticisi yonetici = denemeOturumuAc(new DepartmanYoneticisi(), 901, Rol.DEPARTMAN_YONETICISI);
        yonetici.setYonetilenDepartmanId(9);
        yonetici.setDepartman(dept); yonetici.setDepartmanId(9);
        dept.getCalisanlar().add(calisan);
        dept.getCalisanlar().add(yonetici);

        IzinTalebi calisanTalebi = new IzinTalebi(1, 902, LocalDate.of(2027, 3, 1),
                LocalDate.of(2027, 3, 5), IzinTuru.YILLIK, "Yillik", IzinDurumu.BEKLIYOR,
                0, null, LocalDateTime.now(), null);
        calisan.getIzinTalepleri().add(calisanTalebi);

        // Yonetici calisanin talebini onayliyor
        System.out.println("1) Yonetici calisan talebini onayliyor:");
        boolean onaylandi = yonetici.izinTalebiDegerlendir(calisanTalebi, true, null);
        System.out.println("  Sonuc: " + onaylandi + " | Durum: " + calisanTalebi.getDurum());
        if (!onaylandi || calisanTalebi.getDurum() != IzinDurumu.ONAYLANDI)
            System.out.println("  Uyari: Izin onayi basarisiz.");

        // Yonetici kendi talebini onaylayamaz
        IzinTalebi bekleyenTalep = new IzinTalebi(2, 902, LocalDate.of(2027, 4, 1),
                LocalDate.of(2027, 4, 3), IzinTuru.MAZERET, "Mazeret", IzinDurumu.BEKLIYOR,
                0, null, LocalDateTime.now(), null);

        System.out.println("2) Yoneticinin kendi talebini onaylama engeli:");
        IzinTalebi kendi = new IzinTalebi(3, 901, LocalDate.of(2027, 5, 1),
                LocalDate.of(2027, 5, 3), IzinTuru.YILLIK, "Kendim", IzinDurumu.BEKLIYOR,
                0, null, LocalDateTime.now(), null);
        boolean kendi_onay = yonetici.izinTalebiDegerlendir(kendi, true, null);
        System.out.println("  Sonuc: " + kendi_onay);
        if (kendi_onay) System.out.println("  Uyari: Kendi talebi onaylanamaz.");

        // Gerekcesiz ret denemesi
        System.out.println("3) Gerekce olmadan ret engeli:");
        boolean bosBosRet = yonetici.izinTalebiDegerlendir(bekleyenTalep, false, "");
        System.out.println("  Sonuc: " + bosBosRet);
        if (bosBosRet) System.out.println("  Uyari: Gerekcesiz ret kabul edilmemeli.");

        // Gerekce ile ret
        System.out.println("4) Gerekce belirterek ret:");
        boolean gerekceliRet = yonetici.izinTalebiDegerlendir(bekleyenTalep, false, "Kritik proje donemi");
        System.out.println("  Sonuc: " + gerekceliRet + " | Durum: " + bekleyenTalep.getDurum());
        if (!gerekceliRet || bekleyenTalep.getDurum() != IzinDurumu.REDDEDILDI)
            System.out.println("  Uyari: Gerekce ile ret basarisiz.");
    }

    // ==================== IK-10 ====================
    // Adnan Dervis - 230404935
    private static void testIK10(SistemAyarlari ayarlar) {
        System.out.println("\n--- IK-10: Izin Iptal Kurallari ---");

        Calisan calisan = denemeOturumuAc(new Calisan(), 1001, Rol.CALISAN);
        SistemYoneticisi sy = denemeOturumuAc(new SistemYoneticisi(), 1002, Rol.SISTEM_YONETICISI);

        // BEKLIYOR durumunda talep - calisan direkt iptal edebilir
        IzinTalebi bekleyenTalep = new IzinTalebi(10, 1001, LocalDate.of(2027, 6, 1),
                LocalDate.of(2027, 6, 5), IzinTuru.YILLIK, "Test", IzinDurumu.BEKLIYOR,
                0, null, LocalDateTime.now(), null);
        calisan.getIzinTalepleri().add(bekleyenTalep);

        System.out.println("1) Calisanin bekleyen talebini iptal etmesi:");
        boolean iptal1 = calisan.izinTalebiIptalEt(bekleyenTalep);
        System.out.println("  Iptal: " + iptal1 + " | Durum: " + bekleyenTalep.getDurum());
        if (!iptal1 || bekleyenTalep.getDurum() != IzinDurumu.IPTAL_EDILDI)
            System.out.println("  Uyari: BEKLIYOR talep iptal edilemedi.");

        // ONAYLANDI talebi calisan direkt iptal edemez
        IzinTalebi onaylananTalep = new IzinTalebi(11, 1001, LocalDate.of(2027, 7, 1),
                LocalDate.of(2027, 7, 5), IzinTuru.MAZERET, "Onaylanmis", IzinDurumu.ONAYLANDI,
                1002, null, LocalDateTime.now(), null);
        calisan.getIzinTalepleri().add(onaylananTalep);

        System.out.println("2) Onayli talebi dogrudan iptal engeli:");
        boolean iptal2 = onaylananTalep.iptalEt(1001, false);
        System.out.println("  Sonuc: " + iptal2 + " | Durum: " + onaylananTalep.getDurum());
        if (iptal2) System.out.println("  Uyari: Onaylanmis talep dogrudan iptal edilmemeli.");

        // Sistem yoneticisi ONAYLANDI talebini iptal edebilir
        System.out.println("3) Sistem yoneticisi onayi ile iptal:");
        boolean iptal3 = sy.onayliIzinIptalOnayla(onaylananTalep, "Acil gorevlendirme");
        System.out.println("  Iptal: " + iptal3 + " | Durum: " + onaylananTalep.getDurum());
        if (!iptal3 || onaylananTalep.getDurum() != IzinDurumu.IPTAL_EDILDI)
            System.out.println("  Uyari: Yonetici iptal onayi basarisiz.");

        // REDDEDILDI talep iptal edilemez
        System.out.println("4) Reddedilen talebi iptal engeli:");
        IzinTalebi reddedilenTalep = new IzinTalebi(12, 1001, LocalDate.of(2027, 8, 1),
                LocalDate.of(2027, 8, 3), IzinTuru.UCRETSIZ, "Red", IzinDurumu.REDDEDILDI,
                1002, "Sebep", LocalDateTime.now(), null);
        boolean iptal4 = sy.onayliIzinIptalOnayla(reddedilenTalep, "Deneme");
        System.out.println("  Sonuc: " + iptal4);
        if (iptal4) System.out.println("  Uyari: Reddedilmis talep iptal edilemez.");
    }

    // ==================== IK-11 ====================
    // Youssef AYOUBI - 230404906
    private static void testIK11(SistemAyarlari ayarlar) {
        System.out.println("\n--- IK-11: Gorev Atama Kurallari ---");

        Departman dept = new Departman(11, "Muhendislik", 1101, true, LocalDate.now());
        DepartmanYoneticisi yonetici = denemeOturumuAc(new DepartmanYoneticisi(), 1101, Rol.DEPARTMAN_YONETICISI);
        yonetici.setYonetilenDepartmanId(11); yonetici.setDepartman(dept); yonetici.setDepartmanId(11);

        Calisan aktifCalisan = denemeOturumuAc(new Calisan(), 1102, Rol.CALISAN);
        aktifCalisan.setDepartmanId(11); aktifCalisan.setDepartman(dept);

        // Pasif calisan - gorev atanamaz
        Calisan pasifCalisan = new Calisan();
        pasifCalisan.setKullaniciId(1103); pasifCalisan.setAktifMi(false);
        pasifCalisan.setDepartmanId(11); pasifCalisan.setDepartman(dept);

        // Farkli departman calisani - gorev atanamaz
        Calisan farkliDept = denemeOturumuAc(new Calisan(), 1104, Rol.CALISAN);
        farkliDept.setDepartmanId(99);

        dept.getCalisanlar().add(aktifCalisan);
        dept.getCalisanlar().add(pasifCalisan);

        Gorev gorev1 = new Gorev(1, "API Gelistirme", "REST API tasarla", Oncelik.YUKSEK,
                LocalDate.now().plusDays(30), 11, 0, 1101, GorevDurumu.YENI, null, null);
        Gorev gorevFarkliDept = new Gorev(2, "Muhasebe Raporu", "Rapor hazirla", Oncelik.ORTA,
                LocalDate.now().plusDays(10), 99, 0, 1101, GorevDurumu.YENI, null, null);

        // Ayni departmandaki aktif calisana atama - basarili olmali
        System.out.println("1) Ayni departmandaki aktif calisana gorev atama:");
        boolean atama1 = yonetici.gorevAta(gorev1, aktifCalisan);
        System.out.println("  Sonuc: " + atama1 + " | Gorev durumu: " + gorev1.getDurum());
        if (!atama1) System.out.println("  Uyari: Gecerli atama basarisiz oldu.");

        // Farkli departman calisanina atama engellenmeli
        Gorev gorev2 = new Gorev(3, "Yeni Gorev", "Aciklama", Oncelik.DUSUK, LocalDate.now().plusDays(20), 11, 0, 1101, GorevDurumu.YENI, null, null);
        System.out.println("2) Farkli departman calisanina atama engeli:");
        boolean atama2 = yonetici.gorevAta(gorev2, farkliDept);
        System.out.println("  Sonuc: " + atama2);
        if (atama2) System.out.println("  Uyari: Farkli departman calisanina atama olmamali.");

        // Pasif calisana atama engellenmeli
        System.out.println("3) Pasif calisana gorev atama engeli:");
        boolean atama3 = yonetici.gorevAta(gorev2, pasifCalisan);
        System.out.println("  Sonuc: " + atama3);
        if (atama3) System.out.println("  Uyari: Pasif calisana gorev atanamamali.");

        // Farkli departmana ait gorevi atama engellenmeli
        System.out.println("4) Baska departman gorevini atama engeli:");
        boolean atama4 = yonetici.gorevAta(gorevFarkliDept, aktifCalisan);
        System.out.println("  Sonuc: " + atama4);
        if (atama4) System.out.println("  Uyari: Baska departman gorevi atanamamali.");
    }

    // ==================== IK-12 ====================
    // Youssef AYOUBI - 230404906
    private static void testIK12(SistemAyarlari ayarlar) {
        System.out.println("\n--- IK-12: Gorev Yasam Dongusu ve Inceleme ---");

        Departman dept = new Departman(12, "AR-GE", 1201, true, LocalDate.now());
        DepartmanYoneticisi yonetici = denemeOturumuAc(new DepartmanYoneticisi(), 1201, Rol.DEPARTMAN_YONETICISI);
        yonetici.setYonetilenDepartmanId(12); yonetici.setDepartman(dept); yonetici.setDepartmanId(12);

        // Yanlis sorumlu - bu yonetici gorevi onaylayamaz
        DepartmanYoneticisi yanlisSorumlu = denemeOturumuAc(new DepartmanYoneticisi(), 1205, Rol.DEPARTMAN_YONETICISI);
        yanlisSorumlu.setYonetilenDepartmanId(12);

        Calisan calisan = denemeOturumuAc(new Calisan(), 1202, Rol.CALISAN);
        calisan.setDepartmanId(12); calisan.setDepartman(dept);
        dept.getCalisanlar().add(calisan);
        dept.getCalisanlar().add(yonetici);

        Gorev gorev = new Gorev(12, "Makine Ogrenimi Modeli", "ML modeli egit", Oncelik.ACIL,
                LocalDate.now().plusDays(60), 12, 0, 1201, GorevDurumu.YENI, null, null);
        yonetici.gorevAta(gorev, calisan);

        // Calisan DEVAM_EDIYOR yapabilir
        System.out.println("1) Gorevi DEVAM_EDIYOR durumuna gecirme:");
        boolean devamEdiyor = calisan.gorevDurumuGuncelle(12, GorevDurumu.DEVAM_EDIYOR, "Basladim");
        System.out.println("  Sonuc: " + devamEdiyor + " | Durum: " + gorev.getDurum());
        if (!devamEdiyor) System.out.println("  Uyari: DEVAM_EDIYOR yapilamadi.");

        // Calisan TAMAMLANDI yapamaz - inceleme sorumlusu yapabilir
        System.out.println("2) Calisanin direkt TAMAMLANDI yapma engeli:");
        boolean tamamlandi = calisan.gorevDurumuGuncelle(12, GorevDurumu.TAMAMLANDI, "Bitti");
        System.out.println("  Sonuc: " + tamamlandi);
        if (tamamlandi) System.out.println("  Uyari: Calisan TAMAMLANDI yapamamali.");

        // INCELEME_BEKLIYOR durumuna gecis
        System.out.println("3) Gorevi incelemeye gonderme:");
        boolean inceleme = calisan.gorevDurumuGuncelle(12, GorevDurumu.INCELEME_BEKLIYOR, "Incelemeye hazir");
        System.out.println("  Sonuc: " + inceleme + " | Durum: " + gorev.getDurum());
        if (!inceleme) System.out.println("  Uyari: INCELEME_BEKLIYOR yapilamadi.");

        // Yanlis sorumlu onay veremez
        System.out.println("4) Yetkisiz sorumlunun onay engeli:");
        boolean yanlisTamamla = yanlisSorumlu.goreviInceleVeOnayla(gorev, true, "Tamam");
        System.out.println("  Sonuc: " + yanlisTamamla);
        if (yanlisTamamla) System.out.println("  Uyari: Yanlis sorumlu onay vermemeli.");

        // Gerekcesiz duzeltme istegi reddedilmeli
        System.out.println("5) Gerekcesiz duzeltme istegi engeli:");
        boolean bosDuzeltme = yonetici.goreviInceleVeOnayla(gorev, false, "");
        System.out.println("  Sonuc: " + bosDuzeltme);
        if (bosDuzeltme) System.out.println("  Uyari: Gerekcesiz ret olmamali.");

        // Gerekce ile duzeltme istegi
        System.out.println("6) Gerekce ile duzeltme istegi:");
        boolean duzeltme = yonetici.goreviInceleVeOnayla(gorev, false, "Dogruluk orani yetersiz");
        System.out.println("  Sonuc: " + duzeltme + " | Durum: " + gorev.getDurum());
        if (!duzeltme) System.out.println("  Uyari: Duzeltme istegi basarisiz.");

        // Duzeltme sonrasi tekrar inceleme ve TAMAMLANDI onayi
        System.out.println("7) Duzeltme sonrasi tekrar inceleme ve onay:");
        calisan.gorevDurumuGuncelle(12, GorevDurumu.INCELEME_BEKLIYOR, "Duzeltildi");
        boolean finalOnay = yonetici.goreviInceleVeOnayla(gorev, true, "Mukemmel calisma");
        System.out.println("  Sonuc: " + finalOnay + " | Durum: " + gorev.getDurum());
        if (!finalOnay || gorev.getDurum() != GorevDurumu.TAMAMLANDI)
            System.out.println("  Uyari: Final TAMAMLANDI onayi basarisiz.");
    }

    // ==================== IK-13 ====================
    // Ahmad Alamin - 230404933 / Mohamad BIRAHJAKLI - 230404939
    private static void testIK13(SistemAyarlari ayarlar) {
        System.out.println("\n--- IK-13: Puanlama (Erken Giris, Gecikme, Onay) ---");

        SistemYoneticisi yonetici = denemeOturumuAc(new SistemYoneticisi(), 1301, Rol.SISTEM_YONETICISI);
        KonumBilgisi konum = new KonumBilgisi(41.0085, 28.9788, 10.0, LocalDateTime.now());

        // 08:00 girisi - mesai 09:00, esik 60 dk - ERKEN + adayYesilPuan=1 bekleniyor
        System.out.println("1) Erken giris (08:00, esik=60dk):");
        DevamKaydi erkenKayit = new DevamKaydi();
        erkenKayit.girisKaydet(LocalDate.of(2025, 11, 5).atTime(8, 0), konum, ayarlar);
        System.out.println("  Durum: " + erkenKayit.getDurum()
                + " | AdayYesil: " + erkenKayit.getAdayYesilPuan()
                + " | KesinlesenYesil: " + erkenKayit.getKesinlesenYesilPuan());
        if (erkenKayit.getDurum() != DevamDurumu.ERKEN || erkenKayit.getAdayYesilPuan() != 1 || erkenKayit.getKesinlesenYesilPuan() != 0)
            System.out.println("  Uyari: Erken giris puanlamasi beklenen gibi degil.");

        // Yonetici erken giris onayliyor - kesinlesenYesilPuan=1 olmali
        System.out.println("2) Yonetici erken giris onayi:");
        boolean onay = yonetici.erkenGirisOnayla(erkenKayit, true);
        System.out.println("  Onaylandi mi: " + onay + " | KesinlesenYesil: " + erkenKayit.getKesinlesenYesilPuan());
        if (!onay || erkenKayit.getKesinlesenYesilPuan() != 1)
            System.out.println("  Uyari: Erken giris onayi basarisiz.");

        // Oturum kapali yonetici onay veremez
        System.out.println("3) Oturumu kapali yonetici onay engeli:");
        SistemYoneticisi yetkisiz = new SistemYoneticisi();
        yetkisiz.setKullaniciId(1399);
        yetkisiz.setAktifMi(true); yetkisiz.setOturumAcikMi(false);
        yetkisiz.setRol(Rol.SISTEM_YONETICISI);
        DevamKaydi erkenKayit2 = new DevamKaydi();
        erkenKayit2.girisKaydet(LocalDate.of(2025, 11, 6).atTime(8, 0), konum, ayarlar);
        boolean yetkisizOnay = yetkisiz.erkenGirisOnayla(erkenKayit2, true);
        System.out.println("  Sonuc: " + yetkisizOnay);
        if (yetkisizOnay) System.out.println("  Uyari: Oturum kapali iken onay verilmemeli.");

        // 11:15 girisi - ASIRI_GEC bekleniyor
        System.out.println("4) Asiri gec giris (11:15):");
        DevamKaydi asiriGecKayit = new DevamKaydi();
        asiriGecKayit.girisKaydet(LocalDate.now().atTime(11, 15), konum, ayarlar);
        System.out.println("  Durum: " + asiriGecKayit.getDurum() + " | KirmiziPuan: " + asiriGecKayit.getKirmiziPuan());
        if (asiriGecKayit.getDurum() != DevamDurumu.ASIRI_GEC)
            System.out.println("  Uyari: 11:15 girisi ASIRI_GEC olmali.");

        // 09:30 girisi - GEC bekleniyor
        System.out.println("5) Hafif gecikme (09:30):");
        DevamKaydi gecKayit = new DevamKaydi();
        gecKayit.girisKaydet(LocalDate.now().atTime(9, 30), konum, ayarlar);
        System.out.println("  Durum: " + gecKayit.getDurum() + " | KirmiziPuan: " + gecKayit.getKirmiziPuan());

        // Eksik sistem ayari ile puanlama - durum null olmali
        System.out.println("6) Eksik sistem ayari ile puanlama kontrolu:");
        DevamKaydi eksikAyarKayit = new DevamKaydi(0, 0, LocalDate.now(), LocalDate.now().atTime(9, 30),
                null, null, null, null, 0, 0, 0, false);
        eksikAyarKayit.durumuVePuanlariHesapla(new SistemAyarlari());
        System.out.println("  Durum: " + eksikAyarKayit.getDurum());
    }

    // ==================== IK-14 ====================
    // Mohamad BIRAHJAKLI - 230404939
    private static void testIK14(SistemAyarlari ayarlar) {
        System.out.println("\n--- IK-14: Aylik Mali Ozet ve Donem Kilidi ---");
        ik14AylikMaliOzetSenaryolari(ayarlar);
    }

    // ==================== YARDIMCI METOTLAR ====================

    // Mohamad BIRAHJAKLI - 230404939
    private static void ik07DevamDuzeltmeSenaryolari(SistemAyarlari ayarlar) {
        LocalDate tarih = LocalDate.of(2025, 11, 5);
        DevamKaydi kayit = ornekDevamKaydi(1, 701, tarih, DevamDurumu.ZAMANINDA, 0, 0);
        Calisan calisan = denemeOturumuAc(new Calisan(List.of(kayit)), 701, Rol.CALISAN);
        SistemYoneticisi yonetici = denemeOturumuAc(new SistemYoneticisi(), 702, Rol.SISTEM_YONETICISI);
        // yetkisiz: Rol=CALISAN atandi - karari uygulayamaz
        SistemYoneticisi yetkisiz = denemeOturumuAc(new SistemYoneticisi(), 703, Rol.CALISAN);

        // Kayit listesi disaridan degistirilemez olmali (IK-07)
        try {
            calisan.getDevamKayitlari().clear();
            throw new AssertionError("IK-07: Kayit listesi disaridan degistirildi.");
        } catch (UnsupportedOperationException beklenen) {
            senaryoDogrula(calisan.getDevamKayitlari().size() == 1, "IK-07 kayit listesi korumasi");
        }

        DevamDuzeltmeTalebi talep = calisan.devamDuzeltmeTalebiOlustur(tarih, DuzeltmeTuru.EKSIK_CIKIS,
                "Cikis kaydi eksik.");
        senaryoDogrula(talep != null && talep.getDurum() == TalepDurumu.BEKLIYOR, "IK-07 talep olusturma");
        IslemGecmisiLog onayLogu = new IslemGecmisiLog();
        LocalDateTime cikis = tarih.atTime(17, 0);

        // Yetkisiz kullanici karar veremez
        senaryoDogrula(!yetkisiz.devamDuzeltmeTalebiDegerlendir(talep, true, null, cikis, "Yetkisiz onay.",
                null, ayarlar)
                && talep.getDurum() == TalepDurumu.BEKLIYOR && kayit.getCikisZamani() == null,
                "IK-07 yonetici karari olmadan degisiklik yok");

        // Yonetici onayi - kayit guncellenmeli
        senaryoDogrula(yonetici.devamDuzeltmeTalebiDegerlendir(talep, true, null, cikis, "Kayit dogrulandi.",
                onayLogu, ayarlar) && cikis.equals(kayit.getCikisZamani())
                && talep.getDurum() == TalepDurumu.ONAYLANDI && onayLogu.getEskiDeger().contains("Cikis=null")
                && onayLogu.getYeniDeger().contains(cikis.toString())
                && onayLogu.getIslemYapanKullaniciId() == yonetici.getKullaniciId()
                && onayLogu.getIslemZamani().equals(talep.getKararZamani()), "IK-07 onay ve log");

        int onaySonrasiLogSayisi = IslemGecmisiLog.getKayitlar().size();

        // Tekrar onay denemesi - engellenmeli
        senaryoDogrula(!yonetici.devamDuzeltmeTalebiDegerlendir(talep, true, null, cikis.plusHours(1),
                "Tekrar onay.", null, ayarlar)
                && cikis.equals(kayit.getCikisZamani())
                && IslemGecmisiLog.getKayitlar().size() == onaySonrasiLogSayisi, "IK-07 tekrar onay engeli");

        System.out.println("Bilgi: Duzeltme onayi -> " + talep.getDurum()
                + " | Eski: " + onayLogu.getEskiDeger()
                + " | Yeni: " + onayLogu.getYeniDeger());

        DevamDuzeltmeTalebi retTalebi = calisan.devamDuzeltmeTalebiOlustur(tarih, DuzeltmeTuru.KONUM_HATASI,
                "Konum hatasinin incelenmesini istiyorum.");
        int logSayisi = IslemGecmisiLog.getKayitlar().size();

        // Bos gerekce ile ret olmaz
        senaryoDogrula(!yonetici.devamDuzeltmeTalebiDegerlendir(retTalebi, false, null, null, " ", null, ayarlar)
                && retTalebi.getDurum() == TalepDurumu.BEKLIYOR
                && IslemGecmisiLog.getKayitlar().size() == logSayisi, "IK-07 bos karar gerekcesi");

        senaryoDogrula(!yonetici.devamDuzeltmeTalebiDegerlendir(retTalebi, true, null, tarih.atTime(8, 0),
                "Gecersiz saat denemesi.", null, ayarlar), "IK-07 hatali duzeltme saati");

        senaryoDogrula(!yonetici.devamDuzeltmeTalebiDegerlendir(retTalebi, true, null, cikis,
                "Ayni saati uygulama denemesi.", null, ayarlar), "IK-07 degisiklik icermeyen duzeltme");

        // Gerekce ile ret - basarili olmali
        IslemGecmisiLog retLogu = new IslemGecmisiLog();
        senaryoDogrula(yonetici.devamDuzeltmeTalebiDegerlendir(retTalebi, false, null, null, "Hata dogrulanamadi.",
                retLogu, ayarlar) && retTalebi.getDurum() == TalepDurumu.REDDEDILDI
                && retLogu.getEskiDeger().equals(retLogu.getYeniDeger()) && cikis.equals(kayit.getCikisZamani())
                && IslemGecmisiLog.getKayitlar().size() == logSayisi + 1, "IK-07 ret ve ret logu");

        System.out.println("Bilgi: Ret gerekcesi -> " + retTalebi.getKararGerekcesi() + " | Kayit degismedi.");

        // Eksik giris talebi - yeni kayit eklenebilmeli
        LocalDate oncekiGun = tarih.minusDays(1);
        DevamDuzeltmeTalebi eksikGiris = calisan.devamDuzeltmeTalebiOlustur(oncekiGun, DuzeltmeTuru.EKSIK_GIRIS,
                "Giris kaydi eksik.");
        senaryoDogrula(eksikGiris != null && calisan.getDevamKayitlari().size() == 1
                && yonetici.devamDuzeltmeTalebiDegerlendir(eksikGiris, true, oncekiGun.atTime(10, 0), null,
                "Giris saati dogrulandi.", null, ayarlar) && calisan.getDevamKayitlari().size() == 2
                && calisan.getDevamKayitlari().get(1).getKirmiziPuan() == 1, "IK-07 eksik giris ve puan hesabi");
    }

    // Mohamad BIRAHJAKLI - 230404939
    private static void ik14AylikMaliOzetSenaryolari(SistemAyarlari ayarlar) {
        int id = 1401;
        LocalDate ay = LocalDate.of(2025, 11, 1);
        List<DevamKaydi> kayitlar = new ArrayList<>();

        // 7 erken giris kaydi - hepsi kesinlesmis yesil puan
        for (int i = 0; i < 7; i++) {
            kayitlar.add(ornekDevamKaydi(i + 1, id, ay.plusDays(i + 2), DevamDurumu.ERKEN, 1, 0));
        }
        // Aday puan - henuz kesinlesmemis
        DevamKaydi adayKayit = ornekDevamKaydi(8, id, ay.plusDays(9), DevamDurumu.ERKEN, 0, 0);
        adayKayit.setAdayYesilPuan(1);
        kayitlar.add(adayKayit);
        kayitlar.add(ornekDevamKaydi(9, id, ay.plusDays(10), DevamDurumu.GEC, 0, 1));
        kayitlar.add(ornekDevamKaydi(10, id, ay.plusDays(11), DevamDurumu.GEC, 0, 1));
        kayitlar.add(ornekDevamKaydi(11, id, ay.plusDays(12), DevamDurumu.DEVAMSIZ, 0, 0));
        kayitlar.add(ornekDevamKaydi(12, id, ay.plusDays(13), DevamDurumu.GEC, 0, 1));
        // Sonraki ay icin kayitlar
        for (int i = 0; i < 3; i++) {
            kayitlar.add(ornekDevamKaydi(13 + i, id, ay.plusMonths(1).plusDays(i), DevamDurumu.ERKEN, 1, 0));
        }

        Calisan calisan = denemeOturumuAc(new Calisan(kayitlar), id, Rol.CALISAN);
        IKVeHesapSorumlusu sorumlu = denemeOturumuAc(new IKVeHesapSorumlusu(), 1402, Rol.IK_HESAP_SORUMLUSU);

        // Onaylanmis izin talebi ekle (2 gun izin)
        IzinTalebi izin = new IzinTalebi();
        izin.setCalisanId(id);
        izin.setBaslangicTarihi(ay.plusDays(12));
        izin.setBitisTarihi(ay.plusDays(13));
        izin.setDurum(IzinDurumu.ONAYLANDI);
        calisan.getIzinTalepleri().add(izin);

        // Kasim 2025 mali ozet hesapla ve kilitle
        AylikMaliOzet ozet = sorumlu.aylikMaliOzetOlusturVeKilitle(calisan, 2025, 11, ayarlar);
        senaryoDogrula(ozet != null && ozet.isKilitliMi() && ozet.getToplamYesilPuan() == 7
                && ozet.getToplamKirmiziPuan() == 2 && ozet.getToplamIzinliGun() == 2
                && ozet.getToplamDevamsizGun() == 0 && ozet.getOrnekPrimTutari() == 500.0
                && ozet.getOrnekKesintiTutari() == 200.0 && ozet.getNetMaliFark() == 300.0, "IK-14 ornek hesap");

        senaryoDogrula(sorumlu.sirketDevamRaporuAl(kayitlar, ay, ay.plusDays(29)).size() == 12
                && calisan.kendiRaporunuGoruntule(2025, 11).contains("Izinli gun: 2"), "Kasim devam raporlari");

        System.out.printf("Bilgi: Prim=%.2f TL | Kesinti=%.2f TL | Net fark=%.2f TL%n",
                ozet.getOrnekPrimTutari(), ozet.getOrnekKesintiTutari(), ozet.getNetMaliFark());

        SistemAyarlari yeniTarife = new SistemAyarlari();
        yeniTarife.setYesilPuanPrimTutari(750.0);
        DevamKaydi duzeltilenKayit = kayitlar.get(0);
        LocalDate duzeltmeTarihi = duzeltilenKayit.getTarih();
        SistemYoneticisi yonetici = denemeOturumuAc(new SistemYoneticisi(), 1403, Rol.SISTEM_YONETICISI);

        DevamDuzeltmeTalebi talep = calisan.devamDuzeltmeTalebiOlustur(duzeltmeTarihi, DuzeltmeTuru.KONUM_HATASI,
                "Giris saati kontrol edilsin.");
        senaryoDogrula(talep != null && yonetici.devamDuzeltmeTalebiDegerlendir(talep, true,
                duzeltmeTarihi.atTime(9, 0), null, "Giris saati dogrulandi.", null, ayarlar)
                && duzeltilenKayit.getKesinlesenYesilPuan() == 0
                && !duzeltilenKayit.isErkenGirisOnaylandiMi(), "IK-14 kilit sonrasi devam duzeltmesi");

        // Kilitli ay tekrar hesaplanmaz - onceki sonuc doner
        senaryoDogrula(sorumlu.aylikMaliOzetOlusturVeKilitle(calisan, 2025, 11, yeniTarife) == ozet
                && ozet.getOrnekPrimTutari() == 500.0 && ozet.getToplamYesilPuan() == 7, "IK-14 kilitli sonuc");

        // Sonraki ay hesapla - puan devralmaz
        AylikMaliOzet sonrakiAy = sorumlu.aylikMaliOzetOlusturVeKilitle(calisan, 2025, 12, yeniTarife);
        senaryoDogrula(sonrakiAy != null && sonrakiAy.getToplamYesilPuan() == 3
                && sonrakiAy.getOrnekPrimTutari() == 0.0, "IK-14 puan devri yok");

        // Kilitli donem uzerinde hesaplama denemesi - istisna firlatmali
        try {
            ozet.maliOzetHesapla(kayitlar, calisan.getIzinTalepleri(), calisan.getTemelUcret(), yeniTarife);
            throw new AssertionError("IK-14: Kilitli donem yeniden hesaplanmamali.");
        } catch (IllegalStateException hata) {
            System.out.println("Uyari: Kilitli donem korumasi -> " + hata.getMessage());
        }
    }

    // Test icin oturum acan ve kullanici bilgilerini set eden yardimci metot
    // Mohamad BIRAHJAKLI - 230404939
    private static <T extends Kullanici> T denemeOturumuAc(T kullanici, int id, Rol rol) {
        kullanici.setKullaniciId(id);
        kullanici.setKullaniciAdi("ornek-" + id);
        kullanici.setAd("Ornek");
        kullanici.setSoyad(rol.name());
        kullanici.setSifreHash("ornek-sifre");
        kullanici.setAktifMi(true);
        kullanici.setRol(rol);
        senaryoDogrula(kullanici.girisYap(kullanici.getKullaniciAdi(), "ornek-sifre"), "Deneme oturumu");
        return kullanici;
    }

    // Ornek DevamKaydi olusturan yardimci metot
    // Mohamad BIRAHJAKLI - 230404939
    private static DevamKaydi ornekDevamKaydi(int id, int calisanId, LocalDate tarih,
                                              DevamDurumu durum, int yesilPuan, int kirmiziPuan) {
        LocalDateTime giris = switch (durum) {
            case ERKEN -> tarih.atTime(8, 0);
            case GEC -> tarih.atTime(10, 0);
            case ASIRI_GEC -> tarih.atTime(11, 0);
            case ZAMANINDA -> tarih.atTime(9, 0);
            case DEVAMSIZ -> null;
        };
        return new DevamKaydi(id, calisanId, tarih, giris, null, null, null, durum,
                yesilPuan, yesilPuan, kirmiziPuan, yesilPuan > 0);
    }

    // Test senaryosu dogrulama - basarisiz olursa AssertionError firlat
    // Mohamad BIRAHJAKLI - 230404939
    private static void senaryoDogrula(boolean kabul, String senaryo) {
        if (!kabul) {
            throw new AssertionError(senaryo);
        }
    }
}
