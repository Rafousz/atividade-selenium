package exselenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestLoginUsuario {

    private WebDriver driver;

    @BeforeEach
    public void abrirNavegador() {
        driver = new ChromeDriver();
        driver.get("https://automationexercise.com/");

        assertEquals("Automation Exercise", driver.getTitle());
        WebElement paginaInicial = driver.findElement(By.id("slider"));
        assertEquals("block", paginaInicial.getCssValue("display"));
        assertEquals("visible", paginaInicial.getCssValue("visibility"));

        driver.findElement(By.linkText("Signup / Login")).click();

        WebElement tituloLogin = driver.findElement(By.cssSelector(".login-form h2"));
        assertEquals("Login to your account", tituloLogin.getText());
        assertEquals("block", tituloLogin.getCssValue("display"));
        assertEquals("visible", tituloLogin.getCssValue("visibility"));
    }

    @Test
    @DisplayName("Teste 1: e-mail nao cadastrado e senha incorreta")
    public void deveExibirErroAoFazerLoginComEmailESenhaIncorretos() {
        enviarLogin("amocoxinhadecarne@bom.com", "coxinha123!");
        verificarErroDeCredenciais();
    }

    @Test
    @DisplayName("Teste 2: e-mail nao cadastrado e senha com 1 caractere")
    public void deveExibirErroComSenhaDeUmCaractere() {
        enviarLogin("gostodeesfihadecalabresa@bom.com", "a");
        verificarErroDeCredenciais();
    }

    @Test
    @DisplayName("Teste 3: senha vazia (0 caracteres)")
    public void deveBloquearLoginComSenhaVazia() {
        enviarLogin("asvezesumpaodequeijochega@bom.com", "");
        verificarBloqueioDoFormulario("login-password", "password", "");
    }

    @Test
    @DisplayName("Teste 4: e-mail vazio")
    public void deveBloquearLoginComEmailVazio() {
        enviarLogin("", "picanha123!");
        verificarBloqueioDoFormulario("login-email", "email", "");
    }

    @Test
    @DisplayName("Teste 5: e-mail sem arroba")
    public void deveBloquearLoginComEmailMalformado() {
        enviarLogin("pizzadecalabresa.bom.com", "pizza123!");
        verificarBloqueioDoFormulario("login-email", "email", "pizzadecalabresa.bom.com");
    }

    private void enviarLogin(String email, String senha) {
        WebElement campoEmail = driver.findElement(By.cssSelector("[data-qa='login-email']"));
        campoEmail.clear();
        if (!email.isEmpty()) {
            campoEmail.sendKeys(email);
        }
        WebElement campoSenha = driver.findElement(By.cssSelector("[data-qa='login-password']"));
        campoSenha.clear();
        if (!senha.isEmpty()) {
            campoSenha.sendKeys(senha);
        }
        driver.findElement(By.cssSelector("[data-qa='login-button']")).click();
    }

    private void verificarErroDeCredenciais() {
        WebElement mensagemErro = driver.findElement(By.cssSelector(".login-form p"));
        assertEquals("Your email or password is incorrect!", mensagemErro.getText());
        assertEquals("block", mensagemErro.getCssValue("display"));
        assertEquals("visible", mensagemErro.getCssValue("visibility"));
    }

    private void verificarBloqueioDoFormulario(String dataQa, String tipo, String valor) {
        WebElement campo = driver.findElement(By.cssSelector("[data-qa='" + dataQa + "']"));
        assertEquals(tipo, campo.getAttribute("type"));
        assertEquals("true", campo.getAttribute("required"));
        assertEquals(valor, campo.getAttribute("value"));

        String mensagemValidacao = campo.getAttribute("validationMessage");
        assertNotNull(mensagemValidacao, "O navegador deve informar a validacao do campo");
        assertFalse(mensagemValidacao.isEmpty(), "O campo invalido deve ter mensagem de validacao");
        assertEquals("https://automationexercise.com/login", driver.getCurrentUrl());
        assertEquals("Login to your account",
                driver.findElement(By.cssSelector(".login-form h2")).getText());
        assertEquals(0, driver.findElements(By.cssSelector(".login-form p")).size(),
                "O formulario bloqueado nao deve mostrar o erro de credenciais do servidor");
    }

    @AfterEach
    public void fecharNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }
}
