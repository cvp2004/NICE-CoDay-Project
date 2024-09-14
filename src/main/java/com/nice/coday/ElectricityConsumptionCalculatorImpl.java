package com.nice.coday;

import com.nice.coday.models.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

        this.chargingStationList = csvReader.readChargingStations(resourceInfo.getChargingStationInfoPath());
        this.entryExitPointList = csvReader.readEntryExitPoints(resourceInfo.getEntryExitPointInfoPath());
        this.vehicleTypeList = csvReader.readVehicleTypes(resourceInfo.getVehicleTypeInfoPath());
        this.chargingTimeList = csvReader.readChargingTimes(resourceInfo.getTimeToChargeVehicleInfoPath());
        this.tripList = csvReader.readTrips(resourceInfo.getTripDetailsPath());

        for(VehicleType v : vehicleTypeList) {
            unitsConsumed.put(v.getType(), 0.0);
            timeRequired.put(v.getType(), 0.0);
            noOfTripsFinished.put(v.getType(), 0.0);
        }

        for (ChargingStation c : chargingStationList) chargeStationTotalTime.put(c.getName(), 0L);

        for (Trip trip : tripList) processTrip(trip);

        ConsumptionResult result = new ConsumptionResult();


        for (VehicleType v : vehicleTypeList) {
            System.out.println(v.getType() + " " + unitsConsumed.get(v.getType()) + " " + timeRequired.get(v.getType()) + " " + noOfTripsFinished.get(v.getType()));
            ConsumptionDetails details = new ConsumptionDetails(
                    v.getType(),
                    roundToTwoDecimalPlaces(unitsConsumed.get(v.getType()) * 0.52775),
                    (long) roundToTwoDecimalPlaces(timeRequired.get(v.getType())),
                    (long) roundToTwoDecimalPlaces(noOfTripsFinished.get(v.getType()))
            );
            result.getConsumptionDetails().add(details);
        }

        for (ChargingStation c : chargingStationList) {
            System.out.println(c.getName() + " " + chargeStationTotalTime.get(c.getName()));
            result.getTotalChargingStationTime().put(c.getName(), chargeStationTotalTime.get(c.getName()));
        }

        return result;
    }

