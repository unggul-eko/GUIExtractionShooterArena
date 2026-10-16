package com.mycompany.extractionshootergui;

/**
 * Kelas utilitas untuk mendapatkan informasi lingkungan sistem eksekusi (runtime environment).
 * <p>
 * Menyediakan metode statis untuk membaca versi Java dan versi JavaFX yang sedang aktif.
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class SystemInfo {

    /**
     * Konstruktor privat untuk mencegah pembuatan instansiasi kelas utilitas ini.
     */
    private SystemInfo() {}

    /**
     * Mengambil versi Java Development Kit (JDK/JRE) yang sedang berjalan.
     *
     * @return string versi Java dari properti sistem {@code java.version}.
     */
    public static String javaVersion() {
        return System.getProperty("java.version");
    }

    /**
     * Mengambil versi pustaka JavaFX yang sedang digunakan oleh aplikasi.
     *
     * @return string versi JavaFX dari properti sistem {@code javafx.version}.
     */
    public static String javafxVersion() {
        return System.getProperty("javafx.version");
    }

}