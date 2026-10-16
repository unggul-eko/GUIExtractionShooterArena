package com.mycompany.extractionshootergui;

/**
 * Merepresentasikan amunisi khusus untuk senjata {@link Rifle}.
 * <p>
 * Peluru ini berkaliber 5.56 mm dengan ketajaman bernilai 7 dan harga Rp25.000 per unit/paket.
 * Cocok untuk pertempuran jarak menengah dengan penetrasi yang baik.
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class PeluruRifle extends Peluru {

    /**
     * Mengonstruksi objek {@code PeluruRifle} dengan jumlah butir peluru yang ditentukan.
     *
     * @param jumlah kuantitas amunisi awal peluru rifle.
     */
    public PeluruRifle(int jumlah) {
        super("Peluru Rifle (5.56mm)", 5.56, 7, 25000, jumlah);
    }
}

