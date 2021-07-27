<p align="center">
  <img alt="GitHub language count" src="https://img.shields.io/github/languages/count/rickweb3/servidor-de-arquivos?color=%2304D361">
  <img alt="Repository size" src="https://img.shields.io/github/repo-size/rickweb3/servidor-de-arquivos">
  <a href="https://github.com/rickweb3/servidor-de-arquivos/commits/master">
    <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/rickweb3/servidor-de-arquivos">
  </a>
</p>



<h4 align="center"> 
	🚧 Servidor de Arquivos - Concluído 🚀 🚧
</h4>

<p align="center">
 <a href="#-sobre-o-projeto">Sobre</a> •
 <a href="#-features">Features</a> • 
 <a href="#-tecnologias">Tecnologias</a> • 
 <a href="#-dev">Dev's</a> 
</p>




## 💻 Sobre

O projeto Servidor de Arquivos foi desenvolvido na disciplina Sistemas Distribuídos do curso Sistemas de Informação da Universidade Federal
de Sergipe - Campus Itabaiana

---




## ⚙️ Features

O Servidor de Arquivos contempla os seguintes requisitos:

O cliente(interface gráfica) irá abrir uma conexão TCP/IP via unicast com o servidor
principal solicitando ao mesmo os nomes e endereços(IP) dos servidores de arquivos que
contem um arquivo em especifico.

Assim que o servidor principal recebe a solicitação do(s) cliente(s) o mesmo envia uma
mensagem multicast ou broadcast na rede direcionada aos servidores de arquivos
perguntando quais desses possuem o arquivo solicitado pelo cliente. Nesse momento o
servidor principal determina um timeout de 10 segundos(sem receber mensagens) para
receber a resposta dos servidores de arquivos que possuem aquele determinado arquivo.
Obs: O Servidor principal pode receber várias solicitações ao mesmo tempo de vários
clientes

Assim que o servidor de arquivo recebe uma solicitação, o mesmo verifica em seu diretório
padrão se o arquivo solicitado existe em sua base de arquivos. Caso exista envia uma
mensagem para o servidor principal informando que possui aquele determinado arquivo e
o seu nome(nome do computador) e caso não possua não irá fazer nada. Obs: O servidor
de arquivos podem receber vários pedidos de pesquisas do servidor principal ao mesmo
tempo.


O servidor principal guarda em uma coleção todos os endereços e nomes de todos os
servidores de arquivos que informaram possuir aquele arquivo e ao termino do timeout
responde ao cliente informando todos os servidores de arquivos que possuem o arquivo
solicitado.


Assim que o cliente receber a lista dos servidores de arquivos que possuem o determinado
arquivo, o usuário através da interface gráfica vai escolher da lista(que contem o nome do
servidor e ip) qual servidor que o mesmo irá baixar o arquivo. Essa conexão será realizada
através de uma conexão TCP/IP via unicast com o servidor escolhido. Obs: O servidor de
arquivo pode enviar vários arquivos ao mesmo tempo para vários clientes.

---



## 🛠 Tecnologias

As seguintes ferramentas foram usadas na construção do projeto:

#### **Java e WindowBuilder** 

---




## 🦸 Dev's
<table>
   <tr>
    	<td align="center"><a href="https://github.com/rickweb3"><img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/u/55119449" width="100px;" alt="Henrique Prado"/><br /><sub><b>Henrique Prado</b></sub>
	<td align="center"><a href="https://github.com/faelteles"><img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/u/63653883?v=4" width="100px;" alt="Rafael Antônio"/><br /><sub><b>Rafael Antônio</b></sub>
	<td align="center"><a href="https://github.com/MarcosSD"><img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/u/63070489?v=4" width="100px;" alt="Marcos Machado"/><br /><sub><b>Marcos Machado</b></sub>
   </tr>
</table>
