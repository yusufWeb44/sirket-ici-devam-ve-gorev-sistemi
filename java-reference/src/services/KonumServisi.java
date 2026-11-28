// Ahmad Alamin - 230404933
package services;

import models.KonumBilgisi;
import models.SistemAyarlari;

public class KonumServisi {

    private static final double DUNYA_YARICAPI_METRE = 6371000.0;

    // IK-03: iki konum arasi mesafe hesabi (Haversine)
    public static double mesafeHesapla(KonumBilgisi konum1, KonumBilgisi konum2) {
        if (konum1 == null || konum2 == null || !konum1.gecerliKoordinatMi() || !konum2.gecerliKoordinatMi()) {
            System.out.println("Uyari: Mesafe hesabi icin konum bilgisi gecersiz.");
            return -1.0;
        }

        double lat1Rad = Math.toRadians(konum1.getEnlem());
        double lat2Rad = Math.toRadians(konum2.getEnlem());
        double deltaLatRad = Math.toRadians(konum2.getEnlem() - konum1.getEnlem());
        double deltaLonRad = Math.toRadians(konum2.getBoylam() - konum1.getBoylam());

        double a = Math.sin(deltaLatRad / 2.0) * Math.sin(deltaLatRad / 2.0)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLonRad / 2.0) * Math.sin(deltaLonRad / 2.0);

        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return DUNYA_YARICAPI_METRE * c;
    }

    // IK-03: anlik konum, dogruluk payi ve sirket yaricapi kontrolu
    public static boolean konumDogrula(KonumBilgisi anlikKonum, SistemAyarlari ayarlar) {
        if (anlikKonum == null || !anlikKonum.gecerliKoordinatMi()) {
            System.out.println("Uyari: Konum alinamadi veya koordinat bilgisi gecersiz.");
            return false;
        }

        if (ayarlar == null) {
            System.out.println("Uyari: Sistem ayarlari bulunamadi.");
            return false;
        }

        // sirket merkez koordinatlarini ayarlar nesnesinden al
        KonumBilgisi sirketKonumu = new KonumBilgisi(
                ayarlar.getSirketEnlem(),
                ayarlar.getSirketBoylam(),
                0.0,
                anlikKonum.getOlcumZamani()
        );

        double mesafeMetre = mesafeHesapla(anlikKonum, sirketKonumu);
        if (mesafeMetre < 0) {
            return false;
        }

        double toplamUzaklikVeBelirsizlik = mesafeMetre + anlikKonum.getDogrulukPayi();
        double izinVerilenYaricap = ayarlar.getKabulYaricapiMetre();

        if (toplamUzaklikVeBelirsizlik > izinVerilenYaricap) {
            System.out.println("Uyari: Konum sirket siniri disinda (mesafe + pay: "
                    + String.format("%.2f", toplamUzaklikVeBelirsizlik)
                    + "m, limit: " + izinVerilenYaricap + "m)");
            return false;
        }

        return true;
    }
}