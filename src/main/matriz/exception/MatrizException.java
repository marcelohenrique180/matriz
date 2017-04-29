package main.matriz.exception;



public class MatrizException extends Exception{

    public MatrizException(String err){
        super("Erro de Matriz: " + err);
    }
}
