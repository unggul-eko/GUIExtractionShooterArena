package com.mycompany.extractionshootergui;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Aplikasi antarmuka grafis (GUI) berbasis JavaFX untuk simulasi Extraction Shooter.
 * <p>
 * Menyediakan antarmuka visual yang modern dan interaktif dengan fitur:
 * </p>
 * <ul>
 *   <li>Layar konfigurasi pemain (pemilihan nama dan senjata awal).</li>
 *   <li>Panel status real-time untuk Pemain 1 dan Pemain 2 (HP/status, senjata, amunisi, skor, level, saldo).</li>
 *   <li>Aksi tembak interaktif per giliran dan auto-battle simulasi.</li>
 *   <li>Fitur looting dinamis (dari jalanan atau merampas musuh yang gugur).</li>
 *   <li>Pembelian amunisi dan penjualan item langsung dari antarmuka.</li>
 *   <li>Log pertempuran visual berbasis streaming event dengan {@link GameLogger}.</li>
 * </ul>
 *
 * @author UnggulWindows
 * @version 1.0
 */
public class App extends Application {

    /**
     * Membuat instance aplikasi {@code App}.
     */
    public App() {
        super();
    }

    // ========== Game State ==========
    /**
     * Sesi arena permainan aktif.
     */
    private Game game;

    /**
     * Pemain pertama (Player 1).
     */
    private Pemain player1;

    /**
     * Pemain kedua (Player 2).
     */
    private Pemain player2;

    /**
     * Toko perlengkapan dalam permainan.
     */
    private Toko toko;

    // ========== UI Components ==========
    /**
     * Kontainer tata letak utama (root layout) jendela aplikasi.
     */
    private BorderPane root;

    /**
     * Kontainer konten utama.
     */
    private StackPane mainContent;

    /**
     * Area teks untuk menampilkan log pertempuran secara visual.
     */
    private TextArea battleLog;

    /**
     * Kartu panel visual Pemain 1 dan Pemain 2.
     */
    private VBox player1Card, player2Card;

    // Player 1 stat labels
    private Label p1NameLabel, p1StatusLabel, p1WeaponLabel, p1AmmoLabel, p1ScoreLabel, p1BalanceLabel, p1LevelLabel;
    // Player 2 stat labels
    private Label p2NameLabel, p2StatusLabel, p2WeaponLabel, p2AmmoLabel, p2ScoreLabel, p2BalanceLabel, p2LevelLabel;

    // Setup controls
    private TextField player1NameField, player2NameField;
    private ComboBox<String> player1WeaponCombo, player2WeaponCombo;
    private Runnable p1UpdateLootOptions, p2UpdateLootOptions;

    /**
     * Titik masuk utama aplikasi JavaFX yang menginisialisasi panggung (Stage) dan scene.
     *
     * @param stage panggung utama yang disediakan oleh JavaFX runtime.
     */
    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        root.getStyleClass().add("root");

        // Show the welcome/setup screen first
        showSetupScreen();

