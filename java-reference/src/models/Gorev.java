// Youssef AYOUBI - 230404906
package models;

import enums.BildirimTuru;
import enums.GorevDurumu;
import enums.Oncelik;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
        // IK-11: Gorev calisana atanir ve baslangic inceleme sorumlusu yonetici olarak belirlenir.
        GorevDurumu onceki = (this.durum != null) ? this.durum : GorevDurumu.YENI;
        this.atananCalisanId = calisanId;
        this.incelemeSorumlusuId = yoneticiId;
        this.durum = GorevDurumu.ATANDI;

        if (this.olusturmaTarihi == null) {
            // IK-04: Sunucu zamani (Europe/Istanbul)
            this.olusturmaTarihi = LocalDateTime.now(ZoneId.of("Europe/Istanbul"));
        }

        GorevGecmisi kayit = GorevGecmisi.kayitOlustur(
                this.gorevId,
                onceki,
                GorevDurumu.ATANDI,
                yoneticiId,
                "Gorev calisan (ID: " + calisanId + ") uzerine atandi."
        );
        this.gecmis.add(kayit);

        Bildirim.bildirimGonder(
                calisanId,
                "Yeni Gorev Atandi",
                "'" + this.baslik + "' baslikli gorev size atandi.",
                BildirimTuru.GOREV_ATAMA,
                this.gorevId
        );
    }

    public boolean durumaGecis(GorevDurumu yeniDurum, int islemYapanId, String aciklama) {
        // IK-12 kurali: calisan gorevi dogrudan tamamlandi yapamaz
        if (yeniDurum == null) {
            System.out.println("Uyari: Gecersiz gorev durumu.");
            return false;
        }
        if (yeniDurum == GorevDurumu.TAMAMLANDI) {
            System.out.println("Uyari: Gorev dogrudan TAMAMLANDI durumuna gecirilemez, inceleme onayi gerekiyor.");
            return false;
        }
        if (this.durum == GorevDurumu.TAMAMLANDI) {
            System.out.println("Uyari: Tamamlanmis gorevin durumu degistirilemez.");
            return false;
        }

        GorevDurumu onceki = this.durum;
        this.durum = yeniDurum;

        GorevGecmisi kayit = GorevGecmisi.kayitOlustur(
                this.gorevId,
                onceki,
                yeniDurum,
                islemYapanId,
                (aciklama != null && !aciklama.isBlank()) ? aciklama : "Durum guncellendi."
        );
        this.gecmis.add(kayit);

        System.out.println("Bilgi: Gorev (ID: " + this.gorevId + ") durumu guncellendi -> " + onceki + " -> " + yeniDurum);
        return true;
    }

    public boolean tamamlandiOnayla(int incelemeSorumlusuId, String aciklama) {
        // IK-12: sadece inceleme sorumlusu onaylayip TAMAMLANDI yapabilir
        if (this.incelemeSorumlusuId != incelemeSorumlusuId) {
            System.out.println("Uyari: Bu gorevi yalnizca inceleme sorumlusu (ID: " +
                    this.incelemeSorumlusuId + ") onaylayabilir.");
            return false;
        }
        if (this.durum != GorevDurumu.INCELEME_BEKLIYOR) {
            System.out.println("Uyari: Onay icin gorev INCELEME_BEKLIYOR olmali. Mevcut: " + this.durum);
            return false;
        }

        GorevDurumu onceki = this.durum;
        this.durum = GorevDurumu.TAMAMLANDI;
        // IK-04: sunucu saati Europe/Istanbul
        this.tamamlanmaTarihi = LocalDateTime.now(ZoneId.of("Europe/Istanbul"));

        GorevGecmisi kayit = GorevGecmisi.kayitOlustur(
                this.gorevId,
                onceki,
                GorevDurumu.TAMAMLANDI,
                incelemeSorumlusuId,
                (aciklama != null && !aciklama.isBlank()) ? aciklama : "Gorev incelendi ve onaylandi."
        );
        this.gecmis.add(kayit);

        Bildirim.bildirimGonder(
                this.atananCalisanId,
                "Gorev Onaylandi",
                "'" + this.baslik + "' goreviniz incelendi ve TAMAMLANDI olarak onaylandi.",
                BildirimTuru.GOREV_INCELEME,
                this.gorevId
        );

        System.out.println("Bilgi: Gorev onaylandi ve TAMAMLANDI oldu (ID: " + this.gorevId + ")");
        return true;
    }

    public boolean duzeltmeIste(int incelemeSorumlusuId, String retGerekcesi) {
        // IK-12: sadece inceleme sorumlusu duzeltme isteyebilir, gerekce zorunlu
        if (this.incelemeSorumlusuId != incelemeSorumlusuId) {
            System.out.println("Uyari: Duzeltme talebi sadece inceleme sorumlusu (ID: " +
                    this.incelemeSorumlusuId + ") tarafindan yapilabilir.");
            return false;
        }
        if (this.durum != GorevDurumu.INCELEME_BEKLIYOR) {
            System.out.println("Uyari: Duzeltme istenebilmesi icin gorev INCELEME_BEKLIYOR olmali.");
            return false;
        }
        if (retGerekcesi == null || retGerekcesi.trim().isEmpty()) {
            System.out.println("Uyari: Reddetme islemi icin gerekce girilmesi zorunludur.");
            return false;
        }

        GorevDurumu onceki = this.durum;
        this.durum = GorevDurumu.DUZELTME_ISTENDI;

        GorevGecmisi kayit = GorevGecmisi.kayitOlustur(
                this.gorevId,
                onceki,
                GorevDurumu.DUZELTME_ISTENDI,
                incelemeSorumlusuId,
                "Duzeltme istendi - Gerekce: " + retGerekcesi
        );
        this.gecmis.add(kayit);

        Bildirim.bildirimGonder(
                this.atananCalisanId,
                "Gorev Icin Duzeltme Istendi",
                "'" + this.baslik + "' gorevi icin duzeltme istendi. Gerekce: " + retGerekcesi,
                BildirimTuru.GOREV_INCELEME,
                this.gorevId
        );

        System.out.println("Bilgi: Gorev icin duzeltme istendi (ID: " + this.gorevId + ") -> DUZELTME_ISTENDI");
        return true;
    }
}