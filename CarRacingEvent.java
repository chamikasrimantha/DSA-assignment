import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

public class CarRacingEvent {
    private LinkedList<Car> cars;

    public CarRacingEvent() {
        cars = new LinkedList<>();
    }

    private class Car {
        private int carNumber;
        private String carBrand;
        private String sponsor;
        private String driver;
        private int[] roundRanks;
        private boolean eliminated;

        public Car(int carNumber, String carBrand, String sponsor, String driver) {
            this.carNumber = carNumber;
            this.carBrand = carBrand;
            this.sponsor = sponsor;
            this.driver = driver;
            this.roundRanks = new int[3]; // Assuming 3 rounds in the racing event
            this.eliminated = false;
        }

        public int getCarNumber() {
            return carNumber;
        }

        public String getCarBrand() {
            return carBrand;
        }

        public String getSponsor() {
            return sponsor;
        }

        public String getDriver() {
            return driver;
        }
        
        public void setCarBrand(String carBrand){
            this.carBrand = carBrand;
        }
        
        public void setSponsor(String sponsor){
            this.sponsor = sponsor;
        }

        public void setDriver(String driver){
            this.driver = driver;
        }
        
        public int getRoundRank(int roundNumber) {
            if (roundNumber >= 1 && roundNumber <= 3) {
                return roundRanks[roundNumber - 1];
            } else {
                System.out.println("Invalid round number: " + roundNumber);
                return -1; // or throw an exception, depending on your requirements
            }
        }

        public void setRoundRank(int roundNumber, int rank) {
            if (roundNumber >= 1 && roundNumber <= 3) {
                roundRanks[roundNumber - 1] = rank;
            } else {
                System.out.println("Invalid round number: " + roundNumber);
            }
        }

        public int calculateOverallRank() {
            int totalRank = 0;
            for (int rank : roundRanks) {
                totalRank += rank;
            }
            return totalRank;
        }

        public void eliminate() {
            eliminated = true;
        }

        public boolean isEliminated() {
            return eliminated;
        }
    }

    public void registerCar(int carNumber, String carBrand, String sponsor, String driver) {
        if (cars.size() >= 6) {
            System.out.println("Maximum number of cars already registered.");
            return;
        }

        for (Car car : cars) {
            if (car.getCarNumber() == carNumber) {
                System.out.println("Car with number " + carNumber + " is already registered.");
                return;
            }
        }

        Car car = new Car(carNumber, carBrand, sponsor, driver);
        cars.add(car);
        System.out.println("Car with number " + carNumber + " has been registered.");
    }

    public void deleteCar(int carNumber) {
        boolean foundCar = false;
        LinkedList<Car> tempLinkedList = new LinkedList<>();

        for (Car car : cars) {
            if (car.getCarNumber() == carNumber) {
                foundCar = true;
            } else {
                tempLinkedList.add(car);
            }
        }

        cars = tempLinkedList;

        if (foundCar) {
            System.out.println("Car with number " + carNumber + " has been removed.");
        } else {
            System.out.println("Car with number " + carNumber + " not found.");
        }
    }

