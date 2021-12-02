import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.*;
import java.util.ArrayList;

public interface ServerHotelIntf extends Remote{
    String register_user(String email, String password) throws RemoteException;
    boolean verify_user(String email) throws RemoteException, FileNotFoundException, IOException;
    ArrayList<String> list_rooms(String dataInicialProposta, String dataFinalProposta) throws RemoteException;
    String cancel_room(int id, String dataInicialReserva, String dataFinalReserva)throws RemoteException;
    String book_room(int id, String dataInicialReserva, String dataFinalReserva) throws RemoteException;
}

