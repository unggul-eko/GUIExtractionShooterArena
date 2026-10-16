package com.mycompany.extractionshootergui;

/**
 * Merepresentasikan senjata yang dapat digunakan oleh {@link Pemain} untuk bertempur.
 * <p>
 * Kelas ini merupakan turunan (subclass) dari {@link Item} dan memiliki atribut tambahan
 * berupa {@code damage} (besaran daya rusak). Kelas ini juga bertindak sebagai basis untuk
 * jenis senjata spesifik seperti {@link Pistol}, {@link Rifle}, dan {@link Shotgun}.
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class Weapon extends Item {

    /**
     * Besaran nilai daya rusak (damage) dasar yang dihasilkan oleh senjata ini.
     */
    private int damage;

    /**
     * Mengonstruksi objek {@code Weapon} baru dengan spesifikasi nama, harga, dan damage.
     *
     * @param nama   nama senjata (diteruskan ke superclass {@link Item}).
     * @param harga  harga beli senjata dalam mata uang permainan (Rupiah).
     * @param damage nilai daya hancur/kerusakan senjata.
     */
    public Weapon(String nama, int harga, int damage) {
        super(nama, harga);
        this.damage = damage;
    }

    /**
     * Mengambil nilai daya rusak (damage) dari senjata.
     *
     * @return nilai damage berupa bilangan bulat.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Mengembalikan representasi informasi senjata dalam format nama dan damage.
     *
     * @return representasi string senjata dengan informasi damage.
     */
    @Override
    public String toString() {
        return getNama() + " [damage " + damage + "] - Rp" + getHarga();
    }
}

