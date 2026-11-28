// Mohamad BIRAHJAKLI - 230404939
package models;

import enums.DevamDurumu;
import enums.IzinDurumu;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AylikMaliOzet {
    private static final int PRIM_ICIN_YESIL_PUAN = 5;

    private int ozetId;
    private int calisanId;
    private int yil;
    private int ay;
    private int toplamYesilPuan;
    private int toplamKirmiziPuan;
    private int toplamIzinliGun;
    private int toplamDevamsizGun;
    private double temelUcret;
    private double ornekPrimTutari;
    private double ornekKesintiTutari;
    private double netMaliFark;
    private boolean kilitliMi;
    private LocalDateTime olusturmaZamani;

    public AylikMaliOzet() {
    }

    public AylikMaliOzet(int ozetId, int calisanId, int yil, int ay, int toplamYesilPuan,
                         int toplamKirmiziPuan, int toplamIzinliGun, int toplamDevamsizGun,
                         double temelUcret, double ornekPrimTutari, double ornekKesintiTutari,
                         double netMaliFark, boolean kilitliMi, LocalDateTime olusturmaZamani) {
        this.ozetId = ozetId;
        this.calisanId = calisanId;
        this.yil = yil;
        this.ay = ay;
        this.toplamYesilPuan = toplamYesilPuan;
        this.toplamKirmiziPuan = toplamKirmiziPuan;
        this.toplamIzinliGun = toplamIzinliGun;
        this.toplamDevamsizGun = toplamDevamsizGun;
        this.temelUcret = temelUcret;
        this.ornekPrimTutari = ornekPrimTutari;
        this.ornekKesintiTutari = ornekKesintiTutari;
        this.netMaliFark = netMaliFark;
        this.kilitliMi = kilitliMi;
        this.olusturmaZamani = olusturmaZamani;
    }

    public int getOzetId() {
        return ozetId;
    }

    public void setOzetId(int ozetId) {
        kilitliOlmadiginiDogrula();
        this.ozetId = ozetId;
    }

    public int getCalisanId() {
        return calisanId;
    }

    public void setCalisanId(int calisanId) {
        kilitliOlmadiginiDogrula();
        this.calisanId = calisanId;
    }

    public int getYil() {
        return yil;
    }

    public void setYil(int yil) {
        kilitliOlmadiginiDogrula();
        this.yil = yil;
    }

    public int getAy() {
        return ay;
    }

    public void setAy(int ay) {
        kilitliOlmadiginiDogrula();
        this.ay = ay;
    }

    public int getToplamYesilPuan() {
        return toplamYesilPuan;
    }

    public void setToplamYesilPuan(int toplamYesilPuan) {
        kilitliOlmadiginiDogrula();
        this.toplamYesilPuan = toplamYesilPuan;
    }

    public int getToplamKirmiziPuan() {
        return toplamKirmiziPuan;
    }

    public void setToplamKirmiziPuan(int toplamKirmiziPuan) {
        kilitliOlmadiginiDogrula();
        this.toplamKirmiziPuan = toplamKirmiziPuan;
    }

    public int getToplamIzinliGun() {
        return toplamIzinliGun;
    }

    public void setToplamIzinliGun(int toplamIzinliGun) {
        kilitliOlmadiginiDogrula();
        this.toplamIzinliGun = toplamIzinliGun;
    }

    public int getToplamDevamsizGun() {
        return toplamDevamsizGun;
    }

    public void setToplamDevamsizGun(int toplamDevamsizGun) {
        kilitliOlmadiginiDogrula();
        this.toplamDevamsizGun = toplamDevamsizGun;
    }

    public double getTemelUcret() {
        return temelUcret;
    }

    public void setTemelUcret(double temelUcret) {
        kilitliOlmadiginiDogrula();
        this.temelUcret = temelUcret;
    }

    public double getOrnekPrimTutari() {
        return ornekPrimTutari;
    }

    public void setOrnekPrimTutari(double ornekPrimTutari) {
        kilitliOlmadiginiDogrula();
        this.ornekPrimTutari = ornekPrimTutari;
    }

    public double getOrnekKesintiTutari() {
        return ornekKesintiTutari;
    }

    public void setOrnekKesintiTutari(double ornekKesintiTutari) {
        kilitliOlmadiginiDogrula();
        this.ornekKesintiTutari = ornekKesintiTutari;
    }

    public double getNetMaliFark() {
        return netMaliFark;
    }

    public void setNetMaliFark(double netMaliFark) {
        kilitliOlmadiginiDogrula();
        this.netMaliFark = netMaliFark;
    }

    public boolean isKilitliMi() {
        return kilitliMi;
    }

    public void setKilitliMi(boolean kilitliMi) {
        if (kilitliMi) {
            ozetiKilitle();
        } else {
            kilitliOlmadiginiDogrula();
        }
    }

    public LocalDateTime getOlusturmaZamani() {
        return olusturmaZamani;
    }

    public void setOlusturmaZamani(LocalDateTime olusturmaZamani) {
        kilitliOlmadiginiDogrula();
        this.olusturmaZamani = olusturmaZamani;
    }

    // Mohamad BIRAHJAKLI - 230404939
    public void maliOzetHesapla(List<DevamKaydi> kayitlar, List<IzinTalebi> izinler,
                                double temelUcret, SistemAyarlari ayarlar) {
        kilitliOlmadiginiDogrula();
        if (calisanId <= 0 || !donemGecerliMi(yil, ay) || ayarlar == null) {
            throw new IllegalArgumentException("IK-14: Gecerli bir calisan, donem ve sistem ayarlari gereklidir.");
        }
        if (!gecerliTutarMi(temelUcret) || !gecerliTutarMi(ayarlar.getYesilPuanPrimTutari())
                || !gecerliTutarMi(ayarlar.getKirmiziPuanKesintiTutari())) {
            throw new IllegalArgumentException("IK-14: Ucret, prim ve kesinti tutarlari negatif veya gecersiz olamaz.");
        }

        YearMonth donem = YearMonth.of(yil, ay);
        Set<LocalDate> izinliGunler = onayliIzinGunleriniBul(izinler, donem);
        Set<LocalDate> devamsizGunler = new HashSet<>();
        int yesilPuan = 0;
        int kirmiziPuan = 0;
        for (DevamKaydi kayit : kayitlar == null ? List.<DevamKaydi>of() : kayitlar) {
            if (kayit == null || kayit.getCalisanId() != calisanId || kayit.getTarih() == null
                    || !YearMonth.from(kayit.getTarih()).equals(donem)) {
                continue;
            }
            yesilPuan += kayit.getKesinlesenYesilPuan();
            // IK-10: Onayli izin gunu devamsizlik veya gecikme kesintisi olusturmaz.
            if (!izinliGunler.contains(kayit.getTarih())) {
                kirmiziPuan += kayit.getKirmiziPuan();
                if (kayit.getDurum() == DevamDurumu.DEVAMSIZ) {
                    devamsizGunler.add(kayit.getTarih());
                }
            }
        }

        // IK-14: Sadece bu ayin kesinlesen besli yesil puan gruplarina prim verilir.
        BigDecimal prim = tutarHesapla(ayarlar.getYesilPuanPrimTutari(), yesilPuan / PRIM_ICIN_YESIL_PUAN);
        BigDecimal kesinti = tutarHesapla(ayarlar.getKirmiziPuanKesintiTutari(), kirmiziPuan);
        this.olusturmaZamani = ayarlar.sunucuZamaniGetir().toLocalDateTime();
        this.toplamYesilPuan = yesilPuan;
        this.toplamKirmiziPuan = kirmiziPuan;
        this.toplamIzinliGun = izinliGunler.size();
        this.toplamDevamsizGun = devamsizGunler.size();
        this.temelUcret = temelUcret;
        this.ornekPrimTutari = prim.doubleValue();
        this.ornekKesintiTutari = kesinti.doubleValue();
        this.netMaliFark = prim.subtract(kesinti).doubleValue();
    }

    // Mohamad BIRAHJAKLI - 230404939
    public void ozetiKilitle() {
        // IK-14: Kesinlesen donem sonraki tarife veya kayit degisikliklerinden etkilenmez.
        if (olusturmaZamani == null) {
            throw new IllegalStateException("IK-14: Kilitlemeden once mali ozet hesaplanmalidir.");
        }
        kilitliMi = true;
    }

    static boolean donemGecerliMi(int yil, int ay) {
        return yil >= 1 && yil <= 9999 && ay >= 1 && ay <= 12;
    }

    private Set<LocalDate> onayliIzinGunleriniBul(List<IzinTalebi> izinler, YearMonth donem) {
        Set<LocalDate> gunler = new HashSet<>();
        LocalDate ayBaslangici = donem.atDay(1);
        LocalDate aySonu = donem.atEndOfMonth();
        for (IzinTalebi izin : izinler == null ? List.<IzinTalebi>of() : izinler) {
            if (izin == null || izin.getCalisanId() != calisanId || izin.getDurum() != IzinDurumu.ONAYLANDI
                    || izin.getBaslangicTarihi() == null || izin.getBitisTarihi() == null) {
                continue;
            }
            LocalDate baslangic = izin.getBaslangicTarihi();
            LocalDate bitis = izin.getBitisTarihi();
            if (baslangic.isBefore(ayBaslangici)) {
                baslangic = ayBaslangici;
            }
            if (bitis.isAfter(aySonu)) {
                bitis = aySonu;
            }
            for (LocalDate tarih = baslangic; !tarih.isAfter(bitis); tarih = tarih.plusDays(1)) {
                gunler.add(tarih);
            }
        }
        return gunler;
    }

    private static boolean gecerliTutarMi(double tutar) {
        return Double.isFinite(tutar) && tutar >= 0;
    }

    private static BigDecimal tutarHesapla(double birimTutar, int adet) {
        return BigDecimal.valueOf(birimTutar).multiply(BigDecimal.valueOf(adet)).setScale(2, RoundingMode.HALF_UP);
    }

    private void kilitliOlmadiginiDogrula() {
        if (kilitliMi) {
            throw new IllegalStateException("IK-14: Kilitli mali ozet degistirilemez.");
        }
    }
}
