package com.nice.coday.models;

public class VehicleData {
     private String vehicleType;
     private double totalEnergyConsumed;
     private double totalChargingTime;
     private int numberOfTrips;

     public VehicleData(String vehicleType) {
          this.vehicleType = vehicleType;
          this.totalEnergyConsumed = 0;
          this.totalChargingTime = 0;
          this.numberOfTrips = 0;
     }
     public String getVehicleType() {
          return vehicleType;
     }
     public double getTotalEnergyConsumed() {
          return totalEnergyConsumed;
     }
     public double getTotalChargingTime() {
          return totalChargingTime;
     }
     public int getNumberOfTrips() {
          return numberOfTrips;
     }
     public void addEnergyConsumed(double energy) {
          this.totalEnergyConsumed += energy;
     }
     public void addChargingTime(double time) {
          this.totalChargingTime += time;
     }
     public void incrementTrips() {
          this.numberOfTrips++;
     }
     @Override
     public String toString() {
          return "VehicleType: " + vehicleType +
                  ", Total Energy Consumed: " + totalEnergyConsumed +
                  ", Total Charging Time: " + totalChargingTime +
                  ", Number of Trips: " + numberOfTrips;
     }
}