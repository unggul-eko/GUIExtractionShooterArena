package com.mycompany.extractionshootergui;

/**
 * Merepresentasikan entitas pemain dalam permainan Extraction Shooter.
 * <p>
 * Setiap pemain memiliki identitas unik, status kehidupan ({@link Status}),
 * senjata yang sedang dipegang ({@link Weapon}), amunisi ({@link Peluru}),
 * level dan skor, serta ruang penyimpanan inventaris ({@link Inventory}).
 * Pemain dapat melakukan berbagai aksi seperti memasuki game, menembak musuh,
 * merampas loot, membeli amunisi, dan meraih kemenangan.
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class Pemain {

    // ================= STATE (atribut privat) =================
    /**
     * Kode unik pengenal pemain (misal: "P001").
     */
    private String idPemain;

    /**
     * Nama karakter pemain.
     */
    private String nama;

    /**
     * Status kondisi hidup atau siklus bermain pemain (sedang bermain, menang, mati).
     */
    private Status status;

    /**
     * Tumpukan peluru (amunisi) yang saat ini dibawa pemain.
     */
    private Peluru peluru;

    /**
     * Senjata aktif yang saat ini digenggam/digunakan oleh pemain.
     */
    private Weapon Weapon;

    /**
     * Level pengalaman pemain.
     */
    private int level;

    /**
     * Skor perolehan pemain selama bertanding di arena.
     */
    private int score;

    /**
     * Inventaris penyimpanan barang rampasan dan saldo uang pemain.
     */
    private Inventory inventory;

    // ================= CONSTRUCTOR =================
    /**
     * Mengonstruksi objek {@code Pemain} baru dengan ID dan nama yang ditentukan.
     * Menginisialisasi status awal, level 1, skor 0, dan inventaris baru.
     *
     * @param idPemain pengenal unik pemain.
     * @param nama     nama tampilan pemain.
     */
    public Pemain(String idPemain, String nama) {
        this.idPemain = idPemain;
        this.nama = nama;
        this.status = new Status();
        this.peluru = null;
        this.level = 1;
        this.score = 0;
        this.inventory = new Inventory();
    }

    // ================= BEHAVIOR (method publik) =================

    /**
     * Memasukkan pemain ke dalam arena pertandingan.
     * Mengubah status pemain menjadi sedang bermain dan mengalokasikan
     * amunisi default (30 butir) sesuai jenis senjata yang sedang dibawa.
     */
    public void masukGame() {
        this.status.mulaiBermain();
        // Beri jatah peluru awal jika belum punya, sesuai dengan senjata yang dipegang
        if (this.peluru == null && this.Weapon != null) {
            String jenisSenjata = Weapon.getClass().getSimpleName();
            if (jenisSenjata.equals("Pistol")) this.peluru = new PeluruPistol(30);
            else if (jenisSenjata.equals("Shotgun")) this.peluru = new PeluruShotgun(30);
            else if (jenisSenjata.equals("Rifle")) this.peluru = new PeluruRifle(30);
        }
        GameLogger.getInstance().log(nama + " masuk ke dalam arena Match.");
    }

    /**
     * Melakukan tembakan latihan (tanpa target spesifik).
     * Memeriksa kelayakan aksi, kecocokan peluru dengan senjata, dan mengurangi 1 butir amunisi.
     *
     * @return {@code true} jika tembakan berhasil dieksekusi; {@code false} jika gagal.
     */
    public boolean menembak() {
        if (!bisaBertindak())
            return false;
            
        if (Weapon == null) {
            GameLogger.getInstance().log(nama + " tidak memegang senjata!");
            return false;
        }

        if (peluru == null) {
            GameLogger.getInstance().log("Peluru " + nama + " habis/kosong!");
            return false;
        }
        
        String jenisSenjata = Weapon.getClass().getSimpleName();
        String jenisPeluru = peluru.getClass().getSimpleName();
        boolean cocok = false;
        if (jenisSenjata.equals("Pistol") && jenisPeluru.equals("PeluruPistol")) cocok = true;
        if (jenisSenjata.equals("Shotgun") && jenisPeluru.equals("PeluruShotgun")) cocok = true;
        if (jenisSenjata.equals("Rifle") && jenisPeluru.equals("PeluruRifle")) cocok = true;
        
        if (!cocok) {
            GameLogger.getInstance().log("Gagal menembak! " + peluru.getNama() + " tidak cocok untuk " + Weapon.getNama());
            return false;
        }

        if (!peluru.pakai()) {
            GameLogger.getInstance().log("Peluru " + nama + " habis!");
            return false;
        }
        GameLogger.getInstance().log(nama + " menembak pakai " + Weapon.getNama() + " dengan " + peluru.getNama()
                + ". Sisa peluru: " + peluru.getJumlah());
        return true;
    }

    /**
     * Menembak pemain lawan (target).
     * <p>
     * Mengurangi satu butir peluru dan menghitung peluang keberhasilan eliminasi (instan kill)
     * berdasarkan ketajaman peluru yang digunakan. Jika tembakan mematikan, target akan dinyatakan tewas.
     * </p>
     *
     * @param target objek {@link Pemain} yang menjadi sasaran tembakan.
     * @return {@code true} jika tembakan berhasil menewaskan lawan;
     *         {@code false} jika tembakan meleset, tidak mematikan, atau gagal dieksekusi.
     */
    public boolean menembak(Pemain target) {
        if (!bisaBertindak())
            return false;
            
        if (Weapon == null) {
            GameLogger.getInstance().log(nama + " tidak memegang senjata!");
            return false;
        }

        if (peluru == null) {
            GameLogger.getInstance().log("Peluru " + nama + " habis/kosong!");
            return false;
        }
        
        String jenisSenjata = Weapon.getClass().getSimpleName();
        String jenisPeluru = peluru.getClass().getSimpleName();
        boolean cocok = false;
        if (jenisSenjata.equals("Pistol") && jenisPeluru.equals("PeluruPistol")) cocok = true;
        if (jenisSenjata.equals("Shotgun") && jenisPeluru.equals("PeluruShotgun")) cocok = true;
        if (jenisSenjata.equals("Rifle") && jenisPeluru.equals("PeluruRifle")) cocok = true;
        
        if (!cocok) {
            GameLogger.getInstance().log("Gagal menembak! " + peluru.getNama() + " tidak cocok untuk " + Weapon.getNama());
            return false;
        }

        if (!peluru.pakai()) {
            GameLogger.getInstance().log("Peluru " + nama + " habis!");
            return false;
        }
        
        int totalDamage = Weapon.getDamage() + (int) (peluru.getKetajaman() * 2);
        GameLogger.getInstance().log(nama + " menembak " + target.getNama() + " pakai " + Weapon.getNama() + " (" + peluru.getNama() + ")");

        double peluangBunuh = peluru.getKetajaman() * 0.07; // 7% per poin ketajaman
        if (Math.random() < peluangBunuh) {
            target.mati();
            return true;
        }
        GameLogger.getInstance().log("Tembakan meleset atau tidak mematikan, " + target.getNama() + " masih hidup.");
        return false;
    }

    /**
     * Mengubah status pemain menjadi tewas (mati). Senjata yang digenggam
     * akan dijatuhkan dan dipindahkan ke dalam inventaris stash pemain.
     */
    public void mati() {
        this.status.mati();
        if (this.Weapon != null) {
            this.inventory.tambahLoot(this.Weapon);
            this.Weapon = null;
        }
        GameLogger.getInstance().log(nama + " TEWAS dan menjatuhkan senjatanya!");
    }

    /**
     * Merampas seluruh loot (senjata, barang, dan saldo uang) dari pemain korban yang telah mati.
     *
     * @param korban objek {@link Pemain} yang telah tewas.
     */
    public void ambilLootDari(Pemain korban) {
        if (!korban.isMati()) {
            GameLogger.getInstance().log(korban.getNama() + " masih hidup, tidak bisa diambil lootnya.");
            return;
        }
        
        if (korban.getWeapon() != null) {
            this.inventory.tambahLoot(korban.getWeapon());
            GameLogger.getInstance().log(nama + " mengambil senjata " + korban.getWeapon().getNama() + " dari " + korban.getNama());
        }
        
        korban.getStash().pindahkanSemuaKe(this.inventory);
        GameLogger.getInstance().log(nama + " mengambil semua loot dari " + korban.getNama());
    }

    /**
     * Merampas atau mengambil item bebas (looting) di jalanan secara cuma-cuma.
     * Menambahkan item ke stash dan memberikan bonus 10 poin skor.
     *
     * @param barang objek {@link Item} yang dirampas.
     */
    public void merampas(Item barang) {
        inventory.tambahLoot(barang);
        this.score += 10;
        GameLogger.getInstance().log(nama + " merampas " + barang.getNama() + ". Score: " + score);
    }

    /**
     * Menyatakan pemain sebagai pemenang pertandingan jika pemain masih hidup.
     * Memberikan tambahan 100 poin skor kemenangan.
     */
    public void menang() {
        if (status.isMati()) {
            GameLogger.getInstance().log(
                    nama + " sudah mati dan tidak bisa menang.");
            return;
        }

        this.status.menang();
        this.score += 100;
        GameLogger.getInstance().log(nama + " MENANG! Score akhir: " + score);
    }

    /**
     * Menaikkan level karakter pemain sebanyak satu tingkat.
     */
    public void naikLevel() {
        this.level++;
        GameLogger.getInstance().log(nama + " naik ke level " + level);
    }

    /**
     * Membeli amunisi baru menggunakan saldo dari inventaris.
     * Jika jenis peluru sama dengan yang sedang dibawa, jumlah butirnya akan ditambahkan.
     * Jika berbeda, peluru lama akan digantikan oleh peluru baru tersebut.
     *
     * @param peluruBaru objek {@link Peluru} yang ingin dibeli.
     * @return {@code true} jika saldo cukup dan pembelian sukses; {@code false} jika saldo kurang.
     */
    public boolean belanjaPeluru(Peluru peluruBaru) {
        if (!inventory.bayar(peluruBaru.getHarga())) {
            GameLogger.getInstance().log(nama + " tidak punya cukup saldo buat beli peluru.");
            return false;
        }
        if (peluru != null && peluru.getNama().equals(peluruBaru.getNama())) {
            peluru.tambahPeluru(peluruBaru.getJumlah());
        } else {
            peluru = peluruBaru; // ganti jenis - sisa jenis lama tidak diakumulasi
        }
        GameLogger.getInstance().log(nama + " membeli " + peluruBaru + ". Total sekarang: " + peluru.getJumlah());
        return true;
    }

    /**
     * Memeriksa apakah pemain dalam kondisi valid untuk melakukan aksi (hidup dan sedang bermain).
     *
     * @return {@code true} jika pemain dapat bertindak; {@code false} jika mati atau belum masuk game.
     */
    private boolean bisaBertindak() {
        if (status.isMati()) {
            GameLogger.getInstance().log(nama + " sudah mati, tidak bisa bertindak.");
            return false;
        }
        if (!status.isSedangBermain()) {
            GameLogger.getInstance().log(nama + " belum masuk game.");
            return false;
        }
        return true;
    }

    /**
     * Memasangkan (equip) senjata ke tangan pemain untuk digunakan bertempur.
     *
     * @param weapon objek {@link Weapon} yang akan dipasang.
     */
    public void equipWeapon(Weapon weapon) {
        this.Weapon = weapon;
        GameLogger.getInstance().log(nama + " sekarang memegang " + weapon.getNama() + " (Damage: " + weapon.getDamage() + ")");
    }

    /**
     * Mengambil senjata yang sedang aktif digenggam oleh pemain saat ini.
     *
     * @return objek {@link Weapon} yang sedang aktif, atau {@code null} jika tidak memegang senjata.
     */
    public Weapon getWeapon() {
        return this.Weapon;
    }

    // ================= GETTER =================
    /**
     * Mengambil ID pengenal pemain.
     *
     * @return kode ID pemain.
     */
    public String getIdPemain() {
        return idPemain;
    }

    /**
     * Mengambil nama pemain.
     *
     * @return nama pemain.
     */
    public String getNama() {
        return nama;
    }

    /**
     * Memeriksa apakah pemain sedang aktif bermain di arena.
     *
     * @return {@code true} jika sedang bermain; sebaliknya {@code false}.
     */
    public boolean isSedangBermain() {
        return status.isSedangBermain();
    }

    /**
     * Memeriksa apakah pemain telah dinobatkan sebagai pemenang.
     *
     * @return {@code true} jika menang; sebaliknya {@code false}.
     */
    public boolean isMenang() {
        return status.isMenang();
    }

    /**
     * Memeriksa apakah pemain telah gugur (mati).
     *
     * @return {@code true} jika sudah mati; sebaliknya {@code false}.
     */
    public boolean isMati() {
        return status.isMati();
    }

    /**
     * Mengambil tumpukan amunisi (peluru) yang sedang dibawa pemain.
     *
     * @return objek {@link Peluru} yang dibawa, atau {@code null} jika kosong.
     */
    public Peluru getPeluru() {
        return peluru;
    }

    /**
     * Mengambil level saat ini dari pemain.
     *
     * @return tingkat level pemain.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Mengambil total skor perolehan pemain.
     *
     * @return nilai skor pemain.
     */
    public int getScore() {
        return score;
    }

    /**
     * Mengambil referensi inventaris (stash) milik pemain.
     *
     * @return objek {@link Inventory} pemain.
     */
    public Inventory getStash() {
        return inventory;
    }
}