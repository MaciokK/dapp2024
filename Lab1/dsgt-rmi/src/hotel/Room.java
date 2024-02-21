package hotel;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Room {

	private Integer roomNumber;
	private List<BookingDetail> bookings;

	public Room(Integer roomNumber) {
		this.roomNumber = roomNumber;
		bookings = new ArrayList<BookingDetail>();
	}

	public Integer getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	public List<BookingDetail> getBookings() {
		return bookings;
	}

	public void setBookings(List<BookingDetail> bookings) {
		this.bookings = bookings;
	}

	/**
	 * Check if the room is available on the given date.
	 */
	public boolean isAvailable(LocalDate date) {
		for (BookingDetail booking : bookings) {
			if (booking.getDate().isEqual(date)) {
				return false; // The room is already booked on this date
			}
		}
		return true; // No bookings on this date, room is available
	}

	/**
	 * Add a new booking if the room is available.
	 */
	public void addBooking(BookingDetail newBooking) throws Exception {
		if (isAvailable(newBooking.getDate())) {
			bookings.add(newBooking);
		} else {
			throw new Exception("Room " + roomNumber + " is not available on " + newBooking.getDate());
		}
	}
}