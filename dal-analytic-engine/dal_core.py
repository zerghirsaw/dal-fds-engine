# ============================================================
# DAL - Drawback Asymmetrical Loop (Core Engine)
# Copyright (C) 2026 Zerg Hirsaw
# ============================================================

import numpy as np
from sklearn.preprocessing import MinMaxScaler

class DrawbackAsymmetricalLoop:
    """
    Drawback Asymmetrical Loop (DAL)
    --------------------------------
    Metode analisis berbasis:
    - Pola utama (trend)
    - Pola negasi asimetris
    - Faktor keseimbangan adaptif (cross factor)
    """

    def __init__(self, data):
        if len(data) < 3:
            raise ValueError("DAL membutuhkan minimal 3 data point")

        self.raw_data = np.asarray(data, dtype=float)
        self.data = None
        self.scaler = MinMaxScaler()

    # --------------------------------------------------------
    # Normalisasi
    # --------------------------------------------------------
    def normalize_data(self, enable=True):
        if not enable:
            self.data = self.raw_data.copy()
            return

        self.data = self.scaler.fit_transform(
            self.raw_data.reshape(-1, 1)
        ).flatten()

    # --------------------------------------------------------
    # Deteksi Pola Utama
    # --------------------------------------------------------
    def detect_pattern(self):
        x = np.arange(len(self.data))
        slope, intercept = np.polyfit(x, self.data, 1)

        predicted = slope * x + intercept
        residual = self.data - predicted

        return {
            "slope": slope,
            "intercept": intercept,
            "residual_strength": np.std(residual)
        }

    # --------------------------------------------------------
    # Pola Negasi Asimetris
    # --------------------------------------------------------
    def find_negation(self):
        mean = np.mean(self.data)
        std = np.std(self.data) + 1e-8

        deviation = self.data - mean
        asym_factor = np.abs(deviation) / std

        negation = mean - deviation * asym_factor
        return negation

    # --------------------------------------------------------
    # DAL Core Loop
    # --------------------------------------------------------
    def drawback_loop(self, normalize=True):
        self.normalize_data(enable=normalize)

        pattern = self.detect_pattern()
        negation = self.find_negation()

        # Cross Factor (stabil & adaptif)
        cross_factor = (
            abs(pattern["slope"]) /
            (pattern["residual_strength"] + 1e-6)
        )
        cross_factor = np.clip(cross_factor, 0.0, 3.0)

        # Bobot adaptif
        weight = np.clip(
            cross_factor / (cross_factor + 1.0),
            0.2,
            0.8
        )

        final_output = (
            self.data * (1 - weight) +
            negation * weight
        )

        return final_output
