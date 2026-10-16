package com.mycompany.extractionshootergui;

/**
 * Mengelola status siklus hidup (lifecycle state) dari seorang pemain dalam permainan.
 * <p>
 * Status ini mencakup tiga kondisi utama pemain:
 * </p>
 * <ul>
 *   <li>{@code sedangBermain}: Pemain aktif berada dalam arena pertandingan.</li>
 *   <li>{@code menang}: Pemain berhasil bertahan hidup atau menyelesaikan misi kemenangan.</li>
 *   <li>{@code mati}: Pemain telah tewas/tumbang dalam arena pertempuran.</li>
 * </ul>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class Status {

    /**
     * Menandakan apakah pemain sedang aktif bertanding di arena.
     */
    private boolean sedangBermain;

    /**
     * Menandakan apakah pemain telah memenangkan pertandingan.
     */
    private boolean menang;

    /**
     * Menandakan apakah pemain telah gugur/mati.
     */
    private boolean mati;

    /**
     * Mengonstruksi objek {@code Status} baru dengan kondisi awal:
     * belum bermain, belum menang, dan tidak mati (hidup/siap).
     */
    public Status() {
        this.sedangBermain = false;
        this.menang = false;
        this.mati = false;
    }

    // ================= GETTER =================

    /**
     * Memeriksa apakah pemain sedang aktif berada dalam permainan.
     *
     * @return {@code true} jika pemain sedang bermain, sebaliknya {@code false}.
     */
    public boolean isSedangBermain() {
        return sedangBermain;
    }

    /**
     * Memeriksa apakah status pemain adalah pemenang.
     *
     * @return {@code true} jika pemain menang, sebaliknya {@code false}.
     */
    public boolean isMenang() {
        return menang;
    }

    /**
     * Memeriksa apakah pemain sudah mati.
     *
     * @return {@code true} jika pemain gugur/tewas, sebaliknya {@code false}.
     */
    public boolean isMati() {
        return mati;
    }

    // ================= STATE CHANGER =================

    /**
     * Mengubah status pemain menjadi sedang aktif bermain.
     * Mereset status mati dan menang kembali menjadi {@code false}.
     */
    public void mulaiBermain() {
        this.sedangBermain = true;
        this.mati = false;
        this.menang = false;
    }

    /**
     * Mengubah status pemain menjadi pemenang pertandingan.
     * Mengakhiri status sedang bermain menjadi {@code false}.
     */
    public void menang() {
        this.menang = true;
        this.sedangBermain = false;
    }

    /**
     * Mengubah status pemain menjadi tewas (mati).
     * Mengakhiri status sedang bermain menjadi {@code false}.
     */
    public void mati() {
        this.mati = true;
        this.sedangBermain = false;
    }

}

