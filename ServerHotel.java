import java.rmi.*;

public class ServerHotel {    
    public static void main(String[] args) {
        try {
            System.setProperty("java.security.policy", "./policy");

            ServerHotelImpl serverHotelImpl = new ServerHotelImpl();
            Naming.rebind("Server", serverHotelImpl);

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }   
    }
}