//    private void processTrip(Trip trip, ArrayList<VehicleData> vehicleDataList, Map<String, ChargingStationData> chargingStationDataMap) {
//        // Fetch the required data once for readability and efficiency
//        EntryExitPoint entryPoint = findEntryExitPoint(trip.getEntryPoint());
//        EntryExitPoint exitPoint = findEntryExitPoint(trip.getExitPoint());
//        VehicleType vehicleType = findVehicleType(trip.getVehicleType());
//
//        double remainingBatteryPercentage = trip.getRemainingBatteryPercentage();
//        double mileagePerUnit = vehicleType.getMileage() / vehicleType.getNumberOfUnitsForFullyCharge();
//        double totalDistance = exitPoint.getDistanceFromStart() - entryPoint.getDistanceFromStart();
//
//        // Calculate the initial battery range
//        double initialBatteryRange = ((remainingBatteryPercentage * vehicleType.getNumberOfUnitsForFullyCharge())/100) * mileagePerUnit;
//        double currentBatteryRange = initialBatteryRange;
//
//        // Pre-fetch reachable charging stations
//        ArrayList<ChargingStation> reachableStations = getReachableChargingStations(entryPoint.getDistanceFromStart(), exitPoint.getDistanceFromStart());
//
//        // Initialize tracking variables
//        double totalChargingTime = 0;
//        double totalEnergyConsumed = 0;
//
//        // Loop to manage battery and charging
//        while (currentBatteryRange < totalDistance) {
//            ChargingStation lastReachableStation = getLastReachableStation(reachableStations, currentBatteryRange);
//            if (lastReachableStation == null) {
//                // If no station is reachable, the trip cannot be completed
//                return;
//            }
//
//            // Calculate the distance to the last reachable station
//            double distanceToLastStation = lastReachableStation.getDistanceFromStart() - entryPoint.getDistanceFromStart();
//
//            // Calculate the energy to be added during charging
//            double unitsToAdd = vehicleType.getNumberOfUnitsForFullyCharge() - (remainingBatteryPercentage * vehicleType.getNumberOfUnitsForFullyCharge() / 100);
//            totalEnergyConsumed += unitsToAdd;
//
//            // Calculate the time required to charge at the last reachable station
//            double chargingTime = unitsToAdd * findChargingTime(vehicleType.getType(), lastReachableStation.getName());
//            totalChargingTime += chargingTime;
//
//            // Update vehicle and station data
//            updateVehicleData(vehicleType.getType(), totalEnergyConsumed, totalChargingTime, vehicleDataList);
//            updateChargingStationData(lastReachableStation.getName(), chargingTime, chargingStationDataMap);
//
//            // Update current battery range and remaining distance
//            currentBatteryRange = vehicleType.getMileage();
//            totalDistance -= distanceToLastStation;
//
//            // Update the entry point to the last reachable station
//            entryPoint = new EntryExitPoint(entryPoint.getName(), lastReachableStation.getDistanceFromStart());
//        }
//
//        // Final update for the trip data
//        updateVehicleData(vehicleType.getType(), totalEnergyConsumed, totalChargingTime, vehicleDataList);
//    }

    Map<String, Double> unitsConsumed = new HashMap<>();
    Map<String, Double> timeRequired = new HashMap<>();
    Map<String, Double> noOfTripsFinished = new HashMap<>();
    Map<String, Long> chargeStationTotalTime = new HashMap<>();

    int totalChargesV1 = 0;
    private void processTrip(Trip trip) {
        EntryExitPoint entryPoint = findEntryExitPoint(trip.getEntryPoint());
        EntryExitPoint exitPoint = findEntryExitPoint(trip.getExitPoint());
        VehicleType vehicleType = findVehicleType(trip.getVehicleType());

        double mileagePerUnit = vehicleType.getMileage() / vehicleType.getNumberOfUnitsForFullyCharge();
        double distanceToTravel = exitPoint.getDistanceFromStart() - entryPoint.getDistanceFromStart();
        double remainingUnits = (vehicleType.getNumberOfUnitsForFullyCharge() * trip.getRemainingBatteryPercentage()) / 100;
        double currentPosition = entryPoint.getDistanceFromStart();

        // Track energy consumed and charging time
        double totalEnergyConsumed = 0;
        double totalChargingTime = 0;

        while (currentPosition < exitPoint.getDistanceFromStart()) {
            double distanceCanTravel = remainingUnits * mileagePerUnit;

            if (distanceCanTravel + currentPosition >= exitPoint.getDistanceFromStart()) {
                double remainingDistance = exitPoint.getDistanceFromStart() - currentPosition;
                double unitsUsed = roundToTwoDecimalPlaces(remainingDistance / mileagePerUnit); // Round to 2 decimal places
                totalEnergyConsumed += unitsUsed;
                totalEnergyConsumed = roundToTwoDecimalPlaces(totalEnergyConsumed); // Ensure total is rounded
                noOfTripsFinished.merge(vehicleType.getType(), 1.0, Double::sum);
                break;
            }

            ArrayList<ChargingStation> stations = getReachableChargingStations(currentPosition, exitPoint.getDistanceFromStart());
            ChargingStation lastReachableStation = getLastReachableStation(stations, distanceCanTravel + currentPosition);

            if (lastReachableStation == null) {
                return;
            }

            double distanceToStation = lastReachableStation.getDistanceFromStart() - currentPosition;
            double unitsUsedForLeg = roundToTwoDecimalPlaces(distanceToStation / mileagePerUnit); // Round to 2 decimal places
            totalEnergyConsumed += unitsUsedForLeg;
            totalEnergyConsumed = roundToTwoDecimalPlaces(totalEnergyConsumed); // Ensure total is rounded

            currentPosition = lastReachableStation.getDistanceFromStart();
            distanceToTravel -= distanceToStation;
            remainingUnits -= unitsUsedForLeg;

            double nextDistanceToTravel = exitPoint.getDistanceFromStart() - currentPosition;
            if (remainingUnits < (nextDistanceToTravel / mileagePerUnit)) {
                double requiredUnits = (nextDistanceToTravel / mileagePerUnit) - remainingUnits;
                requiredUnits = roundToTwoDecimalPlaces(requiredUnits); // Round to 2 decimal places
                totalEnergyConsumed += requiredUnits;
                totalEnergyConsumed = roundToTwoDecimalPlaces(totalEnergyConsumed); // Ensure total is rounded

                double chargingTime = requiredUnits * findChargingTime(vehicleType.getType(), lastReachableStation.getName());
                chargingTime = roundToTwoDecimalPlaces(chargingTime); // Round charging time

                totalChargingTime += chargingTime;
                totalChargingTime = roundToTwoDecimalPlaces(totalChargingTime); // Ensure total is rounded

                chargeStationTotalTime.merge(lastReachableStation.getName(), (long) chargingTime, Long::sum);
                timeRequired.merge(vehicleType.getType(), chargingTime, Double::sum);

                remainingUnits += requiredUnits;
                remainingUnits = roundToTwoDecimalPlaces(remainingUnits); // Ensure remaining units are rounded
            }
        }

        unitsConsumed.merge(vehicleType.getType(), totalEnergyConsumed, Double::sum);
        unitsConsumed.put(vehicleType.getType(), roundToTwoDecimalPlaces(unitsConsumed.get(vehicleType.getType()))); // Ensure final total is rounded
        timeRequired.merge(vehicleType.getType(), totalChargingTime, Double::sum);
    }

    private double roundToTwoDecimalPlaces(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP); // Rounding to 2 decimal places
        return bd.doubleValue();
    }

    private double roundToFourDecimalPlaces(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(4, RoundingMode.HALF_UP); // Rounding to 4 decimal places
        return bd.doubleValue();
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