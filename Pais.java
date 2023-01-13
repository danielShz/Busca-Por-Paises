package buscapaises;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pais implements Serializable {
  private String nome;
  private String idh;
  private String areaTotal;
  private Map<String, Integer> nomeOficial;
  private Map<String, Integer> capital;
  private Map<String, Integer> linguas;
  private Map<String, Integer> religiao;
  private Map<String, Integer> moeda;
  public Map<String, Integer> descricao;
  
  private float pontuacao;

  private transient TratamentoDeTexto tratamentoDeTexto;

  public Pais(String nome, String wikipediaUrl, TratamentoDeTexto tratamentoDeTexto) throws IOException {
    this.tratamentoDeTexto = tratamentoDeTexto;

    this.nome = nome;

    Document htmlDocument = Jsoup.connect(wikipediaUrl).get();

    this.nomeOficial = buscarNome(htmlDocument);
    this.capital = buscarCapital(htmlDocument);
    this.linguas = buscarLingua(htmlDocument);
    this.religiao = buscarReligiao(htmlDocument);
    this.areaTotal = buscarAreaTotal(htmlDocument);
    this.moeda = buscarMoeda(htmlDocument);
    this.idh = buscarIdh(htmlDocument);

    this.descricao = buscarDescricao(htmlDocument);
  }
  
  private Map<String, Integer> buscarNome(Document htmlDocument) throws NullPointerException {
    try {
      Element nomeElement = htmlDocument.selectFirst(".mw-parser-output tbody:first-of-type tr:first-child td:first-child b:first-of-type");
  
      String nomeTexto = tratamentoDeTexto.limparTexto(nomeElement.text());
      ArrayList<String> palavras = tratamentoDeTexto.quebrarTextoEmPalavras(nomeTexto);
      
      palavras = tratamentoDeTexto.removerStopWords(palavras);
  
      return tratamentoDeTexto.computarFrequenciaDasPalavras(palavras);
    } catch (NullPointerException e) {
      e.printStackTrace();

      return new HashMap<String, Integer>();
    }
  }

  private Map<String, Integer> buscarCapital(Document htmlDocument) throws NullPointerException {
    try {
      Element capitalElement = htmlDocument.selectFirst(".mw-parser-output tbody:first-of-type tr td:first-child a[title=Capital]").parent().parent().nextElementSibling();
      String capitalTexto = tratamentoDeTexto.limparTexto(capitalElement.selectFirst("a:first-of-type").text());
      ArrayList<String> palavras = tratamentoDeTexto.quebrarTextoEmPalavras(capitalTexto);
      
      palavras = tratamentoDeTexto.removerStopWords(palavras);
  
      return tratamentoDeTexto.computarFrequenciaDasPalavras(palavras);
    } catch (NullPointerException e) {
      e.printStackTrace();

      return new HashMap<String, Integer>();
    }
  }

  private Map<String, Integer> buscarLingua(Document htmlDocument) throws NullPointerException {
    try {
      Element linguaElement = htmlDocument.selectFirst(".mw-parser-output tbody:first-of-type tr td:first-child a[title=Língua oficial]").parent().parent().nextElementSibling();
      String linguaTexto = tratamentoDeTexto.limparTexto(
        linguaElement.select("a").size() > 0 ? linguaElement.select("a").text() : linguaElement.text() 
      );

      ArrayList<String> palavras = tratamentoDeTexto.quebrarTextoEmPalavras(linguaTexto);
  
      palavras = tratamentoDeTexto.removerStopWords(palavras);
  
      return tratamentoDeTexto.computarFrequenciaDasPalavras(palavras);
      
    } catch (NullPointerException e) {
      e.printStackTrace();

      return new HashMap<String, Integer>();
    }
  }

  private Map<String, Integer> buscarReligiao(Document htmlDocument) throws NullPointerException {
    try {
      Element religiaoElement = htmlDocument.selectFirst(".mw-parser-output tbody:first-of-type tr td:first-child a[title=Religião oficial]");
  
      if(religiaoElement != null) {
        String religiaoTexto = tratamentoDeTexto.limparTexto(religiaoElement.parent().parent().nextElementSibling().select("a").text());
        ArrayList<String> palavras = tratamentoDeTexto.quebrarTextoEmPalavras(religiaoTexto);
      
        palavras = tratamentoDeTexto.removerStopWords(palavras);
    
        return tratamentoDeTexto.computarFrequenciaDasPalavras(palavras);
      }
  
      return null;
    } catch(NullPointerException e) {
      e.printStackTrace();

      return null;
    }
  }

  private String buscarAreaTotal(Document htmlDocument) throws NullPointerException {
    try {
      Element areaElement = htmlDocument.selectFirst(".mw-parser-output tbody:first-of-type tr td:first-child a[title=Lista de países e territórios por área]").parent().parent().parent().nextElementSibling();
  
      String areaTexto = areaElement.select("td:nth-child(2)").text();
      areaTexto = areaTexto.replaceAll("\\[(.*?)\\]", " ");
      areaTexto = areaTexto.split("\\W*( km²)\\W*")[0].replaceAll("[^\\d.,/\\s]", "").trim() + " km²";
      areaTexto = areaTexto.replaceAll("(?<=\\d)\\s(?=\\d)", ".");
      
      return areaTexto; 
    } catch(NullPointerException e) {
      e.printStackTrace();

      return null;
    }
  }

  private Map<String, Integer> buscarMoeda(Document htmlDocument) throws NullPointerException {
    try {
      Element moedaElement = htmlDocument.selectFirst(".mw-parser-output tbody:first-of-type tr td:first-child a[title=Moeda]").parent().parent().nextElementSibling();
      
      if(moedaElement.select("a").size() > 0) {
        String moedaTexto = tratamentoDeTexto.limparTexto(
          moedaElement.select("a").text().split("[(]")[0]
        );
  
        ArrayList<String> palavras = tratamentoDeTexto.quebrarTextoEmPalavras(moedaTexto);
        
        palavras = tratamentoDeTexto.removerStopWords(palavras);
    
        return tratamentoDeTexto.computarFrequenciaDasPalavras(palavras);
      }

      return new HashMap<String, Integer>();
    } catch(NullPointerException e) {
      e.printStackTrace();

      return new HashMap<String, Integer>();
    }
  }

  private String buscarIdh(Document htmlDocument) throws NullPointerException {
    try {
      Element idhElement = htmlDocument.selectFirst(".mw-parser-output tbody:first-of-type tr td:first-child a[title=Índice de Desenvolvimento Humano]").parent().parent().nextElementSibling();
  
      String idhTexto = idhElement.text().split(" ")[0];
  
      return idhTexto;
    } catch (NullPointerException e) {
      return null;
    }
  }

  private Map<String, Integer> buscarDescricao(Document htmlDocument) throws NullPointerException {
    try {
      Elements paragrafos = htmlDocument.select("p");
      String descricao = "";
  
      for(Element p : paragrafos) {
        descricao += p.text();
      }
      
      descricao = tratamentoDeTexto.limparTexto(descricao);
  
      ArrayList<String> palavras = tratamentoDeTexto.quebrarTextoEmPalavras(descricao);
      palavras = tratamentoDeTexto.removerStopWords(palavras);
  
      return tratamentoDeTexto.computarFrequenciaDasPalavras(palavras);
    } catch (NullPointerException e) {
      e.printStackTrace();

      return null;
    }
  }

   public float gerarPontuacao(String palavraChave) {
    int campoAtributo = 0;

    ArrayList<Map<String,Integer>> listaAtributos = new ArrayList<>();

    if(this.nomeOficial != null) listaAtributos.add(this.nomeOficial);
    if(this.capital != null) listaAtributos.add(this.capital);
    if(this.linguas != null) listaAtributos.add(this.linguas);
    if(this.religiao != null) listaAtributos.add(this.religiao);
    if(this.moeda != null) listaAtributos.add(this.moeda);

    for(Map<String,Integer> atributo : listaAtributos) {
       if(atributo.equals(this.nomeOficial))
         campoAtributo = 1;

       if(atributo.equals(this.capital))
         campoAtributo = 2;

       if(atributo.equals(this.linguas))
         campoAtributo = 3;

       if(atributo.equals(this.religiao))
         campoAtributo = 4;

       if(atributo.equals(this.moeda))
         campoAtributo = 5;

       if(atributo.containsKey(palavraChave))
         this.pontuacao += (1.0/campoAtributo)*atributo.get(palavraChave);
     }
     
     if(this.descricao.containsKey(palavraChave))
       this.pontuacao += 0.1*this.descricao.get(palavraChave);

     return this.pontuacao;
   }

  public String getNome() {
    return this.nome;
  }

  public static Map<String, String> buscarPaisesDaOnu() throws IOException, NullPointerException {
    try {
      Document html = Jsoup.connect("https://pt.wikipedia.org/wiki/Estados-membros_das_Na%C3%A7%C3%B5es_Unidas").get();
  
      Elements letterCountries = html.select("h3 ~ ul");
  
      Map<String, String> countries = new HashMap<String, String>();
  
      for (int i = 0; i < 23; i++) {
          for(Element line : letterCountries.get(i).select("li a")) {
              if(!line.text().matches(".*\\d+.*") && !line.absUrl("href").contains(".svg") && line.hasAttr("title") && line.text().length() > 0) {
                  countries.put(line.text(), line.absUrl("href"));
              }
          }
      }
  
      return countries; 
    } catch (IOException e) {
      e.printStackTrace();

      return null;
    } catch (NullPointerException e) {
      e.printStackTrace();

      return null;
    }
  }

  public static boolean salvarPais(Pais pais, String diretorio) throws IOException {
    try {
      File file = new File(diretorio);
      File parent = file.getParentFile();

      if(!parent.exists())
        parent.mkdir();

      file.createNewFile();

      FileOutputStream fileOut = new FileOutputStream(diretorio);
      ObjectOutputStream out = new ObjectOutputStream(fileOut);

      out.writeObject(pais);
      
      out.close();
      fileOut.close();

      return true;
    } catch (IOException e) {
      e.printStackTrace();

      return false;
    }
  }

  public static Pais lerPais(String diretorio) throws IOException, ClassNotFoundException {
    try {
      FileInputStream file = new FileInputStream(diretorio);
      ObjectInputStream inputStream = new ObjectInputStream(file);

      Pais pais = (Pais) inputStream.readObject();

      inputStream.close();
      file.close();

      return pais;
    } catch (IOException e) {
      e.printStackTrace();

      return null;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();

      return null;
    }
  }
}
