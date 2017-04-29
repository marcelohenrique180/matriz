package main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MatrizFileReader {
    private String dir;

    public MatrizFileReader(String dir){
        this.dir = dir;
    }

    /*
    *   getFilesFromDir()
    *   returns: List<File>
    *
    *   Retorna todos os arquivos do diretório especificado
    */
    public List<File> getFilesFromDir() throws Exception{
        // var que será retornada com arquivos
        List<File> files = new ArrayList<>();

        File directory = new File(dir); // diret�rio definido
        File[] fList = directory.listFiles(); // todos os itens do dir

        // lanca erro se não houverem itens
        if(fList == null)
            throw new Exception("Pasta N�o Existe!");

        for (File file : fList){
            if(file.isFile()){
                files.add(file);
            }
        }

        if(files.isEmpty())
            throw new Exception("Nenhum arquivo encontrado!");

        return files;
    }

    /*
    *   read()
    *   returns: String
    *
    *   Retorna o conteudo do arquivo
    */
    public String read(File file) throws Exception{
        String content;

        BufferedReader buffer = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        StringBuilder sb = new StringBuilder();
        String line = buffer.readLine();

        while (line != null){
            sb.append(line);
            sb.append(System.lineSeparator());
            line = buffer.readLine();
        }

        content = sb.toString();

        if (content.equals("")) {
            buffer.close();
            throw new Exception("Arquivo "+file.getName()+" vazio!");
        }
            buffer.close();
        return content;
    }
}
