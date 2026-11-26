// Youssef AYOUBI - 230404906
package models;

import enums.BildirimTuru;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Bildirim {
    private static int idSayaci = 1;

    private int bildirimId;
    private int aliciKullaniciId;
    private String baslik;
    private String mesaj;
    private BildirimTuru tur;
    private int ilgiliKayitId;
    private boolean okunduMu;
    private LocalDateTime olusturmaZamani;

    public Bildirim() {
    }

    public Bildirim(int bildirimId, int aliciKullaniciId, String baslik, String mesaj,
                    BildirimTuru tur, int ilgiliKayitId, boolean okunduMu, LocalDateTime olusturmaZamani) {
        this.bildirimId = bildirimId;
        this.aliciKullaniciId = aliciKullaniciId;
        this.baslik = baslik;
        this.mesaj = mesaj;
        this.tur = tur;
        this.ilgiliKayitId = ilgiliKayitId;
        this.okunduMu = okunduMu;
        this.olusturmaZamani = olusturmaZamani;
    }

    public int getBildirimId() {
        return bildirimId;
    }

    public void setBildirimId(int bildirimId) {
        this.bildirimId = bildirimId;
    }

    public int getAliciKullaniciId() {
        return aliciKullaniciId;
    }

    public void setAliciKullaniciId(int aliciKullaniciId) {
        this.aliciKullaniciId = aliciKullaniciId;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public BildirimTuru getTur() {
        return tur;
    }

    public void setTur(BildirimTuru tur) {
        this.tur = tur;
    }

    public int getIlgiliKayitId() {
        return ilgiliKayitId;
    }

    public void setIlgiliKayitId(int ilgiliKayitId) {
        this.ilgiliKayitId = ilgiliKayitId;
    }

    public boolean isOkunduMu() {
        return okunduMu;
    }

    public void setOkunduMu(boolean okunduMu) {
        this.okunduMu = okunduMu;
    }

    public LocalDateTime getOlusturmaZamani() {
        return olusturmaZamani;
    }

    public void setOlusturmaZamani(LocalDateTime olusturmaZamani) {
        this.olusturmaZamani = olusturmaZamani;
    }

    public void okunduOlarakIsaretle() {
        this.okunduMu = true;
        System.out.println("BİLDİRİM OKUNDU (ID: " + this.bildirimId + "): " + this.baslik);
    }

    public static Bildirim bildirimGonder(int aliciKullaniciId, String baslik, String mesaj,BildirimTuru tur, int ilgiliKayitId) {
        // IK-04: Bildirim zamanı için Europe/Istanbul sunucu saati kullanılır.
        LocalDateTime simdi = LocalDateTime.now(ZoneId.of("Europe/Istanbul"));
        Bildirim yeniBildirim = new Bildirim(
            idSayaci++,
            aliciKullaniciId,
            baslik,
            mesaj,
            tur,
            ilgiliKayitId,
            false,
            simdi
        );
        System.out.println("BİLDİRİM GÖNDERİLDİ -> Alici ID: " + aliciKullaniciId +" | Tür: " + tur + " | Başlik: " + baslik + " | Mesaj: " + mesaj);
        return yeniBildirim;
    }
}
