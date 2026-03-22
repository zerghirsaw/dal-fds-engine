# 🛡️ DAL-FDS Engine 
### (Drawback Asymmetrical Loop - Fraud Detection System)

[![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)](https://www.oracle.com/java/)
[![Python](https://img.shields.io/badge/Python-3.10+-blue?style=for-the-badge&logo=python)](https://www.python.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![FastAPI](https://img.shields.io/badge/FastAPI-Latest-009688?style=for-the-badge&logo=fastapi)](https://fastapi.tiangolo.com/)
[![Docker](https://img.shields.io/badge/Docker-PostgreSQL-2496ED?style=for-the-badge&logo=docker)](https://www.docker.com/)

**DAL-FDS Engine** adalah sistem deteksi *fraud* (penipuan) transaksi keuangan generasi terbaru yang menggunakan arsitektur **Microservices** dan algoritma analitik kustom **Drawback Asymmetrical Loop (DAL)**.

Sistem ini dirancang untuk mendeteksi pola transaksi anomali secara real-time dengan tingkat keamanan tinggi melalui pendekatan *Backend-Driven UI*.

---

## 🏗️ System Architecture

Proyek ini dibangun dengan memisahkan tanggung jawab (Separation of Concerns) menjadi 3 lapisan utama:

1.  **Analytic Engine (Python/FastAPI)**: "Otak" AI yang menjalankan algoritma DAL untuk menghitung skor risiko berdasarkan pola asimetris data.
2.  **Core Gateway (Java/Spring Boot)**: Jembatan orkestrasi yang mengelola logika bisnis, autentikasi, dan komunikasi antar layanan.
3.  **Audit Database (PostgreSQL/Docker)**: Penyimpanan persisten untuk setiap riwayat analisis guna kebutuhan kepatuhan (*compliance*) dan audit trail.

---

## ✨ Key Features

* **Real-time Risk Scoring**: Analisis instan menggunakan mesin Python yang dioptimalkan untuk perhitungan asimetris.
* **Interactive Threshold Tuning**: Analis dapat menyesuaikan sensitivitas deteksi langsung dari UI tanpa mengubah kode.
* **Backend-Driven UI (Vaadin)**: Keamanan maksimal karena logika deteksi tidak pernah dikirim ke sisi browser.
* **Session-Hardened Security**: Dilengkapi dengan manajemen sesi anti-DDoS untuk menjaga stabilitas RAM server.
* **Audit Trail Grid**: Laporan riwayat analisis yang lengkap dan tersimpan secara permanen di PostgreSQL.

---

## 🚀 Quick Start (Installation)

### 1. Prasyarat
* Java 17 & Maven
* Python 3.10+
* Docker & Docker Compose

### 2. Jalankan Database (Docker)
```bash
docker compose up -d
```

### 3. Jalankan Python Analytic Engine
```bash

cd dal-analytic-engine
source daltic-engine/bin/activate
pip install -r requirements.txt
uvicorn main:app --reload --port 8000
```
### 4. Jalankan Java Core Gateway
```bash

cd dal-core-gateway
mvn spring-boot:run
```
Buka browser di: http://localhost:8080
 The DAL Algorithm (Drawback Asymmetrical Loop)

Berbeda dengan algoritma Black-box standar, DAL bekerja dengan mencari deviasi asimetris dalam urutan transaksi. Ia menghitung "daya tarik" pola normal dan memberikan penalti (skor risiko) jika ditemukan lompatan data yang tidak sesuai dengan ritme looping transaksional pengguna.
 Security Measures

   * Logic Isolation: Algoritma deteksi terkunci di sisi server (mencegah reverse-engineering).

   * Anti-DoS Policy: Penutupan sesi otomatis untuk aktivitas idle guna mencegah pemborosan memori.

   * Environment Isolated: Database berjalan di dalam container Docker yang terisolasi dari jaringan publik laptop.

### Developed by @zerghirsaw
Built for the Future of Financial Security.
