package staff;

import java.time.LocalDate;
import hotel.BookingManager;
import hotel.BookingDetail;
import hotel.Room;
import java.util.Set;

class Main {
    public static void main(String[] args) {
        try {
            // Create a BookingManager instance
            BookingManager bookingManager = new BookingManager();

            // Set the booking details: guest name, room number, and date
            String guestName = "John Doe";
            Integer roomNumber = 101; // Assuming room 101 is an existing room
            LocalDate bookingDate = LocalDate.now(); // Today's date

            // Create a BookingDetail instance with the provided information
            BookingDetail bookingDetail = new BookingDetail(guestName, roomNumber, bookingDate);

            // Add the booking using the BookingManager
            bookingManager.addBooking(bookingDetail);

            // Check the available rooms after adding the booking
            Set<Integer> availableRooms = bookingManager.getAvailableRooms(bookingDate);
            System.out.println("Available rooms on " + bookingDate + ": " + availableRooms);
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace if an exception occurs
        }
    }
}

