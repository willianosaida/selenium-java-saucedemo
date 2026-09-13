# Automação Web com Java, Selenium e JUnit

Projeto completo de testes automatizados para o site público de demonstração
[SauceDemo](https://www.saucedemo.com/). Ele foi preparado para ser aberto no
Visual Studio Code e pode ser usado como estudo ou como base para meu portfólio de QA.

## O que o projeto testa

- Login válido.
- Login com usuário/senha inválidos ou vazios (teste parametrizado).
- Bloqueio de um usuário impedido de acessar o sistema.
- Inclusão de produto no carrinho.
- Atualização da quantidade no ícone do carrinho.
- Ordenação dos produtos por preço.
- Fluxo completo de compra: login, produto, carrinho, dados do cliente e confirmação.

O SauceDemo é uma aplicação de treinamento. As credenciais usadas aqui são públicas
e aparecem na própria tela inicial do site.

## Tecnologias e práticas aplicadas

- Java 25.
- Selenium WebDriver 4.49.0.
- JUnit Jupiter 6.1.2.
- Maven.
- Page Object Model.
- Esperas explícitas com `WebDriverWait`.
- Execução em Chrome, Firefox ou Edge.
- Execução com navegador visível ou em modo headless.
- Testes parametrizados e organizados por tags `smoke` e `regression`.
- Screenshot automático quando um teste falha.
- Relatórios do Maven Surefire.
- Pipeline de integração contínua com GitHub Actions.

## Estrutura de pastas

```text
selenium-java-saucedemo/
├── .github/workflows/testes.yml       # Pipeline de CI
├── .vscode/                           # Recomendações para o VS Code
├── src/test/java/br/com/willian/automation/
│   ├── config/                        # Leitura das configurações
│   ├── driver/                        # Criação e ciclo de vida do WebDriver
│   ├── extensions/                    # Screenshot automático em falhas
│   ├── pages/                         # Page Objects
│   └── tests/                         # Casos de teste
├── src/test/resources/
│   ├── config.properties              # URL, navegador, timeout e credenciais
│   └── junit-platform.properties      # Configuração do JUnit
├── pom.xml                            # Dependências e plugins do Maven
└── README.md
```

## Pré-requisitos

Instale os itens abaixo antes de executar:

1. **JDK 25 ou superior**
   - Sugestão: [Eclipse Temurin](https://adoptium.net/temurin/releases/?version=25).
   - Escolha o JDK, e não apenas o JRE.
2. **Apache Maven 3.9 ou superior**
   - Download e instruções: [maven.apache.org/install.html](https://maven.apache.org/install.html).
3. **Visual Studio Code**
   - Download: [code.visualstudio.com](https://code.visualstudio.com/).
4. **Um navegador compatível**
   - Google Chrome, Mozilla Firefox ou Microsoft Edge.
5. **Extensões do VS Code**
   - Extension Pack for Java.
   - Maven for Java.

Ao abrir o projeto, o VS Code também exibirá essas extensões como recomendações.

### O que não é necessário baixar

Você não precisa baixar `chromedriver`, `geckodriver` ou `msedgedriver` manualmente.
O Selenium Manager, que já faz parte do Selenium, identifica o navegador e gerencia
o driver compatível. Na primeira execução, mantenha a internet ativa para que as
dependências do Maven e o driver possam ser baixados e guardados em cache.

## Conferindo a instalação no Windows

Abra um novo terminal no VS Code e execute:

```powershell
java -version
mvn -version
```

O primeiro comando deve mostrar Java 25 ou superior. O segundo deve mostrar a
versão do Maven e também o caminho do Java usado pelo Maven.

Se `java` ou `mvn` não for reconhecido, feche e reabra o VS Code depois da
instalação. Caso continue, confira as variáveis `JAVA_HOME` e `Path` do Windows.

## Abrindo o projeto no VS Code

1. Extraia o arquivo ZIP.
2. No VS Code, selecione **File > Open Folder**.
3. Abra a pasta `selenium-java-saucedemo`, que contém o arquivo `pom.xml`.
4. Se o VS Code perguntar se você confia nos autores, confirme para habilitar os
   recursos Java do workspace.
5. Aguarde o Maven baixar e indexar as dependências.

É importante abrir a pasta do projeto, e não apenas um arquivo Java isolado.

## Executando todos os testes

No terminal, dentro da pasta do projeto:

```powershell
mvn clean test
```

Por padrão, os testes abrem o Google Chrome e você consegue acompanhar as ações.

Também é possível executar pelo painel **Testing** do VS Code ou pelo botão
**Run Test** mostrado acima de cada método de teste. Para garantir que propriedades
como navegador e modo headless sejam aplicadas, o terminal Maven é a opção mais
previsível.

## Outras formas de execução

### Sem abrir a janela do navegador

```powershell
mvn clean test -Dheadless=true
```

### Em outro navegador

```powershell
mvn clean test -Dbrowser=firefox
mvn clean test -Dbrowser=edge
```

Valores aceitos: `chrome`, `firefox` e `edge`.

### Somente os testes smoke

```powershell
mvn clean test -Dgroups=smoke
```

### Somente os testes de regressão

```powershell
mvn clean test -Dgroups=regression
```

### Uma classe específica

```powershell
mvn test -Dtest=LoginTest
```

### Um método específico

```powershell
mvn test -Dtest=CheckoutTest#shouldCompletePurchaseEndToEnd
```

### Alterando mais de uma opção

```powershell
mvn clean test -Dbrowser=edge -Dheadless=true -Dgroups=smoke
```

## Configuração

As configurações padrão estão em `src/test/resources/config.properties`:

```properties
base.url=https://www.saucedemo.com/
browser=chrome
headless=false
timeout.seconds=10
window.width=1440
window.height=900
standard.username=standard_user
standard.password=secret_sauce
```

Você pode editar esse arquivo ou substituir qualquer valor pelo terminal com
`-Dnome.da.propriedade=valor`. O parâmetro do terminal tem prioridade e não altera
o arquivo. Exemplo:

```powershell
mvn test -Dtimeout.seconds=20 -Dwindow.width=1920 -Dwindow.height=1080
```

## Resultados, relatório e evidências

Depois de `mvn test`, os resultados técnicos ficam em:

```text
target/surefire-reports/
```

Para gerar também um relatório HTML a partir da última execução:

```powershell
mvn surefire-report:report-only
```

Abra este arquivo no navegador:

```text
target/reports/relatorio-testes.html
```

Quando um teste falha, a extensão do projeto tira um screenshot antes de fechar
o navegador. As imagens ficam em:

```text
target/screenshots/
```

A pasta `target` é gerada automaticamente e não deve ser versionada no Git.

## Entendendo a arquitetura

### `TestConfig`

Centraliza as configurações. Primeiro procura uma propriedade enviada no comando
Maven e, se não encontrar, usa o valor de `config.properties`.

### `DriverFactory` e `DriverManager`

`DriverFactory` cria o navegador solicitado e aplica suas opções. `DriverManager`
guarda o driver da execução atual e garante que ele seja encerrado após o teste.

### Page Objects

As classes em `pages` concentram os localizadores e as ações de cada tela. Os
testes descrevem o comportamento esperado sem repetir detalhes do Selenium.

### `BaseTest`

Abre a página antes de cada teste e encerra o navegador depois. Cada método de
teste recebe uma sessão nova, o que evita que um cenário dependa de outro.

### Screenshot de falha

`ScreenshotOnFailureExtension` usa o mecanismo de extensões do JUnit. Se o método
falhar, ela salva a tela em `target/screenshots` antes do encerramento do driver.

## Integração contínua

O arquivo `.github/workflows/testes.yml` executa os testes automaticamente em
Chrome headless quando houver push ou pull request nas branches `main` ou `master`.
Também permite início manual pela aba **Actions** do GitHub.

Mesmo em caso de falha, o workflow publica os relatórios e screenshots como um
artefato chamado `evidencias-testes`.

## Solução de problemas

### `mvn` não é reconhecido

O Maven não está instalado ou sua pasta `bin` não está no `Path`. Refaça a etapa
de instalação e abra um terminal novo.

### `JAVA_HOME is not defined correctly`

Configure `JAVA_HOME` apontando para a pasta do JDK, por exemplo:

```text
C:\Program Files\Eclipse Adoptium\jdk-25...
```

Depois, inclua `%JAVA_HOME%\bin` no `Path`.

### O navegador abre e fecha muito rápido

Esse é o comportamento normal de um teste automatizado. Consulte o resultado no
terminal. Para depurar no VS Code, coloque um breakpoint no teste e escolha
**Debug Test**. Evite adicionar `Thread.sleep`, pois isso deixa a suíte lenta e
instável.

### Falha ao obter o driver

Verifique a conexão com a internet, proxy corporativo, antivírus e firewall. O
Selenium Manager precisa consultar e baixar o driver na primeira execução. Atualize
o navegador e tente novamente.

### O teste falhou porque o site não abriu

Confirme se [https://www.saucedemo.com/](https://www.saucedemo.com/) abre no mesmo
computador. Como o sistema testado é externo, indisponibilidade ou mudanças feitas
pelo proprietário do site podem afetar os testes.

### O VS Code ainda mostra erros depois do Maven baixar tudo

Abra a paleta de comandos com `Ctrl+Shift+P`, execute **Java: Clean Java Language
Server Workspace** e reinicie o VS Code quando solicitado.

## Como evoluir o projeto

Boas próximas práticas para estudo:

- Criar testes de remoção de produtos do carrinho.
- Validar campos obrigatórios do checkout.
- Adicionar execução paralela com isolamento por `ThreadLocal`.
- Executar em Selenium Grid ou em uma plataforma de browsers na nuvem.
- Publicar o projeto em um repositório GitHub e exibir o resultado do pipeline.

## Observação

Este projeto foi criado para fins educacionais e de portfólio. O sistema testado
pertence aos seus respectivos responsáveis, e sua disponibilidade não faz parte
deste repositório.
