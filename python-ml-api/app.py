from flask import Flask, request, jsonify
import numpy as np
from ml_model import MLModel

app = Flask(__name__)
model = MLModel()

@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.json
        features = np.array(data.get('features', []))
        probability = model.predict(features)
        return jsonify({'success': True, 'probability': float(probability)})
    except Exception as e:
        return jsonify({'success': False, 'error': str(e)}), 400

@app.route('/health', methods=['GET'])
def health():
    return jsonify({'status': 'healthy'})

if __name__ == '__main__':
    app.run(debug=True, port=5000}