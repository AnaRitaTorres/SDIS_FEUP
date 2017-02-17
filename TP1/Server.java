import java.lang.*;
import java.util.Vector;

class Server{

    public Vector<Vehicle> vehicles;

    //retorna o nome do owner associado à matrícula, caso exista, caso contrário, retorna "NOT_FOUND";

    public Integer searchPlateNumber(String plate_number){

        for (Integer i=0; i<vehicles.size(); i++){
            if (vehicles.get(i).getPlateNumber() == plate_number){
                return i;
            }
        }

        return -1;

    }

    public String lookup(String plate_number){

        Integer number = searchPlateNumber(plate_number);
        if (number != -1)
            return vehicles.get(number).getOwnerName();
        else
            return "NOT_FOUND";
    }

    public Integer register(String owner_name, String plate_number){

        Integer number = searchPlateNumber(plate_number);
        if (number != -1){
            return -1;
        }
        else{
            return vehicles.size();
        }
    }

}