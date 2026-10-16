/**
 * Modul utama untuk aplikasi Extraction Shooter berbasis JavaFX.
 * <p>
 * Menyediakan konfigurasi dependensi modul JavaFX ({@code javafx.controls} dan {@code javafx.fxml})
 * serta mengekspor dan membuka paket {@code com.mycompany.extractionshootergui}
 * agar dapat diakses oleh runtime JavaFX.
 * </p>
 *
 * @author UnggulWindows
 * @version 1.0
 */
module com.mycompany.extractionshootergui {
    requires javafx.controls;
    requires javafx.fxml;
    opens com.mycompany.extractionshootergui to javafx.fxml;
    exports com.mycompany.extractionshootergui;
}

