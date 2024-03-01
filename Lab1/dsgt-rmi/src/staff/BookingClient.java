package staff;

import hotel.BookingDetail;
import hotel.BookingManagerInterface;
import java.rmi.Naming;
import java.time.LocalDate;
import java.util.Set;
import java.rmi.RemoteException;

public class BookingClient extends AbstractScriptedSimpleTest {

	private BookingManagerInterface bookingManager; // Use the interface type

	public BookingClient() {
		try {
			// Lookup the remote BookingManager object in the RMI registry
			bookingManager = (BookingManagerInterface) Naming.lookup("rmi://localhost/BookingManager");
		} catch (Exception exp) {
			exp.printStackTrace();
			System.exit(1); // Exit if there's an issue finding the remote object
		}
	}

	// Method to print all available rooms for a given date
	public void printAvailableRooms(LocalDate date) {
		try {
			Set<Integer> availableRooms = bookingManager.getAvailableRooms(date);
			System.out.println("Available rooms on " + date + ": " + availableRooms);
		} catch (Exception e) {
			System.err.println("Error fetching available rooms: " + e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public boolean isRoomAvailable(Integer roomNumber, LocalDate date) throws RemoteException {
		return bookingManager.isRoomAvailable(roomNumber, date);
	}

	@Override
	public void addBooking(BookingDetail bookingDetail) throws RemoteException {
		bookingManager.addBooking(bookingDetail);
	}

	@Override
	public Set<Integer> getAvailableRooms(LocalDate date) throws RemoteException {
		return bookingManager.getAvailableRooms(date);
	}

	@Override
	public Set<Integer> getAllRooms() throws RemoteException {
		return bookingManager.getAllRooms();
	}

	public static void main(String[] args) {
		try {
			// Look up the remote BookingManager object in the RMI registry
			BookingManagerInterface bookingManager = (BookingManagerInterface) Naming.lookup("rmi://localhost/BookingManager");

			// Check the availability of all rooms for today
			LocalDate today = LocalDate.now();
			Set<Integer> availableRooms = bookingManager.getAvailableRooms(today);

			// Print the result
			System.out.println("Available rooms on " + today + ": " + availableRooms);

			// Example: Attempt to book a room to see if the system is responsive
			if (!availableRooms.isEmpty()) {
				Integer roomNumber = availableRooms.iterator().next(); // Just take the first available room for demonstration
				BookingDetail bookingDetail = new BookingDetail("John Doe", roomNumber, today);
				bookingManager.addBooking(bookingDetail);
				System.out.println("Successfully booked room " + roomNumber + " for John Doe on " + today);

				// Check availability again to see the change
				Set<Integer> availableRoomsAfterBooking = bookingManager.getAvailableRooms(today);
				System.out.println("Available rooms after booking on " + today + ": " + availableRoomsAfterBooking);
			}
			// Check availability of a room 101, and 102 to see usage of isRoomAvailable method
			// First room 101
			Integer roomNumber = 201;
			Boolean isRoom101AvailableToday = bookingManager.isRoomAvailable(roomNumber, today);
			if (isRoom101AvailableToday) {
				System.out.println(today + " the room " + roomNumber + " is available");
			} else {
				System.out.println(today + " the room " + roomNumber + " is not available");
			}
			// Next room 102
			Integer roomNumber2 = 102;
			Boolean isRoom101AvailableToday2 = bookingManager.isRoomAvailable(roomNumber2, today);
			if (isRoom101AvailableToday2) {
				System.out.println(today + " the room " + roomNumber2 + " is available");
			} else {
				System.out.println(today + " the room " + roomNumber2 + " is not available");
			}

		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
