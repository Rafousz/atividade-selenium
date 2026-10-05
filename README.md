# Repositório com os exemplos usados na aula sobre Selenium

Sequência das branches

1. aula_google
2. testa_busca
3. testa_usuario
4. testa_usuario_po

## Executar o teste

Requisitos: JDK 11 ou superior, Maven e Google Chrome instalado. O `JAVA_HOME`
deve apontar para o JDK. Confira o Java utilizado pelo Maven com `mvn -version`.
O Selenium Manager resolve o ChromeDriver automaticamente; a primeira execução
precisa de acesso à internet para baixar as dependências e o driver.

Na pasta do projeto, execute:

```powershell
mvn clean test
```

O teste abre o Google, verifica o título e o campo de busca, digita `Selenium`
e fecha o navegador ao terminar. O resultado esperado é `Tests run: 1,
Failures: 0, Errors: 0, Skipped: 0` e `BUILD SUCCESS`.

Para executar sem mostrar a janela do Chrome:

```powershell
mvn test -Dheadless=true
```

Os relatórios ficam em `target/surefire-reports`. Se houver erro em classes
compiladas anteriormente pela IDE, use `mvn clean test` para recompilar.
A configuração `.mvn/jvm.config` usa TLS 1.2 para evitar a falha de download
`No PSK available. Unable to resume.` observada com o JDK 11.0.2 instalado.
O teste depende de acesso ao Google; indisponibilidade, CAPTCHA ou mudanças
na página podem causar falha.
