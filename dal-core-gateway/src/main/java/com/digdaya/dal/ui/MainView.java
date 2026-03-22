package com.digdaya.dal.ui;

import com.vaadin.flow.component.textfield.NumberField;
import com.digdaya.dal.entity.DalHistoryEntity;
import com.digdaya.dal.repository.DalHistoryRepository;
import com.digdaya.dal.service.DalEngineService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;
import java.util.Map;

/**
 * Halaman Utama (Dashboard) DAL-FDS
 */
@Route("") // Menandakan ini adalah halaman root (/)
public class MainView extends VerticalLayout {

    private final DalEngineService dalEngineService;
    private final DalHistoryRepository dalHistoryRepository;

    // Komponen Visual
    private Grid<DalHistoryEntity> historyGrid;
    private Span currentStatusSpan;
    private Span currentRiskScoreSpan;
    private NumberField thresholdField;

    // --- Constructor (Sihir Dependency Injection Terjadi di Sini) ---
    public MainView(DalEngineService dalEngineService, DalHistoryRepository dalHistoryRepository) {
        this.dalEngineService = dalEngineService;
        this.dalHistoryRepository = dalHistoryRepository;

        // Atur layout utama
        setSizeFull();
        setPadding(true);
        addClassName(LumoUtility.Background.BASE);

        // Susun Komponen
        addHeader();
        addControlPanel();
        addResultView();
        addHistoryGrid();

        // Load data sejarah saat halaman pertama kali dibuka
        updateGridData();
    }

    // ======================================================================
    // UI LAYOUTING (LUKISAN "FISIK" DALAM JAVA)
    // ======================================================================

    private void addHeader() {
        H1 title = new H1("🛡️ DAL-FDS Engine");
        title.addClassNames(
            LumoUtility.FontSize.XXLARGE,
            LumoUtility.Margin.Top.NONE,
            LumoUtility.TextColor.HEADER
        );
        add(title);
    }

    private void addControlPanel() {
        H2 subtitle = new H2("Simulasi Analisis Transaksi");
        subtitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.Top.MEDIUM);
    
            // --- TAMBAHAN BARU: Input Threshold Interaktif ---
        thresholdField = new NumberField("Batas Anomali (Threshold)");
        thresholdField.setValue(0.65); // Nilai bawaan
        thresholdField.setStep(0.01);
        thresholdField.setMin(0.01);
        thresholdField.setMax(1.00);
            
        Button analyzeButton = new Button("Jalankan Analisis", VaadinIcon.PLAY_CIRCLE.create());
        analyzeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
            
            // Gabungkan dalam satu baris horizontal agar rapi
        HorizontalLayout controls = new HorizontalLayout(thresholdField, analyzeButton);
        controls.setAlignItems(FlexComponent.Alignment.BASELINE); // Sejajarkan ke bawah
    
        analyzeButton.addClickListener(click -> performAnalysis());
    
