package com.digdaya.dal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dal_analysis_history")
public class DalHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "analysis_date")
    private LocalDateTime analysisDate;

    @Column(name = "analyzed_count")
    private Integer analyzedCount;

    @Column(name = "risk_score")
    private Double riskScore;

    @Column(name = "is_anomaly")
    private Boolean isAnomaly;

    // Konstruktor kosong wajib untuk JPA
    public DalHistoryEntity() {}

    // Konstruktor untuk mempermudah insert data
    public DalHistoryEntity(Integer analyzedCount, Double riskScore, Boolean isAnomaly) {
        this.analysisDate = LocalDateTime.now();
        this.analyzedCount = analyzedCount;
        this.riskScore = riskScore;
        this.isAnomaly = isAnomaly;
    }

    // --- Getter & Setter ---
    public Long getId() { return id; }
    public LocalDateTime getAnalysisDate() { return analysisDate; }
    public Integer getAnalyzedCount() { return analyzedCount; }
    public Double getRiskScore() { return riskScore; }
    public Boolean getIsAnomaly() { return isAnomaly; }
}
