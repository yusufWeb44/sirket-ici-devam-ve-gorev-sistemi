// Mohamad BIRAHJAKLI - 230404939
package models;

import enums.Rol;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class IKVeHesapSorumlusu extends Calisan {
    private static final Map<DonemAnahtari, AylikMaliOzet> kilitliOzetler = new HashMap<>();

    private record DonemAnahtari(int calisanId, YearMonth donem) {}

    private String sorumlulukAlani;

    public IKVeHesapSorumlusu() {
        super();
    }

    public IKVeHesapSorumlusu(int kullaniciId, String kullaniciAdi, String sifreHash, String ad, String soyad,
                              String eposta, String telefon, Rol rol, boolean aktifMi,
                              LocalDateTime olusturmaTarihi, LocalDateTime sonGirisTarihi,
                              int departmanId, Departman departman, String unvan, double temelUcret,
                              String sorumlulukAlani) {
        super(kullaniciId, kullaniciAdi, sifreHash, ad, soyad, eposta, telefon, rol, aktifMi,
                olusturmaTarihi, sonGirisTarihi, departmanId, departman, unvan, temelUcret);
        this.sorumlulukAlani = sorumlulukAlani;
    }

    public String getSorumlulukAlani() {
        return sorumlulukAlani;
    }

    public void setSorumlulukAlani(String sorumlulukAlani) {
        this.sorumlulukAlani = sorumlulukAlani;
    }

    // Mohamad BIRAHJAKLI - 230404939
    public List<DevamKaydi> sirketDevamRaporuAl(List<DevamKaydi> tumKayitlar, LocalDate baslangic, LocalDate bitis) {
        // IK-01 ve IK-02: Sirket raporuna aktif IK ve Hesap Sorumlusu erisebilir.
        if (!raporYetkisiVarMi()) {
            System.out.println("RED (IK-02): Sirket raporu icin IK ve Hesap Sorumlusu oturumu gereklidir.");
            return List.of();
        }
        if (baslangic == null || bitis == null || bitis.isBefore(baslangic) || tumKayitlar == null) {
            System.out.println("Gecersiz rapor tarih araligi veya kayit listesi.");
            return List.of();
        }
        List<DevamKaydi> rapor = tumKayitlar.stream().filter(Objects::nonNull)
                .filter(kayit -> kayit.getTarih() != null && !kayit.getTarih().isBefore(baslangic)
                        && !kayit.getTarih().isAfter(bitis))
                .sorted(Comparator.comparing(DevamKaydi::getTarih).thenComparingInt(DevamKaydi::getCalisanId))
                .toList();
        if (rapor.isEmpty()) {
            System.out.println("Kayit bulunamadi.");
        }
        return rapor;
    }

    // Mohamad BIRAHJAKLI - 230404939
    public AylikMaliOzet aylikMaliOzetOlusturVeKilitle(Calisan calisan, int yil, int ay, SistemAyarlari ayarlar) {
        // IK-01, IK-02 ve IK-14: Mali donemi yalnizca yetkili IK sorumlusu kilitleyebilir.
        if (!raporYetkisiVarMi() || calisan == null || calisan.getKullaniciId() <= 0
                || !AylikMaliOzet.donemGecerliMi(yil, ay)) {
            System.out.println("RED (IK-14): Yetkili oturum, gecerli calisan ve donem gereklidir.");
            return null;
        }
        DonemAnahtari anahtar = new DonemAnahtari(calisan.getKullaniciId(), YearMonth.of(yil, ay));
        synchronized (kilitliOzetler) {
            AylikMaliOzet ozet = kilitliOzetler.get(anahtar);
            if (ozet != null) {
                return ozet;
            }
            try {
                ozet = new AylikMaliOzet();
                ozet.setOzetId(kilitliOzetler.size() + 1);
                ozet.setCalisanId(anahtar.calisanId());
                ozet.setYil(yil);
                ozet.setAy(ay);
                synchronized (calisan) {
                    ozet.maliOzetHesapla(calisan.getDevamKayitlari(), calisan.getIzinTalepleri(),
                            calisan.getTemelUcret(), ayarlar);
                }
                ozet.ozetiKilitle();
                kilitliOzetler.put(anahtar, ozet);
                return ozet;
            } catch (IllegalArgumentException hata) {
                System.out.println("RED (" + hata.getMessage() + ")");
                return null;
            }
        }
    }

    private boolean raporYetkisiVarMi() {
        return isAktifMi() && isOturumAcikMi() && getRol() == Rol.IK_HESAP_SORUMLUSU;
    }
}
