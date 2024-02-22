package staff;

import hotel.BookingDetail;
import hotel.BookingManager;
import hotel.BookingManagerInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Set;

public class BookingClientLocal extends AbstractScriptedSimpleTest {

	private BookingManager bm = null;

//	public static void main(String[] args) throws Exception {
//		BookingClient client = new BookingClient();
//		client.run();
//	}
public static void main(String[] args) {
	try {
		// Look up the remote BookingManager object in the RMI registry
		BookingManagerInterface bookingManager = (BookingManagerInterface) Naming.lookup("rmi://localhost/BookingManager");

		// Invoke a method on the remote object
		boolean isAvailable = bookingManager.isRoomAvailable(101, LocalDate.now());
		System.out.println("Is room 101 available? " + isAvailable);

		// More remote method invocations can be added here as required
	} catch (Exception e) {
		System.err.println("Client exception: " + e.toString());
		e.printStackTrace();
	}
}

	/***************
	 * CONSTRUCTOR *
	 ***************/
	public BookingClientLocal() {
		try {
			//Look up the registered remote instance
			bm = new BookingManager();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	@Override
	public boolean isRoomAvailable(Integer roomNumber, LocalDate date) throws RemoteException {
		// Delegate the call to the BookingManager's isRoomAvailable method
		return bm.isRoomAvailable(roomNumber, date);
	}

	@Override
	public void addBooking(BookingDetail bookingDetail) throws Exception {
		// Delegate the call to the BookingManager's addBooking method
		bm.addBooking(bookingDetail);
	}

	@Override
	public Set<Integer> getAvailableRooms(LocalDate date) throws RemoteException {
		// Delegate the call to the BookingManager's getAvailableRooms method
		return bm.getAvailableRooms(date);
	}

	@Override
	public Set<Integer> getAllRooms() throws RemoteException {
		return bm.getAllRooms();
	}
}
