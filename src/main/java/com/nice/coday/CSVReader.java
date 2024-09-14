package com.nice.coday;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Data Model for Charging Station
class ChargingStation {
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

// Data Model for Entry-Exit Point
class EntryExitPoint {
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

     public EntryExitPoint(String name, double distanceFromStart) {
          this.name = name;
          this.distanceFromStart = distanceFromStart;
     }
}

// Data Model for Vehicle Type
class VehicleType {
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

// Data Model for Time to Charge
class ChargingTime {
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

// Data Model for Trip Details
class Trip {
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

public class CSVReader {

     public static ArrayList<ChargingStation> readChargingStations(Path filePath) throws IOException {
          ArrayList<ChargingStation> stations = new ArrayList<>();
          BufferedReader br = new BufferedReader(new FileReader(filePath.toString()));
          String line;
          br.readLine(); // Skip header
          while ((line = br.readLine()) != null) {
               String[] data = line.split(",");
               stations.add(new ChargingStation(data[0], Double.parseDouble(data[1])));
          }
          return stations;
     }

     public static ArrayList<EntryExitPoint> readEntryExitPoints(Path filePath) throws IOException {
          ArrayList<EntryExitPoint> points = new ArrayList<>();
          BufferedReader br = new BufferedReader(new FileReader(filePath.toString()));
          String line;
          br.readLine(); // Skip header
          while ((line = br.readLine()) != null) {
               String[] data = line.split(",");
               points.add(new EntryExitPoint(data[0], Double.parseDouble(data[1])));
          }
          return points;
     }

     public static ArrayList<VehicleType> readVehicleTypes(Path filePath) throws IOException {
          ArrayList<VehicleType> vehicles = new ArrayList<>();
          BufferedReader br = new BufferedReader(new FileReader(filePath.toString()));
          String line;
          br.readLine(); // Skip header
          while ((line = br.readLine()) != null) {
               String[] data = line.split(",");
               vehicles.add(new VehicleType(data[0], Integer.parseInt(data[1]), Double.parseDouble(data[2])));
          }
          return vehicles;
     }

     public static ArrayList<ChargingTime> readChargingTimes(Path filePath) throws IOException {
          ArrayList<ChargingTime> chargingTimes = new ArrayList<>();
          BufferedReader br = new BufferedReader(new FileReader(filePath.toString()));
          String line;
          br.readLine(); // Skip header
          while ((line = br.readLine()) != null) {
               String[] data = line.split(",");
               chargingTimes.add(new ChargingTime(data[0], data[1], Integer.parseInt(data[2])));
          }
          return chargingTimes;
     }

     public static ArrayList<Trip> readTrips(Path filePath) throws IOException {
          ArrayList<Trip> trips = new ArrayList<>();
          BufferedReader br = new BufferedReader(new FileReader(filePath.toString()));
          String line;
          br.readLine(); // Skip header
          while ((line = br.readLine()) != null) {
               String[] data = line.split(",");
               trips.add(new Trip(Integer.parseInt(data[0]), data[1], Double.parseDouble(data[2]), data[3], data[4]));
          }
          return trips;
     }
}
