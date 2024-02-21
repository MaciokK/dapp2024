package hotel;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BookingManager {

	private Room[] rooms;

	public BookingManager() {
		this.rooms = initializeRooms();
	}

	public Set<Integer> getAllRooms() {
		Set<Integer> allRooms = new HashSet<Integer>();
		Iterable<Room> roomIterator = Arrays.asList(rooms);
		for (Room room : roomIterator) {
			allRooms.add(room.getRoomNumber());
		}
		return allRooms;
	}

	public boolean isRoomAvailable(Integer roomNumber, LocalDate date) {
		for (Room room : rooms) {
			if (room.getRoomNumber().equals(roomNumber)) {
				return room.isAvailable(date);
			}
		}
		return false;
	}

	public void addBooking(BookingDetail bookingDetail) throws Exception {
		for (Room room : rooms){
			if (room.getRoomNumber().equals(bookingDetail.getRoomNumber())){
				room.addBooking(bookingDetail);
				return;
			}
		}
		throw new Exception("Room number " + bookingDetail.getRoomNumber() + " does not exist.");
	}

	public Set<Integer> getAvailableRooms(LocalDate date) {
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
