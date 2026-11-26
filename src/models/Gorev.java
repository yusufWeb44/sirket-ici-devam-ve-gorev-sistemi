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
        // IK-11: Görev çalışana atanır ve başlangıç inceleme sorumlusu yönetici olarak belirlenir.
        GorevDurumu onceki = (this.durum != null) ? this.durum : GorevDurumu.YENI;
        this.atananCalisanId = calisanId;
        this.incelemeSorumlusuId = yoneticiId;
        this.durum = GorevDurumu.ATANDI;

        if (this.olusturmaTarihi == null) {
            // IK-04: Sunucu zamanı (Europe/Istanbul)
            this.olusturmaTarihi = LocalDateTime.now(ZoneId.of("Europe/Istanbul"));
        }

        GorevGecmisi kayit = GorevGecmisi.kayitOlustur(
                this.gorevId,
                onceki,
                GorevDurumu.ATANDI,
                yoneticiId,
                "Görev çalışan (ID: " + calisanId + ") üzerine atandı."
        );
        this.gecmis.add(kayit);

        Bildirim.bildirimGonder(
                calisanId,
                "Yeni Görev Atandı",
                "'" + this.baslik + "' başlıklı görev size atandı.",
                BildirimTuru.GOREV_ATAMA,
                this.gorevId
        );
    }

    public boolean durumaGecis(GorevDurumu yeniDurum, int islemYapanId, String aciklama) {
        // IK-12: Görev durum geçişleri kontrol edilir; çalışan doğrudan TAMAMLANDI yapamaz.
        if (yeniDurum == null) {
            System.out.println("HATA: Geçersiz görev durumu!");
            return false;
        }
        if (yeniDurum == GorevDurumu.TAMAMLANDI) {
            System.out.println("HATA (IK-12): Görev doğrudan TAMAMLANDI durumuna geçirilemez; inceleme onayı gereklidir.");
            return false;
        }
        if (this.durum == GorevDurumu.TAMAMLANDI) {
            System.out.println("HATA (IK-12): Tamamlanmış görev üzerinde tekrar durum değişikliği yapılamaz.");
            return false;
        }

        GorevDurumu onceki = this.durum;
        this.durum = yeniDurum;

        GorevGecmisi kayit = GorevGecmisi.kayitOlustur(
                this.gorevId,
                onceki,
                yeniDurum,
                islemYapanId,
                (aciklama != null && !aciklama.isBlank()) ? aciklama : "Durum güncellendi."
        );
        this.gecmis.add(kayit);

        System.out.println("BAŞARILI (IK-12): Görev (ID: " + this.gorevId + ") durumu güncellendi: " + onceki + " -> " + yeniDurum);
        return true;
    }

    public boolean tamamlandiOnayla(int incelemeSorumlusuId, String aciklama) {
        // IK-12: Yalnızca incelemeSorumlusu görevi onaylayarak TAMAMLANDI durumuna geçirebilir.
        if (this.incelemeSorumlusuId != incelemeSorumlusuId) {
            System.out.println("HATA (IK-12): Yetkisiz inceleme! Bu görevi yalnızca inceleme sorumlusu (ID: " +
                    this.incelemeSorumlusuId + ") onaylayabilir.");
            return false;
        }
        if (this.durum != GorevDurumu.INCELEME_BEKLIYOR) {
            System.out.println("HATA (IK-12): Görev onaylanmadan önce INCELEME_BEKLIYOR durumunda olmalıdır. Mevcut durum: " + this.durum);
            return false;
        }

        GorevDurumu onceki = this.durum;
        this.durum = GorevDurumu.TAMAMLANDI;
        // IK-04: Sunucu zamanı (Europe/Istanbul)
        this.tamamlanmaTarihi = LocalDateTime.now(ZoneId.of("Europe/Istanbul"));

        GorevGecmisi kayit = GorevGecmisi.kayitOlustur(
                this.gorevId,
                onceki,
                GorevDurumu.TAMAMLANDI,
                incelemeSorumlusuId,
                (aciklama != null && !aciklama.isBlank()) ? aciklama : "Görev incelendi ve onaylandı."
        );
        this.gecmis.add(kayit);

        Bildirim.bildirimGonder(
                this.atananCalisanId,
                "Görev Onaylandı",
                "'" + this.baslik + "' göreviniz incelendi ve TAMAMLANDI olarak onaylandı.",
                BildirimTuru.GOREV_INCELEME,
                this.gorevId
        );

        System.out.println("BAŞARILI (IK-12): Görev (ID: " + this.gorevId + ") onaylandı ve TAMAMLANDI durumuna alındı.");
        return true;
    }

    public boolean duzeltmeIste(int incelemeSorumlusuId, String retGerekcesi) {
        // IK-12: Yalnızca incelemeSorumlusu düzeltme isteyebilir ve retGerekcesi zorunludur.
        if (this.incelemeSorumlusuId != incelemeSorumlusuId) {
            System.out.println("HATA (IK-12): Yetkisiz inceleme! Düzeltme talebi yalnızca inceleme sorumlusu (ID: " +
                    this.incelemeSorumlusuId + ") tarafından yapılabilir.");
            return false;
        }
        if (this.durum != GorevDurumu.INCELEME_BEKLIYOR) {
            System.out.println("HATA (IK-12): Düzeltme istenebilmesi için görev INCELEME_BEKLIYOR durumunda olmalıdır.");
            return false;
        }
        if (retGerekcesi == null || retGerekcesi.trim().isEmpty()) {
            System.out.println("HATA (IK-12): Görev reddedilirken veya düzeltme istenirken gerekçe (retGerekcesi) girilmesi zorunludur!");
            return false;
        }

        GorevDurumu onceki = this.durum;
        this.durum = GorevDurumu.DUZELTME_ISTENDI;

        GorevGecmisi kayit = GorevGecmisi.kayitOlustur(
                this.gorevId,
                onceki,
                GorevDurumu.DUZELTME_ISTENDI,
                incelemeSorumlusuId,
                "Düzeltme İstendi - Gerekçe: " + retGerekcesi
        );
        this.gecmis.add(kayit);

        Bildirim.bildirimGonder(
                this.atananCalisanId,
                "Görev İçin Düzeltme İstendi",
                "'" + this.baslik + "' görevi için düzeltme istendi. Gerekçe: " + retGerekcesi,
                BildirimTuru.GOREV_INCELEME,
                this.gorevId
        );

        System.out.println("BAŞARILI (IK-12): Görev (ID: " + this.gorevId + ") için düzeltme istendi -> DUZELTME_ISTENDI.");
        return true;
    }
}