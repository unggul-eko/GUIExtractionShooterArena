package com.mycompany.extractionshootergui;

/**
 * Merepresentasikan jenis senjata api ringan Pistol.
 * <p>
 * Pistol merupakan senjata awal dengan damage 20 dan harga beli Rp500.000.
 * Senjata ini menggunakan amunisi {@link PeluruPistol}.
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class Pistol extends Weapon {

    /**
     * Mengonstruksi senjata {@code Pistol} dengan nama "Pistol", harga 500.000, dan damage 20.
     */
    public Pistol() {
        super("Pistol", 500000, 20);
    }
}

