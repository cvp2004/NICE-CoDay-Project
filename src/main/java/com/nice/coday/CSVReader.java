package com.nice.coday;

import com.nice.coday.models.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CSVReader {

     public static ArrayList<com.nice.coday.models.ChargingStation> readChargingStations(Path filePath) throws IOException {
          ArrayList<com.nice.coday.models.ChargingStation> stations = new ArrayList<>();
          BufferedReader br = new BufferedReader(new FileReader(filePath.toString()));
          String line;
          br.readLine(); // Skip header
          while ((line = br.readLine()) != null) {
               String[] data = line.split(",");
               stations.add(new com.nice.coday.models.ChargingStation(data[0], Double.parseDouble(data[1])));
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
               vehicles.add(new VehicleType(
                       data[0],
                       Integer.parseInt(data[1]),
                       Double.parseDouble(data[2])
               ));
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