        add(subtitle, controls);
    }

    private void addResultView() {
        HorizontalLayout resultLayout = new HorizontalLayout();
        resultLayout.setWidthFull();
        resultLayout.setPadding(true);
        resultLayout.addClassNames(
            LumoUtility.Background.CONTRAST_5,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Margin.Top.MEDIUM
        );
        resultLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);

        // Panel Status Anomali
        VerticalLayout statusPanel = new VerticalLayout();
        Span statusLabel = new Span("STATUS TERAKHIR");
        statusLabel.addClassName(LumoUtility.TextColor.SECONDARY);
        currentStatusSpan = new Span("-");
        currentStatusSpan.addClassNames(LumoUtility.FontSize.XXLARGE, LumoUtility.FontWeight.BOLD);
        statusPanel.add(statusLabel, currentStatusSpan);
        statusPanel.setAlignItems(FlexComponent.Alignment.CENTER);

        // Panel Skor Risiko
        VerticalLayout scorePanel = new VerticalLayout();
        Span scoreLabel = new Span("SKOR RISIKO");
        scoreLabel.addClassName(LumoUtility.TextColor.SECONDARY);
        currentRiskScoreSpan = new Span("0.0000");
        currentRiskScoreSpan.addClassNames(LumoUtility.FontSize.XXLARGE, LumoUtility.FontWeight.BOLD);
        scorePanel.add(scoreLabel, currentRiskScoreSpan);
        scorePanel.setAlignItems(FlexComponent.Alignment.CENTER);

        resultLayout.add(statusPanel, scorePanel);
        add(resultLayout);
    }

    private void addHistoryGrid() {
        H2 subtitle = new H2("Riwayat Audit Analisis (PostgreSQL)");
        subtitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.Top.XLARGE);

        // Inisialisasi Tabel/Grid
        historyGrid = new Grid<>(DalHistoryEntity.class, false);
        historyGrid.setSizeFull();
        historyGrid.addClassName(LumoUtility.Margin.Top.SMALL);

        // Kolom Tanggal
        historyGrid.addColumn(DalHistoryEntity::getAnalysisDate)
                .setHeader("Waktu Audit")
                .setAutoWidth(true);

        // Kolom Jumlah Data
        historyGrid.addColumn(DalHistoryEntity::getAnalyzedCount)
                .setHeader("Total Transaksi")
                .setAutoWidth(true);

        // Kolom Skor (Gaya Badge)
        historyGrid.addColumn(DalHistoryEntity::getRiskScore)
                .setHeader("Skor Risiko")
                .setAutoWidth(true);

        // Kolom IsAnomaly (Sihir Visual: Merah/Hijau)
        historyGrid.addComponentColumn(entity -> {
            Span badge = new Span();
            if (entity.getIsAnomaly()) {
                badge.setText("⚠️ ANOMALI");
                badge.getElement().setAttribute("theme", "badge error pill");
            } else {
                badge.setText("✅ AMAN");
                badge.getElement().setAttribute("theme", "badge success pill");
            }
            return badge;
        }).setHeader("Kesimpulan").setAutoWidth(true);

        add(subtitle, historyGrid);
    }

    // ======================================================================
    // LOGIKA BISNIS (MENGHUBUNGKAN JAVA -> PYTHON -> POSTGRES)
    // ======================================================================

    private void performAnalysis() {
        List<Double> simulatedData = List.of(10.5, 12.0, 15.2, 14.8, 11.1, 99.9, 13.5, 16.0, 12.2);
    
            // Ambil nilai dari UI
        double currentThreshold = thresholdField.getValue();
    
            // Tembak Python Engine dengan mengirimkan threshold
        Map<String, Object> result = dalEngineService.analyzeTransactions(simulatedData, currentThreshold);
        
        // 3. Update UI Berdasarkan Respon Python
        if ("success".equals(result.get("status"))) {
            Double riskScore = (Double) result.get("risk_score");
            Boolean isAnomaly = (Boolean) result.get("is_anomaly");
            Integer analyzedCount = (Integer) result.get("analyzed_count");

            // Perbarui Visual Utama
            currentRiskScoreSpan.setText(String.format("%.4f", riskScore));
            if (isAnomaly) {
                currentStatusSpan.setText("ANOMALI");
                currentStatusSpan.addClassName(LumoUtility.TextColor.ERROR);
                currentStatusSpan.removeClassName(LumoUtility.TextColor.SUCCESS);
                showNotification("Peringatan FDS: Pola Asimetris Terdeteksi!", NotificationVariant.LUMO_ERROR);
            } else {
                currentStatusSpan.setText("AMAN");
                currentStatusSpan.addClassName(LumoUtility.TextColor.SUCCESS);
                currentStatusSpan.removeClassName(LumoUtility.TextColor.ERROR);
                showNotification("Analisis Selesai: Transaksi Terlihat Wajar.", NotificationVariant.LUMO_SUCCESS);
            }

            // 4. SIMPAN KE POSTGRESQL (Sihir "Kepribadian" Database)
            DalHistoryEntity entity = new DalHistoryEntity(analyzedCount, riskScore, isAnomaly);
            dalHistoryRepository.save(entity);

            // 5. Perbarui Tabel Riwayat
            updateGridData();

        } else {
            // Jika Python Gagal
            currentStatusSpan.setText("ERROR");
            currentStatusSpan.addClassName(LumoUtility.TextColor.SECONDARY);
            currentRiskScoreSpan.setText("0.0000");
            showNotification("Gagal menghubungi Mesin Analitik.", NotificationVariant.LUMO_ERROR);
        }
    }

    private void updateGridData() {
        // Ambil data terbaru dari Postgres dan masukkan ke Grid
        List<DalHistoryEntity> history = dalHistoryRepository.findAllByOrderByAnalysisDateDesc();
        historyGrid.setItems(history);
    }

    private void showNotification(String text, NotificationVariant variant) {
        Notification notification = Notification.show(text);
        notification.setDuration(3000); // 3 detik
        notification.setPosition(Notification.Position.BOTTOM_END);
        notification.addThemeVariants(variant);
    }
}
