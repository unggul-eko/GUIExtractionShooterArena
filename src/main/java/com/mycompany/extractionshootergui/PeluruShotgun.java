package com.mycompany.extractionshootergui;

/**
 * Merepresentasikan amunisi khusus untuk senjata {@link Shotgun}.
 * <p>
 * Peluru ini bertipe 12 Gauge dengan kaliber 18.5 mm, ketajaman/daya rusak bernilai 8,
 * dan harga Rp30.000 per unit/paket. Memberikan peluang serangan mematikan yang tinggi.
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class PeluruShotgun extends Peluru {

    /**
     * Mengonstruksi objek {@code PeluruShotgun} dengan jumlah butir peluru yang ditentukan.
     *
     * @param jumlah kuantitas amunisi awal peluru shotgun.
     */
    public PeluruShotgun(int jumlah) {
        super("Peluru Shotgun (12 Gauge)", 18.5, 8, 30000, jumlah);
    }
}

