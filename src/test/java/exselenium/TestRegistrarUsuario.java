package exselenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestRegistrarUsuario {

    private WebDriver driver;
    private WebDriverWait wait;
    private boolean cadastroEnviado;
    private boolean contaExcluida;

    @BeforeEach
    public void abrirNavegador() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    @DisplayName("Teste 1: dados validos e campos preenchidos")
    public void deveRegistrarEExcluirUsuarioComDadosValidos() {
        acessarTelaCadastro();
        String email = gerarEmail();
        iniciarCadastro("Aluno Selenium", email);
        preencherInformacoes("Aluno Selenium", email, "Cadastro123!", "15", "6");
        criarVerificarEExcluirConta("Aluno Selenium");
    }

    @Test
    @DisplayName("Teste 2: nome e senha com 1 caractere")
    public void deveRegistrarNosLimitesDeUmCaractere() {
        acessarTelaCadastro();
        String email = gerarEmail();
        iniciarCadastro("A", email);
        preencherInformacoes("A", email, "a", "1", "1");
        criarVerificarEExcluirConta("A");
    }

    @Test
    @DisplayName("Teste 3: nome vazio (0 caracteres)")
    public void deveBloquearCadastroComNomeVazio() {
        acessarTelaCadastro();
        preencherInicio("", gerarEmail());
        clicar(By.cssSelector("[data-qa='signup-button']"));
        verificarCampoInvalido(By.cssSelector("[data-qa='signup-name']"), "");
        assertEquals("https://automationexercise.com/login", driver.getCurrentUrl());
        verificarTextoVisivel(By.cssSelector(".signup-form h2"), "New User Signup!");
        assertEquals(0, driver.findElements(By.id("password")).size());
    }

    @Test
    @DisplayName("Teste 4: e-mail malformado, sem arroba")
    public void deveBloquearCadastroComEmailMalformado() {
        acessarTelaCadastro();
        preencherInicio("Aluno Selenium", "aluno.selenium.example.com");
        clicar(By.cssSelector("[data-qa='signup-button']"));
        verificarCampoInvalido(By.cssSelector("[data-qa='signup-email']"), "aluno.selenium.example.com");
        assertEquals("https://automationexercise.com/login", driver.getCurrentUrl());
        verificarTextoVisivel(By.cssSelector(".signup-form h2"), "New User Signup!");
        assertEquals(0, driver.findElements(By.id("password")).size());
    }

    @Test
    @DisplayName("Teste 5: senha vazia (0 caracteres)")
    public void deveBloquearCriacaoDeContaComSenhaVazia() {
        acessarTelaCadastro();
        String email = gerarEmail();
        iniciarCadastro("Aluno Selenium", email);
        preencherInformacoes("Aluno Selenium", email, "", "15", "6");
        clicar(By.cssSelector("[data-qa='create-account']"));
        verificarCampoInvalido(By.id("password"), "");
        assertEquals("https://automationexercise.com/signup", driver.getCurrentUrl());
        verificarTextoVisivel(By.cssSelector(".login-form h2 b"), "ENTER ACCOUNT INFORMATION");
        assertEquals(0, driver.findElements(By.cssSelector("[data-qa='account-created']")).size());
    }

    private void acessarTelaCadastro() {
        driver.get("https://automationexercise.com/");
        assertEquals("Automation Exercise", driver.getTitle());
        WebElement paginaInicial = driver.findElement(By.id("slider"));
        assertEquals("block", paginaInicial.getCssValue("display"));
        assertEquals("visible", paginaInicial.getCssValue("visibility"));
        clicar(By.linkText("Signup / Login"));
        verificarTextoVisivel(By.cssSelector(".signup-form h2"), "New User Signup!");
    }

    private String gerarEmail() {
        return "cadastro-aulaselenium-" + java.util.UUID.randomUUID() + "@example.com";
    }

    private void preencherInicio(String nome, String email) {
        preencherCampo(By.cssSelector("[data-qa='signup-name']"), nome);
        preencherCampo(By.cssSelector("[data-qa='signup-email']"), email);
    }

    private void iniciarCadastro(String nome, String email) {
        preencherInicio(nome, email);
        clicar(By.cssSelector("[data-qa='signup-button']"));
        verificarTextoVisivel(By.cssSelector(".login-form h2 b"), "ENTER ACCOUNT INFORMATION");
    }

    private void preencherInformacoes(String nome, String email, String senha, String dia, String mes) {
        clicar(By.id("id_gender1"));
        assertTrue(driver.findElement(By.id("id_gender1")).isSelected());
        preencherCampo(By.id("name"), nome);
        // O site preenche o e-mail na primeira etapa e desabilita sua edicao.
        assertEquals(email, driver.findElement(By.id("email")).getAttribute("value"));
        preencherCampo(By.id("password"), senha);
        new Select(driver.findElement(By.id("days"))).selectByValue(dia);
        new Select(driver.findElement(By.id("months"))).selectByValue(mes);
        new Select(driver.findElement(By.id("years"))).selectByValue("2000");
        assertEquals(dia, new Select(driver.findElement(By.id("days")))
                .getFirstSelectedOption().getAttribute("value"));
        assertEquals(mes, new Select(driver.findElement(By.id("months")))
                .getFirstSelectedOption().getAttribute("value"));
        assertEquals("2000", new Select(driver.findElement(By.id("years")))
                .getFirstSelectedOption().getAttribute("value"));

        clicar(By.id("newsletter"));
        clicar(By.id("optin"));
        assertTrue(driver.findElement(By.id("newsletter")).isSelected());
        assertTrue(driver.findElement(By.id("optin")).isSelected());

        preencherCampo(By.id("first_name"), "Aluno");
        preencherCampo(By.id("last_name"), "Selenium");
        preencherCampo(By.id("company"), "Empresa de Teste");
        preencherCampo(By.id("address1"), "Rua de Teste 123");
        preencherCampo(By.id("address2"), "Apartamento 10");
        new Select(driver.findElement(By.id("country"))).selectByVisibleText("Canada");
        preencherCampo(By.id("state"), "Ontario");
        preencherCampo(By.id("city"), "Toronto");
        preencherCampo(By.id("zipcode"), "M5V 3L9");
        preencherCampo(By.id("mobile_number"), "4165550100");
    }

    private void criarVerificarEExcluirConta(String nome) {
        cadastroEnviado = true;
        clicar(By.cssSelector("[data-qa='create-account']"));
        verificarTextoVisivel(By.cssSelector("[data-qa='account-created']"), "ACCOUNT CREATED!");
        clicar(By.cssSelector("[data-qa='continue-button']"));
        verificarTextoVisivel(By.cssSelector(".shop-menu li a b"), nome);
        assertEquals("Logged in as " + nome,
                driver.findElement(By.xpath("//a[b]")).getText());
        clicar(By.linkText("Delete Account"));
        verificarTextoVisivel(By.cssSelector("[data-qa='account-deleted']"), "ACCOUNT DELETED!");
        contaExcluida = true;
        clicar(By.cssSelector("[data-qa='continue-button']"));
        wait.until(navegador -> !navegador.findElements(By.id("slider")).isEmpty());
        assertEquals("Automation Exercise", driver.getTitle());
    }

    private void preencherCampo(By localizador, String valor) {
        WebElement campo = driver.findElement(localizador);
        campo.clear();
        if (!valor.isEmpty()) {
            campo.sendKeys(valor);
        }
    }

    private void clicar(By localizador) {
        wait.until(navegador -> {
            try {
                navegador.findElement(localizador).click();
                return true;
            } catch (ElementClickInterceptedException erro) {
                fecharAnuncio();
                return false;
            } catch (StaleElementReferenceException erro) {
                return false;
            }
        });
    }

    private void verificarTextoVisivel(By localizador, String texto) {
        WebElement elemento = wait.until(navegador -> {
            try {
                fecharAnuncio();
                WebElement encontrado = navegador.findElement(localizador);
                return texto.equals(encontrado.getText())
                        && !"none".equals(encontrado.getCssValue("display"))
                        && "visible".equals(encontrado.getCssValue("visibility")) ? encontrado : null;
            } catch (StaleElementReferenceException erro) {
                return null;
            }
        });
        assertEquals(texto, elemento.getText());
        assertFalse("none".equals(elemento.getCssValue("display")));
        assertEquals("visible", elemento.getCssValue("visibility"));
    }

    private void fecharAnuncio() {
        String janela = driver.getWindowHandle();
        for (WebElement frame : driver.findElements(By.tagName("iframe"))) {
            String estilo = frame.getAttribute("style");
            if (estilo == null || !estilo.contains("100vh")) {
                continue;
            }
            try {
                driver.switchTo().frame(frame);
                if (clicarFecharAnuncio()) {
                    return;
                }
                if (!driver.findElements(By.id("ad_iframe")).isEmpty()) {
                    driver.switchTo().frame("ad_iframe");
                    if (clicarFecharAnuncio()) {
                        return;
                    }
                }
            } catch (NoSuchFrameException | StaleElementReferenceException erro) {
                // O anuncio pode desaparecer enquanto seu frame e examinado.
            } finally {
                driver.switchTo().window(janela);
            }
        }
    }

    private boolean clicarFecharAnuncio() {
        for (WebElement fechar : driver.findElements(By.id("dismiss-button"))) {
            try {
                fechar.click();
                return true;
            } catch (ElementNotInteractableException erro) {
                // Alguns anuncios possuem um botao oculto e outro no frame interno.
                try {
                    java.nio.file.Files.writeString(java.nio.file.Path.of("target", "anuncio.html"),
                            driver.getPageSource());
                } catch (java.io.IOException falhaArquivo) {
                    erro.addSuppressed(falhaArquivo);
                }
            }
        }
        return false;
    }

    private void verificarCampoInvalido(By localizador, String valor) {
        WebElement campo = driver.findElement(localizador);
        assertEquals(valor, campo.getAttribute("value"));
        assertEquals("true", campo.getAttribute("required"));
        String mensagem = campo.getAttribute("validationMessage");
        assertNotNull(mensagem);
        assertFalse(mensagem.isEmpty(), "O navegador deve bloquear o campo invalido");
    }

    @AfterEach
    public void fecharNavegador() {
        if (driver != null) {
            try {
                if (cadastroEnviado && !contaExcluida
                        && (driver.getCurrentUrl().contains("/account_created")
                        || !driver.findElements(By.linkText("Delete Account")).isEmpty())) {
                    driver.get("https://automationexercise.com/delete_account");
                    verificarTextoVisivel(By.cssSelector("[data-qa='account-deleted']"), "ACCOUNT DELETED!");
                }
            } finally {
                driver.quit();
            }
        }
    }
}
