// Ortak Sınıf (Shared Class) - Görev Dağılımı:
// - Youssef AYOUBI - 230404906 (Sınıf yapısı, gorevAta, goreviInceleVeOnayla, departmanRaporuGetir)
// - Adnan Derviş - 230404935 (izinTalebiDegerlendir)
package models;

import enums.Rol;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    // Adnan Derviş - 230404935
    public boolean izinTalebiDegerlendir(IzinTalebi talep, boolean onay, String gerekce) {
        // TODO: Implement business logic
        return false;
    }

    // Youssef AYOUBI - 230404906
    public boolean gorevAta(Gorev gorev, Calisan hedefCalisan) {
        // IK-01: Oturum ve hesap aktiflik kontrolü
        if (!this.aktifMi || !this.oturumAcikMi) {
            System.out.println("HATA (IK-01): Görev atamak için aktif yönetici oturumu gereklidir.");
            return false;
        }
        if (gorev == null || hedefCalisan == null) {
            System.out.println("HATA: Geçersiz görev veya çalışan bilgisi.");
            return false;
        }

        // IK-11: Yönetici yalnızca kendi departmanına ait görevi yönetebilir.
        if (gorev.getHedefDepartmanId() != this.yonetilenDepartmanId) {
            System.out.println("HATA (IK-11): Yetkisiz departman! Bu görev (Departman ID: " +
                    gorev.getHedefDepartmanId() + ") sizin departmanınıza ait değil.");
            return false;
        }

        // IK-11: Yönetici görevi yalnızca departmanındaki aktif bir çalışana atayabilir.
        if (hedefCalisan.getDepartmanId() != this.yonetilenDepartmanId) {
            System.out.println("HATA (IK-11): Atama reddedildi! Seçilen çalışan (" + hedefCalisan.getAd() +
                    ") bu departmana ait değil.");
            return false;
        }
        if (!hedefCalisan.isAktifMi()) {
            System.out.println("HATA (IK-11): Atama reddedildi! Pasif durumdaki çalışana (" +
                    hedefCalisan.getAd() + ") görev atanamaz.");
            return false;
        }

        gorev.calisanaAta(hedefCalisan.getKullaniciId(), this.kullaniciId);
        if (!hedefCalisan.getAtananGorevler().contains(gorev)) {
            hedefCalisan.getAtananGorevler().add(gorev);
        }
        if (this.departman != null && !this.departman.getGorevler().contains(gorev)) {
            this.departman.getGorevler().add(gorev);
        }

        System.out.println("BAŞARILI (IK-11): '" + gorev.getBaslik() + "' görevi " +
                hedefCalisan.getAd() + " " + hedefCalisan.getSoyad() + " çalışanına atandı.");
        return true;
    }

    // Youssef AYOUBI - 230404906
    public boolean goreviInceleVeOnayla(Gorev gorev, boolean uygunMu, String aciklama) {
        // IK-01: Oturum ve hesap aktiflik kontrolü
        if (!this.aktifMi || !this.oturumAcikMi) {
            System.out.println("HATA (IK-01): Görev incelemek için aktif yönetici oturumu gereklidir.");
            return false;
        }
        if (gorev == null) {
            System.out.println("HATA: İncelenecek görev bulunamadı.");
            return false;
        }

        // IK-12: İnceleme sorumlusu uygun bulursa TAMAMLANDI yapar, değilse gerekçeli düzeltme ister.
        if (uygunMu) {
            return gorev.tamamlandiOnayla(this.kullaniciId, aciklama);
        } else {
            return gorev.duzeltmeIste(this.kullaniciId, aciklama);
        }
    }

    // Youssef AYOUBI - 230404906
    public List<DevamKaydi> departmanRaporuGetir(LocalDate baslangic, LocalDate bitis) {
        // IK-01: Oturum kontrolü
        if (!this.aktifMi || !this.oturumAcikMi) {
            System.out.println("HATA (IK-01): Departman raporunu görüntülemek için oturum açılmalıdır.");
            return new ArrayList<>();
        }

        // IK-02: Departman yöneticisi yalnızca kendi departmanındaki çalışanların kayıtlarını görebilir.
        List<DevamKaydi> sonuc = new ArrayList<>();
        if (this.departman == null) {
            System.out.println("UYARI (IK-02): Yöneticiye bağlı departman kaydı bulunamadı.");
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

        System.out.println("BAŞARILI (IK-02): " + this.departman.getDepartmanAdi() +
                " departmanı için " + sonuc.size() + " adet devam kaydı listelendi.");
        return sonuc;
    }
}