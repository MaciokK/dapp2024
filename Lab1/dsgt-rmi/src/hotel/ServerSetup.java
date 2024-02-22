import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import hotel.BookingManager;

public class ServerSetup {
    public static void main(String[] args) {
        try {
            // Create the remote object
            BookingManager bookingManager = new BookingManager();

            Registry registry = LocateRegistry.createRegistry(1099); // Default RMI port
            System.out.println("RMI Registry started on port 1099");
            // Proceed to bind your remote objects here
            // Bind the remote object's stub in the registry under the name "BookingManager"
            Naming.rebind("BookingManager", bookingManager);
            System.out.println("BookingManager is bound in the RMI registry");


        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
