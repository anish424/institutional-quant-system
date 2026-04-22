import numpy as np
from sklearn.ensemble import RandomForestClassifier

class MLModel:
    def __init__(self):
        self.model = RandomForestClassifier(n_estimators=100, random_state=42)
        self.is_trained = False

    def train(self, X, y):
        self.model.fit(X, y)
        self.is_trained = True

    def predict(self, features):
        if not self.is_trained:
            return np.random.random()
        return self.model.predict_proba([features])[0][1]