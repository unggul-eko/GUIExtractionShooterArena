package com.mycompany.extractionshootergui;

import java.util.ArrayList;

/**
 * Merepresentasikan toko perlengkapan tempur (Armory Shop) dalam game.
 * <p>
 * Toko menyediakan katalog senjata ({@link Weapon}) dan amunisi ({@link Peluru})
 * yang dapat dibeli oleh pemain menggunakan saldo dari {@link Inventory} mereka.
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class Toko {

    /**
     * Daftar senjata yang dijual di dalam toko.
     */
    private ArrayList<Weapon> daftarSenjata;

    /**
     * Daftar amunisi/peluru yang tersedia untuk dibeli di toko.
     */
    private ArrayList<Peluru> daftarPeluru;

    /**
     * Mengonstruksi objek {@code Toko} baru dengan katalog senjata dan peluru yang masih kosong.
     */
    public Toko() {
        daftarSenjata = new ArrayList<>();
        daftarPeluru = new ArrayList<>();
    }

    /**
     * Menambahkan senjata baru ke dalam katalog penjualan toko.
     *
     * @param w objek {@link Weapon} yang ditambahkan ke toko.
     */
    public void tambahSenjata(Weapon w) {
        daftarSenjata.add(w);
    }

    /**
     * Menambahkan jenis peluru baru ke dalam katalog penjualan toko.
     *
     * @param p objek {@link Peluru} yang ditambahkan ke toko.
     */
    public void tambahPeluru(Peluru p) {
        daftarPeluru.add(p);
    }

    /**
     * Menampilkan katalog seluruh item (senjata dan peluru) beserta harganya ke log game.
     */
    public void tampilkanKatalog() {
        GameLogger log = GameLogger.getInstance();
        log.log("\n=== KATALOG TOKO ===");
        log.log("Senjata:");
        for (int i = 0; i < daftarSenjata.size(); i++) {
            log.log((i + 1) + ". " + daftarSenjata.get(i).getNama() + " - Rp" + daftarSenjata.get(i).getHarga());
        }
        log.log("Peluru:");
        for (int i = 0; i < daftarPeluru.size(); i++) {
            log.log((i + 1 + daftarSenjata.size()) + ". " + daftarPeluru.get(i).getNama() + " - Rp" + daftarPeluru.get(i).getHarga());
        }
    }

    /**
     * Melayani transaksi pembelian senjata oleh pemain.
     * Jika saldo mencukupi, uang akan dipotong dan senjata langsung dipasangkan (equip) ke pemain.
     *
     * @param pemain  pembeli senjata.
     * @param senjata objek {@link Weapon} yang ingin dibeli.
     */
    public void beliSenjata(Pemain pemain, Weapon senjata) {
        if (pemain.getStash().bayar(senjata.getHarga())) {
            pemain.equipWeapon(senjata);
            GameLogger.getInstance().log(pemain.getNama() + " berhasil membeli " + senjata.getNama());
        } else {
            GameLogger.getInstance().log(pemain.getNama() + " tidak memiliki cukup uang untuk membeli " + senjata.getNama());
        }
    }

    /**
     * Melayani transaksi pembelian amunisi oleh pemain.
     *
     * @param pemain     pembeli amunisi.
     * @param peluruBaru objek {@link Peluru} yang ingin dibeli.
     */
    public void beliPeluru(Pemain pemain, Peluru peluruBaru) {
        pemain.belanjaPeluru(peluruBaru);
    }

    /**
     * Mengambil daftar senjata yang tersedia di toko.
     *
     * @return {@link ArrayList} berisi objek {@link Weapon}.
     */
    public ArrayList<Weapon> getDaftarSenjata() {
        return daftarSenjata;
    }

    /**
     * Mengambil daftar amunisi peluru yang tersedia di toko.
     *
     * @return {@link ArrayList} berisi objek {@link Peluru}.
     */
    public ArrayList<Peluru> getDaftarPeluru() {
        return daftarPeluru;
    }
}

