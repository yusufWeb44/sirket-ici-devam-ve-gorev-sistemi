// Mohamad BIRAHJAKLI - 230404939
package models;

import enums.BildirimTuru;
import enums.DuzeltmeTuru;
import enums.TalepDurumu;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public final class DevamDuzeltmeTalebi {
    private final Calisan calisan;
    private final int talepId;
    private final int calisanId;
    private int devamKaydiId;
    private final LocalDate tarih;
    private final DuzeltmeTuru duzeltmeTuru;
    private final String gerekce;
    private TalepDurumu durum = TalepDurumu.BEKLIYOR;
    private int kararVerenAdminId;
    private String kararGerekcesi;
    private LocalDateTime kararZamani;

    // Mohamad BIRAHJAKLI - 230404939
    static final class OnayliSaatDuzeltmesi {
        final LocalDateTime giris;
        final LocalDateTime cikis;

        private OnayliSaatDuzeltmesi(LocalDateTime giris, LocalDateTime cikis) {
            this.giris = giris;
            this.cikis = cikis;
        }
    }

    // Mohamad BIRAHJAKLI - 230404939
    DevamDuzeltmeTalebi(Calisan calisan, int talepId, int devamKaydiId, LocalDate tarih,
                       DuzeltmeTuru duzeltmeTuru, String gerekce) {
        this.calisan = calisan;
        this.talepId = talepId;
        this.calisanId = calisan.getKullaniciId();
        this.devamKaydiId = devamKaydiId;
        this.tarih = tarih;
        this.duzeltmeTuru = duzeltmeTuru;
        this.gerekce = gerekce;
    }

    public int getTalepId() {
        return talepId;
    }

    public int getCalisanId() {
        return calisanId;
    }

    public int getDevamKaydiId() {
        return devamKaydiId;
    }

    public LocalDate getTarih() {
        return tarih;
    }

    public DuzeltmeTuru getDuzeltmeTuru() {
        return duzeltmeTuru;
    }

    public String getGerekce() {
        return gerekce;
    }

    public TalepDurumu getDurum() {
        return durum;
    }

    public int getKararVerenAdminId() {
        return kararVerenAdminId;
    }

    public String getKararGerekcesi() {
        return kararGerekcesi;
    }

    public LocalDateTime getKararZamani() {
        return kararZamani;
    }

    // Mohamad BIRAHJAKLI - 230404939
    boolean karariUygula(SistemYoneticisi yonetici, boolean onay, LocalDateTime yeniGiris,
                         LocalDateTime yeniCikis, String neden, IslemGecmisiLog sonucLogu,
                         SistemAyarlari ayarlar) {
        synchronized (calisan) {
            // IK-01, IK-02, IK-07: sadece yetkili yonetici gerekceli karar verebilir
            if (yonetici == null || !yonetici.devamKarariYetkisiVarMi()) {
                System.out.println("Uyari: Bu islem icin yetkili Sistem Yoneticisi oturumu gerekiyor.");
                return false;
            }
            if (durum != TalepDurumu.BEKLIYOR || calisan.getKullaniciId() != calisanId || ayarlar == null
                    || neden == null || neden.isBlank()) {
                System.out.println("Uyari: Bekleyen talep, calisan, sistem ayarlari ve karar gerekcesi zorunludur.");
                return false;
            }

            LocalDateTime simdi = ayarlar.sunucuZamaniGetir().toLocalDateTime();
            DevamKaydi kayit = calisan.tarihtekiDevamKaydiniBul(tarih);
            String eskiDeger = kayitDegeri(kayit);
            if (onay) {
                if (!duzeltmeGecerliMi(kayit, yeniGiris, yeniCikis, simdi)
                        || (devamKaydiId != 0 && (kayit == null || kayit.getKayitId() != devamKaydiId))
                        || (yeniGiris != null && ayarlar.getMesaiBaslangic() == null)) {
                    System.out.println("Uyari: Girilen tarih veya saatler talep turuyle uyusmuyor.");
                    return false;
                }
                boolean yeniKayit = kayit == null;
                if (kayit == null) {
                    int yeniKayitId = calisan.getDevamKayitlari().stream()
                            .mapToInt(DevamKaydi::getKayitId).max().orElse(0) + 1;
                    kayit = new DevamKaydi(yeniKayitId, calisanId, tarih, null, null, null, null,
                            null, 0, 0, 0, false);
                }
                kayit.onayliDuzeltmeyiUygula(new OnayliSaatDuzeltmesi(yeniGiris, yeniCikis), ayarlar);
                if (yeniKayit) {
                    calisan.onayliDevamKaydiniEkle(kayit);
                }
                devamKaydiId = kayit.getKayitId();
            }

            kararVerenAdminId = yonetici.getKullaniciId();
            kararGerekcesi = neden.strip();
            kararZamani = simdi;
            durum = onay ? TalepDurumu.ONAYLANDI : TalepDurumu.REDDEDILDI;

            int hedefId = kayit == null ? talepId : kayit.getKayitId();
            IslemGecmisiLog log = IslemGecmisiLog.logKaydet(kararVerenAdminId, "DEVAM_DUZELTME_" + durum,
                    hedefId, eskiDeger, kayitDegeri(kayit), kararGerekcesi, simdi);
            if (sonucLogu != null) {
                sonucLogu.kayittanKopyala(log);
            }
            Bildirim bildirim = Bildirim.bildirimGonder(calisanId, "Devam duzeltme sonucu",
                    durum + " - " + kararGerekcesi, BildirimTuru.DUZELTME_SONUCU, talepId);
            calisan.getBildirimler().add(bildirim);
            return true;
        }
    }

    // IK-05 ve IK-07: Duzeltilen saatler ayni gune ve talep turune ait olmalidir.
    private boolean duzeltmeGecerliMi(DevamKaydi kayit, LocalDateTime yeniGiris, LocalDateTime yeniCikis,
                                      LocalDateTime simdi) {
        if (tarih == null || duzeltmeTuru == null || (yeniGiris == null && yeniCikis == null)) {
            return false;
        }
        LocalDateTime eskiGiris = kayit == null ? null : kayit.getGirisZamani();
        LocalDateTime eskiCikis = kayit == null ? null : kayit.getCikisZamani();
        LocalDateTime giris = yeniGiris == null ? eskiGiris : yeniGiris;
        LocalDateTime cikis = yeniCikis == null ? eskiCikis : yeniCikis;
        if (giris == null || !giris.toLocalDate().equals(tarih) || giris.isAfter(simdi)
                || (cikis != null && (!cikis.toLocalDate().equals(tarih)
                || cikis.isBefore(giris) || cikis.isAfter(simdi)))) {
            return false;
        }

        return switch (duzeltmeTuru) {
            case EKSIK_GIRIS -> eskiGiris == null && yeniGiris != null;
            case EKSIK_CIKIS -> eskiGiris != null && eskiCikis == null && yeniGiris == null && yeniCikis != null;
            case KONUM_HATASI -> !giris.equals(eskiGiris) || !Objects.equals(cikis, eskiCikis);
        };
    }

    private String kayitDegeri(DevamKaydi kayit) {
        String kimlik = "Calisan=" + calisanId + ", Talep=" + talepId + ", Tarih=" + tarih;
        if (kayit == null) {
            return kimlik + ", Devam kaydi yok";
        }
        return kimlik + ", Giris=" + kayit.getGirisZamani() + ", Cikis=" + kayit.getCikisZamani()
                + ", Durum=" + kayit.getDurum() + ", Aday yesil=" + kayit.getAdayYesilPuan()
                + ", Kesinlesen yesil=" + kayit.getKesinlesenYesilPuan()
                + ", Kirmizi=" + kayit.getKirmiziPuan();
    }

    static SistemAyarlari varsayilanAyarlar() {
        SistemAyarlari ayarlar = new SistemAyarlari();
        ayarlar.setMesaiBaslangic(LocalTime.of(9, 0));
        return ayarlar;
    }
}
