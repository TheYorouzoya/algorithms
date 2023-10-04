import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TSPApprox {
    public record City(double x, double y, int index) {
        public double distance(City city2) {
            return Math.hypot(this.x - city2.x, this.y - city2.y);
        }
    };

    public record CityPair(City city, int index) {};

    public static class CityXComparator implements Comparator<City> {
        @Override
        public int compare(City A, City B) {
            if(A.x > B.x) return +1;
            if(A.x < B.x) return -1;
            return 0;
        }
    }

    public static class CityYComparator implements Comparator<City> {
        @Override
        public int compare(City A, City B) {
            if(A.y > B.y) return +1;
            if(A.y < B.y) return -1;
            return 0;
        }
    }

    private City[] cityList;
    private City startingCity;
    private boolean[] visited;
    private int cities;
    private double maxX, minX, maxY, minY;
    private boolean sortFlag;

    int test = 0;

    public TSPApprox(String filename) {
        // File structure is as follows:
        // [number_of_cities]
        // [city_number] [city_x_coordinate] [city_y_coordinate]

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            cities = Integer.parseInt(line);
            cityList = new City[cities];
            visited = new boolean[cities];
            maxX = maxY = Double.NEGATIVE_INFINITY;
            minX = minY = Double.POSITIVE_INFINITY;
            sortFlag = false;

            int index = 0;
            while((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                double x = Double.parseDouble(data[1]);
                double y = Double.parseDouble(data[2]);
                if(maxX < x) maxX = x;
                if(maxY < y) maxY = y;
                if(minX > x) minX = x;
                if(minY > y) minY = y;
                cityList[index] = new City(x, y, index);
                index++;
            }

            startingCity = cityList[0];
            
            // sort the rest of the list excluding the first city
            if (maxY - minY > maxX - minX)
                sortFlag = true;
            if(sortFlag) {
                Arrays.sort(cityList, new CityYComparator());
            } else {
                Arrays.sort(cityList, new CityXComparator());
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int computeTour() {
        int currentCity = findStartingCity();
        visited[startingCity.index] = true;
        int count = cities - 1;
        double distance = 0.0;

        while(count > 0) {
            int closest = closestNeighbour(currentCity);
            visited[cityList[closest].index] = true;
            distance += cityList[currentCity].distance(cityList[closest]);
            currentCity = closest;
            count--;
        }

        distance += startingCity.distance(cityList[currentCity]);
        return (int) Math.floor(distance);

    }

    // Return the distance to the closest neighbour from the given city index
    // Also marks the neighbour as visited
    private int closestNeighbour(int city) {
        City currentCity = cityList[city];
        City nextCity;
        List<CityPair> candidates = new ArrayList<CityPair>();
        int minIndex = city, currIndex = city;
        double limit = Double.POSITIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;

        // Process cities to the left of the current city
        currIndex = city - 1;
        while(currIndex >= 0) {
            nextCity = cityList[currIndex];
            if(indexPastLimit(currentCity, nextCity, limit)) {
                // System.out.println("Broken left loop");
                break;
            }

            if (!visited[nextCity.index] && cityWithinLimit(nextCity, currentCity, limit)) {
                limit = currentCity.distance(nextCity);
                if(limit <= min) {
                    min = limit;
                    minIndex = currIndex;
                    candidates.add(new CityPair(nextCity, currIndex));
                }
                limit = min;
            }

            currIndex--;
            test++;
        }

        // Process cities to the right of the current city
        currIndex = city + 1;
        while(currIndex < cities) {
            nextCity = cityList[currIndex];
            if(indexPastLimit(currentCity, nextCity, limit)) {
                // System.out.println("Broken right loop.");
                break;
            }

            if(!visited[nextCity.index] && cityWithinLimit(nextCity, currentCity, limit)) {
                limit = currentCity.distance(nextCity);
                if (limit <= min) {
                    min = limit;
                    minIndex = currIndex;
                    candidates.add(new CityPair(nextCity, currIndex));
                }
                limit = min;
            }

            currIndex++;
            test++;
        }

        for(CityPair pair : candidates) {
            if(currentCity.distance(pair.city()) == limit && pair.index() != minIndex) {
                if(pair.city.index < cityList[minIndex].index) {
                    minIndex = pair.index;
                }
            }
        }

        return minIndex;
    }

    private boolean indexPastLimit(City currentCity, City nextCity, double limit) {
        double test;
        if(sortFlag) {  // data is sorted along Y-axis
            test = Math.abs(currentCity.y - nextCity.y);
        } else {    // data is sorted along X-axis
            test = Math.abs(currentCity.x - nextCity.x);
        }
        return test > limit;
    }

    private boolean cityWithinLimit(City nextCity, City currentCity, double limit) {
        double X, Y;
        X = Math.abs(nextCity.x - currentCity.x);
        Y = Math.abs(nextCity.y - currentCity.y);
        return (X < limit && Y < limit);
    }

    // Return the index of the starting city after the list has been sorted
    private int findStartingCity() {
        for(int i = 0; i < cities; i++) {
            if(cityList[i].index == startingCity.index)
                return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        String filename = args[0];
        long start, stop;
        start = System.currentTimeMillis();
        TSPApprox obj = new TSPApprox(filename);
        stop = System.currentTimeMillis();
        System.out.println("Loading took: " + (stop - start) + "ms");

        start = System.currentTimeMillis();
        int tour = obj.computeTour();
        stop = System.currentTimeMillis();
        System.out.println("Processing took: " + (stop - start) + "ms");
        System.out.println("Shortest tour is of length: " + tour);
        System.out.println("Processed " + obj.test + " problems");
        System.out.println("Starting city index: " + obj.findStartingCity());
        System.out.println("Sort flag is: " + obj.sortFlag);
        
    }
}
