// Youssef AYOUBI - 230404906
package models;

import enums.GorevDurumu;
import java.time.LocalDateTime;

public class GorevGecmisi {
    private int gecmisId;
    private int gorevId;
    private GorevDurumu oncekiDurum;
    private GorevDurumu yeniDurum;
    private int islemYapanId;
    private LocalDateTime islemZamani;
    private String yorum;

    public GorevGecmisi() {
    }

    public GorevGecmisi(int gecmisId, int gorevId, GorevDurumu oncekiDurum, GorevDurumu yeniDurum,
                        int islemYapanId, LocalDateTime islemZamani, String yorum) {
        this.gecmisId = gecmisId;
        this.gorevId = gorevId;
        this.oncekiDurum = oncekiDurum;
        this.yeniDurum = yeniDurum;
        this.islemYapanId = islemYapanId;
        this.islemZamani = islemZamani;
        this.yorum = yorum;
    }

    public int getGecmisId() {
        return gecmisId;
    }

    public void setGecmisId(int gecmisId) {
        this.gecmisId = gecmisId;
    }

    public int getGorevId() {
        return gorevId;
    }

    public void setGorevId(int gorevId) {
        this.gorevId = gorevId;
    }

    public GorevDurumu getOncekiDurum() {
        return oncekiDurum;
    }

    public void setOncekiDurum(GorevDurumu oncekiDurum) {
        this.oncekiDurum = oncekiDurum;
    }

    public GorevDurumu getYeniDurum() {
        return yeniDurum;
    }

    public void setYeniDurum(GorevDurumu yeniDurum) {
        this.yeniDurum = yeniDurum;
    }

    public int getIslemYapanId() {
        return islemYapanId;
    }

    public void setIslemYapanId(int islemYapanId) {
        this.islemYapanId = islemYapanId;
    }

    public LocalDateTime getIslemZamani() {
        return islemZamani;
    }

    public void setIslemZamani(LocalDateTime islemZamani) {
        this.islemZamani = islemZamani;
    }

    public String getYorum() {
        return yorum;
    }

    public void setYorum(String yorum) {
        this.yorum = yorum;
    }

    public static GorevGecmisi kayitOlustur(int gorevId, GorevDurumu oncekiDurum, GorevDurumu yeniDurum,
                                            int islemYapanId, String yorum) {
        // TODO: Implement business logic
        return null;
    }
}
