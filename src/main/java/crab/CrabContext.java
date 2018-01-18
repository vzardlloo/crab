package crab;


import lombok.Getter;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.util.Map;

@Getter
@Setter
public class CrabContext {

    private String rootPath;

    private String publicPath;

    private Map<String,String> locale;

    private CrabContext(String rootPath){
        this.rootPath = rootPath;
    }

    public static CrabContext init(String rootDir) throws FileNotFoundException{
        CrabContext context = new CrabContext(rootDir);
        return context;
    }



}
