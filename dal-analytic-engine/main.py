from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
import numpy as np
from dal_core import DrawbackAsymmetricalLoop

app = FastAPI(title="DAL-FDS Analytic Engine", version="1.0")

class TransactionPayload(BaseModel):
    values: List[float]
    threshold: float = 0.65  # Tambahkan baris ini (nilai default)

@app.post("/api/v1/analyze")
def analyze_transactions(payload: TransactionPayload):
    if len(payload.values) < 3:
        raise HTTPException(status_code=400, detail="DAL membutuhkan minimal 3 data point")
    
    try:
        dal = DrawbackAsymmetricalLoop(payload.values)
        result = dal.drawback_loop(normalize=True)
        
        pattern = dal.detect_pattern()
        risk_metric = float(pattern["residual_strength"])
        
        return {
            "status": "success",
            "analyzed_count": len(payload.values),
            "risk_score": round(risk_metric, 4),
            "is_anomaly": risk_metric > payload.threshold, # Ganti angka 0.65 jadi dinamis
            "dal_normalized_output": result.tolist()
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/health")
def health_check():
    return {"status": "DAL Engine is running perfectly"}
