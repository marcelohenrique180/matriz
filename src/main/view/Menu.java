package main.view;


import main.MatrizFileReader;
import main.matriz.Matriz;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Menu {

    public static void start(){
        HashMap<String, Matriz> matrizes;
        Scanner scanner = new Scanner(System.in);
        View view = new View(3);

        try {

            System.out.println("Carregando matrizes do diretório 'files/'...");
            matrizes = loadFromDisk();

            int opt = 0;
            while(opt != 99){
                try {
                    String nome = null;
                    Matriz resultado = null;
                    System.out.println("\nDigite o que deseja fazer:");
                    System.out.println("1 - Listar todas");
                    System.out.println("2 - Nova Matriz");
                    System.out.println("3 - Cálculo com Matriz");
                    System.out.println("4 - Escalonar Sistema");
                    //System.out.println("5 - Calcular Determinante");
                    //System.out.println("6 - Classificar Sistema");
                    System.out.println("99 - Sair");
                    opt = scanner.nextInt();

                    clearConsole();

                    switch (opt) {
                        case 1: // Listar Todas
                            System.out.println("------------------MATRIZES------------------");
                            matrizes.values().forEach(view::exibirMatriz);
                            System.out.println();
                            break;
                        case 2: // Nova Matriz
                            System.out.print("Insira o nome da Matriz: ");
                            nome = scanner.next();
                            System.out.print("Insira a Matriz: ");
                            scanner.nextLine();
                            String sMatriz = scanner.nextLine();
                            matrizes.put(nome, new Matriz(nome, sMatriz));
                            System.out.println("\n\nInserida Com Sucesso!\n");
                            break;
                        case 3: // Calculo com Matriz

                            System.out.println("Pressione 'h' para ajuda");
                            System.out.print("\nInsira sua Operação: ");
                            scanner.nextLine();
                            String op = scanner.nextLine();

                            if(op.equalsIgnoreCase("h")){
                                System.out.println("Exemplos:");
                                System.out.println("A * B");
                                System.out.println("A + B");
                                System.out.println("A - B");
                                System.out.println("A T -> A Transposta");
                            }else{
                                Pattern patternOperacao = Pattern.compile("(.+)\\s+([+|\\-|*])\\s+(.+)");
                                Pattern patternTransposicao = Pattern.compile("(.+)\\s+T");

                                Matcher matchOperacao = patternOperacao.matcher(op);
                                Matcher matchTransposicao = patternTransposicao.matcher(op);

                                if(matchOperacao.find()){
                                    Matriz matrizA = matrizes.get(matchOperacao.group(1));
                                    String operacao = matchOperacao.group(2);
                                    Matriz matrizB = matrizes.get(matchOperacao.group(3));

                                    switch (operacao){
                                        case "+":
                                            resultado = matrizA.adicaoMatriz(matrizB);
                                            break;
                                        case "-":
                                        	resultado = matrizA.subtracaoMatriz(matrizB);
                                        	break;
                                        case "*":
                                            resultado = matrizA.multiplicar(matrizB);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                else if(matchTransposicao.find()){
                                    Matriz matriz = matrizes.get(matchTransposicao.group(1));
                                    resultado = matriz.transpor();
                                }else{
                                    throw new Exception("Operação Inválida");
                                }

                                view.exibirMatriz(resultado);
                                System.out.println("Deseja Armazenar o Resultado? (S -> sim; ENTER -> não)");
                                String armazena = scanner.nextLine();

                                if (armazena.equalsIgnoreCase("s")){
                                    System.out.print("Escolha seu nome (Dica: "+resultado.getNome()+"): ");
                                    resultado.setNome(scanner.next());
                                    matrizes.put(resultado.getNome(), resultado);

                                    System.out.println("\n\n "+resultado.getNome()+" Armazenado com Sucesso!");
                                }
                            }
                            break;
                        case 4: // escalonar Sistema
                            System.out.println("Digite o nome da Matriz:");
                            nome = scanner.next();
                            resultado = matrizes.get(nome);
                            System.out.println("Insira os termos independentes:");
                            String independentes = scanner.next();

                            Matriz tIndependentes = resultado.escalonarSistema(new Matriz("IND", independentes));

                            System.out.println("Matriz Escalonada:\\n");
                            view.exibirMatriz(resultado);
                            System.out.println("Termos Independentes:");
                            view.exibirMatriz(tIndependentes);
                            break;
                        /*case 5: // Determinante
                            System.out.println("Digite o nome da Matriz:");
                            nome = scanner.next();
                            resultado = matrizes.get(nome);

                            Double determinante = resultado.getDeterminante();
                            System.out.println("Matriz:\\n");
                            view.exibirMatriz(resultado);
                            System.out.println("Determinante: " + determinante);
                            break;
                        case 6: // Classificar Sistema
                            System.out.println("Digite o nome da Matriz:");
                            nome = scanner.next();
                            resultado = matrizes.get(nome);

                            String classificacao = resultado.classificar();
                            System.out.println("Matriz:\\n");
                            view.exibirMatriz(resultado);
                            System.out.println("Classificação: " + classificacao);
                            break;
                        */
                        case 99:
                            System.out.println("Made by Aléxia, Marcelo and Paula =D");
                            break;
                        default:
                            throw new Exception("Opção Inválida");
                    }
                    System.out.println();
                }
                catch (NullPointerException err){
                    System.out.println();
                    System.out.println("----------------------");
                    System.out.println("Matriz Não Encontrada!");
                    System.out.println("----------------------");
                }
                catch (Exception err){
                    System.out.println();
                    System.out.println("---------------------------");
                    System.out.println(err.getMessage());
                    System.out.println("---------------------------");
                }
            }

        }catch (Exception err){
            System.err.println(err.getMessage());
        }
    }

    private static HashMap<String, Matriz> loadFromDisk() throws Exception{
        MatrizFileReader fr = new MatrizFileReader("files/");
        HashMap<String, Matriz> matrizes = new HashMap<>();
        List<File> files = fr.getFilesFromDir();

        for (File file : files) {
            try {
                String nome = file.getName().replace(".txt", "");
                String content = fr.read(file);
                String encontraPadrao = "\\[.+\\]";
                Pattern pattern = Pattern.compile(encontraPadrao);
                Matcher matcher = pattern.matcher(content);

                if (matcher.find()) {
                    String sMatriz = matcher.group(0);
                    Matriz matriz = new Matriz(nome, sMatriz);

                    matrizes.put(matriz.getNome(), matriz);
                }else{
                    throw new Exception("Matriz não encontrada no arquivo "+nome+".txt");
                }
            } catch (Exception err) {
                //System.err.println(err.getMessage());
                err.printStackTrace();
            }
        }
        return matrizes;
    }

    private static void clearConsole(){
        for(int i = 0; i< 30; i++){
            System.out.println();
        }
    }
}
