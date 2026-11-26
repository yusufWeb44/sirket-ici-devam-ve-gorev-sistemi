// Ahmad Alamin - 230404933
package models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class SistemAyarlari {
    private int ayarId;
    private double sirketEnlem;
    private double sirketBoylam;
    private double kabulYaricapiMetre;
    private double maksimumBelirsizlikMetre;
    private LocalTime mesaiBaslangic;
    private LocalTime mesaiBitis;
    private List<DayOfWeek> calismaGunleri;
    private int yesilPuanEsikDakika = 60;
    private int kirmiziPuanAltDakika = 60;
    private int asiriGecEsikDakika = 120;
    private double yesilPuanPrimTutari = 500.0;
    private double kirmiziPuanKesintiTutari = 100.0;
    private String zamanDilimi = "Europe/Istanbul";

    public SistemAyarlari() {
        this.calismaGunleri = new ArrayList<>();
    }

    public SistemAyarlari(int ayarId, double sirketEnlem, double sirketBoylam, double kabulYaricapiMetre,
                          double maksimumBelirsizlikMetre, LocalTime mesaiBaslangic, LocalTime mesaiBitis,
                          List<DayOfWeek> calismaGunleri, int yesilPuanEsikDakika, int kirmiziPuanAltDakika,
                          int asiriGecEsikDakika, double yesilPuanPrimTutari, double kirmiziPuanKesintiTutari,
                          String zamanDilimi) {
        this.ayarId = ayarId;
        this.sirketEnlem = sirketEnlem;
        this.sirketBoylam = sirketBoylam;
        this.kabulYaricapiMetre = kabulYaricapiMetre;
        this.maksimumBelirsizlikMetre = maksimumBelirsizlikMetre;
        this.mesaiBaslangic = mesaiBaslangic;
        this.mesaiBitis = mesaiBitis;
        this.calismaGunleri = calismaGunleri != null ? new ArrayList<>(calismaGunleri) : new ArrayList<>();
        this.yesilPuanEsikDakika = yesilPuanEsikDakika;
        this.kirmiziPuanAltDakika = kirmiziPuanAltDakika;
        this.asiriGecEsikDakika = asiriGecEsikDakika;
        this.yesilPuanPrimTutari = yesilPuanPrimTutari;
        this.kirmiziPuanKesintiTutari = kirmiziPuanKesintiTutari;
        this.zamanDilimi = zamanDilimi;
    }

    public int getAyarId() {
        return ayarId;
    }

    public void setAyarId(int ayarId) {
        this.ayarId = ayarId;
    }

    public double getSirketEnlem() {
        return sirketEnlem;
    }

    public void setSirketEnlem(double sirketEnlem) {
        this.sirketEnlem = sirketEnlem;
    }

    public double getSirketBoylam() {
        return sirketBoylam;
    }

    public void setSirketBoylam(double sirketBoylam) {
        this.sirketBoylam = sirketBoylam;
    }

    public double getKabulYaricapiMetre() {
        return kabulYaricapiMetre;
    }

    public void setKabulYaricapiMetre(double kabulYaricapiMetre) {
        this.kabulYaricapiMetre = kabulYaricapiMetre;
    }

    public double getMaksimumBelirsizlikMetre() {
        return maksimumBelirsizlikMetre;
    }

    public void setMaksimumBelirsizlikMetre(double maksimumBelirsizlikMetre) {
        this.maksimumBelirsizlikMetre = maksimumBelirsizlikMetre;
    }

    public LocalTime getMesaiBaslangic() {
        return mesaiBaslangic;
    }

    public void setMesaiBaslangic(LocalTime mesaiBaslangic) {
        this.mesaiBaslangic = mesaiBaslangic;
    }

    public LocalTime getMesaiBitis() {
        return mesaiBitis;
    }

    public void setMesaiBitis(LocalTime mesaiBitis) {
        this.mesaiBitis = mesaiBitis;
    }

    public List<DayOfWeek> getCalismaGunleri() {
        return calismaGunleri;
    }

    public void setCalismaGunleri(List<DayOfWeek> calismaGunleri) {
        this.calismaGunleri = calismaGunleri;
    }

    public int getYesilPuanEsikDakika() {
        return yesilPuanEsikDakika;
    }

    public void setYesilPuanEsikDakika(int yesilPuanEsikDakika) {
        this.yesilPuanEsikDakika = yesilPuanEsikDakika;
    }

    public int getKirmiziPuanAltDakika() {
        return kirmiziPuanAltDakika;
    }

    public void setKirmiziPuanAltDakika(int kirmiziPuanAltDakika) {
        this.kirmiziPuanAltDakika = kirmiziPuanAltDakika;
    }

    public int getAsiriGecEsikDakika() {
        return asiriGecEsikDakika;
    }

    public void setAsiriGecEsikDakika(int asiriGecEsikDakika) {
        this.asiriGecEsikDakika = asiriGecEsikDakika;
    }

    public double getYesilPuanPrimTutari() {
        return yesilPuanPrimTutari;
    }

    public void setYesilPuanPrimTutari(double yesilPuanPrimTutari) {
        this.yesilPuanPrimTutari = yesilPuanPrimTutari;
    }

    public double getKirmiziPuanKesintiTutari() {
        return kirmiziPuanKesintiTutari;
    }

    public void setKirmiziPuanKesintiTutari(double kirmiziPuanKesintiTutari) {
        this.kirmiziPuanKesintiTutari = kirmiziPuanKesintiTutari;
    }

    public String getZamanDilimi() {
        return zamanDilimi;
    }

    public void setZamanDilimi(String zamanDilimi) {
        this.zamanDilimi = zamanDilimi;
    }

    public boolean calismaGunuMu(LocalDate tarih) {
        // TODO: Implement business logic
        return false;
    }

    public ZonedDateTime sunucuZamaniGetir() {
        // TODO: Implement business logic
        return null;
    }
}
