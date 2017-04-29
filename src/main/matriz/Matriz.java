package main.matriz;

import main.matriz.exception.ImpossivelEscalonar;
import main.matriz.exception.ImpossivelMultiplicarException;
import main.matriz.exception.ImpossivelSomarException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matriz { // TODO retornar versão String da Matriz
    private Double[][] matriz;
    private String nome;
    private int linhas, colunas;
    private String iLivre;

    public String getNome() {
        return nome;
    }

    public int getLinhas() {
         return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public Double getMatrizValue(int linhas, int colunas){
        return this.matriz[linhas][colunas];
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setILivre(String iLivre) {
        this.iLivre = iLivre;
    }

    /*
    *   Retorna Versão String da Matriz
    */
    public String getMatriz(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int linha=0; linha < getLinhas(); linha++){
            for (int coluna=0; coluna < getColunas(); coluna++){
                if (coluna == getColunas()-1)
                    sb.append(getMatrizValue(linha, coluna));
                else
                    sb.append(getMatrizValue(linha, coluna).toString().concat(" "));
            }
            if (linha != getLinhas()-1)
                sb.append(" ; ");
        }
        sb.append("]");
        return sb.toString();
    }

    public void setMatriz(Double[][] matriz){
        this.matriz = matriz;
    }

    public void setMatriz(String content) throws Exception{

        // Se nao existe espaço entre numero e ";", adicione
        // Deve possuir grupo para adicionar valores posteriormente
        String corrigePadraoLeft = "([^\\s]);";
        String corrigePadraoRight = ";([^\\s])";

        // Remove []s
        // Retira espaços das pontas
        // adiciona espaço entre numeros e ";"
        //      n1;n2 -> n1 ; n2
        // Transforma míltiplos espaços em apenas um
        content = content.replaceAll("\\[|\\]","");
        content = content.trim();
        content = content.replaceAll(corrigePadraoLeft, "$1 ;");
        content = content.replaceAll(corrigePadraoRight, "; $1");
        content = content.replaceAll(",", ".");
        content = content.replaceAll("[\\s]+"," ");


        String[] elementos = content.split(" ");

        // linhas são iguais ao número de
        // divisoes feitas a partir do ";"
        linhas = content.split(";").length;

        // Conta quantidade de Colunas
        // para inicializar array Bidimensional
        for(String elemento : elementos){
            if (elemento.equals(";")){
                break;
            }
            colunas++;
        }

        // inicializa matriz
        matriz = new Double[linhas][colunas];

        // agora nao sera necessario ";"
        // array elementos eh aproveitado
        content = content.replaceAll("\\s;\\s", " ");
        elementos = content.split("\\s");

        int tmp = 0;
        try {
            for (int linha = 0; linha < linhas; linha++) {
                for (int coluna = 0; coluna < colunas; coluna++) {

                    Pattern pattern = Pattern.compile("(.+)/(.+)");
                    Matcher matcher = pattern.matcher(elementos[tmp]);

                    if(matcher.find()){
                        this.matriz[linha][coluna] = parseFraction(
                                Double.parseDouble(matcher.group(1)),
                                Double.parseDouble(matcher.group(2))
                        );
                    }else{
                        this.matriz[linha][coluna] = Double.parseDouble(elementos[tmp]);
                    }
                    tmp++;
                }
            }
            if (tmp != elementos.length){
                throw new Exception();
            }
        }catch(ArrayIndexOutOfBoundsException err){
            throw new Exception("Matriz \""+this.nome+"\" Inválida!");
        }
    }

    public void setValue(Double value, int linha, int coluna){
        this.matriz[linha][coluna] = value;
    }

    public Matriz(String nome, String matriz) throws Exception{
        this.nome = nome;
        setMatriz(matriz);
    }

    public Matriz(String nome, Double[][] matriz) throws Exception{
        this.nome = nome;
        setMatriz(matriz);
        this.linhas = matriz[0].length;
        this.colunas = matriz[1].length;
    }

    public Matriz(String nome, int linhas, int colunas){
        this.nome = nome;
        this.linhas = linhas;
        this.colunas = colunas;
        setMatriz(new Double[linhas][colunas]);
    }

    private Double parseFraction(Double d1, Double d2){
        return d1/d2;
    }

    public Matriz transpor(){
        Matriz transposta = new Matriz(getNome()+"T", getColunas(), getLinhas());

        for (int linha=0; linha < this.getLinhas(); linha++){
            for (int coluna=0; coluna < getColunas(); coluna++){
                transposta.setValue( this.getMatrizValue(linha, coluna) , coluna, linha);
            }
        }

        return transposta;
    }

    public Matriz multiplicar(Matriz matrizB) throws Exception {
        if (this.getColunas() != matrizB.getLinhas() ){
            throw new ImpossivelMultiplicarException();
        }

        Matriz multiplicada = new Matriz(this.getNome()+matrizB.getNome(), this.getLinhas(), matrizB.getColunas());

        for(int linhaA=0; linhaA<this.getLinhas(); linhaA++){
            for(int colunaB=0; colunaB<matrizB.getColunas(); colunaB++){

                Double tmp, soma=0.0;
                for(int k=0; k<this.getColunas(); k++){

                    tmp = this.getMatrizValue(linhaA, k) * matrizB.getMatrizValue(k, colunaB);
                    soma += tmp;

                }
                multiplicada.setValue( soma,  linhaA, colunaB );
            }
        }
        return multiplicada;
    }

    public Matriz adicaoMatriz(Matriz matriz) throws Exception {
        return somarMatriz(matriz, 1);
    }

    public Matriz subtracaoMatriz(Matriz matriz) throws Exception {
        return somarMatriz(matriz, -1);
    }

	private Matriz somarMatriz(Matriz matrizB, double k) throws Exception {
		Matriz matrizSoma = new Matriz(this.getNome() + "+" + matrizB.getNome(), matrizB.getLinhas(), matrizB.getColunas());
		if (this.getLinhas() != matrizB.getLinhas()	|| this.getColunas() != matrizB.getColunas()) {
			throw new ImpossivelSomarException();
		}
		for (int linhas = 0; linhas < this.getLinhas(); linhas++) {
			for (int colunas = 0; colunas < this.getColunas(); colunas++) {
				matrizSoma.setValue(this.getMatrizValue(linhas, colunas) + (matrizB.getMatrizValue(linhas, colunas) * k), linhas, colunas);
			}
		}
		return matrizSoma;
	}

	public Matriz multiplicarEscalar(double k) {
		Matriz multi = new Matriz(this.getNome(), this.getLinhas(),
				this.getColunas());
		for (int linhas = 0; linhas < this.getLinhas(); linhas++) {
			for (int colunas = 0; colunas < this.getColunas(); colunas++) {
				multi.setValue(this.getMatrizValue(linhas, colunas) * k,
						linhas, colunas);
			}
		}
		return multi;
	}

    public static Matriz getIdentidade(int k) {

        Matriz identidade = new Matriz("Id", k,k);
        for (int linhas = 0; linhas < k; linhas++) {
            for (int colunas = 0; colunas < k; colunas++) {
                if (linhas == colunas) {
                    identidade.setValue(1.0, linhas, colunas);
                } else {
                    identidade.setValue(0.0, linhas, colunas);
                }
            }
        }
        return identidade;
    }

    // MÉTODO 1: Divisão
    private Matriz escalonarSistemaDivisao(Matriz independentes) throws Exception {
        Matriz matriz = this;
        if (this.getMatrizValue(0, 0) != 0) {

            for (int linhas = 0; linhas < matriz.getLinhas(); linhas++) {
                for (int colunas = 0; colunas < matriz.getColunas(); colunas++) {
                    Double k;
                    if (linhas == 0 && colunas == 0) {
                        k = matriz.getMatrizValue(linhas, colunas);
                        for (int j = 0; j < getColunas(); j++) {
                            setValue(matriz.getMatrizValue(0, j) / k, 0, j);
                        }
                        independentes.setValue(independentes.getMatrizValue(linhas, 0) / k, linhas, 0);
                    }

                    if (linhas > colunas && colunas == 0) {

                        k = matriz.getMatrizValue(linhas, colunas);
                        for (int j = 0; j < matriz.getColunas(); j++) {
                            setValue(matriz.getMatrizValue(linhas, j) - (k * matriz.getMatrizValue(0, j)),linhas, j);
                        }

                        independentes.setValue(independentes.getMatrizValue(linhas, 0) - (k * independentes.getMatrizValue(0,0)), linhas, 0);


                    } else if (linhas > colunas) {
                        if (linhas-1 == colunas) {
                            k = matriz.getMatrizValue(linhas-1, colunas);
                            for (int j = 0; j < matriz.getColunas(); j++) {
                                setValue(matriz.getMatrizValue(linhas-1, j) / k, linhas-1, j);
                            }
                            independentes.setValue(independentes.getMatrizValue(linhas-1, 0) / k, linhas-1, 0);
                        }
                        k = matriz.getMatrizValue(linhas, colunas);
                        for (int j = 0; j < getColunas(); j++) {
                            setValue(matriz.getMatrizValue(linhas, j) - k * matriz.getMatrizValue(linhas-1, j),linhas, j);
                        }
                        independentes.setValue(independentes.getMatrizValue(linhas, 0) - (k * independentes.getMatrizValue(linhas-1,0)), linhas, 0);

                    }
                }
            }

        } else {
            throw new ImpossivelEscalonar();
        }

        return matriz;
    }

    /*
    *   Método de Escalonamento Público inverter Matriz caso necessário
    *   Além de que futuramente pode direcionar pra diferentes métodos de escalonamento
    */
    public Matriz escalonarSistema(Matriz independentes) throws Exception {
        Matriz matriz = this;

        try{
            matriz.escalonarSistemaDivisao(independentes);
        }catch (ImpossivelEscalonar err){
            matriz = Matriz.inverter(matriz);
            matriz.escalonarSistemaDivisao(independentes);
            matriz = Matriz.inverter(matriz);
        }
        return matriz;
    }

    /*
    *   Método para inverter Matriz
    *   Atenção: Esse Método de inversão não se refere à transposição
    *
    * */
    public static Matriz inverter(Matriz matriz){
        int cont = matriz.getColunas();
        Double currentValue = 0.0;
        for (int coluna = 0; coluna < matriz.getColunas()/2; coluna++) {
            cont--;
            for (int linha = 0; linha < matriz.getLinhas(); linha++) {
                currentValue = matriz.getMatrizValue(linha, cont);
                matriz.setValue(matriz.getMatrizValue(linha, coluna), linha, cont);
                matriz.setValue(currentValue, linha, coluna);
            }
        }

        cont = matriz.getLinhas();
        for (int linha = 0; linha < matriz.getLinhas()/2; linha++) {
            cont--;
            for (int coluna = 0; coluna < matriz.getColunas(); coluna++) {
                currentValue = matriz.getMatrizValue(cont, coluna);
                matriz.setValue(matriz.getMatrizValue(linha, coluna), cont, coluna);
                matriz.setValue(currentValue, linha, coluna);
            }
        }
        return matriz;
    }

    public double calcularDeterminanteEscalada () {
        double det = 1;
        for (int linhas = 0; linhas < this.getLinhas(); linhas++) {
            for (int colunas = 0; colunas < this.getColunas(); colunas++) {
                if (linhas == colunas) {
                    det = det * this.getMatrizValue(linhas, colunas);
                }
            }
        }
        return det;
    }

    public Matriz resolverSistemaEscalonado(Matriz independente) throws Exception {

        if (isSPD()) {
            Matriz incognitas = new Matriz("S", this.getLinhas(), 1);
            if (this.getColunas() > this.getLinhas()) {
                incognitas.setILivre("Para incógnita livre = 0");
            }
            for (int linha = this.getLinhas() - 1; linha >= 0; linha--) {
                Double tmp = 0.0;
                for (int coluna = this.getColunas() - 1; coluna >= 0; coluna--) {
                    if (coluna == this.getColunas() - 1) {
                        if (coluna == linha) {
                            tmp = (independente.getMatrizValue(linha, 0) / this
                                    .getMatrizValue(linha, coluna));
                        } else {
                            tmp = (independente.getMatrizValue(linha, 0) - (this
                                    .getMatrizValue(linha, coluna) * incognitas
                                    .getMatrizValue(coluna, 0)));
                        }
                    } else if (coluna > linha) {
                        tmp = (tmp - (this.getMatrizValue(linha, coluna) * incognitas
                                .getMatrizValue(coluna, 0)));

                    } else if (linha == coluna) {
                        tmp = (tmp / this.getMatrizValue(linha, coluna));
                    }
                    incognitas.setValue(tmp, linha, 0);
                }
            }
            return incognitas;

        } else {
            if (independente.getMatrizValue(independente.getLinhas()-1, 0) == 0) {
                throw new Exception("Sistema Possível Indeterminado");
            } else {
                throw new Exception("Sistema Impossível");
            }
        }
    }

    private boolean isSPD() {
        if (calcularDeterminanteEscalada() != 0) {
            return true;
        }
        return false;
    }

    /*
    * Compara se duas Matrizes são iguais.
    * Retorna: Boolean
    */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Matriz)){
            return false;
        }else{
            if (!(this.getLinhas() == ((Matriz) o).getLinhas() && this.getColunas() == ((Matriz) o).getColunas())){
                return false;
            }

            for (int linha=0; linha < this.getLinhas(); linha++){
                for (int coluna=0; coluna < this.getColunas(); coluna++){
                    if (!this.getMatrizValue(linha, coluna).equals(((Matriz) o).getMatrizValue(linha, coluna))){
                        return false;
                    }
                }
            }

            return true;
        }
    }
}