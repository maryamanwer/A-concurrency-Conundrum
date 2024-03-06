package Main;

import Concurrency.*;

import java.util.HashMap;
import java.util.Map;

public class ConcurrencyAssignment {

    public static void main(String[] args) {
        // Scenario1
        int[] entryPointsConfig1 = {550, 300, 550};
        int[] junctionsConfig1 = {60, 60, 30, 30};
        runSimulation(1, entryPointsConfig1, junctionsConfig1);

        // Scenario 2
        int[] entryPointsConfig2 = {550, 300, 550};
        int[] junctionsConfig2 = {30, 30, 20, 30};
        runSimulation(2, entryPointsConfig2, junctionsConfig2);

        // Scenario 3
        int[] entryPointsConfig3 = {250, 300, 200};
        int[] junctionsConfig3 = {60, 60, 30, 30};
        runSimulation(3, entryPointsConfig3, junctionsConfig3);

        // Scenario 4
        int[] entryPointsConfig4 = {450, 100, 450};
        int[] junctionsConfig4 = {25, 25, 25, 30};
        runSimulation(4, entryPointsConfig4, junctionsConfig4);

        // Scenario 5
        int[] entryPointsConfig5 = {450, 200, 450};
        int[] junctionsConfig5 = {90, 90, 90, 30};
        runSimulation(5, entryPointsConfig5, junctionsConfig5);
    }

    private static void runSimulation(int scenarioNumber, int[] entryPointsConfig, int[] junctionsConfig) {
        System.out.println("Scenario " + scenarioNumber + ": Running...");

        Clock myClock = new Clock();

        try {
            CarPark[] carParks = new CarPark[4];

            // Entry point roads:
            Road entry_a1 = new Road(entryPointsConfig[0]);
            Road entry_a2 = new Road(entryPointsConfig[1]);
            Road entry_a3 = new Road(entryPointsConfig[2]);

            // Junction 1 out Roads:
            Road road1A_North = new Road(junctionsConfig[0]);
            Road road1B_West = new Road(junctionsConfig[1]);

            // Junction 2 out Roads:
            Road road2A_North = new Road(junctionsConfig[2]);
            Road road2B_South = new Road(junctionsConfig[3]);

            // Junction 3 out Roads:
            Road road3A_West = new Road(junctionsConfig[0]);
            Road road3B_East = new Road(junctionsConfig[1]);
            Road road3C_South = new Road(junctionsConfig[2]);

            // Junction 4 out Roads:
            Road road4A_North = new Road(junctionsConfig[3]);
            Road road4B_South = new Road(junctionsConfig[2]);

            EntryPoint northEntry = new EntryPoint(entry_a1, "north", entryPointsConfig[0], myClock);
            EntryPoint eastEntry = new EntryPoint(entry_a2, "east", entryPointsConfig[1], myClock);
            EntryPoint southEntry = new EntryPoint(entry_a3, "south", entryPointsConfig[2], myClock);

            // Junction 1 destination list
            Map<Integer, String> destinationList1 = new HashMap<>();
            destinationList1.put(1, "NORTH");
            destinationList1.put(2, "NORTH");
            destinationList1.put(3, "NORTH");
            destinationList1.put(4, "WEST");

            // Junction 1
            Junction junction1 = new Junction(myClock, 1, destinationList1);
            junction1.setLightsSequence("NORTH", "SOUTH", null, null);
            junction1.setInRoads(road2B_South, null, entry_a3, null);
            junction1.setOutRoads(road1A_North, null, null, road1B_West);
            junction1.setSouthDuration(340);
            junction1.setNorthDuration(380);

            // Junction 2 destination list
            Map<Integer, String> destinationList2 = new HashMap<>();
            destinationList2.put(1, "NORTH");
            destinationList2.put(2, "NORTH");
            destinationList2.put(3, "NORTH");
            destinationList2.put(4, "SOUTH");

            // Junction 2
            Junction junction2 = new Junction(myClock, 2, destinationList2);
            junction2.setLightsSequence("NORTH", "SOUTH", "EAST", null);
            junction2.setInRoads(road3B_East, entry_a2, road1A_North, null);
            junction2.setOutRoads(road2A_North, null, road2B_South, null);
            junction2.setNorthDuration(360);
            junction2.setEastDuration(100);
            junction2.setSouthDuration(340);

            // Initialize car parks
            carParks[0] = new CarPark(1, "University", road4A_North, 100, myClock);
            carParks[1] = new CarPark(2, "Station", road4B_South, 150, myClock);
            carParks[2] = new CarPark(3, "Shopping Centre", road3C_South, 400, myClock);
            carParks[3] = new CarPark(4, "Industrial Park", road1B_West, 1000, myClock);

            // Set car parks for reporting
            myClock.setCarParks(carParks);

            // Start simulation threads
            startSimulation(northEntry, eastEntry, southEntry, junction1, junction2, myClock, carParks);

            // Wait for the simulation to finish
            myClock.join();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Scenario " + scenarioNumber + ": Finished.");
    }

    private static void startSimulation(EntryPoint northEntry, EntryPoint eastEntry, EntryPoint southEntry,
                                        Junction junction1, Junction junction2, Clock myClock, CarPark[] carParks) {
        northEntry.start();
        eastEntry.start();
        southEntry.start();
        junction1.start();
        junction2.start();
        for (CarPark carPark : carParks) {
            carPark.start();
        }
        myClock.start();
    }
}
