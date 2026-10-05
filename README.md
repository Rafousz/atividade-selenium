# Requisitos

- Java JDK 11 ou superior (versão utilizada: 11.0.2).
- Apache Maven 3.10.0 (versão utilizada).
- Selenium 4.20.0.
- JUnit Jupiter 5.6.2.
- Google Chrome instalado.
- `JAVA_HOME` configurado para o JDK e Maven disponível no `PATH`.
- Acesso à internet para acessar o site e baixar dependências e ChromeDriver.

Selenium e JUnit são baixados pelo Maven. O ChromeDriver é gerenciado pelo Selenium.

# Comandos

Execute os comandos na pasta do projeto.

Verificar o ambiente:

```powershell
mvn -version
```

Executar todos os testes:

```powershell
mvn clean test
```

Executar apenas os testes de login:

```powershell
mvn test -Dtest=TestLoginUsuario
```

Executar apenas os testes de cadastro:

```powershell
mvn test -Dtest=TestRegistrarUsuario
```
