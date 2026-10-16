package com.mycompany.extractionshootergui;

import java.util.ArrayList;
import java.util.List;

/**
 * Mengelola ruang penyimpanan barang (stash) dan keuangan pemain.
 * <p>
 * Kelas ini bertanggung jawab atas penyimpanan item polimorfik (turunan dari {@link Item}),
 * akumulasi saldo uang, serta mekanisme transaksi seperti menjual, membeli, membayar,
 * dan memindahkan seluruh aset rampasan saat musuh tereliminasi.
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class Inventory {

    // ================= STATE (atribut privat) =================
    /**
     * Jumlah saldo uang tunai yang dimiliki pemain dalam mata uang permainan (Rupiah).
     */
    private int saldo;

    /**
     * Daftar koleksi item yang disimpan dalam stash pemain.
     */
    private List<Item> barang;

    // ================= CONSTRUCTOR =================
    /**
     * Mengonstruksi objek {@code Inventory} baru dengan saldo awal 0
     * dan daftar penyimpanan barang yang kosong.
     */
    public Inventory() {
        this.saldo = 0;
        this.barang = new ArrayList<>();
    }

    // ================= BEHAVIOR (method publik) =================

    /**
     * Menambahkan item hasil rampasan (looting) gratis ke dalam stash.
     * Item ini bukan didapat dari hasil pembelian di toko.
     *
     * @param item objek {@link Item} yang berhasil dirampas atau ditemukan.
     */
    public void tambahLoot(Item item) {
        barang.add(item);
        GameLogger.getInstance().log("  -> " + item.getNama() + " masuk stash");
    }

    /**
     * Menjual item yang ada di stash. Item akan dihapus dari daftar barang
     * dan saldo pemain akan bertambah sesuai harga jual item tersebut.
     *
     * @param item item yang ingin dijual dari stash.
     * @return {@code true} jika item ada di stash dan berhasil dijual;
     *         {@code false} jika item tidak ditemukan.
     */
    public boolean jualItem(Item item) {
        if (!barang.remove(item)) {
            GameLogger.getInstance().log(item.getNama() + " tidak ada di stash.");
            return false;
        }
        saldo += item.getHarga();
        GameLogger.getInstance().log("Menjual " + item.getNama() + " seharga " + item.getHarga());
        return true;
    }

    /**
     * Membeli item dan menyimpannya ke dalam stash jika saldo mencukupi.
     *
     * @param item item yang ingin dibeli.
     * @return {@code true} jika saldo mencukupi dan pembelian berhasil;
     *         {@code false} jika saldo tidak mencukupi.
     */
    public boolean beliItem(Item item) {
        if (saldo < item.getHarga()) {
            GameLogger.getInstance().log("Saldo tidak cukup untuk membeli " + item.getNama());
            return false;
        }
        saldo -= item.getHarga();
        barang.add(item);
        GameLogger.getInstance().log("Membeli " + item.getNama() + " seharga " + item.getHarga());
        return true;
    }

    /**
     * Melakukan pembayaran langsung (misalnya saat mengisi amunisi atau membeli perlengkapan)
     * yang tidak langsung dimasukkan ke dalam daftar item stash.
     *
     * @param jumlah nominal uang yang harus dibayar.
     * @return {@code true} jika pembayaran sukses (saldo &gt;= jumlah);
     *         {@code false} jika saldo tidak mencukupi.
     */
    public boolean bayar(int jumlah) {
        if (saldo >= jumlah) {
            saldo -= jumlah;
            return true;
        }
        return false;
    }

    /**
     * Memindahkan seluruh barang dalam stash dan seluruh saldo uang ke inventaris lain.
     * Biasanya dipanggil saat pemain yang menang merampas seluruh harta milik musuh yang tewas.
     *
     * @param penerima inventaris penerima aset.
     */
    public void pindahkanSemuaKe(Inventory penerima) {
        penerima.barang.addAll(this.barang);
        penerima.saldo += this.saldo;
        this.barang.clear();
        this.saldo = 0;
    }

    /**
     * Menambahkan sejumlah saldo uang tunai ke dalam inventaris.
     *
     * @param jumlah nominal saldo yang ditambahkan.
     */
    public void tambahSaldo(int jumlah) {
        this.saldo += jumlah;
    }

    // ================= GETTER =================
    /**
     * Mengambil total saldo uang yang dimiliki saat ini.
     *
     * @return saldo uang tunai dalam Rupiah.
     */
    public int getSaldo() {
        return saldo;
    }

    /**
     * Mengambil jumlah total barang yang tersimpan di dalam stash saat ini.
     *
     * @return kuantitas barang dalam stash.
     */
    public int getJumlahBarang() {
        return barang.size();
    }

    /**
     * Mengambil referensi daftar item yang ada di stash.
     *
     * @return {@link List} berisi objek {@link Item}.
     */
    public List<Item> getBarangList() {
        return barang;
    }
}