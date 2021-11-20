import java.rmi.*;

public class ServerHotel {    
    public static void main(String[] args) {
        try {
            ServerHotelImpl serverHotelImpl = new ServerHotelImpl();
            Naming.rebind("Server", serverHotelImpl);

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}