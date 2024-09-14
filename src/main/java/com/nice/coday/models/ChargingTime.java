package com.nice.coday.models;

public class ChargingTime {
     String vehicleType;
     String chargingStation;
     int timeToChargePerUnit;

     public String getVehicleType() {
          return vehicleType;
     }

     public void setVehicleType(String vehicleType) {
          this.vehicleType = vehicleType;
     }

     public String getChargingStation() {
          return chargingStation;
     }

     public void setChargingStation(String chargingStation) {
          this.chargingStation = chargingStation;
     }

     public int getTimeToChargePerUnit() {
          return timeToChargePerUnit;
     }

     public void setTimeToChargePerUnit(int timeToChargePerUnit) {
          this.timeToChargePerUnit = timeToChargePerUnit;
     }
     public ChargingTime(String vehicleType, String chargingStation, int timeToChargePerUnit) {
          this.vehicleType = vehicleType;
          this.chargingStation = chargingStation;
          this.timeToChargePerUnit = timeToChargePerUnit;
     }
}
