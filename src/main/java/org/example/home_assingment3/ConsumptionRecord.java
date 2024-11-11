package org.example.home_assingment3;

public class ConsumptionRecord {
    private double distance, fuel;
    public ConsumptionRecord(double distance, double fuel){
        this.distance = distance;
        this.fuel = fuel;
    }
    public double calculateConsumption() {
        return fuel != 0 ? (fuel * 1.67) : 0;
    }
}
