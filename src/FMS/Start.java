package FMS;

import java.lang.reflect.Constructor;
import java.sql.SQLException;

public class Start {
    public static void main(String[] args) throws SQLException, ClassNotFoundException{
        //new FlowerDatabase();
        MyLogin.getMylogin();


        //new FlowerDatabase();
        //new StoreManagerGUI();
        //new ShopAssistantGUI();
        //new FlowerDatabase().into_excel();

        /*new FlowerDatabase().insertFlowersData(310);
        new FlowerDatabase().insertSuppliesData(310);
        new FlowerDatabase().insertFertilizersData(310);
        new FlowerDatabase().update_ID();*/
    }
}
