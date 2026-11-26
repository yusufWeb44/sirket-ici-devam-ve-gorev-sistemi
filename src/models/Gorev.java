// Youssef AYOUBI - 230404906
package models;

import enums.GorevDurumu;
import enums.Oncelik;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Gorev {
    private int gorevId;
    private String baslik;
    private String aciklama;
    private Oncelik oncelik;
    private LocalDate sonTeslimTarihi;
    private int hedefDepartmanId;
    private int atananCalisanId;
    private int incelemeSorumlusuId;
    private GorevDurumu durum;
    private LocalDateTime olusturmaTarihi;
    private LocalDateTime tamamlanmaTarihi;
    private List<GorevGecmisi> gecmis;

    public Gorev() {
        this.gecmis = new ArrayList<>();
    }

    public Gorev(int gorevId, String baslik, String aciklama, Oncelik oncelik, LocalDate sonTeslimTarihi,
                 int hedefDepartmanId, int atananCalisanId, int incelemeSorumlusuId, GorevDurumu durum,
                 LocalDateTime olusturmaTarihi, LocalDateTime tamamlanmaTarihi) {
        this.gorevId = gorevId;
        this.baslik = baslik;
        this.aciklama = aciklama;
        this.oncelik = oncelik;
        this.sonTeslimTarihi = sonTeslimTarihi;
        this.hedefDepartmanId = hedefDepartmanId;
        this.atananCalisanId = atananCalisanId;
        this.incelemeSorumlusuId = incelemeSorumlusuId;
        this.durum = durum;
        this.olusturmaTarihi = olusturmaTarihi;
        this.tamamlanmaTarihi = tamamlanmaTarihi;
        this.gecmis = new ArrayList<>();
    }

    public int getGorevId() {
        return gorevId;
    }

    public void setGorevId(int gorevId) {
        this.gorevId = gorevId;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Oncelik getOncelik() {
        return oncelik;
    }

    public void setOncelik(Oncelik oncelik) {
        this.oncelik = oncelik;
    }

    public LocalDate getSonTeslimTarihi() {
        return sonTeslimTarihi;
    }

    public void setSonTeslimTarihi(LocalDate sonTeslimTarihi) {
        this.sonTeslimTarihi = sonTeslimTarihi;
    }

    public int getHedefDepartmanId() {
        return hedefDepartmanId;
    }

    public void setHedefDepartmanId(int hedefDepartmanId) {
        this.hedefDepartmanId = hedefDepartmanId;
    }

    public int getAtananCalisanId() {
        return atananCalisanId;
    }

    public void setAtananCalisanId(int atananCalisanId) {
        this.atananCalisanId = atananCalisanId;
    }

    public int getIncelemeSorumlusuId() {
        return incelemeSorumlusuId;
    }

    public void setIncelemeSorumlusuId(int incelemeSorumlusuId) {
        this.incelemeSorumlusuId = incelemeSorumlusuId;
    }

    public GorevDurumu getDurum() {
        return durum;
    }

    public void setDurum(GorevDurumu durum) {
        this.durum = durum;
    }

    public LocalDateTime getOlusturmaTarihi() {
        return olusturmaTarihi;
    }

    public void setOlusturmaTarihi(LocalDateTime olusturmaTarihi) {
        this.olusturmaTarihi = olusturmaTarihi;
    }

    public LocalDateTime getTamamlanmaTarihi() {
        return tamamlanmaTarihi;
    }

    public void setTamamlanmaTarihi(LocalDateTime tamamlanmaTarihi) {
        this.tamamlanmaTarihi = tamamlanmaTarihi;
    }

    public List<GorevGecmisi> getGecmis() {
        return gecmis;
    }

    public void setGecmis(List<GorevGecmisi> gecmis) {
        this.gecmis = gecmis;
    }

    public void calisanaAta(int calisanId, int yoneticiId) {
        // TODO: Implement business logic
    }

    public boolean durumaGecis(GorevDurumu yeniDurum, int islemYapanId, String aciklama) {
        // TODO: Implement business logic
        return false;
    }

    public boolean tamamlandiOnayla(int incelemeSorumlusuId, String aciklama) {
        // TODO: Implement business logic
        return false;
    }

    public boolean duzeltmeIste(int incelemeSorumlusuId, String retGerekcesi) {
        // TODO: Implement business logic
        return false;
    }
}
