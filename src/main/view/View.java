package main.view;

import main.matriz.Matriz;

public class View {
    private int casasDecimais;

    public View(int casasDecimais){
        this.casasDecimais = casasDecimais;
    }

    public void exibirMatriz(Matriz matriz){
        System.out.print(matriz.getNome()+" = ");
        for(int linha = 0; linha < matriz.getLinhas(); linha++){

            //Se a linha eh a primeria OU a ultima
            if(linha == 0 || linha == matriz.getLinhas()-1){
                //Se a linha eh a primeria E a ultima
                if(linha == 0 && linha == matriz.getLinhas()-1){
                    System.out.print("\t,_");
                    for(int i=0; i<matriz.getColunas(); i++)
                        System.out.print("\t\t");
                    System.out.println("_,");
                    System.out.print("\t|_");
                }
                // se eh a primeira
                else if(linha == 0){
                    System.out.print("\t,_");
                    for(int i=0; i<matriz.getColunas(); i++)
                        System.out.print("\t\t");
                    System.out.println("_,");
                    System.out.print("\t|");
                }
                // se eh a ultima
                else{
                    System.out.print("\t|_");
                }
            }else{
                System.out.print("\t|");
            }

            for(int coluna = 0; coluna < matriz.getColunas(); coluna++){
                System.out.print("\t"+
                        matriz.getMatrizValue(linha , coluna)
                                .toString()
                                .replaceAll("\\.0$","") // nao exibe .0
                                .replaceAll("\\.(\\d{"+casasDecimais+"})\\d+$",".$1") //reduz casas decimais
                                .replaceAll("\\.$", "") // caso de 0 casas decimais
                        +"\t");
            }
            // Se � a ultima linha
            if (linha != matriz.getLinhas()-1) {
                System.out.println(" |");
            } else {
                System.out.println("_|\t("+matriz.getLinhas()+"x"+matriz.getColunas()+")");
            }
        }
        System.out.println();
    }

    public static void exibirMatrizStatic(Matriz matriz){
        new View(3).exibirMatriz(matriz);
    }
}
