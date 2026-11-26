// Mohamad BIRAHJAKLI - 230404939
package models;

import enums.DuzeltmeTuru;
import enums.TalepDurumu;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DevamDuzeltmeTalebi {
    private int talepId;
    private int calisanId;
    private int devamKaydiId;
    private LocalDate tarih;
    private DuzeltmeTuru duzeltmeTuru;
    private String gerekce;
    private TalepDurumu durum;
    private int kararVerenAdminId;
    private String kararGerekcesi;
    private LocalDateTime kararZamani;

    public DevamDuzeltmeTalebi() {
    }

    public DevamDuzeltmeTalebi(int talepId, int calisanId, int devamKaydiId, LocalDate tarih,
                               DuzeltmeTuru duzeltmeTuru, String gerekce, TalepDurumu durum,
                               int kararVerenAdminId, String kararGerekcesi, LocalDateTime kararZamani) {
        this.talepId = talepId;
        this.calisanId = calisanId;
        this.devamKaydiId = devamKaydiId;
        this.tarih = tarih;
        this.duzeltmeTuru = duzeltmeTuru;
        this.gerekce = gerekce;
        this.durum = durum;
        this.kararVerenAdminId = kararVerenAdminId;
        this.kararGerekcesi = kararGerekcesi;
        this.kararZamani = kararZamani;
    }

    public int getTalepId() {
        return talepId;
    }

    public void setTalepId(int talepId) {
        this.talepId = talepId;
    }

    public int getCalisanId() {
        return calisanId;
    }

    public void setCalisanId(int calisanId) {
        this.calisanId = calisanId;
    }

    public int getDevamKaydiId() {
        return devamKaydiId;
    }

    public void setDevamKaydiId(int devamKaydiId) {
        this.devamKaydiId = devamKaydiId;
    }

    public LocalDate getTarih() {
        return tarih;
    }

    public void setTarih(LocalDate tarih) {
        this.tarih = tarih;
    }

    public DuzeltmeTuru getDuzeltmeTuru() {
        return duzeltmeTuru;
    }

    public void setDuzeltmeTuru(DuzeltmeTuru duzeltmeTuru) {
        this.duzeltmeTuru = duzeltmeTuru;
    }

    public String getGerekce() {
        return gerekce;
    }

    public void setGerekce(String gerekce) {
        this.gerekce = gerekce;
    }

    public TalepDurumu getDurum() {
        return durum;
    }

    public void setDurum(TalepDurumu durum) {
        this.durum = durum;
    }

    public int getKararVerenAdminId() {
        return kararVerenAdminId;
    }

    public void setKararVerenAdminId(int kararVerenAdminId) {
        this.kararVerenAdminId = kararVerenAdminId;
    }

    public String getKararGerekcesi() {
        return kararGerekcesi;
    }

    public void setKararGerekcesi(String kararGerekcesi) {
        this.kararGerekcesi = kararGerekcesi;
    }

    public LocalDateTime getKararZamani() {
        return kararZamani;
    }

    public void setKararZamani(LocalDateTime kararZamani) {
        this.kararZamani = kararZamani;
    }

    public boolean onayla(int adminId, String kararGerekcesi) {
        // TODO: Implement business logic
        return false;
    }

    public boolean reddet(int adminId, String kararGerekcesi) {
        // TODO: Implement business logic
        return false;
    }

    public void duzeltmeUygula(DevamKaydi kayit, LocalDateTime yeniGiris, LocalDateTime yeniCikis) {
        // TODO: Implement business logic
    }
}
