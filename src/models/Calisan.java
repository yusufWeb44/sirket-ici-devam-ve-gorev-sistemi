// Ortak Sinif - Gorev Dagilimi:
// - Youssef AYOUBI - 230404906 (Sinif yapisi ve gorevDurumuGuncelle)
// - Ahmad Alamin - 230404933 (iseGirisYap, iseCikisYap, gunlukGirisKontrolu)
// - Adnan Dervis - 230404935 (izinTalebiOlustur, izinCakismasiVarMi, izinTalebiIptalEt)
// - Mohamad BIRAHJAKLI - 230404939 (devamDuzeltmeTalebiOlustur, kendiRaporunuGoruntule)
package models;

import enums.BildirimTuru;
import enums.DuzeltmeTuru;
import enums.GorevDurumu;
import enums.IzinDurumu;
import enums.IzinTuru;
import enums.Rol;
import enums.TalepDurumu;
import services.KonumServisi;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Calisan extends Kullanici {
    // Adnan Dervis - 230404935
    private static int izinIdSayaci = 1;
    protected int departmanId;
    protected Departman departman;
    protected String unvan;
    protected double temelUcret;
    private List<DevamKaydi> devamKayitlari;
    protected List<IzinTalebi> izinTalepleri;
    protected List<Gorev> atananGorevler;
    private final List<DevamDuzeltmeTalebi> duzeltmeTalepleri;
    protected List<Bildirim> bildirimler;

    public Calisan() {
        this.devamKayitlari = new ArrayList<>();
        this.izinTalepleri = new ArrayList<>();
        this.atananGorevler = new ArrayList<>();
        this.duzeltmeTalepleri = new ArrayList<>();
        this.bildirimler = new ArrayList<>();
    }

    // Mohamad BIRAHJAKLI - 230404939
    public Calisan(List<DevamKaydi> oncekiKayitlar) {
        this();
        this.devamKayitlari.addAll(List.copyOf(oncekiKayitlar));
    }

    public Calisan(int kullaniciId, String kullaniciAdi, String sifreHash, String ad, String soyad,
                   String eposta, String telefon, Rol rol, boolean aktifMi,
                   LocalDateTime olusturmaTarihi, LocalDateTime sonGirisTarihi,
                   int departmanId, Departman departman, String unvan, double temelUcret) {
        super(kullaniciId, kullaniciAdi, sifreHash, ad, soyad, eposta, telefon, rol, aktifMi,
                olusturmaTarihi, sonGirisTarihi);
        this.departmanId = departmanId;
        this.departman = departman;
        this.unvan = unvan;
        this.temelUcret = temelUcret;
        this.devamKayitlari = new ArrayList<>();
        this.izinTalepleri = new ArrayList<>();
        this.atananGorevler = new ArrayList<>();
        this.duzeltmeTalepleri = new ArrayList<>();
        this.bildirimler = new ArrayList<>();
    }

    public int getDepartmanId() {
        return departmanId;
    }

    public void setDepartmanId(int departmanId) {
        this.departmanId = departmanId;
    }

    public Departman getDepartman() {
        return departman;
    }

    public void setDepartman(Departman departman) {
        this.departman = departman;
    }

    public String getUnvan() {
        return unvan;
    }

    public void setUnvan(String unvan) {
        this.unvan = unvan;
    }

    public double getTemelUcret() {
        return temelUcret;
    }

    public void setTemelUcret(double temelUcret) {
        this.temelUcret = temelUcret;
    }

    // Mohamad BIRAHJAKLI - 230404939
    public synchronized List<DevamKaydi> getDevamKayitlari() {
        return List.copyOf(devamKayitlari);
    }

    public List<IzinTalebi> getIzinTalepleri() {
        return izinTalepleri;
    }

    public void setIzinTalepleri(List<IzinTalebi> izinTalepleri) {
        this.izinTalepleri = izinTalepleri;
    }

    public List<Gorev> getAtananGorevler() {
        return atananGorevler;
    }

    public void setAtananGorevler(List<Gorev> atananGorevler) {
        this.atananGorevler = atananGorevler;
    }

    // Mohamad BIRAHJAKLI - 230404939
    public synchronized List<DevamDuzeltmeTalebi> getDuzeltmeTalepleri() {
        return List.copyOf(duzeltmeTalepleri);
    }

    public List<Bildirim> getBildirimler() {
        return bildirimler;
    }

    public void setBildirimler(List<Bildirim> bildirimler) {
        this.bildirimler = bildirimler;
    }

    // Ahmad Alamin - 230404933
    // IK-01, IK-03, IK-04, IK-05: aktif hesap, konum dogrulama ve gunluk tek giris kontrolu
    public DevamKaydi iseGirisYap(KonumBilgisi konum, SistemAyarlari ayarlar) {
        // IK-01: pasif hesap giris yapamaz
        if (!isAktifMi()) {
            System.out.println("Uyari: Pasif hesap ise giris yapamaz.");
            return null;
        }

        if (ayarlar == null) {
            System.out.println("Uyari: Sistem ayarlari bulunamadi.");
            return null;
        }

        // IK-04: sunucu saati
        ZonedDateTime sunucuZamani = ayarlar.sunucuZamaniGetir();
        LocalDate bugun = sunucuZamani.toLocalDate();

        // IK-05: ayni gun ikinci giris engeli
        if (gunlukGirisKontrolu(bugun)) {
            System.out.println("Uyari: Bugun (" + bugun + ") icin zaten giris kaydi var.");
            return null;
        }

        // IK-03: konum siniri kontrolu
        if (!KonumServisi.konumDogrula(konum, ayarlar)) {
            System.out.println("Uyari: Konum dogrulanamadi, ise giris reddedildi.");
            return null;
        }

        if (this.devamKayitlari == null) {
            this.devamKayitlari = new ArrayList<>();
        }

        DevamKaydi yeniKayit = new DevamKaydi();
        yeniKayit.setKayitId(this.devamKayitlari.size() + 1);
        yeniKayit.setCalisanId(getKullaniciId());
        yeniKayit.girisKaydet(sunucuZamani.toLocalDateTime(), konum, ayarlar);

        if (yeniKayit.getGirisZamani() != null) {
            this.devamKayitlari.add(yeniKayit);
            return yeniKayit;
        }

        return null;
    }

    // Ahmad Alamin - 230404933
    // IK-01, IK-03, IK-04, IK-05: aktif giris kaydi varsa konum ve sunucu saatiyle cikis kaydeder
    public DevamKaydi iseCikisYap(KonumBilgisi konum, SistemAyarlari ayarlar) {
        // IK-01: pasif hesap cikis yapamaz
        if (!isAktifMi()) {
            System.out.println("Uyari: Pasif hesap ise cikis yapamaz.");
            return null;
        }

        if (ayarlar == null) {
            System.out.println("Uyari: Sistem ayarlari bulunamadi.");
            return null;
        }

        // IK-04: sunucu saati
        ZonedDateTime sunucuZamani = ayarlar.sunucuZamaniGetir();
        LocalDate bugun = sunucuZamani.toLocalDate();

        if (this.devamKayitlari == null || this.devamKayitlari.isEmpty()) {
            System.out.println("Uyari: Giris kaydi olmadan cikis yapilamaz.");
            return null;
        }

        // Bugune ait giris kaydini bul
        DevamKaydi bugunkuKayit = null;
        for (DevamKaydi kayit : this.devamKayitlari) {
            if (kayit != null && bugun.equals(kayit.getTarih()) && kayit.getGirisZamani() != null) {
                bugunkuKayit = kayit;
                break;
            }
        }

        // IK-05: giris kaydi olmadan cikis yapilamaz
        if (bugunkuKayit == null) {
            System.out.println("Uyari: " + bugun + " icin aktif giris kaydi bulunamadi.");
            return null;
        }

        // IK-05: ayni gun ikinci cikis engeli
        if (bugunkuKayit.getCikisZamani() != null) {
            System.out.println("Uyari: Bugun icin zaten cikis yapilmis.");
            return null;
        }

        // IK-03: konum kontrolu
        if (!KonumServisi.konumDogrula(konum, ayarlar)) {
            System.out.println("Uyari: Cikis konum dogrulamasi basarisiz.");
            return null;
        }

        boolean cikisBasarili = bugunkuKayit.cikisKaydet(sunucuZamani.toLocalDateTime(), konum);
        return cikisBasarili ? bugunkuKayit : null;
    }

    // Ahmad Alamin - 230404933
    // IK-05: verilen tarihte calisana ait giris kaydi olup olmadigini kontrol eder
    public boolean gunlukGirisKontrolu(LocalDate tarih) {
        if (tarih == null) {
            System.out.println("Uyari: Gunluk giris kontrolu icin tarih bos olamaz.");
            return false;
        }
        if (this.devamKayitlari == null || this.devamKayitlari.isEmpty()) {
            return false;
        }
        for (DevamKaydi kayit : this.devamKayitlari) {
            if (kayit != null && tarih.equals(kayit.getTarih()) && kayit.getGirisZamani() != null) {
                return true;
            }
        }
        return false;
    }

    // Adnan Dervis - 230404935
    public IzinTalebi izinTalebiOlustur(LocalDate baslangic, LocalDate bitis, IzinTuru tur, String aciklama) {
        int hedefYoneticiId = (this.departman != null) ? this.departman.getYoneticiId() : 0;
        return izinTalebiOlustur(baslangic, bitis, tur, aciklama, hedefYoneticiId);
    }

    // Adnan Dervis - 230404935
    public IzinTalebi izinTalebiOlustur(int calisanId, LocalDate baslangicTarihi, LocalDate bitisTarihi, IzinTuru izinTuru) {
        if (calisanId != this.kullaniciId) {
            System.out.println("Uyari: Baska bir calisan adina izin talebi olusturulamaz.");
            return null;
        }
        int hedefYoneticiId = (this.departman != null) ? this.departman.getYoneticiId() : 0;
        return izinTalebiOlustur(baslangicTarihi, bitisTarihi, izinTuru, "", hedefYoneticiId);
    }

    // Adnan Dervis - 230404935
    public IzinTalebi izinTalebiOlustur(LocalDate baslangicTarihi, LocalDate bitisTarihi, IzinTuru izinTuru,
                                        String aciklama, int hedefYoneticiId) {
        // IK-08: gecersiz tarih araligi veya cakisan izin kontrolu
        if (baslangicTarihi == null || bitisTarihi == null || bitisTarihi.isBefore(baslangicTarihi)
                || izinCakismasiVarMi(baslangicTarihi, bitisTarihi)) {
            System.out.println("Uyari: Gecersiz veya cakisan izin tarih araligi.");
            return null;
        }

        if (this.izinTalepleri == null) {
            this.izinTalepleri = new ArrayList<>();
        }

        // IK-04: sunucu saati Europe/Istanbul
        LocalDateTime olusturmaZamani = ZonedDateTime.now(ZoneId.of("Europe/Istanbul")).toLocalDateTime();

        IzinTalebi yeniTalep = new IzinTalebi(
                izinIdSayaci++,
                this.kullaniciId,
                baslangicTarihi,
                bitisTarihi,
                izinTuru,
                aciklama,
                IzinDurumu.BEKLIYOR,
                0,
                null,
                olusturmaZamani,
                null
        );

        long gunSayisi = yeniTalep.gunSayisiHesapla();
        this.izinTalepleri.add(yeniTalep);

        Bildirim.bildirimGonder(
                hedefYoneticiId,
                "Yeni Izin Talebi",
                this.ad + " " + this.soyad + " tarafindan " + baslangicTarihi + " - " + bitisTarihi +
                        " tarihleri arasi (" + gunSayisi + " gun, " + izinTuru + ") izin talebi olusturuldu.",
                BildirimTuru.IZIN_KARARI,
                yeniTalep.getIzinId()
        );

        System.out.println("Bilgi: Izin talebi olusturuldu -> ID: " + yeniTalep.getIzinId() +
                ", Sure: " + gunSayisi + " gun, Durum: " + yeniTalep.getDurum());
        return yeniTalep;
    }

    // Adnan Dervis - 230404935
    public boolean izinCakismasiVarMi(LocalDate baslangic, LocalDate bitis) {
        // IK-08: BEKLIYOR veya ONAYLANDI durumundaki izinlerle tarih cakismasi kontrolu
        if (baslangic == null || bitis == null || this.izinTalepleri == null) {
            return false;
        }

        for (IzinTalebi mevcut : this.izinTalepleri) {
            if (mevcut == null || mevcut.getBaslangicTarihi() == null || mevcut.getBitisTarihi() == null) {
                continue;
            }
            if (mevcut.getDurum() == IzinDurumu.BEKLIYOR || mevcut.getDurum() == IzinDurumu.ONAYLANDI) {
                boolean cakismaVar = !(bitis.isBefore(mevcut.getBaslangicTarihi())
                        || baslangic.isAfter(mevcut.getBitisTarihi()));
                if (cakismaVar) {
                    return true;
                }
            }
        }
        return false;
    }

    // Adnan Dervis - 230404935
    public boolean izinTalebiIptalEt(int izinId) {
        if (this.izinTalepleri == null) {
            System.out.println("Uyari: Izin talebi (ID: " + izinId + ") bulunamadi.");
            return false;
        }
        for (IzinTalebi talep : this.izinTalepleri) {
            if (talep != null && talep.getIzinId() == izinId) {
                return izinTalebiIptalEt(talep);
            }
        }
        System.out.println("Uyari: Izin talebi (ID: " + izinId + ") bulunamadi.");
        return false;
    }

    // Adnan Dervis - 230404935
    public boolean izinTalebiIptalEt(IzinTalebi talep) {
        // IK-10: bekleyen izin direkt iptal edilebilir, onayli izin icin yonetici onayi gerekir
        if (talep == null) {
            System.out.println("Uyari: Iptal edilecek izin talebi bulunamadi.");
            return false;
        }
        if (talep.getDurum() == IzinDurumu.BEKLIYOR) {
            return talep.iptalEt(this.kullaniciId, false);
        }
        if (talep.getDurum() == IzinDurumu.ONAYLANDI) {
            return talep.iptalEt(this.kullaniciId, false);
        }
        return talep.iptalEt(this.kullaniciId, false);
    }

    // Youssef AYOUBI - 230404906
    public boolean gorevDurumuGuncelle(int gorevId, GorevDurumu yeniDurum, String aciklama) {
        // IK-01: pasif veya oturumu kapali calisan islem yapamaz
        if (!this.aktifMi || !this.oturumAcikMi) {
            System.out.println("Uyari: Gorev durumu guncellemek icin once oturum acmalisiniz.");
            return false;
        }

        // IK-12 kurali: calisan gorevi dogrudan tamamlandi yapamaz
        if (yeniDurum == GorevDurumu.TAMAMLANDI) {
            System.out.println("Uyari: Gorev dogrudan TAMAMLANDI durumuna gecirilemez, inceleme onayi gerekiyor.");
            return false;
        }

        // Atanan gorevler arasinda hedef gorevi bul
        Gorev hedefGorev = null;
        for (Gorev g : this.atananGorevler) {
            if (g.getGorevId() == gorevId) {
                hedefGorev = g;
                break;
            }
        }

        if (hedefGorev == null) {
            System.out.println("Uyari: Bu gorev (ID: " + gorevId + ") size atanmamis veya bulunamadi.");
            return false;
        }

        // IK-12: calisan gorevi DEVAM_EDIYOR veya INCELEME_BEKLIYOR durumlarina alabilir
        boolean guncellendi = hedefGorev.durumaGecis(yeniDurum, this.kullaniciId, aciklama);
        if (guncellendi && yeniDurum == GorevDurumu.INCELEME_BEKLIYOR) {
            Bildirim.bildirimGonder(
                    hedefGorev.getIncelemeSorumlusuId(),
                    "Gorev Inceleme Bekliyor",
                    this.ad + " " + this.soyad + " '" + hedefGorev.getBaslik() + "' gorevini incelemeye gonderdi.",
                    enums.BildirimTuru.GOREV_INCELEME,
                    hedefGorev.getGorevId()
            );
            System.out.println("Bilgi: Gorev inceleme icin sorumluya iletildi ve bildirim gonderildi.");
        }
        return guncellendi;
    }

    // Mohamad BIRAHJAKLI - 230404939
    public synchronized DevamDuzeltmeTalebi devamDuzeltmeTalebiOlustur(LocalDate tarih, DuzeltmeTuru tur, String gerekce) {
        // IK-01, IK-02, IK-07: calisan kendi kaydi icin gerekceli duzeltme talebi acabilir
        if (!isAktifMi() || !isOturumAcikMi() || getKullaniciId() <= 0 || tarih == null
                || tarih.isAfter(LocalDate.now(ZoneId.of("Europe/Istanbul")))
                || tur == null || gerekce == null || gerekce.isBlank()) {
            System.out.println("Uyari: Duzeltme talebi icin aktif oturum, gecerli tarih ve gerekce gerekiyor.");
            return null;
        }

        DevamKaydi kayit = tarihtekiDevamKaydiniBul(tarih);
        if ((tur == DuzeltmeTuru.EKSIK_GIRIS && kayit != null && kayit.getGirisZamani() != null)
                || (tur == DuzeltmeTuru.EKSIK_CIKIS
                && (kayit == null || kayit.getGirisZamani() == null || kayit.getCikisZamani() != null))) {
            System.out.println("Uyari: Mevcut devam kaydi secilen talep turuyle uyusmuyor.");
            return null;
        }
        if (duzeltmeTalepleri.stream().anyMatch(talep -> tarih.equals(talep.getTarih())
                && tur == talep.getDuzeltmeTuru() && talep.getDurum() == TalepDurumu.BEKLIYOR)) {
            System.out.println("Uyari: Bu tarih ve tur icin zaten bekleyen bir talep var.");
            return null;
        }

        int talepId = duzeltmeTalepleri.stream().mapToInt(DevamDuzeltmeTalebi::getTalepId).max().orElse(0) + 1;
        int kayitId = kayit == null ? 0 : kayit.getKayitId();
        DevamDuzeltmeTalebi talep = new DevamDuzeltmeTalebi(this, talepId, kayitId, tarih, tur, gerekce.strip());
        duzeltmeTalepleri.add(talep);
        return talep;
    }

    // Mohamad BIRAHJAKLI - 230404939
    void onayliDevamKaydiniEkle(DevamKaydi kayit) {
        devamKayitlari.add(kayit);
    }

    // Mohamad BIRAHJAKLI - 230404939
    DevamKaydi tarihtekiDevamKaydiniBul(LocalDate tarih) {
        return devamKayitlari.stream()
                .filter(kayit -> kayit.getCalisanId() == getKullaniciId() && tarih.equals(kayit.getTarih()))
                .findFirst().orElse(null);
    }

    // Mohamad BIRAHJAKLI - 230404939
    public String kendiRaporunuGoruntule(int yil, int ay) {
        // IK-01 ve IK-02: Calisan raporu yalnizca kendi kayitlarindan olusturulur.
        if (!isAktifMi() || !isOturumAcikMi() || getKullaniciId() <= 0) {
            return "RED (IK-02): Rapor icin aktif oturum gereklidir.";
        }
        if (!AylikMaliOzet.donemGecerliMi(yil, ay)) {
            return "Gecersiz yil veya ay.";
        }
        AylikMaliOzet ozet = new AylikMaliOzet();
        ozet.setCalisanId(getKullaniciId());
        ozet.setYil(yil);
        ozet.setAy(ay);
        ozet.maliOzetHesapla(devamKayitlari, izinTalepleri, getTemelUcret(), new SistemAyarlari());
        YearMonth donem = YearMonth.of(yil, ay);
        boolean devamVar = devamKayitlari.stream().anyMatch(kayit -> kayit.getCalisanId() == getKullaniciId()
                && kayit.getTarih() != null && YearMonth.from(kayit.getTarih()).equals(donem));
        if (!devamVar && ozet.getToplamIzinliGun() == 0) {
            return "Kayit bulunamadi.";
        }
        return String.format("%s | Kesinlesen yesil: %d | Kirmizi: %d | Izinli gun: %d | Devamsiz gun: %d",
                donem, ozet.getToplamYesilPuan(), ozet.getToplamKirmiziPuan(), ozet.getToplamIzinliGun(),
                ozet.getToplamDevamsizGun());
    }
}
