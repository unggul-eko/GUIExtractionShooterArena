package com.mycompany.extractionshootergui;

/**
 * Merepresentasikan amunisi (peluru) yang digunakan oleh pemain untuk menembak.
 * <p>
 * Kelas ini merupakan turunan (subclass) dari {@link Item}. Setiap jenis peluru memiliki atribut
 * spesifik seperti ukuran/kaliber (mm), ketajaman (penetrasi damage/peluang bunuh),
 * serta kuantitas amunisi yang tersisa.
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class Peluru extends Item {

    /**
     * Kaliber atau ukuran fisik diameter peluru dalam satuan milimeter (mm).
     */
    private double ukuran;

    /**
     * Skala ketajaman atau daya penetrasi peluru (misalnya 1 - 10).
     * Nilai ini memengaruhi bonus damage dan persentase peluang membunuh instan saat pertempuran.
     */
    private int ketajaman;

    /**
     * Kuantitas/jumlah butir peluru yang masih tersedia.
     */
    private int jumlah;

    /**
     * Mengonstruksi objek {@code Peluru} dengan spesifikasi lengkap.
     *
     * @param jenis     nama atau jenis peluru (diteruskan ke superclass {@link Item}).
     * @param ukuran    diameter kaliber peluru dalam mm.
     * @param ketajaman tingkat ketajaman/penetrasi peluru.
     * @param harga     harga pembelian peluru dalam Rupiah.
     * @param jumlah    jumlah butir peluru awal.
     */
    public Peluru(String jenis, double ukuran, int ketajaman, int harga, int jumlah) {
        super(jenis, harga);
        this.ukuran = ukuran;
        this.ketajaman = ketajaman;
        this.jumlah = jumlah;
    }

    /**
     * Menambahkan jumlah amunisi ke dalam tumpukan peluru saat ini.
     *
     * @param jumlah kuantitas butir peluru yang ditambahkan.
     */
    public void tambahPeluru(int jumlah) {
        this.jumlah += jumlah;
    }

    /**
     * Menggunakan satu butir peluru (mengurangi persediaan peluru sebanyak 1).
     *
     * @return {@code true} jika peluru berhasil digunakan (stok &gt; 0);
     *         {@code false} jika amunisi sudah habis (stok = 0).
     */
    public boolean pakai() {
        if (jumlah > 0) {
            jumlah--;
            return true;
        }
        return false;
    }

    /**
     * Mengambil ukuran/kaliber peluru.
     *
     * @return ukuran kaliber dalam milimeter (mm).
     */
    public double getUkuran() {
        return ukuran;
    }

    /**
     * Mengambil nilai ketajaman peluru.
     *
     * @return tingkat ketajaman peluru.
     */
    public int getKetajaman() {
        return ketajaman;
    }

    /**
     * Mengambil sisa kuantitas butir peluru yang tersedia.
     *
     * @return jumlah butir peluru.
     */
    public int getJumlah() {
        return jumlah;
    }

    /**
     * Mengembalikan informasi ringkas peluru mencakup jenis, kaliber, ketajaman, kuantitas, dan harga.
     *
     * @return teks deskripsi format peluru.
     */
    @Override
    public String toString() {
        return getNama() + " (ukuran " + ukuran + "mm, ketajaman " + ketajaman
                + "/10, sisa " + jumlah + ") - Rp" + getHarga();
    }
}