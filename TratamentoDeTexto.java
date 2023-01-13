package buscapaises;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TratamentoDeTexto {
  private ArrayList<String> stopWords;

  public TratamentoDeTexto(String diretorio) throws IOException {
    try {
      FileReader leitor = new FileReader(diretorio);
      BufferedReader leitorBuffer = new BufferedReader(leitor);

      String linha = leitorBuffer.readLine();
      String texto = "";
      
      while (linha != null) {
        texto += linha + " ";
        linha = leitorBuffer.readLine();
      }

      String textoLimpo = this.limparTexto(texto);
      this.stopWords = this.quebrarTextoEmPalavras(textoLimpo);

      leitorBuffer.close();
    } catch (IOException ioe) {
      throw new IllegalArgumentException("Arquivo não pode ser lido!");
    }
  }

  public String limparTexto(String texto) {

    String textoLimpo;
    textoLimpo = texto.replaceAll("\\(em([^)]+)\\)", " ");
    textoLimpo = textoLimpo.replaceAll("–", "-");
    textoLimpo = textoLimpo.replaceAll("\\[(.*?)\\]", " ");
    textoLimpo = textoLimpo.replaceAll("[^-A-z-Á-ú]", " ");
    textoLimpo = textoLimpo.replaceAll("[ ]{2,}", " ");

    return textoLimpo.toLowerCase().trim();
  }

  public ArrayList<String> quebrarTextoEmPalavras(String texto) {
    String[] array = texto.split("\\s+");
    ArrayList<String> palavras = new ArrayList<String>(Arrays.asList(array));
    
    return palavras;
  }

  public ArrayList<String> removerStopWords(ArrayList<String> palavras) {
    ArrayList<String> palavrasFiltradas = new ArrayList<>();

    for (String palavra : palavras) {
      if(!this.stopWords.contains(palavra) && palavra.length() > 1) {
        palavrasFiltradas.add(palavra);
      }
    }

    return palavrasFiltradas;
  }

  public Map<String, Integer> computarFrequenciaDasPalavras(ArrayList<String> palavras) {
    Map<String, Integer> frequenciaPalavras = new HashMap<>();
  
    for (String palavra : palavras) {
      if (frequenciaPalavras.containsKey(palavra)) {
        Integer frequencia = frequenciaPalavras.get(palavra);

        frequencia += 1;
        frequenciaPalavras.put(palavra, frequencia);
      } else {
        frequenciaPalavras.put(palavra, 1);
      }
    }

    return frequenciaPalavras;
  }

  public static boolean downloadStopWords(String diretorio) throws IOException {
    try {
      Document html = Jsoup.connect("https://virtuati.com.br/cliente/knowledgebase/25/Lista-de-StopWords.html").get();

      Element stop = html.selectFirst("em");
      String[] stopWords = stop.text().trim().split(", ");
  
      BufferedWriter writer = new BufferedWriter(new FileWriter(diretorio, true));
      
      for(String word : stopWords) {
        writer.append(word + "\n");
      }
  
      writer.close();

      return true;
    } catch (IOException e) {
      return false;
    }
  }
}
