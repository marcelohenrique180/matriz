package main.matriz;

import junit.framework.TestCase;
import main.matriz.exception.ImpossivelMultiplicarException;
import main.matriz.exception.ImpossivelSomarException;

public class TestMatriz extends TestCase {
    private Matriz matrizA, matrizB, matrizC, matrizD, matrizE, matrizI3;

    @Override
    protected void setUp() throws Exception {
        matrizA = new Matriz("A", "[1 2 ; 3 4]");
        matrizB = new Matriz("B", "[0 0 ; 2 1]");
        matrizC = new Matriz("C", "[0 0  0; 6 6 6]");
        matrizD = new Matriz("D", "[1 1; 2 3; 5 8]");
        matrizE = new Matriz("E", "[3 1 4; 1 5 9; 2 6 5]");
        matrizI3 = new Matriz("I3", "[1 0 0; 0 1 0; 0 0 1]");
    }

    public void testMatrizEqual() throws Exception {

        Double[][] matrizCompara = new Double[2][2];
        matrizCompara[0][0] = 1.0;
        matrizCompara[0][1] = 2.0;
        matrizCompara[1][0] = 3.0;
        matrizCompara[1][1] = 4.0;
        Matriz matriz = new Matriz("EQ", matrizCompara);

        assertTrue(matrizA.equals(matriz));
    }

    public void testMatrizNotEqual(){
        assertFalse(matrizA.equals(matrizB));
    }

    public void testAdicao() throws Exception {
        try{
            matrizA.adicaoMatriz(matrizB);
            assertTrue(true);
        } catch (ImpossivelSomarException success){
            assertFalse("Não Foi Possível Realizar Adicao", true);
        }
    }

    public void testAdicaoImpossivel() throws Exception {
        try{
            matrizA.adicaoMatriz(matrizC);
            assertFalse("Foi Possível Realizar Adicao", true);
        } catch (ImpossivelSomarException success){
            assertTrue(true);
        }
    }

    public void testSubtracao() throws Exception {
        try{
            matrizA.subtracaoMatriz(matrizB);
            assertTrue(true);
        } catch (ImpossivelSomarException success){
            assertFalse("Não Foi Possível Realizar Subtracao", true);
        }
    }

    public void testSubtracaoImpossivel() throws Exception {
        try{
            matrizC.subtracaoMatriz(matrizA);
            assertFalse("Foi Possível Realizar Subtracao", true);
        } catch (ImpossivelSomarException success){
            assertTrue(true);
        }
    }

    public void testMultiplicacao() throws Exception {
        try {
            matrizD.multiplicar(matrizA);
            assertTrue(true);
        }catch (ImpossivelMultiplicarException error){
            assertFalse("Não Foi Possível Realizar Multiplicacao", true);
        }
    }

    public void testMultiplicacaoImpossivel() throws Exception {
        try {
            matrizA.multiplicar(matrizD);
            assertFalse("Foi Possível Realizar Multiplicacao", true);
        }catch (ImpossivelMultiplicarException success){
            assertTrue(true);
        }
    }

    public void testMultiplicacaoIdentidade() throws Exception {
        Matriz temporaria = matrizE.multiplicar(matrizI3);

        if(matrizE.equals(temporaria))
            assertTrue(true);
        else
            assertFalse("Multiplicar por Identidade deve retornar a Matriz Inalterada", true);
    }

    public void testMultiplicarEscalar() throws Exception {
        Matriz dobrada = new Matriz("D2", "[2 2; 4 6; 10 16]");
        assertTrue(dobrada.equals( matrizD.multiplicarEscalar(2) ));
    }

    public void testTranspor() throws Exception {
        Matriz matrizT = new Matriz("BT", "[0 2 ; 0 1]");
        assertTrue( matrizT.equals( matrizB.transpor() ) );
    }

    public void testGetIdentidade(){
        assertTrue("Matriz Nao Retornou Identidade Correta", matrizI3.equals(Matriz.getIdentidade(3)) );
    }

    public void testGetMatriz(){
        String matrizA = "[1.0 2.0 ; 3.0 4.0]";
        assertEquals(matrizA, this.matrizA.getMatriz());
    }

    public void testEscalonamentoDOWN() throws Exception {
        Matriz tmpMatriz = new Matriz("tmp", "[1 2 ; 0 -2]");
        Matriz matrizAEscalonada = matrizA.escalonarSistema(new Matriz("ind","[0;0]"));
        assertTrue(tmpMatriz.equals(matrizAEscalonada));
    }

    public void testEscalonamentoUP() throws Exception {
        Matriz toBeEscalonada = new Matriz("tmp", "[0 8 ; 2 4]");
        Matriz resultado = new Matriz("tmp", "[-4 0 ; 0.5 1]");

        Matriz matrizAEscalonada = toBeEscalonada.escalonarSistema(new Matriz("ind","[0;0]"));
        assertTrue(resultado.equals(matrizAEscalonada));
    }

    public void testEscalonarSistemaJaEscalonado() throws Exception {
        assertTrue(matrizB.escalonarSistema(new Matriz("", "[0;0]")).equals(matrizB));
    }

    public void testInverter() throws Exception {
        Matriz matrizDInvertida = new Matriz("D", "[8 5; 3 2; 1 1]");
        assertTrue(Matriz.inverter(matrizD).equals(matrizDInvertida));
    }

    public void testResolverSistemaEscalonado() throws Exception {
        Matriz sistema = new Matriz("sis", "[2 1; 0 1]");
        Matriz indepen = new Matriz("ind", "[ 6; 4]");

        indepen = sistema.resolverSistemaEscalonado(indepen);

        Matriz result = new Matriz("res", "[1;4]");
        assertTrue(indepen.equals(result));
    }
}
