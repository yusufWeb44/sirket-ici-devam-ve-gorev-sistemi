// Youssef AYOUBI - 230404906
// Ahmad Alamin - 230404933
// Adnan Derviş - 230404935
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
import enums.IzinDurumu;
import enums.Rol;
import enums.TalepDurumu;
import models.*;
import services.*;

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
        ik07DevamDuzeltmeSenaryolari(ayarlar);

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
        // Mohamad BIRAHJAKLI - 230404939
        SistemYoneticisi erkenGirisYoneticisi = denemeOturumuAc(new SistemYoneticisi(), 1301, Rol.SISTEM_YONETICISI);
        erkenKayit.girisKaydet(LocalDate.of(2025, 11, 5).atTime(8, 0), gecerliKonum, ayarlar);
        senaryoDogrula(erkenKayit.getAdayYesilPuan() == 1 && erkenKayit.getKesinlesenYesilPuan() == 0
                && erkenGirisYoneticisi.erkenGirisOnayla(erkenKayit, true)
                && erkenKayit.getKesinlesenYesilPuan() == 1, "IK-13 erken giris onayi");
        System.out.println("[IK-13 - BASARILI ERKEN] Durum = " + erkenKayit.getDurum()
                + ", Aday Yesil = " + erkenKayit.getAdayYesilPuan()
                + ", Kesinlesen Yesil = " + erkenKayit.getKesinlesenYesilPuan());

        DevamKaydi asiriGecKayit = new DevamKaydi();
        asiriGecKayit.girisKaydet(LocalDate.now().atTime(11, 15), gecerliKonum, ayarlar);
        System.out.println("[IK-13 - BASARILI ASIRI GEC] Durum = " + asiriGecKayit.getDurum()
                + ", Kirmizi Puan = " + asiriGecKayit.getKirmiziPuan());

        // Reddedilen:
        SistemAyarlari eksikAyar = new SistemAyarlari();
        LocalDate denemeTarihi = LocalDate.now();
        DevamKaydi puanRedKayit = new DevamKaydi(0, 0, denemeTarihi, denemeTarihi.atTime(9, 30), null,
                null, null, null, 0, 0, 0, false);
        puanRedKayit.durumuVePuanlariHesapla(eksikAyar);
        System.out.println("[IK-13 - REDDEDILEN] Eksik ayar sonrasi durum = " + puanRedKayit.getDurum());

        // ==================== IK-14 ====================
        ik14AylikMaliOzetSenaryolari(ayarlar);
    }

    // Mohamad BIRAHJAKLI - 230404939
    private static void ik07DevamDuzeltmeSenaryolari(SistemAyarlari ayarlar) {
        System.out.println("\n--- [IK-07] DEVAM DUZELTME VE ISLEM GECMISI ---");
        LocalDate tarih = LocalDate.of(2025, 11, 5);
        DevamKaydi kayit = ornekDevamKaydi(1, 701, tarih, DevamDurumu.ZAMANINDA, 0, 0);
        Calisan calisan = denemeOturumuAc(new Calisan(List.of(kayit)), 701, Rol.CALISAN);
        SistemYoneticisi yonetici = denemeOturumuAc(new SistemYoneticisi(), 702, Rol.SISTEM_YONETICISI);
        SistemYoneticisi yetkisiz = denemeOturumuAc(new SistemYoneticisi(), 703, Rol.CALISAN);

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
        senaryoDogrula(!yetkisiz.devamDuzeltmeTalebiDegerlendir(talep, true, null, cikis, "Yetkisiz onay.",
                null, ayarlar)
                && talep.getDurum() == TalepDurumu.BEKLIYOR && kayit.getCikisZamani() == null,
                "IK-07 yonetici karari olmadan degisiklik yok");
        senaryoDogrula(yonetici.devamDuzeltmeTalebiDegerlendir(talep, true, null, cikis, "Kayit dogrulandi.",
                onayLogu, ayarlar) && cikis.equals(kayit.getCikisZamani())
                && talep.getDurum() == TalepDurumu.ONAYLANDI && onayLogu.getEskiDeger().contains("Cikis=null")
                && onayLogu.getYeniDeger().contains(cikis.toString())
                && onayLogu.getIslemYapanKullaniciId() == yonetici.getKullaniciId()
                && onayLogu.getIslemZamani().equals(talep.getKararZamani()), "IK-07 onay ve log");
        int onaySonrasiLogSayisi = IslemGecmisiLog.getKayitlar().size();
        senaryoDogrula(!yonetici.devamDuzeltmeTalebiDegerlendir(talep, true, null, cikis.plusHours(1),
                "Tekrar onay.", null, ayarlar)
                && cikis.equals(kayit.getCikisZamani())
                && IslemGecmisiLog.getKayitlar().size() == onaySonrasiLogSayisi, "IK-07 tekrar onay engeli");
        System.out.println("[IK-07 - BASARILI] " + talep.getDurum() + " | Eski: " + onayLogu.getEskiDeger()
                + " | Yeni: " + onayLogu.getYeniDeger());

        DevamDuzeltmeTalebi retTalebi = calisan.devamDuzeltmeTalebiOlustur(tarih, DuzeltmeTuru.KONUM_HATASI,
                "Konum hatasinin incelenmesini istiyorum.");
        int logSayisi = IslemGecmisiLog.getKayitlar().size();
        senaryoDogrula(!yonetici.devamDuzeltmeTalebiDegerlendir(retTalebi, false, null, null, " ", null, ayarlar)
                && retTalebi.getDurum() == TalepDurumu.BEKLIYOR
                && IslemGecmisiLog.getKayitlar().size() == logSayisi, "IK-07 bos karar gerekcesi");
        senaryoDogrula(!yonetici.devamDuzeltmeTalebiDegerlendir(retTalebi, true, null, tarih.atTime(8, 0),
                "Gecersiz saat denemesi.", null, ayarlar), "IK-07 hatali duzeltme saati");
        senaryoDogrula(!yonetici.devamDuzeltmeTalebiDegerlendir(retTalebi, true, null, cikis,
                "Ayni saati uygulama denemesi.", null, ayarlar), "IK-07 degisiklik icermeyen duzeltme");
        IslemGecmisiLog retLogu = new IslemGecmisiLog();
        senaryoDogrula(yonetici.devamDuzeltmeTalebiDegerlendir(retTalebi, false, null, null, "Hata dogrulanamadi.",
                retLogu, ayarlar) && retTalebi.getDurum() == TalepDurumu.REDDEDILDI
                && retLogu.getEskiDeger().equals(retLogu.getYeniDeger()) && cikis.equals(kayit.getCikisZamani())
                && IslemGecmisiLog.getKayitlar().size() == logSayisi + 1, "IK-07 ret ve ret logu");
        System.out.println("[IK-07 - REDDEDILEN] " + retTalebi.getKararGerekcesi() + " | Kayit degismedi.");

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
        System.out.println("\n--- [IK-14] AYLIK MALI OZET VE DONEM KILIDI ---");
        int id = 1401;
        LocalDate ay = LocalDate.of(2025, 11, 1);
        List<DevamKaydi> kayitlar = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            kayitlar.add(ornekDevamKaydi(i + 1, id, ay.plusDays(i + 2), DevamDurumu.ERKEN, 1, 0));
        }
        DevamKaydi adayKayit = ornekDevamKaydi(8, id, ay.plusDays(9), DevamDurumu.ERKEN, 0, 0);
        adayKayit.setAdayYesilPuan(1);
        kayitlar.add(adayKayit);
        kayitlar.add(ornekDevamKaydi(9, id, ay.plusDays(10), DevamDurumu.GEC, 0, 1));
        kayitlar.add(ornekDevamKaydi(10, id, ay.plusDays(11), DevamDurumu.GEC, 0, 1));
        kayitlar.add(ornekDevamKaydi(11, id, ay.plusDays(12), DevamDurumu.DEVAMSIZ, 0, 0));
        kayitlar.add(ornekDevamKaydi(12, id, ay.plusDays(13), DevamDurumu.GEC, 0, 1));
        for (int i = 0; i < 3; i++) {
            kayitlar.add(ornekDevamKaydi(13 + i, id, ay.plusMonths(1).plusDays(i), DevamDurumu.ERKEN, 1, 0));
        }
        Calisan calisan = denemeOturumuAc(new Calisan(kayitlar), id, Rol.CALISAN);
        IKVeHesapSorumlusu sorumlu = denemeOturumuAc(new IKVeHesapSorumlusu(), 1402, Rol.IK_HESAP_SORUMLUSU);
        IzinTalebi izin = new IzinTalebi();
        izin.setCalisanId(id);
        izin.setBaslangicTarihi(ay.plusDays(12));
        izin.setBitisTarihi(ay.plusDays(13));
        izin.setDurum(IzinDurumu.ONAYLANDI);
        calisan.getIzinTalepleri().add(izin);

        AylikMaliOzet ozet = sorumlu.aylikMaliOzetOlusturVeKilitle(calisan, 2025, 11, ayarlar);
        senaryoDogrula(ozet != null && ozet.isKilitliMi() && ozet.getToplamYesilPuan() == 7
                && ozet.getToplamKirmiziPuan() == 2 && ozet.getToplamIzinliGun() == 2
                && ozet.getToplamDevamsizGun() == 0 && ozet.getOrnekPrimTutari() == 500.0
                && ozet.getOrnekKesintiTutari() == 200.0 && ozet.getNetMaliFark() == 300.0, "IK-14 ornek hesap");
        senaryoDogrula(sorumlu.sirketDevamRaporuAl(kayitlar, ay, ay.plusDays(29)).size() == 12
                && calisan.kendiRaporunuGoruntule(2025, 11).contains("Izinli gun: 2"), "Kasım devam raporlari");
        System.out.printf("[IK-14 - BASARILI] Prim %.2f TL | Kesinti %.2f TL | Net fark %.2f TL%n",
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
        senaryoDogrula(sorumlu.aylikMaliOzetOlusturVeKilitle(calisan, 2025, 11, yeniTarife) == ozet
                && ozet.getOrnekPrimTutari() == 500.0 && ozet.getToplamYesilPuan() == 7, "IK-14 kilitli sonuc");
        AylikMaliOzet sonrakiAy = sorumlu.aylikMaliOzetOlusturVeKilitle(calisan, 2025, 12, yeniTarife);
        senaryoDogrula(sonrakiAy != null && sonrakiAy.getToplamYesilPuan() == 3
                && sonrakiAy.getOrnekPrimTutari() == 0.0, "IK-14 puan devri yok");
        try {
            ozet.maliOzetHesapla(kayitlar, calisan.getIzinTalepleri(), calisan.getTemelUcret(), yeniTarife);
            throw new AssertionError("IK-14: Kilitli donem yeniden hesaplanmamali.");
        } catch (IllegalStateException hata) {
            System.out.println("[IK-14 - REDDEDILEN] " + hata.getMessage());
        }
    }

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

    // Mohamad BIRAHJAKLI - 230404939
    private static void senaryoDogrula(boolean kabul, String senaryo) {
        if (!kabul) {
            throw new AssertionError(senaryo);
        }
    }
}
