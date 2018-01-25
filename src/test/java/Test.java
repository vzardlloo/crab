import crab.Crab;
import crab.config.impl.XmlConfiguration;
import crab.handler.impl.AssetHandler;

import java.io.IOException;

public class Test {

    public static void main(String[] args) {

        try{
            new Crab().start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
