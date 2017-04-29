package main.matriz.exception;

public class ImpossivelSomarException extends MatrizException {

    public ImpossivelSomarException(){
        super("Impossível somar. As matrizes são de ordens diferentes");
    }

}
