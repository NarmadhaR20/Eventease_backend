package com.eventease.model;

public class Seat {
    private int seatId;
    private String seatNumber;
    private boolean available;

    public Seat() {}

    public Seat(int seatId, String seatNumber, boolean available) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.available = available;
    }

    public int getSeatId() { return seatId; }
    public void setSeatId(int seatId) { this.seatId = seatId; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getStatus() {
        return available ? "Available" : "Booked";
    }

    @Override
    public String toString() {
        return "Seat{" + "seatId=" + seatId + ", seatNumber='" + seatNumber + '\'' + ", available=" + available + '}';
    }
}
