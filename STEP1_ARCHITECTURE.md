# STEP 1: Minimal Working System - Architecture Guide

## Components

### Java Execution Engine
- Main.java: Entry point, trading loop with 10 iterations
- Trade.java: Trade object with entry/exit prices and PnL
- FeatureGenerator.java: Random feature generation (10 features)
- MLClient.java: HTTP client for Flask API

### Python Flask ML API
- app.py: Flask server on localhost:5000 with /predict endpoint
- ml_model.py: RandomForestClassifier (mock predictions in STEP 1)
- requirements.txt: Dependencies

## Data Flow
Main.java -> FeatureGenerator -> MLClient -> Flask API -> Prediction -> Trade Execution -> trades.csv

## Build & Run

mvn clean install
mvn exec:java -Dexec.mainClass="com.quant.engine.Main"

Flask API:
cd python-ml-api
pip install -r requirements.txt
python app.py