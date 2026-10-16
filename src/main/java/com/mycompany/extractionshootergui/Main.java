package com.mycompany.extractionshootergui;

import java.util.Scanner;

/**
 * Titik masuk utama (entry point) untuk menjalankan simulasi Extraction Shooter
 * melalui antarmuka konsol (Command Line Interface / CLI).
 * <p>
 * Kelas ini mendemonstrasikan inisialisasi pemain, pemilihan senjata awal interaktif,
 * pendaftaran ke objek {@link Game}, eksekusi duel melalui {@link Pertempuran},
 * pengambilan barang rampasan (looting), penjualan item, pembelian peluru baru,
 * hingga perolehan status akhir permainan.
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class Main {

    /**
     * Konstruktor default untuk kelas {@code Main}.
     */
    public Main() {
    }

    /**
     * Metode utama yang dieksekusi saat program dijalankan via konsol CLI.
     *
     * @param args argumen baris perintah (command line arguments).
     */
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Game game = new Game("Extraction Arena");

        Pemain unggul = new Pemain("P001", "Unggul");
        Pemain rian = new Pemain("P002", "Rian"); // isi weapon di awal

        System.out.println("Pilih Senjata Awal untuk " + unggul.getNama() + ":");
        System.out.println("1. Pistol (Damage: 20)");
        System.out.println("2. Shotgun (Damage: 60)");
        System.out.println("3. Rifle (Damage: 40)");
        System.out.print("Pilihan (1/2/3): ");
        int pilihan = scanner.nextInt();

        Weapon senjataAwal;
        switch (pilihan) {
            case 2:
                senjataAwal = new Shotgun();
                break;
            case 3:
                senjataAwal = new Rifle();
                break;
            case 1:
            default:
                senjataAwal = new Pistol();
                break;
        }
        System.out.println();
        unggul.equipWeapon(senjataAwal);

        // Rian default weapon
        rian.equipWeapon(new Pistol());
        System.out.println();

        game.tambahPemain(unggul);
        game.tambahPemain(rian);
        game.mulaiGame();
        System.out.println();

        // Menjalankan match menggunakan class Pertempuran
        Pertempuran match = new Pertempuran(unggul, rian);
        match.mulai();

        System.out.println("\n--- Unggul merampas item acak di jalan ---");
        Weapon senjataRampasan = new Weapon("Senjata Laras Panjang", 450000, 40);
        unggul.merampas(senjataRampasan);

        System.out.println("--- Unggul jual senjata rampasan, lalu beli peluru baru ---");
        unggul.getStash().jualItem(senjataRampasan);
        System.out.println("Saldo sekarang: " + unggul.getStash().getSaldo());
        unggul.belanjaPeluru(new Peluru("Armor Piercing", 7.62, 10, 25000, 15));
        System.out.println();

        unggul.menang();
        System.out.println();

        System.out.println("--- Status akhir ---");
        System.out.println(unggul.getNama() + " : hidup=" + !unggul.isMati()
                + ", peluru=" + unggul.getPeluru()
                + ", jumlah barang=" + unggul.getStash().getJumlahBarang()
                + ", saldo=" + unggul.getStash().getSaldo());
        System.out.println(rian.getNama() + " : hidup=" + !rian.isMati()
                + ", jumlah barang=" + rian.getStash().getJumlahBarang());
    }
}