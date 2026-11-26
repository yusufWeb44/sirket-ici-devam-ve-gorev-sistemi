// Mohamad BIRAHJAKLI - 230404939
package models;

import java.time.LocalDateTime;
import java.util.List;

public class AylikMaliOzet {
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
        this.ozetId = ozetId;
    }

    public int getCalisanId() {
        return calisanId;
    }

    public void setCalisanId(int calisanId) {
        this.calisanId = calisanId;
    }

    public int getYil() {
        return yil;
    }

    public void setYil(int yil) {
        this.yil = yil;
    }

    public int getAy() {
        return ay;
    }

    public void setAy(int ay) {
        this.ay = ay;
    }

    public int getToplamYesilPuan() {
        return toplamYesilPuan;
    }

    public void setToplamYesilPuan(int toplamYesilPuan) {
        this.toplamYesilPuan = toplamYesilPuan;
    }

    public int getToplamKirmiziPuan() {
        return toplamKirmiziPuan;
    }

    public void setToplamKirmiziPuan(int toplamKirmiziPuan) {
        this.toplamKirmiziPuan = toplamKirmiziPuan;
    }

    public int getToplamIzinliGun() {
        return toplamIzinliGun;
    }

    public void setToplamIzinliGun(int toplamIzinliGun) {
        this.toplamIzinliGun = toplamIzinliGun;
    }

    public int getToplamDevamsizGun() {
        return toplamDevamsizGun;
    }

    public void setToplamDevamsizGun(int toplamDevamsizGun) {
        this.toplamDevamsizGun = toplamDevamsizGun;
    }

    public double getTemelUcret() {
        return temelUcret;
    }

    public void setTemelUcret(double temelUcret) {
        this.temelUcret = temelUcret;
    }

    public double getOrnekPrimTutari() {
        return ornekPrimTutari;
    }

    public void setOrnekPrimTutari(double ornekPrimTutari) {
        this.ornekPrimTutari = ornekPrimTutari;
    }

    public double getOrnekKesintiTutari() {
        return ornekKesintiTutari;
    }

    public void setOrnekKesintiTutari(double ornekKesintiTutari) {
        this.ornekKesintiTutari = ornekKesintiTutari;
    }

    public double getNetMaliFark() {
        return netMaliFark;
    }

    public void setNetMaliFark(double netMaliFark) {
        this.netMaliFark = netMaliFark;
    }

    public boolean isKilitliMi() {
        return kilitliMi;
    }

    public void setKilitliMi(boolean kilitliMi) {
        this.kilitliMi = kilitliMi;
    }

    public LocalDateTime getOlusturmaZamani() {
        return olusturmaZamani;
    }

    public void setOlusturmaZamani(LocalDateTime olusturmaZamani) {
        this.olusturmaZamani = olusturmaZamani;
    }

    public void maliOzetHesapla(List<DevamKaydi> kayitlar, List<IzinTalebi> izinler,
                                double temelUcret, SistemAyarlari ayarlar) {
        // TODO: Implement business logic
    }

    public void ozetiKilitle() {
        // TODO: Implement business logic
    }
}
