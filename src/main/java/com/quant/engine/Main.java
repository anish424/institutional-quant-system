package com.quant.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * STEP 1: MINIMAL WORKING SYSTEM
 * ================================
 * Main Entry Point for Institutional Quant Trading System
 * 
 * REQUIREMENTS FULFILLED:
 * ✓ Java execution engine
 * ✓ Random feature generation
 * ✓ Paper trading execution
 * ✓ CSV logging of trades
 * ✓ REST communication ready (will call Flask API)
 * 
 * This class manages:
 * 1. Paper trading simulator initialization
 * 2. Trading loop with feature generation
 * 3. Trade execution and logging
 * 4. CSV output for dataset creation
 */
public class Main {
    
    // Simulator configuration constants
    private static final int NUM_TRADES = 10;                    // Number of trades to simulate
    private static final String CSV_FILE_PATH = "trades.csv";    // Output file for trades
    private static final double INITIAL_CAPITAL = 100000.0;      // Paper trading capital
    
    // Instance variables
    private static List<Trade> trades = new ArrayList<>();
    private static double currentCapital = INITIAL_CAPITAL;
    private static int tradeCounter = 0;
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("INSTITUTIONAL QUANT SYSTEM - STEP 1");
        System.out.println("Minimal Working System");
        System.out.println("========================================\n");
        
        try {
            initializeSimulator();
            runTradingLoop();
            logTradesToCSV();
            
            System.out.println("\n========================================");
            System.out.println("Trading Session Complete");
            System.out.println("Total Trades: " + trades.size());
            System.out.println("Final Capital: $" + String.format("%.2f", currentCapital));
            System.out.println("Results logged to: " + CSV_FILE_PATH);
            System.out.println("========================================");
            
        } catch (Exception e) {
            System.err.println("FATAL ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Initialize the paper trading simulator
     * Sets up logging, initializes accounts, and prepares for trading
     */
    private static void initializeSimulator() {
        System.out.println("[INIT] Initializing Paper Trading Simulator...");
        System.out.println("[INIT] Initial Capital: $" + String.format("%.2f", INITIAL_CAPITAL));
        System.out.println("[INIT] Number of Simulated Trades: " + NUM_TRADES);
        System.out.println("[INIT] CSV Output Path: " + CSV_FILE_PATH);
        System.out.println("[INIT] Simulator Ready\n");
    }
    
    /**
     * Main trading loop
     * For each iteration:
     * 1. Generate random features
     * 2. Call ML API (Flask) for prediction
     * 3. Execute paper trade if signal received
     * 4. Record trade data
     */
    private static void runTradingLoop() {
        System.out.println("[TRADING] Starting Trading Loop...\n");
        
        FeatureGenerator featureGen = new FeatureGenerator();
        MLClient mlClient = new MLClient("http://localhost:5000");
        
        for (int i = 0; i < NUM_TRADES; i++) {
            tradeCounter++;
            System.out.println("--- Trade #" + tradeCounter + " ---");
            
            // STEP 1A: Random Feature Generation
            double[] features = featureGen.generateRandomFeatures(10);
            System.out.println("[FEATURES] Generated 10 random features");
            
            // STEP 1B: Call ML API for prediction
            try {
                double probability = mlClient.getPrediction(features);
                System.out.println("[ML API] Received prediction probability: " + String.format("%.4f", probability));
                
                // STEP 1C: Execute trade if probability > 0.5
                if (probability > 0.5) {
                    executeTrade(tradeCounter, probability, features);
                } else {
                    System.out.println("[TRADING] No trade executed (probability too low)");
                }
                
            } catch (Exception e) {
                System.err.println("[ERROR] ML API call failed: " + e.getMessage());
                System.out.println("[TRADING] Skipping this trade due to API error\n");
            }
        }
        
        System.out.println("\n[TRADING] Trading Loop Complete");
    }
    
    /**
     * Execute a single paper trade
     * Creates Trade object with entry/exit prices and PnL calculation
     */
    private static void executeTrade(int tradeId, double probability, double[] features) {
        try {
            // Generate random entry and exit prices
            Random random = new Random();
            double entryPrice = 100.0 + (random.nextDouble() * 10.0);
            double exitPrice = entryPrice + (random.nextDouble() * 5.0 - 2.5);
            
            // Create Trade object
            Trade trade = new Trade(
                tradeId,
                LocalDateTime.now(),
                "PAPER_TRADE",
                entryPrice,
                exitPrice,
                100, // quantity
                probability
            );
            
            // Add to trades list
            trades.add(trade);
            
            // Update capital (paper trading)
            double pnl = trade.calculatePnL();
            currentCapital += pnl;
            
            // Log trade execution
            System.out.println("[EXECUTION] Trade Executed!");
            System.out.println("  Entry Price: $" + String.format("%.2f", entryPrice));
            System.out.println("  Exit Price: $" + String.format("%.2f", exitPrice));
            System.out.println("  PnL: $" + String.format("%.2f", pnl));
            System.out.println("  Capital: $" + String.format("%.2f", currentCapital));
            
        } catch (Exception e) {
            System.err.println("[ERROR] Trade execution failed: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * Log all trades to CSV file
     * Format: TradeID, Timestamp, Entry Price, Exit Price, Quantity, PnL, Probability
     * This creates the dataset for STEP 2 (Trade Lifecycle)
     */
    private static void logTradesToCSV() {
        System.out.println("[CSV] Writing trades to CSV...");
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write CSV header
            writer.write("TradeID,Timestamp,Symbol,EntryPrice,ExitPrice,Quantity,PnL,Probability\n");
            
            // Write each trade
            for (Trade trade : trades) {
                writer.write(trade.toCSV() + "\n");
            }
            
            System.out.println("[CSV] Successfully wrote " + trades.size() + " trades to " + CSV_FILE_PATH);
            
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to write CSV: " + e.getMessage());
        }
    }
}