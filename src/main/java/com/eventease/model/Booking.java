package com.eventease.model;

public class Booking {
    private int bookingId;
    private int userId;
    private String eventName;
    private String bookingDate;
    private double totalAmount;
    private int numberOfSeats;
    private String status;
    private int ticketDownloaded;

    // Constructors
    public Booking() {}

    public Booking(int userId, String eventName, double totalAmount, int numberOfSeats) {
        this.userId = userId;
        this.eventName = eventName;
        this.totalAmount = totalAmount;
        this.numberOfSeats = numberOfSeats;
        this.status = "Pending";
        this.ticketDownloaded = 0;
    }

    // Getters & Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public int getNumberOfSeats() { return numberOfSeats; }
    public void setNumberOfSeats(int numberOfSeats) { this.numberOfSeats = numberOfSeats; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getTicketDownloaded() { return ticketDownloaded; }
    public void setTicketDownloaded(int ticketDownloaded) { this.ticketDownloaded = ticketDownloaded; }
}
