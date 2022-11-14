package cn.coolcollege.fast.exception;

/**
 * 持久化操作数据异常
 */
public class PersistenceException extends RuntimeException {

    public PersistenceException() {
        super();
    }

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
