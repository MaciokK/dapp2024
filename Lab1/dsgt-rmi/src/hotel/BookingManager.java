package hotel;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BookingManager extends UnicastRemoteObject implements BookingManagerInterface {

	private Room[] rooms;

	// Constructor
	public BookingManager() throws RemoteException {
		super(); // Call to UnicastRemoteObject constructor
		this.rooms = initializeRooms();
	}

	@Override
	public Set<Integer> getAllRooms() throws RemoteException {
		Set<Integer> allRooms = new HashSet<Integer>();
		Iterable<Room> roomIterator = Arrays.asList(rooms);
		for (Room room : roomIterator) {
			allRooms.add(room.getRoomNumber());
		}
		return allRooms;
	}

	@Override
	public boolean isRoomAvailable(Integer roomNumber, LocalDate date) throws RemoteException {
		for (Room room : rooms) {
			if (room.getRoomNumber().equals(roomNumber)) {
				return room.isAvailable(date);
			}
		}
		return false;
	}

	@Override
	public void addBooking(BookingDetail bookingDetail) throws RemoteException {
		try {
			for (Room room : rooms) {
				if (room.getRoomNumber().equals(bookingDetail.getRoomNumber())) {
					room.addBooking(bookingDetail);
					return;
				}
			}
			throw new Exception("Room number " + bookingDetail.getRoomNumber() + " does not exist.");
		}
		catch (Exception e) {
			// Wrap and rethrow the exception as RemoteException
			throw new RemoteException(e.getMessage(), e);
		}
	}


	@Override
	public Set<Integer> getAvailableRooms(LocalDate date) throws RemoteException {
		Set<Integer> availableRooms = new HashSet<Integer>();
		for (Room room : rooms){
			if (room.isAvailable(date)){
				availableRooms.add(room.getRoomNumber());
			}
		}
		return availableRooms;
	}

	private static Room[] initializeRooms() {
		Room[] rooms = new Room[4];
		rooms[0] = new Room(101);
		rooms[1] = new Room(102);
		rooms[2] = new Room(201);
		rooms[3] = new Room(203);
		return rooms;
	}
}
