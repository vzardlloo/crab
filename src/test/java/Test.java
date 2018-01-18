import crab.Crab;
import crab.handler.impl.AssetHandler;

import java.io.IOException;

public class Test {

    public static void main(String[] args) {

        try{
             new Crab(9090)
                    .addHandler(new AssetHandler("D:\\work"))
                    .start();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
