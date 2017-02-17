import java.lang.*;

public class Vehicle{
    
    private String owner_name;
    private String plate_number;

    Vehicle(String owner_name, String plate_number){
        this.owner_name = owner_name;
        this.plate_number = plate_number;
    }

    public String getOwnerName(){
        return this.owner_name;
    }

    public String getPlateNumber(){
        return this.plate_number;
    }

    public void setOwnerName(String owner_name){
        this.owner_name = owner_name;
    }

    public void setPlateNumber(String plate_number){
        this.plate_number = plate_number;
    }

    //teste
    public void print_info(){
        System.out.println(this.owner_name);
        System.out.println(this.plate_number);
    }

    /*public static void main(String args[]){
        Vehicle vehicle = new Vehicle("Catarina Correia", "XX-XX-XX");
        
    }*/

}