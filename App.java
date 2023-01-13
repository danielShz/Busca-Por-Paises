package buscapaises;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {
        buscarDados();

        // buscarPorPalavraChave("tupi-guarani");
    }

    // Busca os dados dos países e os armazena em arquivos

    public static void buscarDados() throws IOException, NullPointerException {
        try {
            Map<String, String> paisesDaOnu = Pais.buscarPaisesDaOnu();

            TratamentoDeTexto.downloadStopWords("stop_words.txt");
            TratamentoDeTexto tratamentoDeTexto = new TratamentoDeTexto("stop_words.txt");

            for(String paisNome : paisesDaOnu.keySet()) {
                Pais pais = new Pais(paisNome, paisesDaOnu.get(paisNome), tratamentoDeTexto);
                
                Pais.salvarPais(pais, "paises/" + paisNome.replaceAll("(\\s)", "_") + ".ser");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // Dado que os dados dos países foram buscados, 
    // o método abaixo gera as pontuações dos países de acordo com a palavra-chave fornecida

    public static void buscarPorPalavraChave(String palavraChave) throws IOException, NullPointerException, ClassNotFoundException {
        String palavra = palavraChave.toLowerCase();

        try {
            Map<String, String> paisesDaOnu = Pais.buscarPaisesDaOnu();

            Map<String, Float> paisesPontuacao = new HashMap<String, Float>();
            LinkedHashMap<String, Float> paisesOrdenados = new LinkedHashMap<>();

            for(String paisNome : paisesDaOnu.keySet()) {
                Pais pais = Pais.lerPais("paises/" + paisNome.replaceAll("(\\s)", "_") + ".ser");

                float pontuacao = pais.gerarPontuacao(palavra);

                paisesPontuacao.put(paisNome, pontuacao);
            }

            paisesPontuacao.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
            .forEachOrdered(x -> paisesOrdenados.put(x.getKey(), x.getValue()));
            
            for(String paisNome : paisesOrdenados.keySet()) {
                System.out.println(paisNome + " - " + paisesOrdenados.get(paisNome));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
