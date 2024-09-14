package com.nice.coday.models;

public class ChargingStation {
     String name;
     double distanceFromStart;

     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }

     public double getDistanceFromStart() {
          return distanceFromStart;
     }

     public void setDistanceFromStart(double distanceFromStart) {
          this.distanceFromStart = distanceFromStart;
     }

     public ChargingStation(String name, double distanceFromStart) {
          this.name = name;
          this.distanceFromStart = distanceFromStart;
     }
}
