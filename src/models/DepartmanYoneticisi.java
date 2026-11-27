// Ortak Sınıf (Shared Class) - Görev Dağılımı:
// - Youssef AYOUBI - 230404906 (Sınıf yapısı, gorevAta, goreviInceleVeOnayla, departmanRaporuGetir)
// - Adnan Derviş - 230404935 (izinTalebiDegerlendir)
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

    // Adnan Derviş 230404935
    public boolean izinTalebiDegerlendir(IzinTalebi talep, boolean onaylandiMi, String retGerekcesi) {
        if (talep == null) {
            System.out.println("Hata: Değerlendirilecek izin talebi bulunamadı!");
            return false;
        }

        // IK-09: Departman yöneticisi kendi izin talebini onaylayamaz veya değerlendiremez.
        if (talep.getCalisanId() == this.kullaniciId) {
            System.out.println("Yetkisiz İşlem: Kendi izin talebinizi onaylayamazsınız! (IK-09) - Talep Sistem Yöneticisine yönlendirilmelidir.");
            return false;
        }

        // Departman ve çalışan listesi tanımlıysa, talebin bu yöneticinin departmanındaki çalışana ait olduğunu doğrula
        if (this.departman != null && this.departman.getCalisanlar() != null && !this.departman.getCalisanlar().isEmpty()) {
            boolean departmanCalisaniMi = false;
            for (Calisan c : this.departman.getCalisanlar()) {
                if (c != null && c.getKullaniciId() == talep.getCalisanId()) {
                    departmanCalisaniMi = true;
                    break;
                }
            }
            if (!departmanCalisaniMi) {
                System.out.println("Yetkisiz İşlem: Bu izin talebi sizin yönettiğiniz departmana ait bir çalışana ait değil! (IK-02)");
                return false;
            }
        }

        // IK-10: Yalnızca BEKLIYOR durumundaki talepler değerlendirilebilir.
        if (talep.getDurum() != IzinDurumu.BEKLIYOR) {
            System.out.println("Hata: Yalnızca BEKLIYOR durumundaki izin talepleri değerlendirilebilir! Mevcut durum: "
                    + talep.getDurum() + " (IK-10)");
            return false;
        }

        // IK-04: Sunucu zamanı Europe/Istanbul olarak alınır.
        ZonedDateTime degerlendirmeZamani = ZonedDateTime.now(ZoneId.of("Europe/Istanbul"));

        if (onaylandiMi) {
            talep.onayla(this.kullaniciId);
            if (talep.getDurum() == IzinDurumu.ONAYLANDI) {
                System.out.println("BAŞARILI (IK-09): İzin talebi (ID: " + talep.getIzinId() +
                        ") departman yöneticisi tarafından onaylandı (" + degerlendirmeZamani.toLocalDateTime() + ").");
                return true;
            }
            return false;
        } else {
            // IK-09: Ret işlemi için gerekçe zorunludur.
            if (retGerekcesi == null || retGerekcesi.trim().isEmpty()) {
                System.out.println("Hata: Ret için gerekçe belirtilmesi zorunludur! (IK-09)");
                return false;
            }
            return talep.reddet(this.kullaniciId, retGerekcesi);
        }
    }

    // Adnan Derviş 230404935
    public boolean izinTalebiOnayla(IzinTalebi talep) {
        return izinTalebiDegerlendir(talep, true, null);
    }

    // Adnan Derviş 230404935
    public boolean izinTalebiReddet(IzinTalebi talep, String retGerekcesi) {
        return izinTalebiDegerlendir(talep, false, retGerekcesi);
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