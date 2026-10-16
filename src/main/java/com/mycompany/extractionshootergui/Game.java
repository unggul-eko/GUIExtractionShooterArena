package com.mycompany.extractionshootergui;

import java.util.ArrayList;
import java.util.List;

/**
 * Mengelola sesi permainan dan arena pertempuran Extraction Shooter.
 * <p>
 * Kelas ini bertanggung jawab menyimpan nama arena permainan, mendaftarkan
 * para pemain yang berpartisipasi, mencari pemain berdasarkan ID,
 * serta menginisialisasi pembukaan sesi permainan.
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class Game {

    /**
     * Nama arena atau judul sesi permainan.
     */
    private String namaGame;

    /**
     * Daftar seluruh pemain yang terdaftar dalam sesi permainan ini.
     */
    private List<Pemain> pemain;

    /**
     * Mengonstruksi objek {@code Game} baru dengan nama arena yang ditentukan.
     *
     * @param namaGame nama sesi atau arena permainan.
     */
    public Game(String namaGame) {
        this.namaGame = namaGame;
        this.pemain = new ArrayList<>();
    }

    /**
     * Mendaftarkan pemain baru ke dalam sesi permainan.
     *
     * @param p objek {@link Pemain} yang akan didaftarkan.
     */
    public void tambahPemain(Pemain p) {
        pemain.add(p);
    }

    /**
     * Mencari pemain yang terdaftar berdasarkan kode ID pemain.
     *
     * @param idPemain kode unik pengenal pemain yang dicari.
     * @return objek {@link Pemain} jika ditemukan, atau {@code null} jika tidak terdaftar.
     */
    public Pemain cariPemain(String idPemain) {
        for (Pemain p : pemain) {
            if (p.getIdPemain().equals(idPemain)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Memulai sesi permainan secara resmi dan mencatat daftar peserta ke dalam logger.
     */
    public void mulaiGame() {
        GameLogger.getInstance().log("Game " + namaGame + " dimulai!");
        for (Pemain p : pemain) {
            GameLogger.getInstance().log("- " + p.getNama());
        }
    }

    /**
     * Mengambil nama sesi atau arena permainan.
     *
     * @return nama game berupa teks {@link String}.
     */
    public String getNamaGame() {
        return namaGame;
    }

    /**
     * Mengambil jumlah total pemain yang terdaftar.
     *
     * @return jumlah pemain dalam sesi ini.
     */
    public int getJumlahPemain() {
        return pemain.size();
    }

    /**
     * Mengambil daftar seluruh objek pemain yang terdaftar di game.
     *
     * @return {@link List} dari {@link Pemain}.
     */
    public List<Pemain> getPemainList() {
        return pemain;
    }
}