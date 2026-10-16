package com.mycompany.extractionshootergui;

/**
 * Merepresentasikan jenis senjata Shotgun (senapan gentel).
 * <p>
 * Shotgun memiliki damage tinggi sebesar 60 dan harga beli Rp2.000.000.
 * Senjata ini menggunakan amunisi {@link PeluruShotgun}.
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class Shotgun extends Weapon {

    /**
     * Mengonstruksi senjata {@code Shotgun} dengan nama "Shotgun", harga 2.000.000, dan damage 60.
     */
    public Shotgun() {
        super("Shotgun", 2000000, 60);
    }
}

