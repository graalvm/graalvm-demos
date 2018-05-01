package com.oracle.graalvm.demo.weather;

public class City {
    private int id;
    private String name;
    private String country;
    private int population;
    private double longitude;
    private double lat;
    private double temperature;

    public City(int id, String name, String country, int population, double lat, double longitude, double temperature) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.longitude = longitude;
        this.lat = lat;
        this.temperature = temperature;
        this.population = population;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCountry() { return country; }
    public int getPopulation() { return population; }
    public double getLatitude() { return lat; }
    public double getLongitude() { return longitude; }
    public double getTemperature() { return temperature; }

    public void updateTemperature(double newValue) {
        temperature = newValue;
    }
}