    public void insertRoundResults() {
        if (cars.isEmpty()) {
            System.out.println("No cars registered for the event.");
            return;
        }

        for (int roundNumber = 1; roundNumber <= 3; roundNumber++) {
            System.out.println("Enter Round " + roundNumber + " Results:");
            for (Car car : cars) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter rank for Car " + car.getCarNumber() + ": ");
                try {
                    int rank = scanner.nextInt();
                    if (rank < 1 || rank > cars.size()) {
                        throw new IllegalArgumentException("Invalid rank. Please enter a rank between 1 and " + cars.size());
                    }
                    car.setRoundRank(roundNumber, rank);
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer rank.");
                    scanner.next(); // Consume invalid input
                    car.setRoundRank(roundNumber, -1); // Set an invalid rank
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    return; // Exit the method
                }
            }
            System.out.println("Round " + roundNumber + " results have been inserted.");

            eliminateLowestRankedCar(roundNumber);
        }
    }

    private void eliminateLowestRankedCar(int roundNumber) {
        LinkedList<Car> remainingCars = new LinkedList<>(cars);
        Car lowestRankedCar = remainingCars.get(0);

        for (int i = 1; i < remainingCars.size(); i++) {
            int currentRoundRank = remainingCars.get(i).getRoundRank(roundNumber);
            int lowestRoundRank = lowestRankedCar.getRoundRank(roundNumber);

            if (currentRoundRank > lowestRoundRank) {
                lowestRankedCar = remainingCars.get(i);
            }
        }

        lowestRankedCar.eliminate();
        cars.remove(lowestRankedCar);
        System.out.println("Car " + lowestRankedCar.getCarNumber() + " has been eliminated from the race in Round " + roundNumber + ".");
    }

    public void calculateWinners() {
        if (cars.isEmpty()) {
            System.out.println("No cars registered for the event.");
            return;
        }

        LinkedList<Car> remainingCars = new LinkedList<>(cars);

        Car firstPlace = null;
        Car secondPlace = null;
        Car thirdPlace = null;

        while (!remainingCars.isEmpty()) {
            Car car = remainingCars.poll();
            if (!car.isEliminated() && car.getRoundRank(3) == 1) {
                firstPlace = car;
            }
            if (!car.isEliminated() && car.getRoundRank(3) == 2) {
                secondPlace = car;
            }
            if (!car.isEliminated() && car.getRoundRank(3) == 3) {
                thirdPlace = car;
            }
        }

        if (firstPlace != null) {
            System.out.println("1st Place: Car " + firstPlace.getCarNumber());
        }
        if (secondPlace != null) {
            System.out.println("2nd Place: Car " + secondPlace.getCarNumber());
        }
        if (thirdPlace != null) {
            System.out.println("3rd Place: Car " + thirdPlace.getCarNumber());
        }
    }

    public Car searchCarByNumber(int carNumber) {
        for (Car car : cars) {
            if (car.getCarNumber() == carNumber) {
                return car;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CarRacingEvent racingEvent = new CarRacingEvent();
        int carNumber = 0;

        while (true) {
            System.out.println("===== Car Racing Event =====");
            System.out.println("1. Register Car");
            System.out.println("2. Delete Car");
            System.out.println("3. Insert Round Results");
            System.out.println("4. Find Winners");
            System.out.println("5. Search for a Car");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    for (int i = 1; i <= 6; i++) {
                        System.out.println("===== Register Car " + i + " =====");
                        System.out.print("Enter Car Number: ");
                        carNumber = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter Car Brand: ");
                        String carBrand = scanner.nextLine();
                        System.out.print("Enter Sponsor Details: ");
                        String sponsor = scanner.nextLine();
                        System.out.print("Enter Driver Details: ");
                        String driver = scanner.nextLine();
                        racingEvent.registerCar(carNumber, carBrand, sponsor, driver);
                        System.out.println();
                    }
                    break;
                case 2:
                    System.out.print("Enter Car Number to delete: ");
                    carNumber = scanner.nextInt();
                    racingEvent.deleteCar(carNumber);
                    break;
                case 3:
                    racingEvent.insertRoundResults();
                    break;
                case 4:
                    racingEvent.calculateWinners();
                    break;
                case 5:
                    System.out.print("Enter Car Number to search: ");
                    carNumber = scanner.nextInt();
                    Car car = racingEvent.searchCarByNumber(carNumber);
                    if (car != null) {
                        System.out.println("Car Details - Car Number: " + car.getCarNumber()
                                + ", Brand: " + car.getCarBrand()
                                + ", Sponsor: " + car.getSponsor()
                                + ", Driver: " + car.getDriver());
                    } else {
                        System.out.println("Car with number " + carNumber + " not found.");
                    }
                    break;
                case 6:
                    System.out.println("Exiting Car Racing Event Application.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        }
    }
}