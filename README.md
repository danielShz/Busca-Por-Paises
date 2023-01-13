## 📑 Descrição do projeto

A partir de páginas da Wikipedia em português, o sistema extrai — utilizando a biblioteca [JSoup](https://jsoup.org/) — as seguintes informações sobre os [países integrantes da ONU](https://pt.wikipedia.org/wiki/Estados-membros_das_Na%C3%A7%C3%B5es_Unidas):

1. Nome oficial
2. Capital
3. Língua(s) oficial(is)
4. Religião oficial
5. Moeda
6. Área total
7. IDH
8. Texto de descrição sobre o país

Após essa extração, o usuário pode realizar uma busca por alguma palavra-chave, onde cada país receberá uma pontuação com base na ocorrência da palavra-chave fornecida. Diante disso, o sistema apresenta uma lista de países ordenados em ordem decrescente de acordo com a pontuação final.

> 💡 Com o intuito de melhorar qualidade da busca, o código não é sensível a letras maiúsculas e minúsculas, e ignora as [stop words da língua portuguesa](https://virtuati.com.br/cliente/knowledgebase/25/Lista-de-StopWords.html).

## 🧱 Construção da pontuação

A pontuação é formada por duas regras. Na primeira regra, o país pontua +1/i, onde i é a posição do campo na lista acima, cada vez que a palavra aparecer nos campos 1, 2, 3, 4 ou 5 da lista acima. Por exemplo, se a palavra-chave aparecer no campo Capital ou no campo Moeda, o registro do país pontua respectivamente +1⁄2 ou +1⁄5. Na segunda regra, o país pontua +0,1, cada vez que a palavra-chave aparecer no campo 8.