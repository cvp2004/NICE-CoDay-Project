package com.nice.coday;

import org.apache.poi.ss.formula.eval.NotImplementedException;

import java.io.IOException;
import java.util.ArrayList;

public class ElectricityConsumptionCalculatorImpl implements ElectricityConsumptionCalculator {

    public CSVReader csvReader = new CSVReader();
    public ArrayList<ChargingStation> chargingStationList = new ArrayList<ChargingStation>();
    public ArrayList<EntryExitPoint> entryExitPointList = new ArrayList<EntryExitPoint>();
    public ArrayList<VehicleType> vehicleTypeList = new ArrayList<VehicleType>();
    public ArrayList<ChargingTime> chargingTimeList = new ArrayList<ChargingTime>();
    public ArrayList<Trip> tripList = new ArrayList<Trip>();

    @Override
    public ConsumptionResult calculateElectricityAndTimeConsumption(ResourceInfo resourceInfo) throws IOException {

        this.chargingStationList = csvReader.readChargingStations(resourceInfo.getChargingStationInfoPath());
        this.entryExitPointList = csvReader.readEntryExitPoints(resourceInfo.getEntryExitPointInfoPath());
        this.vehicleTypeList = csvReader.readVehicleTypes(resourceInfo.getVehicleTypeInfoPath());
        this.chargingTimeList = csvReader.readChargingTimes(resourceInfo.getTimeToChargeVehicleInfoPath());
        this.tripList = csvReader.readTrips(resourceInfo.getTripDetailsPath());

        // Display the contents of each ArrayList
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
            System.out.println("Vehicle Type: " + chargingTime.getVehicleType() + "Charging Station: " + chargingTime.getChargingStation() + " Time to Charge/Unit : " +  chargingTime.getTimeToChargePerUnit());
        }

        System.out.println("\nTrips:");
        for (Trip trip : tripList) {
            System.out.println("Trip ID: " + trip.getId() + ", Start: " + trip.getEntryPoint() + ", End: " + trip.getExitPoint() +
                    ", Battery Percentage: " + trip.getRemainingBatteryPercentage() + " km, Vehicle Type: " + trip.getVehicleType());
        }

        throw new NotImplementedException("Not implemented yet.");
    }
}
