package dev.omaremara.bugtracker.model.exception;

public class DataBaseException extends Exception{
    public DataBaseException(String message, Throwable cause){
        super(message, cause);
    }
}