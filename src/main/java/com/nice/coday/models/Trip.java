package com.nice.coday.models;

public class Trip {
     int id;
     String vehicleType;
     double remainingBatteryPercentage;
     String entryPoint;
     String exitPoint;

     public int getId() {
          return id;
     }

     public void setId(int id) {
          this.id = id;
     }

     public String getVehicleType() {
          return vehicleType;
     }

     public void setVehicleType(String vehicleType) {
          this.vehicleType = vehicleType;
     }

     public double getRemainingBatteryPercentage() {
          return remainingBatteryPercentage;
     }

     public void setRemainingBatteryPercentage(double remainingBatteryPercentage) {
          this.remainingBatteryPercentage = remainingBatteryPercentage;
     }

     public String getEntryPoint() {
          return entryPoint;
     }

     public void setEntryPoint(String entryPoint) {
          this.entryPoint = entryPoint;
     }

     public String getExitPoint() {
          return exitPoint;
     }

     public void setExitPoint(String exitPoint) {
          this.exitPoint = exitPoint;
     }

     public Trip(int id, String vehicleType, double remainingBatteryPercentage, String entryPoint, String exitPoint) {
          this.id = id;
          this.vehicleType = vehicleType;
          this.remainingBatteryPercentage = remainingBatteryPercentage;
          this.entryPoint = entryPoint;
          this.exitPoint = exitPoint;
     }
}
