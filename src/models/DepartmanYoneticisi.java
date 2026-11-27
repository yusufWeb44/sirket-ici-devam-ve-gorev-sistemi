// Ortak Sinif - Gorev Dagilimi:
// - Youssef AYOUBI - 230404906 (Sinif yapisi, gorevAta, goreviInceleVeOnayla, departmanRaporuGetir)
// - Adnan Dervis - 230404935 (izinTalebiDegerlendir)
package models;

import enums.IzinDurumu;
import enums.Rol;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class DepartmanYoneticisi extends Calisan {
    private int yonetilenDepartmanId;

    public DepartmanYoneticisi() {
        super();
    }

    public DepartmanYoneticisi(int kullaniciId, String kullaniciAdi, String sifreHash, String ad, String soyad,
                               String eposta, String telefon, Rol rol, boolean aktifMi,
                               LocalDateTime olusturmaTarihi, LocalDateTime sonGirisTarihi,
                               int departmanId, Departman departman, String unvan, double temelUcret,
                               int yonetilenDepartmanId) {
        super(kullaniciId, kullaniciAdi, sifreHash, ad, soyad, eposta, telefon, rol, aktifMi,
                olusturmaTarihi, sonGirisTarihi, departmanId, departman, unvan, temelUcret);
        this.yonetilenDepartmanId = yonetilenDepartmanId;
    }

    public int getYonetilenDepartmanId() {
        return yonetilenDepartmanId;
    }

    public void setYonetilenDepartmanId(int yonetilenDepartmanId) {
        this.yonetilenDepartmanId = yonetilenDepartmanId;
    }

    // Adnan Dervis - 230404935
    public boolean izinTalebiDegerlendir(IzinTalebi talep, boolean onaylandiMi, String retGerekcesi) {
        if (talep == null) {
            System.out.println("Uyari: Degerlendirilecek izin talebi bulunamadi.");
            return false;
        }

        // IK-09: Departman yoneticisi kendi izin talebini onaylayamaz veya degerlendiremez
        if (talep.getCalisanId() == this.kullaniciId) {
            System.out.println("Uyari: Kendi izin talebinizi onaylayamazsiniz, talep Sistem Yoneticisine gitmeli.");
            return false;
        }

        // Departman ve calisan listesi tanimliysa talebin bu departmandaki bir calisana ait oldugunu dogrula
        if (this.departman != null && this.departman.getCalisanlar() != null && !this.departman.getCalisanlar().isEmpty()) {
            boolean departmanCalisaniMi = false;
            for (Calisan c : this.departman.getCalisanlar()) {
                if (c != null && c.getKullaniciId() == talep.getCalisanId()) {
                    departmanCalisaniMi = true;
                    break;
                }
            }
            if (!departmanCalisaniMi) {
                System.out.println("Uyari: Bu izin talebi sizin departmaninizdaki bir calisana ait degil.");
                return false;
            }
        }

        // IK-10: Sadece BEKLIYOR durumundaki talepler degerlendirilebilir
        if (talep.getDurum() != IzinDurumu.BEKLIYOR) {
            System.out.println("Uyari: Sadece BEKLIYOR durumundaki talepler degerlendirilebilir. Mevcut: "
                    + talep.getDurum());
            return false;
        }

        // IK-04: Sunucu zamani Europe/Istanbul saatine gore alinir
        ZonedDateTime degerlendirmeZamani = ZonedDateTime.now(ZoneId.of("Europe/Istanbul"));

        if (onaylandiMi) {
            talep.onayla(this.kullaniciId);
            if (talep.getDurum() == IzinDurumu.ONAYLANDI) {
                System.out.println("Bilgi: Izin talebi onaylandi -> ID: " + talep.getIzinId() +
                        " (" + degerlendirmeZamani.toLocalDateTime() + ")");
                return true;
            }
            return false;
        } else {
            // IK-09: Ret isleminde gerekce zorunlu
            if (retGerekcesi == null || retGerekcesi.trim().isEmpty()) {
                System.out.println("Uyari: Ret islemi icin gerekce belirtilmelidir.");
                return false;
            }
            return talep.reddet(this.kullaniciId, retGerekcesi);
        }
    }

    // Adnan Dervis - 230404935
    public boolean izinTalebiOnayla(IzinTalebi talep) {
        return izinTalebiDegerlendir(talep, true, null);
    }

    // Adnan Dervis - 230404935
    public boolean izinTalebiReddet(IzinTalebi talep, String retGerekcesi) {
        return izinTalebiDegerlendir(talep, false, retGerekcesi);
    }

    // Youssef AYOUBI - 230404906
    public boolean gorevAta(Gorev gorev, Calisan hedefCalisan) {
        // IK-01: Aktif yonetici oturumu kontrolu
        if (!this.aktifMi || !this.oturumAcikMi) {
            System.out.println("Uyari: Gorev atamak icin aktif yonetici oturumu gerekir.");
            return false;
        }
        if (gorev == null || hedefCalisan == null) {
            System.out.println("Uyari: Gecersiz gorev veya calisan bilgisi.");
            return false;
        }

        // IK-11: Yonetici yalnizca kendi departmanindaki gorevi atayabilir
        if (gorev.getHedefDepartmanId() != this.yonetilenDepartmanId) {
            System.out.println("Uyari: Bu gorev (Departman ID: " +
                    gorev.getHedefDepartmanId() + ") sizin departmaniniza ait degil.");
            return false;
        }

        // IK-11: Sadece kendi departmanindaki aktif calisana gorev atanabilir
        if (hedefCalisan.getDepartmanId() != this.yonetilenDepartmanId) {
            System.out.println("Uyari: Secilen calisan (" + hedefCalisan.getAd() +
                    ") bu departmanda degil.");
            return false;
        }
        if (!hedefCalisan.isAktifMi()) {
            System.out.println("Uyari: Pasif durumdaki calisana (" +
                    hedefCalisan.getAd() + ") gorev atanamaz.");
            return false;
        }

        gorev.calisanaAta(hedefCalisan.getKullaniciId(), this.kullaniciId);
        if (!hedefCalisan.getAtananGorevler().contains(gorev)) {
            hedefCalisan.getAtananGorevler().add(gorev);
        }
        if (this.departman != null && !this.departman.getGorevler().contains(gorev)) {
            this.departman.getGorevler().add(gorev);
        }

        System.out.println("Bilgi: Gorev atandi -> '" + gorev.getBaslik() + "' (" +
                hedefCalisan.getAd() + " " + hedefCalisan.getSoyad() + ")");
        return true;
    }

    // Youssef AYOUBI - 230404906
    public boolean goreviInceleVeOnayla(Gorev gorev, boolean uygunMu, String aciklama) {
        // IK-01: Aktif yonetici oturumu kontrolu
        if (!this.aktifMi || !this.oturumAcikMi) {
            System.out.println("Uyari: Gorev incelemek icin aktif yonetici oturumu gerekir.");
            return false;
        }
        if (gorev == null) {
            System.out.println("Uyari: Incelenecek gorev bulunamadi.");
            return false;
        }

        // IK-12: uygunsa TAMAMLANDI yapar, degilse gerekceli duzeltme ister
        if (uygunMu) {
            return gorev.tamamlandiOnayla(this.kullaniciId, aciklama);
        } else {
            return gorev.duzeltmeIste(this.kullaniciId, aciklama);
        }
    }

    // Youssef AYOUBI - 230404906
    public List<DevamKaydi> departmanRaporuGetir(LocalDate baslangic, LocalDate bitis) {
        // IK-01: Oturum kontrolu
        if (!this.aktifMi || !this.oturumAcikMi) {
            System.out.println("Uyari: Departman raporunu gormek icin oturum acilmalidir.");
            return new ArrayList<>();
        }

        // IK-02: Departman yoneticisi sadece kendi departmaninin kayitlarini gorebilir
        List<DevamKaydi> sonuc = new ArrayList<>();
        if (this.departman == null) {
            System.out.println("Bilgi: Yoneticiye bagli departman kaydi bulunamadi.");
            return sonuc;
        }

        for (Calisan c : this.departman.getCalisanlar()) {
            for (DevamKaydi kayit : c.getDevamKayitlari()) {
                LocalDate tarih = kayit.getTarih();
                if (tarih != null &&
                        (baslangic == null || !tarih.isBefore(baslangic)) &&
                        (bitis == null || !tarih.isAfter(bitis))) {
                    sonuc.add(kayit);
                }
            }
        }

        System.out.println("Bilgi: " + this.departman.getDepartmanAdi() +
                " departmani icin " + sonuc.size() + " kayit listelendi.");
        return sonuc;
    }
}