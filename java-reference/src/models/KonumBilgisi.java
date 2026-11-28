// Ahmad Alamin - 230404933
package models;

import java.time.LocalDateTime;

public class KonumBilgisi {
    private double enlem;
    private double boylam;
    private double dogrulukPayi;
    private LocalDateTime olcumZamani;

    public KonumBilgisi() {
    }

    public KonumBilgisi(double enlem, double boylam, double dogrulukPayi, LocalDateTime olcumZamani) {
        this.enlem = enlem;
        this.boylam = boylam;
        this.dogrulukPayi = dogrulukPayi;
        this.olcumZamani = olcumZamani;
    }

    public double getEnlem() {
        return enlem;
    }

    public void setEnlem(double enlem) {
        this.enlem = enlem;
    }

    public double getBoylam() {
        return boylam;
    }

    public void setBoylam(double boylam) {
        this.boylam = boylam;
    }

    public double getDogrulukPayi() {
        return dogrulukPayi;
    }

    public void setDogrulukPayi(double dogrulukPayi) {
        this.dogrulukPayi = dogrulukPayi;
    }

    public LocalDateTime getOlcumZamani() {
        return olcumZamani;
    }

    public void setOlcumZamani(LocalDateTime olcumZamani) {
        this.olcumZamani = olcumZamani;
    }

    // Ahmad Alamin - 230404933
    // IK-03: Koordinat, dogruluk payi ve olcum zamani gecerlilik kontrolu
    public boolean gecerliKoordinatMi() {
        if (Double.isNaN(enlem) || Double.isInfinite(enlem) || enlem < -90.0 || enlem > 90.0) {
            return false;
        }
        if (Double.isNaN(boylam) || Double.isInfinite(boylam) || boylam < -180.0 || boylam > 180.0) {
            return false;
        }
        if (Double.isNaN(dogrulukPayi) || Double.isInfinite(dogrulukPayi) || dogrulukPayi < 0.0) {
            return false;
        }
        if (olcumZamani == null) {
            return false;
        }
        return true;
    }
}