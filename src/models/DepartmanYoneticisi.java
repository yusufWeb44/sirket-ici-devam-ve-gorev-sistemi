// Youssef AYOUBI - 230404906
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
        // TODO: Implement business logic
        return false;
    }

    // Youssef AYOUBI - 230404906
    public boolean goreviInceleVeOnayla(Gorev gorev, boolean uygunMu, String aciklama) {
        // TODO: Implement business logic
        return false;
    }

    // Youssef AYOUBI - 230404906
    public List<DevamKaydi> departmanRaporuGetir(LocalDate baslangic, LocalDate bitis) {
        // TODO: Implement business logic
        return new ArrayList<>();
    }
}
