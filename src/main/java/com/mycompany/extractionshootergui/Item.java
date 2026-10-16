package com.mycompany.extractionshootergui;

/**
 * Merepresentasikan item dasar dalam permainan Extraction Shooter.
 * <p>
 * Kelas ini berfungsi sebagai superclass (induk) untuk segala objek fisik atau inventaris
 * yang dapat dimiliki, diperdagangkan, atau dibawa oleh pemain, seperti senjata ({@link Weapon})
 * dan amunisi/peluru ({@link Peluru}).
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class Item {

    /**
     * Nama dari item.
     */
    private String nama;

    /**
     * Nilai harga jual/beli dari item dalam mata uang permainan (Rupiah).
     */
    private int harga;

    /**
     * Mengonstruksi objek {@code Item} baru dengan nama dan harga yang ditentukan.
     *
     * @param nama  nama atau deskripsi item.
     * @param harga harga item dalam satuan Rupiah.
     */
    public Item(String nama, int harga) {
        this.nama = nama;
        this.harga = harga;
    }

    /**
     * Mengambil nama item.
     *
     * @return nama item berupa {@link String}.
     */
    public String getNama() {
        return nama;
    }

    /**
     * Mengambil harga item.
     *
     * @return harga item berupa bilangan bulat (integer).
     */
    public int getHarga() {
        return harga;
    }

    /**
     * Mengembalikan representasi teks dari objek item dalam format "Nama (RpHarga)".
     *
     * @return representasi string dari item.
     */
    @Override
    public String toString() {
        return nama + " (Rp" + harga + ")";
    }
}


