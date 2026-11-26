// Youssef AYOUBI - 230404906
package models;

import enums.Rol;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IKVeHesapSorumlusu extends Calisan {
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
        // TODO: Implement business logic
        return new ArrayList<>();
    }

    // Mohamad BIRAHJAKLI - 230404939
    public AylikMaliOzet aylikMaliOzetOlusturVeKilitle(Calisan calisan, int yil, int ay, SistemAyarlari ayarlar) {
        // TODO: Implement business logic
        return null;
    }
}
