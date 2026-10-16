package com.mycompany.extractionshootergui;

/**
 * Merepresentasikan amunisi khusus untuk senjata {@link Pistol}.
 * <p>
 * Peluru ini berkaliber 9.0 mm dengan ketajaman bernilai 4 dan harga Rp15.000 per unit/paket.
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class PeluruPistol extends Peluru {

    /**
     * Mengonstruksi objek {@code PeluruPistol} dengan jumlah butir peluru yang ditentukan.
     *
     * @param jumlah kuantitas amunisi awal peluru pistol.
     */
    public PeluruPistol(int jumlah) {
        super("Peluru Pistol (9mm)", 9.0, 4, 15000, jumlah);
    }
}

