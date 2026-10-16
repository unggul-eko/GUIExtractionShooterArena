package com.mycompany.extractionshootergui;

/**
 * Mengelola alur dan logika pertempuran satu lawan satu (1 vs 1) antara dua
 * pemain.
 * <p>
 * Pertempuran dijalankan secara turn-based (bergiliran) di mana setiap pemain
 * saling menembak.
 * Pertandingan berakhir jika salah satu pemain gugur atau jika kedua belah
 * pihak kehabisan peluru (seri).
 * Pemain yang menang akan otomatis merampas seluruh inventaris dan saldo milik
 * lawan yang gugur.
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class Pertempuran {

    /**
     * Pemain pertama (Player 1) dalam duel.
     */
    private Pemain pemain1;

    /**
     * Pemain kedua (Player 2) dalam duel.
     */
    private Pemain pemain2;

    /**
     * Referensi ke pemain yang memenangkan pertempuran. Bernilai {@code null} jika
     * seri.
     */
    private Pemain pemenang;

    /**
     * Menandakan apakah hasil pertempuran berakhir imbang/seri.
     */
    private boolean seri;

    /**
     * Mengonstruksi sesi {@code Pertempuran} antara dua pemain.
     *
     * @param pemain1 petarung pertama.
     * @param pemain2 petarung kedua.
     */
    public Pertempuran(Pemain pemain1, Pemain pemain2) {
        this.pemain1 = pemain1;
        this.pemain2 = pemain2;
        this.pemenang = null;
        this.seri = false;
    }

    /**
     * Memulai jalannya pertempuran turn-based di arena.
     * <p>
     * Kedua pemain dimasukkan ke dalam game, kemudian saling menembak secara
     * bergantian
     * hingga salah satu tewas atau kedua belah pihak kehabisan amunisi.
     * </p>
     */
    public void mulai() {
        GameLogger log = GameLogger.getInstance();

        log.log("\n==================================");
        log.log("      MEMASUKI MATCH ARENA");
        log.log("==================================");
        log.log(pemain1.getNama() + " VS " + pemain2.getNama());
        log.log("==================================\n");

        pemain1.masukGame();
        pemain2.masukGame();

        log.log("\n--- PERTEMPURAN DIMULAI ---");

        // Loop pertempuran bergantian sampai salah satu mati
        while (!pemain1.isMati() && !pemain2.isMati()) {
            log.log("");

            // Pemain 1 menyerang
            log.log("[Giliran " + pemain1.getNama() + "]");
            pemain1.menembak(pemain2);

            if (pemain2.isMati()) {
                log.log(pemain2.getNama() + " telah tumbang!");
                break;
            }

            // Pemain 2 menyerang
            log.log("\n[Giliran " + pemain2.getNama() + "]");
            pemain2.menembak(pemain1);

            if (pemain1.isMati()) {
                log.log(pemain1.getNama() + " telah tumbang!");
                break;
            }

            // Mencegah loop abadi jika keduanya kehabisan peluru
            boolean p1PunyaPeluru = pemain1.getPeluru() != null && pemain1.getPeluru().getJumlah() > 0;
            boolean p2PunyaPeluru = pemain2.getPeluru() != null && pemain2.getPeluru().getJumlah() > 0;

            if (!p1PunyaPeluru && !p2PunyaPeluru) {
                log.log("\n[!] Kedua pemain kehabisan peluru. Pertempuran berakhir seri!");
                break;
            }
        }

        akhiriPertempuran();
    }

    /**
     * Menyelesaikan duel dan menentukan hasil akhir (pemenang atau seri).
     * Jika ada pemenang, pemenang akan mengambil semua loot dari pemain yang kalah.
     */
    private void akhiriPertempuran() {
        GameLogger log = GameLogger.getInstance();

        log.log("\n==================================");
        log.log("        MATCH SELESAI");
        log.log("==================================");

        if (pemain1.isMati() && !pemain2.isMati()) {
            log.log("PEMENANG: " + pemain2.getNama());
            pemenang = pemain2;
            pemain2.menang();
            log.log("\n--- " + pemain2.getNama() + " Mengambil Loot ---");
            pemain2.ambilLootDari(pemain1);
        } else if (pemain2.isMati() && !pemain1.isMati()) {
            log.log("PEMENANG: " + pemain1.getNama());
            pemenang = pemain1;
            pemain1.menang();
            log.log("\n--- " + pemain1.getNama() + " Mengambil Loot ---");
            pemain1.ambilLootDari(pemain2);
        } else {
            log.log("Hasil: SERI (Tidak ada yang menang)");
            seri = true;
        }
    }

    /**
     * Mengambil pemain yang memenangkan pertarungan.
     *
     * @return objek {@link Pemain} pemenang, atau {@code null} jika berakhir seri.
     */
    public Pemain getPemenang() {
        return pemenang;
    }

    /**
     * Memeriksa apakah duel berakhir imbang/seri.
     *
     * @return {@code true} jika seri; {@code false} jika ada pemenang yang jelas.
     */
    public boolean isSeri() {
        return seri;
    }

    /**
     * Mengambil objek pemain pertama.
     *
     * @return objek {@link Pemain} pemain 1.
     */
    public Pemain getPemain1() {
        return pemain1;
    }

    /**
     * Mengambil objek pemain kedua.
     *
     * @return objek {@link Pemain} pemain 2.
     */
    public Pemain getPemain2() {
        return pemain2;
    }
}
