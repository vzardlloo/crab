import crab.Crab;
import crab.handler.impl.AssetHandler;

import java.io.IOException;

public class Test {

    public static void main(String[] args) {

        try{
            Crab crab = new Crab(10000);
            crab.addHandler(new AssetHandler("D:\\work"));
            crab.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
