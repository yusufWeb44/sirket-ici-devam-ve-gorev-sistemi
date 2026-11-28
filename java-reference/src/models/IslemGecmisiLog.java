// Mohamad BIRAHJAKLI - 230404939
package models;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class IslemGecmisiLog {
    private static final List<IslemGecmisiLog> kayitlar = new ArrayList<>();

    private int logId;
    private int islemYapanKullaniciId;
    private String islemTuru;
    private int hedefKayitId;
    private String eskiDeger;
    private String yeniDeger;
    private String gerekce;
    private LocalDateTime islemZamani;

    public IslemGecmisiLog() {
    }

    public IslemGecmisiLog(int logId, int islemYapanKullaniciId, String islemTuru, int hedefKayitId,
                           String eskiDeger, String yeniDeger, String gerekce, LocalDateTime islemZamani) {
        this.logId = logId;
        this.islemYapanKullaniciId = islemYapanKullaniciId;
        this.islemTuru = islemTuru;
        this.hedefKayitId = hedefKayitId;
        this.eskiDeger = eskiDeger;
        this.yeniDeger = yeniDeger;
        this.gerekce = gerekce;
        this.islemZamani = islemZamani;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getIslemYapanKullaniciId() {
        return islemYapanKullaniciId;
    }

    public void setIslemYapanKullaniciId(int islemYapanKullaniciId) {
        this.islemYapanKullaniciId = islemYapanKullaniciId;
    }

    public String getIslemTuru() {
        return islemTuru;
    }

    public void setIslemTuru(String islemTuru) {
        this.islemTuru = islemTuru;
    }

    public int getHedefKayitId() {
        return hedefKayitId;
    }

    public void setHedefKayitId(int hedefKayitId) {
        this.hedefKayitId = hedefKayitId;
    }

    public String getEskiDeger() {
        return eskiDeger;
    }

    public void setEskiDeger(String eskiDeger) {
        this.eskiDeger = eskiDeger;
    }

    public String getYeniDeger() {
        return yeniDeger;
    }

    public void setYeniDeger(String yeniDeger) {
        this.yeniDeger = yeniDeger;
    }

    public String getGerekce() {
        return gerekce;
    }

    public void setGerekce(String gerekce) {
        this.gerekce = gerekce;
    }

    public LocalDateTime getIslemZamani() {
        return islemZamani;
    }

    public void setIslemZamani(LocalDateTime islemZamani) {
        this.islemZamani = islemZamani;
    }

    public static IslemGecmisiLog logKaydet(int islemYapanKullaniciId, String islemTuru, int hedefKayitId,
                                            String eskiDeger, String yeniDeger, String gerekce) {
        // IK-04: Islem zamani sunucu saatinden alinir.
        return logKaydet(islemYapanKullaniciId, islemTuru, hedefKayitId, eskiDeger, yeniDeger, gerekce,
                LocalDateTime.now(ZoneId.of("Europe/Istanbul")));
    }

    // Mohamad BIRAHJAKLI - 230404939
    static synchronized IslemGecmisiLog logKaydet(int kullaniciId, String islemTuru, int hedefKayitId,
                                                 String eskiDeger, String yeniDeger, String gerekce,
                                                 LocalDateTime islemZamani) {
        IslemGecmisiLog kayit = new IslemGecmisiLog(kayitlar.size() + 1, kullaniciId, islemTuru,
                hedefKayitId, eskiDeger, yeniDeger, gerekce, islemZamani);
        kayitlar.add(kayit);
        return kayit.kopyaOlustur();
    }

    // Mohamad BIRAHJAKLI - 230404939
    public static synchronized List<IslemGecmisiLog> getKayitlar() {
        return kayitlar.stream().map(IslemGecmisiLog::kopyaOlustur).toList();
    }

    private IslemGecmisiLog kopyaOlustur() {
        IslemGecmisiLog kopya = new IslemGecmisiLog();
        kopya.kayittanKopyala(this);
        return kopya;
    }

    void kayittanKopyala(IslemGecmisiLog kayit) {
        logId = kayit.logId;
        islemYapanKullaniciId = kayit.islemYapanKullaniciId;
        islemTuru = kayit.islemTuru;
        hedefKayitId = kayit.hedefKayitId;
        eskiDeger = kayit.eskiDeger;
        yeniDeger = kayit.yeniDeger;
        gerekce = kayit.gerekce;
        islemZamani = kayit.islemZamani;
    }
}
