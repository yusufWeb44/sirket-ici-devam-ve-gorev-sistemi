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

    public boolean gecerliKoordinatMi() {
        // TODO: Implement business logic
        return false;
    }
}
