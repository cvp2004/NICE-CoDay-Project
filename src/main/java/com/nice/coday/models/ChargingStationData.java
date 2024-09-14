package com.nice.coday.models;

public class ChargingStationData {
     private String stationName;
     private double totalChargingTime;

     public ChargingStationData(String stationName) {
          this.stationName = stationName;
          this.totalChargingTime = 0;
     }

     public String getStationName() {
          return stationName;
     }

     public long getTotalChargingTime() {
          return (long) totalChargingTime;
     }

     public void addChargingTime(double time) {
          this.totalChargingTime += time;
     }

     @Override
     public String toString() {
          return "ChargingStation: " + stationName +
                  ", Total Charging Time: " + totalChargingTime;
     }
}