        Scene scene = new Scene(root, 1100, 750);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("⚔ Extraction Shooter Simulator");
        stage.setMinWidth(900);
        stage.setMinHeight(650);
        stage.show();
    }

    // =====================================================================
    //  SETUP SCREEN
    // =====================================================================
    /**
     * Menampilkan layar awal konfigurasi pemain (Setup Screen).
     * Memuat judul beranimasi, kartu pengaturan Pemain 1 & 2, serta tombol mulai pertarungan.
     */
    private void showSetupScreen() {
        VBox setupRoot = new VBox(20);
        setupRoot.setAlignment(Pos.CENTER);
        setupRoot.setPadding(new Insets(40));
        setupRoot.setStyle("-fx-background-color: #0d1117;");

        // ---- Animated Title ----
        Label title = new Label("EXTRACTION SHOOTER");
        title.getStyleClass().add("label-title");
        title.setStyle(title.getStyle() + "-fx-font-size: 42px;");

        // Pulsing glow on title
        Glow glow = new Glow(0.0);
        title.setEffect(glow);
        Timeline glowTimeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0.0)),
            new KeyFrame(Duration.seconds(1.5), new KeyValue(glow.levelProperty(), 0.6)),
            new KeyFrame(Duration.seconds(3), new KeyValue(glow.levelProperty(), 0.0))
        );
        glowTimeline.setCycleCount(Animation.INDEFINITE);
        glowTimeline.play();

        Label subtitle = new Label("Arena Pertempuran Simulasi");
        subtitle.getStyleClass().add("label-subtitle");
        subtitle.setStyle("-fx-font-size: 16px;");

        // ---- Decorative Line ----
        Rectangle decorLine = new Rectangle(300, 2);
        decorLine.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.TRANSPARENT),
            new Stop(0.3, Color.web("#ff6a00")),
            new Stop(0.7, Color.web("#ff6a00")),
            new Stop(1, Color.TRANSPARENT)
        ));

        // ---- Player Setup Cards ----
        HBox playersSetup = new HBox(30);
        playersSetup.setAlignment(Pos.CENTER);
        playersSetup.setMaxWidth(800);

        // Player 1 setup
        VBox p1Setup = createPlayerSetupCard("PEMAIN 1", true);
        // Player 2 setup
        VBox p2Setup = createPlayerSetupCard("PEMAIN 2", false);

        playersSetup.getChildren().addAll(p1Setup, p2Setup);
        HBox.setHgrow(p1Setup, Priority.ALWAYS);
        HBox.setHgrow(p2Setup, Priority.ALWAYS);

        // ---- Start Button ----
        Button startBtn = new Button("⚔  MULAI PERTEMPURAN");
        startBtn.getStyleClass().add("btn-primary");
        startBtn.setStyle(startBtn.getStyle() + "-fx-font-size: 18px; -fx-padding: 15 50 15 50;");

        // Hover animation
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), startBtn);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), startBtn);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        startBtn.setOnMouseEntered(e -> scaleUp.playFromStart());
        startBtn.setOnMouseExited(e -> scaleDown.playFromStart());

        startBtn.setOnAction(e -> startGame());

        // ---- Footer ----
        Label footer = new Label("v1.0  •  JavaFX Extraction Shooter Simulator");
        footer.getStyleClass().add("footer-text");

        setupRoot.getChildren().addAll(
            createSpacer(20),
            title, subtitle,
            decorLine,
            createSpacer(10),
            playersSetup,
            createSpacer(10),
            startBtn,
            createSpacer(20),
            footer
        );

        root.setCenter(setupRoot);
        root.setTop(null);
        root.setBottom(null);

        // Fade in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), setupRoot);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    /**
     * Membuat kartu visual formulir pengaturan pemain (nama dan pilihan senjata awal).
     *
     * @param title     judul kartu (misalnya "PEMAIN 1" atau "PEMAIN 2").
     * @param isPlayer1 {@code true} jika kartu ini untuk Pemain 1; {@code false} jika untuk Pemain 2.
     * @return {@link VBox} kartu visual pengaturan pemain.
     */
    private VBox createPlayerSetupCard(String title, boolean isPlayer1) {

        VBox card = new VBox(12);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(20));
        card.setMaxWidth(350);

        // Player icon
        StackPane iconCircle = new StackPane();
        Circle circle = new Circle(30);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(isPlayer1 ? Color.web("#ff6a00") : Color.web("#00d4ff"));
        circle.setStrokeWidth(2);
        Label iconLabel = new Label(isPlayer1 ? "P1" : "P2");
        iconLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " +
            (isPlayer1 ? "#ff6a00" : "#00d4ff") + ";");
        iconCircle.getChildren().addAll(circle, iconLabel);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " +
            (isPlayer1 ? "#ff6a00" : "#00d4ff") + ";");

        // Name field
        Label nameLabel = new Label("Nama Pemain:");
        nameLabel.getStyleClass().add("label-stat-name");
        TextField nameField = new TextField(isPlayer1 ? "Unggul" : "Rian");
        nameField.setPromptText("Masukkan nama...");
        nameField.setMaxWidth(250);

        // Weapon selector
        Label weaponLabel = new Label("Pilih Senjata:");
        weaponLabel.getStyleClass().add("label-stat-name");
        ComboBox<String> weaponCombo = new ComboBox<>();
        weaponCombo.getItems().addAll(
            "🔫 Pistol  (DMG: 20, Rp500.000)",
            "💥 Shotgun (DMG: 60, Rp2.000.000)",
            "🎯 Rifle   (DMG: 40, Rp1.500.000)"
        );
        weaponCombo.getSelectionModel().selectFirst();
        weaponCombo.setMaxWidth(250);

        if (isPlayer1) {
            player1NameField = nameField;
            player1WeaponCombo = weaponCombo;
        } else {
            player2NameField = nameField;
            player2WeaponCombo = weaponCombo;
        }

        Separator sep = new Separator();

        // Weapon stats preview
        VBox statsBox = new VBox(4);
        statsBox.setAlignment(Pos.CENTER_LEFT);
        Label statsTitle = new Label("Info Senjata:");
        statsTitle.setStyle("-fx-text-fill: #8b949e; -fx-font-size: 11px;");
        Label damageLabel = new Label("• Damage: 20");
        damageLabel.setStyle("-fx-text-fill: #e6edf3; -fx-font-size: 12px;");
        Label priceLabel = new Label("• Harga: Rp500.000");
        priceLabel.setStyle("-fx-text-fill: #2dce89; -fx-font-size: 12px;");
        Label ammoLabel = new Label("• Peluru Awal: 30");
        ammoLabel.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 12px;");
        statsBox.getChildren().addAll(statsTitle, damageLabel, priceLabel, ammoLabel);

        // Update stats on weapon change
        weaponCombo.setOnAction(e -> {
            int idx = weaponCombo.getSelectionModel().getSelectedIndex();
            switch (idx) {
                case 0: damageLabel.setText("• Damage: 20"); priceLabel.setText("• Harga: Rp500.000"); break;
                case 1: damageLabel.setText("• Damage: 60"); priceLabel.setText("• Harga: Rp2.000.000"); break;
                case 2: damageLabel.setText("• Damage: 40"); priceLabel.setText("• Harga: Rp1.500.000"); break;
            }
        });

        card.getChildren().addAll(iconCircle, titleLabel, nameLabel, nameField, weaponLabel, weaponCombo, sep, statsBox);
        return card;
    }

    // =====================================================================
    //  GAME SCREEN
    // =====================================================================
    /**
     * Memulai sesi permainan baru berdasarkan input nama dan pilihan senjata dari layar setup.
     * Menginisialisasi objek {@link Pemain}, senjata, amunisi, arena {@link Game}, dan {@link Toko}.
     */
    private void startGame() {
        String name1 = player1NameField.getText().trim();
        String name2 = player2NameField.getText().trim();
        if (name1.isEmpty()) name1 = "Pemain 1";
        if (name2.isEmpty()) name2 = "Pemain 2";

        // Create players
        player1 = new Pemain("P001", name1);
        player2 = new Pemain("P002", name2);

        // Assign weapons
        player1.equipWeapon(createWeaponFromIndex(player1WeaponCombo.getSelectionModel().getSelectedIndex()));
        player2.equipWeapon(createWeaponFromIndex(player2WeaponCombo.getSelectionModel().getSelectedIndex()));

        // Setup game
        game = new Game("Extraction Arena");
        game.tambahPemain(player1);
        game.tambahPemain(player2);

        // Setup toko
        toko = new Toko();
        toko.tambahSenjata(new Pistol());
        toko.tambahSenjata(new Shotgun());
        toko.tambahSenjata(new Rifle());
        toko.tambahPeluru(new PeluruPistol(15));
        toko.tambahPeluru(new PeluruShotgun(10));
        toko.tambahPeluru(new PeluruRifle(15));

        showGameScreen();
    }

    /**
     * Membuat instansiasi polimorfik {@link Weapon} berdasarkan indeks opsi ComboBox.
     *
     * @param idx indeks senjata yang dipilih (0 = Pistol, 1 = Shotgun, 2 = Rifle).
     * @return instansiasi objek {@link Weapon}.
     */
    private Weapon createWeaponFromIndex(int idx) {
        switch (idx) {
            case 1: return new Shotgun();
            case 2: return new Rifle();
            default: return new Pistol();
        }
    }

    /**
     * Menyusun dan menampilkan layar utama permainan (Game Screen) ke jendela aplikasi.
     * Membangun header, kartu stat pemain, tombol kontrol aksi, panel log, dan footer.
     */
    private void showGameScreen() {
        // ---- Header ----
        HBox header = createHeader();

        // ---- Center Content ----
        VBox centerContent = new VBox(15);
        centerContent.setPadding(new Insets(15));

        // Player cards row
        HBox playerRow = new HBox(20);
        playerRow.setAlignment(Pos.CENTER);
        player1Card = createPlayerCard(player1, true);
        player2Card = createPlayerCard(player2, false);

        // VS separator
        VBox vsBox = new VBox(5);
        vsBox.setAlignment(Pos.CENTER);
        vsBox.setPadding(new Insets(0, 10, 0, 10));
        Label vsLabel = new Label("VS");
        vsLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #ff6a00;");
        vsLabel.setEffect(new DropShadow(15, Color.web("#ff6a00", 0.5)));
        vsBox.getChildren().add(vsLabel);

        playerRow.getChildren().addAll(player1Card, vsBox, player2Card);
        HBox.setHgrow(player1Card, Priority.ALWAYS);
        HBox.setHgrow(player2Card, Priority.ALWAYS);

        // Action buttons row
        VBox actionRow = createActionButtons();

        // Battle log
        VBox logContainer = createBattleLogPanel();
        VBox.setVgrow(logContainer, Priority.ALWAYS);

        centerContent.getChildren().addAll(playerRow, actionRow, logContainer);

        // ---- Footer ----
        HBox footer = createFooter();

        root.setTop(header);
        root.setCenter(centerContent);
        root.setBottom(footer);

        // Setup logger
        GameLogger.getInstance().removeAllListeners();
        GameLogger.getInstance().addListener(msg -> {
            Platform.runLater(() -> {
                battleLog.appendText(msg + "\n");
                updatePlayerCards();
            });
        });

        // Transition animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), centerContent);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Initial game start log
        game.mulaiGame();
        player1.masukGame();
        player2.masukGame();
        updatePlayerCards();
    }

    /**
     * Membuat bilah bagian atas (Header) yang memuat judul arena dan tombol Reset Game.
     *
     * @return kontainer {@link HBox} untuk header aplikasi.
     */
    private HBox createHeader() {
        HBox header = new HBox(15);
        header.getStyleClass().add("header-bar");
        header.setAlignment(Pos.CENTER_LEFT);

        // Title
        Label titleIcon = new Label("⚔");
        titleIcon.setStyle("-fx-font-size: 24px;");
        Label title = new Label("EXTRACTION SHOOTER");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ff6a00;");
        Label gameName = new Label("Arena: " + game.getNamaGame());
        gameName.setStyle("-fx-font-size: 13px; -fx-text-fill: #8b949e; -fx-padding: 0 0 0 15;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Reset button
        Button resetBtn = new Button("🔄 Reset Game");
        resetBtn.getStyleClass().add("btn-secondary");
        resetBtn.setOnAction(e -> {
            GameLogger.getInstance().removeAllListeners();
            showSetupScreen();
        });

        header.getChildren().addAll(titleIcon, title, gameName, spacer, resetBtn);
        return header;
    }

    /**
     * Membuat kartu antarmuka pemain yang menampilkan avatar, nama, ID, status bertanding,
     * statistik senjata &amp; amunisi, tombol tembak, serta panel interaksi looting.
     *
     * @param pemain    objek {@link Pemain} yang diasosiasikan dengan kartu.
     * @param isPlayer1 {@code true} jika untuk Pemain 1; {@code false} untuk Pemain 2.
     * @return komponen kontainer {@link VBox} kartu pemain.
     */
    private VBox createPlayerCard(Pemain pemain, boolean isPlayer1) {
        VBox card = new VBox(8);
        card.getStyleClass().addAll("card-player");
        card.setMinWidth(320);
        card.setMaxWidth(450);

        String accentColor = isPlayer1 ? "#ff6a00" : "#00d4ff";

        // Top bar with player icon + name
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);

        StackPane avatar = new StackPane();
        Circle avatarCircle = new Circle(22);
        avatarCircle.setFill(Color.TRANSPARENT);
        avatarCircle.setStroke(Color.web(accentColor));
        avatarCircle.setStrokeWidth(2);
        Label avatarLabel = new Label(isPlayer1 ? "P1" : "P2");
        avatarLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + accentColor + ";");
        avatar.getChildren().addAll(avatarCircle, avatarLabel);

        VBox nameBox = new VBox(2);
        Label nameLabel = new Label(pemain.getNama());
        nameLabel.getStyleClass().add("label-player-name");
        Label idLabel = new Label("ID: " + pemain.getIdPemain());
        idLabel.getStyleClass().add("label-player-id");
        nameBox.getChildren().addAll(nameLabel, idLabel);

        Region nameSpacer = new Region();
        HBox.setHgrow(nameSpacer, Priority.ALWAYS);

        Label statusLabel = new Label("● SIAP");
        statusLabel.getStyleClass().add("label-status-alive");

        topBar.getChildren().addAll(avatar, nameBox, nameSpacer, statusLabel);

        Separator sep1 = new Separator();

        // Stats grid
        GridPane stats = new GridPane();
        stats.setHgap(15);
        stats.setVgap(8);
        stats.setPadding(new Insets(5, 0, 5, 0));

        Label weaponTitle = new Label("SENJATA");
        weaponTitle.getStyleClass().add("label-stat-name");
        Label weaponValue = new Label(pemain.getWeapon() != null ? pemain.getWeapon().getNama() : "—");
        weaponValue.getStyleClass().add("label-weapon-equipped");

        Label ammoTitle = new Label("PELURU");
        ammoTitle.getStyleClass().add("label-stat-name");
        Label ammoValue = new Label(pemain.getPeluru() != null ? String.valueOf(pemain.getPeluru().getJumlah()) : "—");
        ammoValue.getStyleClass().add("label-ammo-count");

        Label scoreTitle = new Label("SCORE");
        scoreTitle.getStyleClass().add("label-stat-name");
        Label scoreValue = new Label(String.valueOf(pemain.getScore()));
        scoreValue.getStyleClass().add("label-stat-value");

        Label levelTitle = new Label("LEVEL");
        levelTitle.getStyleClass().add("label-stat-name");
        Label levelValue = new Label(String.valueOf(pemain.getLevel()));
        levelValue.getStyleClass().add("label-stat-value");

        Label balanceTitle = new Label("SALDO");
        balanceTitle.getStyleClass().add("label-stat-name");
        Label balanceValue = new Label("Rp" + pemain.getStash().getSaldo());
        balanceValue.getStyleClass().add("label-balance");

        stats.add(weaponTitle, 0, 0); stats.add(weaponValue, 1, 0);
        stats.add(ammoTitle, 0, 1);   stats.add(ammoValue, 1, 1);
        stats.add(scoreTitle, 0, 2);  stats.add(scoreValue, 1, 2);
        stats.add(levelTitle, 2, 2);  stats.add(levelValue, 3, 2);
        stats.add(balanceTitle, 0, 3); stats.add(balanceValue, 1, 3);

        Button btnTembak = new Button("🔫 Tembak Musuh");
        btnTembak.getStyleClass().add("btn-danger");
        btnTembak.setMaxWidth(Double.MAX_VALUE);
        btnTembak.setOnAction(e -> {
            Pemain shooter = isPlayer1 ? player1 : player2;
            Pemain target = isPlayer1 ? player2 : player1;
            
            if (shooter.isMati()) {
                GameLogger.getInstance().log("\n[!] " + shooter.getNama() + " sudah mati, tidak bisa menembak.");
                return;
            }
            if (target.isMati()) {
                GameLogger.getInstance().log("\n[!] Musuh (" + target.getNama() + ") sudah mati.");
                return;
            }
            
            shooter.menembak(target);
            updatePlayerCards();
        });

        Separator sep2 = new Separator();
        
        Label lootLabel = new Label("📦 Aksi Looting");
        lootLabel.setStyle("-fx-text-fill: #58a6ff; -fx-font-weight: bold; -fx-font-size: 12px;");

        ComboBox<String> lootSourceCombo = new ComboBox<>();
        lootSourceCombo.getItems().addAll("Dari Jalanan", "Dari Musuh Mati");
        lootSourceCombo.getSelectionModel().selectFirst();
        lootSourceCombo.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> lootItemCombo = new ComboBox<>();
        lootItemCombo.setMaxWidth(Double.MAX_VALUE);

        Runnable updateLootOptions = () -> {
            String prevSource = lootSourceCombo.getValue();
            String prevItem = lootItemCombo.getValue();
            
            lootItemCombo.getItems().clear();
            if ("Dari Jalanan".equals(prevSource)) {
                lootItemCombo.getItems().addAll(
                    "Medkit (Rp 50.000)",
                    "Amunisi Ekstra (Rp 75.000)",
                    "Jam Tangan (Rp 200.000)",
                    "Senjata Karatan (Rp 150.000)"
                );
                lootItemCombo.setDisable(false);
            } else {
                Pemain deadEnemy = isPlayer1 ? player2 : player1;
                if (deadEnemy != null && deadEnemy.isMati()) {
                    boolean hasItems = deadEnemy.getStash().getJumlahBarang() > 0;
                    boolean hasUang = deadEnemy.getStash().getSaldo() > 0;
                    if (hasItems || hasUang) {
                        lootItemCombo.getItems().add("=== AMBIL SEMUA BARANG & UANG ===");
                        for (Item item : deadEnemy.getStash().getBarangList()) {
                            lootItemCombo.getItems().add(item.getNama() + " (Rp" + item.getHarga() + ")");
                        }
                        lootItemCombo.setDisable(false);
                    } else {
                        lootItemCombo.getItems().add("- Musuh tidak punya barang -");
                        lootItemCombo.setDisable(true);
                    }
                } else {
                    lootItemCombo.getItems().add("- Belum ada musuh mati -");
                    lootItemCombo.setDisable(true);
                }
            }
            
            if (prevItem != null && lootItemCombo.getItems().contains(prevItem)) {
                lootItemCombo.setValue(prevItem);
            } else {
                lootItemCombo.getSelectionModel().selectFirst();
            }
        };

        if (isPlayer1) p1UpdateLootOptions = updateLootOptions;
        else p2UpdateLootOptions = updateLootOptions;

        lootSourceCombo.setOnAction(e -> updateLootOptions.run());
        updateLootOptions.run(); // Initial population
        
        Button btnLoot = new Button("Ambil Item");
        btnLoot.getStyleClass().add("btn-secondary");
        btnLoot.setMaxWidth(Double.MAX_VALUE);
        btnLoot.setOnAction(e -> {
            Pemain alive = isPlayer1 ? player1 : player2;
            if (alive.isMati()) {
                GameLogger.getInstance().log("\n[!] " + alive.getNama() + " sudah mati, tidak bisa merampas.");
                return;
            }
            
            String source = lootSourceCombo.getValue();
            String selectedItem = lootItemCombo.getValue();
            
            if ("Dari Jalanan".equals(source)) {
                if (selectedItem == null || selectedItem.isEmpty()) return;
                int price = 0;
                String name = "Item";
                if (selectedItem.contains("Medkit")) { name = "Medkit"; price = 50000; }
                else if (selectedItem.contains("Amunisi")) { name = "Amunisi Ekstra"; price = 75000; }
                else if (selectedItem.contains("Jam Tangan")) { name = "Jam Tangan"; price = 200000; }
                else if (selectedItem.contains("Senjata Karatan")) { name = "Senjata Karatan"; price = 150000; }
                
                GameLogger.getInstance().log("\n--- " + alive.getNama() + " merampas " + name + " dari jalanan ---");
                Item lootItem = new Item(name, price);
                alive.merampas(lootItem);
            } else {
                Pemain dead = isPlayer1 ? player2 : player1;
                if (dead == null || !dead.isMati()) {
                    GameLogger.getInstance().log("\n[!] Musuh belum mati, tidak bisa diambil lootnya.");
                } else {
                    if (selectedItem == null || selectedItem.contains("- ")) {
                        GameLogger.getInstance().log("\n[!] Tidak ada item yang bisa diambil.");
                        return;
                    }
                    
                    if (selectedItem.contains("AMBIL SEMUA")) {
                        GameLogger.getInstance().log("\n--- " + alive.getNama() + " merampas semua item & uang dari " + dead.getNama() + " ---");
                        alive.ambilLootDari(dead);
                    } else {
                        int idx = selectedItem.lastIndexOf(" (Rp");
                        if (idx > 0) {
                            String itemName = selectedItem.substring(0, idx).trim();
                            Item itemToTake = null;
                            for (Item i : dead.getStash().getBarangList()) {
                                if (i.getNama().equals(itemName)) {
                                    itemToTake = i;
                                    break;
                                }
                            }
                            
                            if (itemToTake != null) {
                                GameLogger.getInstance().log("\n--- " + alive.getNama() + " merampas " + itemName + " dari " + dead.getNama() + " ---");
                                dead.getStash().getBarangList().remove(itemToTake);
                                alive.merampas(itemToTake);
                            } else {
                                GameLogger.getInstance().log("\n[!] Item " + itemName + " tidak ditemukan di inventory musuh.");
                            }
                        }
                    }
                }
            }
            updatePlayerCards();
        });

        VBox lootBox = new VBox(5, lootLabel, lootSourceCombo, lootItemCombo, btnLoot);
        lootBox.setPadding(new Insets(10));
        lootBox.setStyle("-fx-background-color: #161b22; -fx-border-color: #30363d; -fx-border-radius: 8; -fx-background-radius: 8;");

        card.getChildren().addAll(topBar, sep1, stats, btnTembak, sep2, lootBox);

        // Store label references for live updates
        if (isPlayer1) {
            p1NameLabel = nameLabel;
            p1StatusLabel = statusLabel;
            p1WeaponLabel = weaponValue;
            p1AmmoLabel = ammoValue;
            p1ScoreLabel = scoreValue;
            p1LevelLabel = levelValue;
            p1BalanceLabel = balanceValue;
        } else {
            p2NameLabel = nameLabel;
            p2StatusLabel = statusLabel;
            p2WeaponLabel = weaponValue;
            p2AmmoLabel = ammoValue;
            p2ScoreLabel = scoreValue;
            p2LevelLabel = levelValue;
            p2BalanceLabel = balanceValue;
        }

        return card;
    }

    /**
     * Membuat bilah tombol aksi permainan (Auto Battle, Beli Peluru, Jual Item, Menang, Bersihkan Log).
     *
     * @return kontainer {@link VBox} yang memuat jajaran tombol aksi.
     */
    private VBox createActionButtons() {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10, 0, 10, 0));

        // Row 1: Main actions
        HBox row1 = new HBox(12);
        row1.setAlignment(Pos.CENTER);

        Button fightBtn = new Button("⚔ Auto Battle");
        fightBtn.getStyleClass().add("btn-primary");
        fightBtn.setOnAction(e -> runBattle());

        Button shopBtn = new Button("🛒 Beli Peluru");
        shopBtn.getStyleClass().add("btn-success");
        shopBtn.setOnAction(e -> runBuyAmmo());

        Button sellBtn = new Button("💰 Jual Item");
        sellBtn.getStyleClass().add("btn-secondary");
        sellBtn.setOnAction(e -> runSellItem());

        Button winBtn = new Button("🏆 Menang & Selesai");
        winBtn.getStyleClass().add("btn-primary");
        winBtn.setStyle("-fx-background-color: linear-gradient(to bottom, #ffd600, #ccab00); -fx-text-fill: #000; -fx-font-weight: bold;");
        winBtn.setOnAction(e -> runWin());

        Button clearLogBtn = new Button("🗑 Bersihkan Log");
        clearLogBtn.getStyleClass().add("btn-danger");
        clearLogBtn.setOnAction(e -> battleLog.clear());

        row1.getChildren().addAll(fightBtn, shopBtn, sellBtn, winBtn, clearLogBtn);
        container.getChildren().addAll(row1);
        return container;
    }

    /**
     * Membuat panel penampil teks log pertempuran (Battle Log).
     *
     * @return kontainer {@link VBox} berisi TextArea dan judul panel.
     */
    private VBox createBattleLogPanel() {
        VBox panel = new VBox(8);

        Label logTitle = new Label("📜 BATTLE LOG");
        logTitle.getStyleClass().add("label-section");

        battleLog = new TextArea();
        battleLog.setEditable(false);
        battleLog.setWrapText(true);
        battleLog.setPrefRowCount(12);
        battleLog.setPromptText("Log pertempuran akan muncul di sini...");
        VBox.setVgrow(battleLog, Priority.ALWAYS);

        panel.getChildren().addAll(logTitle, battleLog);
        return panel;
    }

    /**
     * Membuat bilah bagian bawah (Footer) yang menampilkan informasi versi aplikasi, JavaFX, dan Java runtime.
     *
     * @return kontainer {@link HBox} untuk footer aplikasi.
     */
    private HBox createFooter() {
        HBox footer = new HBox();
        footer.getStyleClass().add("footer-bar");
        footer.setAlignment(Pos.CENTER);

        Label footerText = new Label("Extraction Shooter Simulator v1.0  •  JavaFX " +
            SystemInfo.javafxVersion() + "  •  Java " + SystemInfo.javaVersion());
        footerText.getStyleClass().add("footer-text");

        footer.getChildren().add(footerText);
        return footer;
    }

    // =====================================================================
    //  GAME ACTIONS
    // =====================================================================

    /**
     * Menjalankan simulasi pertempuran otomatis (Auto Battle) secara turn-based di thread terpisah.
     * Mengatur ulang status petarung, menjalankan logika {@link Pertempuran},
     * dan memperbarui tampilan kartu pemenang dengan efek animasi denyut (pulse).
     */
    private void runBattle() {
        if (player1.isMati() || player2.isMati()) {
            GameLogger.getInstance().log("\n[!] Salah satu pemain sudah mati. Reset game untuk bermain lagi.");
            return;
        }

        // Reset status for new match
        player1 = new Pemain(player1.getIdPemain(), player1.getNama());
        player2 = new Pemain(player2.getIdPemain(), player2.getNama());

        // Re-equip weapons from setup
        player1.equipWeapon(createWeaponFromIndex(player1WeaponCombo.getSelectionModel().getSelectedIndex()));
        player2.equipWeapon(createWeaponFromIndex(player2WeaponCombo.getSelectionModel().getSelectedIndex()));

        // Run battle in background thread to not freeze UI
        new Thread(() -> {
            Pertempuran match = new Pertempuran(player1, player2);
            match.mulai();

            Platform.runLater(() -> {
                updatePlayerCards();

                // Animate winner card
                if (match.getPemenang() != null) {
                    VBox winnerCard = (match.getPemenang() == player1) ? player1Card : player2Card;
                    winnerCard.getStyleClass().add("card-player-winner");

                    // Pulse animation on winner
                    ScaleTransition pulse = new ScaleTransition(Duration.millis(300), winnerCard);
                    pulse.setFromX(1.0);
                    pulse.setFromY(1.0);
                    pulse.setToX(1.03);
                    pulse.setToY(1.03);
                    pulse.setCycleCount(4);
                    pulse.setAutoReverse(true);
                    pulse.play();

                    VBox loserCard = (match.getPemenang() == player1) ? player2Card : player1Card;
                    loserCard.getStyleClass().add("card-player-dead");
                }
            });
        }).start();
    }

    /**
     * Menjalankan aksi pembelian amunisi tambahan bertipe armor-piercing untuk pemain yang masih bertahan hidup.
     */
    private void runBuyAmmo() {
        Pemain alive = !player1.isMati() ? player1 : (!player2.isMati() ? player2 : null);
        if (alive == null) {
            GameLogger.getInstance().log("\n[!] Tidak ada pemain hidup untuk membeli peluru.");
            return;
        }

        GameLogger.getInstance().log("\n--- " + alive.getNama() + " membeli peluru baru ---");
        alive.belanjaPeluru(new Peluru("Armor Piercing", 7.62, 10, 25000, 15));
        updatePlayerCards();
    }

    /**
     * Menjual barang pertama yang ada di inventaris stash pemain yang masih hidup untuk memperoleh uang tunai.
     */
    private void runSellItem() {
        Pemain alive = !player1.isMati() ? player1 : (!player2.isMati() ? player2 : null);
        if (alive == null) {
            GameLogger.getInstance().log("\n[!] Tidak ada pemain hidup.");
            return;
        }

        if (alive.getStash().getJumlahBarang() == 0) {
            GameLogger.getInstance().log("\n[!] " + alive.getNama() + " tidak punya barang untuk dijual.");
            return;
        }

        // Sell the first item
        Item itemToSell = alive.getStash().getBarangList().get(0);
        GameLogger.getInstance().log("\n--- " + alive.getNama() + " menjual item ---");
        alive.getStash().jualItem(itemToSell);
        GameLogger.getInstance().log("Saldo sekarang: Rp" + alive.getStash().getSaldo());
        updatePlayerCards();
    }

    /**
     * Memanggil aksi menang bagi pemain yang masih bertahan hidup dan memperbarui kartu pemain di layar.
     */
    private void runWin() {
        Pemain alive = !player1.isMati() ? player1 : (!player2.isMati() ? player2 : null);
        if (alive == null) {
            GameLogger.getInstance().log("\n[!] Tidak ada pemain hidup untuk menang.");
            return;
        }

        alive.menang();
        updatePlayerCards();
    }

    // =====================================================================
    //  UI UPDATES
    // =====================================================================

    /**
     * Memperbarui seluruh label data statistik dan opsi drop-down looting kedua pemain di antarmuka JavaFX thread.
     */
    private void updatePlayerCards() {
        Platform.runLater(() -> {
            updatePlayerLabels(player1, p1StatusLabel, p1WeaponLabel, p1AmmoLabel, p1ScoreLabel, p1LevelLabel, p1BalanceLabel);
            updatePlayerLabels(player2, p2StatusLabel, p2WeaponLabel, p2AmmoLabel, p2ScoreLabel, p2LevelLabel, p2BalanceLabel);
            if (p1UpdateLootOptions != null) p1UpdateLootOptions.run();
            if (p2UpdateLootOptions != null) p2UpdateLootOptions.run();
        });
    }

    /**
     * Memperbarui label visual untuk satu pemain (status kondisi, senjata, sisa amunisi, skor, level, dan saldo).
     *
     * @param p       objek {@link Pemain} sumber data.
     * @param status  label indikator status hidup/menang/tewas.
     * @param weapon  label nama senjata dan damage.
     * @param ammo    label jenis dan kuantitas amunisi.
     * @param score   label total skor.
     * @param level   label tingkat level.
     * @param balance label jumlah saldo uang.
     */
    private void updatePlayerLabels(Pemain p, Label status, Label weapon, Label ammo, Label score, Label level, Label balance) {
        if (status == null) return; // not yet initialized

        // Status
        if (p.isMati()) {
            status.setText("✕ TEWAS");
            status.getStyleClass().removeAll("label-status-alive", "label-status-winner");
            status.getStyleClass().add("label-status-dead");
        } else if (p.isMenang()) {
            status.setText("★ MENANG");
            status.getStyleClass().removeAll("label-status-alive", "label-status-dead");
            status.getStyleClass().add("label-status-winner");
        } else if (p.isSedangBermain()) {
            status.setText("● BERMAIN");
            status.getStyleClass().removeAll("label-status-dead", "label-status-winner");
            status.getStyleClass().add("label-status-alive");
        } else {
            status.setText("● SIAP");
            status.getStyleClass().removeAll("label-status-dead", "label-status-winner");
            status.getStyleClass().add("label-status-alive");
        }

        // Weapon
        weapon.setText(p.getWeapon() != null ? p.getWeapon().getNama() + " (DMG:" + p.getWeapon().getDamage() + ")" : "—");

        // Ammo
        ammo.setText(p.getPeluru() != null ? p.getPeluru().getNama() + " x" + p.getPeluru().getJumlah() : "—");

        // Score
        score.setText(String.valueOf(p.getScore()));

        // Level
        level.setText(String.valueOf(p.getLevel()));

        // Balance
        balance.setText("Rp" + p.getStash().getSaldo());
    }

    // =====================================================================
    //  HELPERS
    // =====================================================================

    /**
     * Membuat elemen ruang pemisah vertikal transparan.
     *
     * @param height tinggi spasi pemisah dalam piksel.
     * @return objek {@link Region} penyedia spasi.
     */
    private Region createSpacer(double height) {
        Region spacer = new Region();
        spacer.setPrefHeight(height);
        return spacer;
    }

    /**
     * Titik masuk eksekusi utama peluncur aplikasi JavaFX.
     *
     * @param args argumen baris perintah.
     */
    public static void main(String[] args) {
        launch();
    }
}