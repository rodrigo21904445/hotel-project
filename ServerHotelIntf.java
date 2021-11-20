import java.rmi.*;

public interface ServerHotelIntf extends Remote{
    String register_user(String email, String password) throws RemoteException;
    boolean verify_user(String email) throws RemoteException;
    String list_rooms(String dataInicialProposta, String dataFinalProposta) throws RemoteException;
    String cancel_room(int id, String dataInicialReserva, String dataFinalReserva)throws RemoteException;
}

