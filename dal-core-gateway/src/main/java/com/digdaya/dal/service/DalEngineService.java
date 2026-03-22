package com.digdaya.dal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DalEngineService {

    // Mengambil URL Python dari application.properties
    @Value("${dal.engine.url}")
    private String pythonApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Mengirim array transaksi ke Python FastAPI dan mengembalikan responsnya
     */
    // Tambahkan parameter double threshold
    public Map<String, Object> analyzeTransactions(List<Double> transactions, double threshold) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
    
            Map<String, Object> body = new HashMap<>();
            body.put("values", transactions);
            body.put("threshold", threshold); // Kirim nilai threshold dari UI ke Python!
    
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
    
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(
                    pythonApiUrl,
                    request,
                    Map.class
            );
            return response;
        } catch (Exception e) {
            System.err.println("❌ Gagal menghubungi DAL Engine: " + e.getMessage());
            return Map.of("status", "error", "message", e.getMessage());
        }
    }
}
