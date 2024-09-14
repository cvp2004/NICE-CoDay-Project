package com.nice.coday.models;

public class VehicleType {
     String type;
     int numberOfUnitsForFullyCharge;
     double mileage;

     public String getType() {
          return type;
     }

     public void setType(String type) {
          this.type = type;
     }

     public int getNumberOfUnitsForFullyCharge() {
          return numberOfUnitsForFullyCharge;
     }

     public void setNumberOfUnitsForFullyCharge(int numberOfUnitsForFullyCharge) {
          this.numberOfUnitsForFullyCharge = numberOfUnitsForFullyCharge;
     }

     public double getMileage() {
          return mileage;
     }

     public void setMileage(double mileage) {
          this.mileage = mileage;
     }

     public VehicleType(String type, int numberOfUnitsForFullyCharge, double mileage) {
          this.type = type;
          this.numberOfUnitsForFullyCharge = numberOfUnitsForFullyCharge;
          this.mileage = mileage;
     }
}
