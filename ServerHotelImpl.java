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
    File users_file, rooms_file;

    public ServerHotelImpl() throws RemoteException, IOException {
        users_file = new File("users.txt");
        users_file.createNewFile();
    
        rooms_file = new File("rooms.txt");
    }

    public String register_user(String email, String password) {

        try {
            FileWriter dbWriter = new FileWriter(this.users_file);

            dbWriter.write(email + ";" + password);

            dbWriter.close();
        } catch (IOException e){
            System.out.println("Exception: " + e);
        }

        return "User registered sucefully!";
    }

    public boolean verify_user(String email) throws IOException {

        Scanner readDb = new Scanner(this.users_file);

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
        list_rooms.add("Rooms available:");

        try{

            Scanner readDb = new Scanner(this.rooms_file);        

            while(readDb.hasNextLine()) {  
                String data = readDb.nextLine();
                String isBooked = data.split(";")[3];

                // convert string to date
                // args 
                Date data_inicial = new SimpleDateFormat("dd/MM/yyyy").parse(dataInicialProposta);
                Date data_final = new SimpleDateFormat("dd/MM/yyyy").parse(dataFinalProposta);
                
                // dates inside of the file
                Date data_inicial_quarto = new SimpleDateFormat("dd/MM/yyyy").parse(data.split(";")[1]);
                Date data_final_quarto = new SimpleDateFormat("dd/MM/yyyy").parse(data.split(";")[2]); 

                // compare dates 
                if((data_inicial.compareTo(data_inicial_quarto) > 0 || data_inicial.compareTo(data_inicial_quarto) == 0) && 
                    (data_final.compareTo(data_final_quarto) < 0 || data_final.compareTo(data_final_quarto) == 0)&& isBooked.equals("nao-reservado")) {
                    
                    list_rooms.add(data.split(";")[0]);
                } 
            }

            readDb.close();

        } catch(ParseException e) {
            System.out.println("Exception: " + e);
        } catch(FileNotFoundException e) {
            System.out.println("Exception: " + e);
        }

        return list_rooms;
    }

    public String book_room(int id, String dataInicialReserva, String dataFinalReserva) throws RemoteException {
        
        try {
            File temporary_file = new File("book_temporary_file.txt");
            FileWriter write_temporary_file = new FileWriter(temporary_file);

            Scanner readDb = new Scanner(this.rooms_file);

            int isFirstLine = 0;
            String edit_room_line = "";

            while(readDb.hasNextLine()) {
                String data = readDb.nextLine();
                String numRoom = data.split(";")[0];

                if(Integer.parseInt(numRoom) == id)  {
                    
                    // in case of the room is in the first line of the file, it doesnt put a new line in the beggining of the sentence
                    if(isFirstLine != 0) {
                        edit_room_line = "\n" + data.split(";")[0] + ";" + data.split(";")[1] + ";" + data.split(";")[2] + ";reservado\n";
                    } else {
                        edit_room_line = data.split(";")[0] + ";" + data.split(";")[1] + ";" + data.split(";")[2] + ";reservado\n";
                    }
                                        
                    write_temporary_file.write(edit_room_line);                                    
                } else {
                    write_temporary_file.write(data);
                }
                
                isFirstLine++;
            }

            temporary_file.renameTo(this.rooms_file);
            this.rooms_file = temporary_file;

            temporary_file.delete();
        
            write_temporary_file.close();
            readDb.close();

            return "Room booked sucefully!";
        } catch(IOException e) {
            System.out.println("Exception: " + e);
        }        

        return "Couldnt booked room.";
    }

    public String cancel_room(int id, String dataInicialReserva, String dataFinalReserva) {

        try {
            File temporary_file = new File("cancel_temporary_file.txt");
            FileWriter write_temporary_file = new FileWriter(temporary_file);

            Scanner readDb = new Scanner(this.rooms_file);

            int isFirstLine = 0;
            String edit_room_line = "";

            while(readDb.hasNextLine()) {
                String data = readDb.nextLine();
                String numRoom = data.split(";")[0];

                if(Integer.parseInt(numRoom) == id)  {
                    
                    // in case of the room is in the first line of the file, it doesnt put a new line in the beggining of the sentence
                    if(isFirstLine != 0) {
                        edit_room_line = "\n" + data.split(";")[0] + ";" + data.split(";")[1] + ";" + data.split(";")[2] + ";nao-reservado\n";
                    } else {
                        edit_room_line = data.split(";")[0] + ";" + data.split(";")[1] + ";" + data.split(";")[2] + ";nao-reservado\n";
                    }
                                        
                    write_temporary_file.write(edit_room_line);                                    
                } else {
                    write_temporary_file.write(data);
                }
                
                isFirstLine++;
            }

            temporary_file.renameTo(this.rooms_file);
            this.rooms_file = temporary_file;

            temporary_file.delete();
        
            write_temporary_file.close();
            readDb.close();

            return "Room canceled sucefully!";
        } catch(IOException e) {
            System.out.println("Exception: " + e);
        }        

        return "Couldnt cancel room.";
    }
}