package crab.exception;

/**
 * 异常处理类
 */
public class CrabException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CrabException(){
        //TODO
    }

    public CrabException(String msg){
        super(msg);
    }

}
