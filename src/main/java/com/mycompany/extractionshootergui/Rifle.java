package com.mycompany.extractionshootergui;

/**
 * Merepresentasikan jenis senjata laras panjang Rifle (senapan serbu).
 * <p>
 * Rifle memiliki damage 40 dan harga beli Rp1.500.000.
 * Senjata ini kompatibel dengan amunisi {@link PeluruRifle}.
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class Rifle extends Weapon {

    /**
     * Mengonstruksi senjata {@code Rifle} dengan nama "Rifle", harga 1.500.000, dan damage 40.
     */
    public Rifle() {
        super("Rifle", 1500000, 40);
    }
}

