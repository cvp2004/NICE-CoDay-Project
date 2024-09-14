package com.nice.coday;

import com.nice.coday.models.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ElectricityConsumptionCalculatorImpl implements ElectricityConsumptionCalculator {

    private CSVReader csvReader = new CSVReader();
    private ArrayList<ChargingStation> chargingStationList = new ArrayList<>();
    private ArrayList<EntryExitPoint> entryExitPointList = new ArrayList<>();
    private ArrayList<VehicleType> vehicleTypeList = new ArrayList<>();
    private ArrayList<ChargingTime> chargingTimeList = new ArrayList<>();
    private ArrayList<Trip> tripList = new ArrayList<>();

    @Override
    public ConsumptionResult calculateElectricityAndTimeConsumption(ResourceInfo resourceInfo) throws IOException {
        System.out.println("inside");
        this.chargingStationList = csvReader.readChargingStations(resourceInfo.getChargingStationInfoPath());
        this.entryExitPointList = csvReader.readEntryExitPoints(resourceInfo.getEntryExitPointInfoPath());
        this.vehicleTypeList = csvReader.readVehicleTypes(resourceInfo.getVehicleTypeInfoPath());
        this.chargingTimeList = csvReader.readChargingTimes(resourceInfo.getTimeToChargeVehicleInfoPath());
        this.tripList = csvReader.readTrips(resourceInfo.getTripDetailsPath());
        System.out.println("list Created");

        // Maps to keep track of the results
        System.out.println("output init started");
        ArrayList<VehicleData> vehicleDataList = new ArrayList<>();
        for(VehicleType vehicle : vehicleTypeList) {
            vehicleDataList.add(new VehicleData(vehicle.getType()));
        }
        Map<String, ChargingStationData> chargingStationDataMap = new HashMap<>();
        System.out.println("output init completed");

        // Process each trip

        System.out.println("Trip Loop Started");
        for (Trip trip : tripList) {
            System.out.println("Trip = " + trip.getId());
            processTrip(trip, vehicleDataList, chargingStationDataMap);
        }
        System.out.println("Trip Loop Completed");

        // Create ConsumptionResult object
        ConsumptionResult result = new ConsumptionResult();

        for (VehicleData vehicleData : vehicleDataList) {
            ConsumptionDetails consumptionDetails = new ConsumptionDetails(vehicleData.getVehicleType(), vehicleData.getTotalEnergyConsumed(), (long) vehicleData.getTotalChargingTime(), (long) vehicleData.getNumberOfTrips());
            result.getConsumptionDetails().add(consumptionDetails);
        }

        for (Map.Entry<String, ChargingStationData> entry : chargingStationDataMap.entrySet()) {
            result.totalChargingStationTime.put(entry.getKey(), entry.getValue().getTotalChargingTime());
        }

        return result;
    }

    private void processTrip(Trip trip, ArrayList<VehicleData> vehicleDataList, Map<String, ChargingStationData> chargingStationDataMap) {
        // Fetch the required data once for readability and efficiency
        EntryExitPoint entryPoint = findEntryExitPoint(trip.getEntryPoint());
        EntryExitPoint exitPoint = findEntryExitPoint(trip.getExitPoint());
        VehicleType vehicleType = findVehicleType(trip.getVehicleType());

        double remainingBatteryPercentage = trip.getRemainingBatteryPercentage();
        double mileagePerUnit = vehicleType.getMileage() / vehicleType.getNumberOfUnitsForFullyCharge();
        double totalDistance = exitPoint.getDistanceFromStart() - entryPoint.getDistanceFromStart();

        // Calculate the initial battery range
        double initialBatteryRange = remainingBatteryPercentage * mileagePerUnit;
        double currentBatteryRange = initialBatteryRange;

        // Pre-fetch reachable charging stations
        ArrayList<ChargingStation> reachableStations = getReachableChargingStations(entryPoint.getDistanceFromStart(), exitPoint.getDistanceFromStart());

        // Initialize tracking variables
        double totalChargingTime = 0;
        double totalEnergyConsumed = 0;

        // Loop to manage battery and charging
        while (currentBatteryRange < totalDistance) {
            ChargingStation lastReachableStation = getLastReachableStation(reachableStations, currentBatteryRange);
            if (lastReachableStation == null) {
                // If no station is reachable, the trip cannot be completed
                return;
            }

            // Calculate the distance to the last reachable station
            double distanceToLastStation = lastReachableStation.getDistanceFromStart() - entryPoint.getDistanceFromStart();

            // Calculate the energy to be added during charging
            double unitsToAdd = vehicleType.getNumberOfUnitsForFullyCharge() - (remainingBatteryPercentage * vehicleType.getNumberOfUnitsForFullyCharge() / 100);
            totalEnergyConsumed += unitsToAdd;

            // Calculate the time required to charge at the last reachable station
            double chargingTime = unitsToAdd * findChargingTime(vehicleType.getType(), lastReachableStation.getName());
            totalChargingTime += chargingTime;

            // Update vehicle and station data
            updateVehicleData(vehicleType.getType(), totalEnergyConsumed, totalChargingTime, vehicleDataList);
            updateChargingStationData(lastReachableStation.getName(), chargingTime, chargingStationDataMap);

            // Update current battery range and remaining distance
            currentBatteryRange = vehicleType.getMileage();
            totalDistance -= distanceToLastStation;

            // Update the entry point to the last reachable station
            entryPoint = new EntryExitPoint(entryPoint.getName(), lastReachableStation.getDistanceFromStart());
        }

        // Final update for the trip data
        updateVehicleData(vehicleType.getType(), totalEnergyConsumed, totalChargingTime, vehicleDataList);
    }


    private EntryExitPoint findEntryExitPoint(String name) {
        return entryExitPointList.stream().filter(point -> point.getName().equals(name)).findFirst().get();
    }

    private VehicleType findVehicleType(String type) {
        return vehicleTypeList.stream().filter(vehicle -> vehicle.getType().equals(type)).findFirst().get();
    }

    private double findChargingTime(String vehicleType, String stationName) {
        return chargingTimeList.stream()
                .filter(chargingTime -> chargingTime.getVehicleType().equals(vehicleType) && chargingTime.getChargingStation().equals(stationName))
                .findFirst().get()
                .getTimeToChargePerUnit();
    }

    private ArrayList<ChargingStation> getReachableChargingStations(double start, double end) {
        ArrayList<com.nice.coday.models.ChargingStation> stations = new ArrayList<>();
        for (com.nice.coday.models.ChargingStation station : chargingStationList) {
            if (station.getDistanceFromStart() > start && station.getDistanceFromStart() < end) {
                stations.add(station);
            }
        }
        return stations;
    }

    private ChargingStation getLastReachableStation(ArrayList<com.nice.coday.models.ChargingStation> stations, double batteryRange) {
        for (int i = stations.size() - 1; i >= 0; i--) {
            if (stations.get(i).getDistanceFromStart() <= batteryRange) {
                return stations.get(i);
            }
        }
        return null;
    }

    private void updateVehicleData(String vehicleType, double energyConsumed, double chargingTime, ArrayList<VehicleData> vehicleDataList) {
        VehicleData data = vehicleDataList.stream().filter(v -> v.getVehicleType().equals(vehicleType)).findFirst().orElse(null);
        if (data == null) {
            data = new VehicleData(vehicleType);
            vehicleDataList.add(data);
        }
        data.addEnergyConsumed(energyConsumed);
        data.addChargingTime(chargingTime);
        data.incrementTrips();
    }

    private void updateChargingStationData(String stationName, double chargingTime, Map<String, ChargingStationData> chargingStationDataMap) {
        ChargingStationData data = chargingStationDataMap.getOrDefault(stationName, new ChargingStationData(stationName));
        data.addChargingTime(chargingTime);
        chargingStationDataMap.put(stationName, data);
    }

    public void showData() {
        System.out.println("Charging Stations:");
        for (ChargingStation station : chargingStationList) {
            System.out.println(station.getName() + " - " + station.getDistanceFromStart() + " km");
        }

        System.out.println("\nEntry-Exit Points:");
        for (EntryExitPoint point : entryExitPointList) {
            System.out.println(point.getName() + " - " + point.getDistanceFromStart() + " km");
        }

        System.out.println("\nVehicle Types:");
        for (VehicleType vehicle : vehicleTypeList) {
            System.out.println(vehicle.getType() + " - " + vehicle.getMileage() + " km/unit, " +
                    vehicle.getNumberOfUnitsForFullyCharge() + " units to fully charge");
        }

        System.out.println("\nCharging Times:");
        for (ChargingTime chargingTime : chargingTimeList) {
            System.out.println("Vehicle Type: " + chargingTime.getVehicleType() + ", Charging Station: " + chargingTime.getChargingStation() + ", Time to Charge/Unit: " + chargingTime.getTimeToChargePerUnit());
        }

        System.out.println("\nTrips:");
        for (Trip trip : tripList) {
            System.out.println("Trip ID: " + trip.getId() + ", Vehicle Type: " + trip.getVehicleType() + ", Start: " + trip.getEntryPoint() + ", End: " + trip.getExitPoint() +
                    ", Battery Percentage: " + trip.getRemainingBatteryPercentage());
        }
    }
}