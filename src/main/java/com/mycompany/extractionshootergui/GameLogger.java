package com.mycompany.extractionshootergui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Kelas pencatat log (logger) global dengan pola desain <b>Singleton</b>.
 * <p>
 * Kelas ini menangkap seluruh pesan aktivitas permainan dan mendistribusikannya ke konsol
 * standar ({@code System.out}) serta ke seluruh listener antarmuka pengguna grafis (GUI) yang terdaftar.
 * Dengan pendekatan ini, komponen GUI dan model logika permainan tetap terpisah (decoupled).
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class GameLogger {

    /**
     * Instance tunggal (Singleton) dari {@code GameLogger}.
     */
    private static GameLogger instance;

    /**
     * Daftar callback listener yang mendengarkan event log baru (misalnya TextArea GUI).
     */
    private final List<Consumer<String>> listeners = new ArrayList<>();

    /**
     * Konstruktor privat untuk mencegah pembuatan instansiasi langsung dari luar kelas (pola Singleton).
     */
    private GameLogger() {}

    /**
     * Mengambil satu-satunya instansiasi (instance) dari {@code GameLogger}.
     * Jika belum pernah dibuat, instansiasi baru akan dibentuk (lazy initialization).
     *
     * @return instansiasi tunggal {@link GameLogger}.
     */
    public static GameLogger getInstance() {
        if (instance == null) {
            instance = new GameLogger();
        }
        return instance;
    }

    /**
     * Mendaftarkan listener baru yang akan dipanggil setiap kali ada pesan log baru.
     *
     * @param listener fungsi penerima pesan berupa {@link Consumer} bertipe {@link String}.
     */
    public void addListener(Consumer<String> listener) {
        listeners.add(listener);
    }

    /**
     * Menghapus seluruh listener yang sedang terdaftar pada logger.
     * Berguna saat mereset game atau beralih tampilan layar.
     */
    public void removeAllListeners() {
        listeners.clear();
    }

    /**
     * Mencatat pesan log baru ke konsol dan menyiarkannya ke seluruh listener yang aktif.
     *
     * @param message teks pesan aktivitas permainan yang ingin dicatat.
     */
    public void log(String message) {
        System.out.println(message); // Tetap mencetak ke konsol terminal
        for (Consumer<String> listener : listeners) {
            listener.accept(message);
        }
    }
}

