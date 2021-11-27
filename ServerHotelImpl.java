import java.rmi.*;
import java.rmi.server.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

public class ServerHotelImpl extends UnicastRemoteObject implements ServerHotelIntf{

    public ServerHotelImpl() throws RemoteException {
    }

    public String register_user(String email, String password) {

        try {
            FileWriter dbWriter = new FileWriter("user.txt");

            dbWriter.write(email + ";" + password);

            dbWriter.close();
        } catch (IOException e){
            System.out.println("Exception: " + e);
        }

        return "User registered sucefully!";
    }

    public boolean verify_user(String email) throws IOException {

        Scanner readDb = new Scanner("user.txt");

        while(readDb.hasNextLine()) {
            String data = readDb.nextLine();
            String emailUser = data.split(";")[0];

            if(email.equals(emailUser)) {
                readDb.close();
                return true;
            }
        }

        readDb.close();

        return false;
    }


    public ArrayList<String> list_rooms(String dataInicialProposta, String dataFinalProposta){        

        ArrayList<String> list_rooms = new ArrayList<String>();

        try{

            Scanner readDb = new Scanner("rooms.txt");        

            while(readDb.hasNextLine()) {  
                String data = readDb.nextLine();
                String isBooked = data.split(";")[3];
                
                Date data_inicial = new SimpleDateFormat("dd/MM/yyyy").parse(dataInicialProposta);
                Date data_final = new SimpleDateFormat("dd/MM/yyyy").parse(dataFinalProposta);

                // convert string to date
                Date data_inicial_quarto = new SimpleDateFormat("dd/MM/yyyy").parse(data.split(";")[1]);
                Date data_final_quarto = new SimpleDateFormat("dd/MM/yyyy").parse(data.split(";")[2]); 

                // compare dates 
                if(data_inicial.after(data_inicial_quarto) && data_final.before(data_final_quarto) && isBooked.equals("nao-reservado")) {
                    list_rooms.add(data.split(";")[0]);
                } 
            }

            readDb.close();

        } catch(ParseException e) {
            System.out.println("Exception: " + e);
        } 

        return list_rooms;
    }

    public String cancel_room(int id, String dataInicialReserva, String dataFinalReserva) {

        try {
            
            File temporary_file = new File("rooms_temporary_file.txt");
            temporary_file.createNewFile();
            FileWriter new_file_writer = new FileWriter("rooms_temporary_file.txt");

            Scanner readDb = new Scanner("rooms.txt");
            // FileWriter dbWriter = new FileWriter("user.txt");

            while(readDb.hasNextLine()) {
                String data = readDb.nextLine();
                String numRoom = data.split(";")[0];

                if(Integer.parseInt(numRoom) == id) {
                    
                    // change room "reservado" to "nao-reservado"
                    String edit_room_line = data.split(";")[0] + data.split(";")[1] + data.split(";")[2] + "nao-reservado";
                    new_file_writer.write(edit_room_line);                    

                } else {
                    new_file_writer.write(data);
                }   
            }

            readDb.close();
            new_file_writer.close();
            // dbWriter.close();

            // delete rooms.txt file and rename the temporary file to rooms.txt
            File file_to_delete = new File("rooms.txt");

            temporary_file.renameTo(file_to_delete);
            file_to_delete.delete();

            return "Room canceled sucefully!";
            
        } catch(FileNotFoundException e) {
            System.out.println("Exception: " + e);
        } catch(IOException e) {
            System.out.println("Exception: " + e);
        }

        return "Room couldnt be canceled.";
    }
}