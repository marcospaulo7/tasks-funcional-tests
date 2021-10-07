package org.jp;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class TasksTest {

    private WebDriver acessarAplicacao() throws MalformedURLException {
        WebDriver driver = this.criarDriver();
        driver.navigate().to("http://192.168.0.3:8001/tasks");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    }

    private WebDriver criarDriver() throws MalformedURLException {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
        URL url = new URL("http://192.168.0.3:4444/wd/hub");
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        return new RemoteWebDriver(url, cap);
    }

    @Test
    public void deveSalvarTarefaComSucesso() throws MalformedURLException {
        WebDriver driver = this.acessarAplicacao();

        try {
            driver.findElement(By.id("addTodo")).click();
            driver.findElement(By.id("task")).sendKeys("Test via Selenium");
            driver.findElement(By.id("dueDate")).sendKeys("01/01/2050");
            driver.findElement(By.id("saveButton")).click();
            String message = driver.findElement(By.id("message")).getText();
            Assert.assertEquals("Success!", message);
        } finally {
            driver.quit();
        }
    }

    @Test
    public void naoDeveSalvarTarefaSemDescricao() throws MalformedURLException {
        WebDriver driver = acessarAplicacao();

        try {
            driver.findElement(By.id("addTodo")).click();
            driver.findElement(By.id("dueDate")).sendKeys("01/01/2050");
            driver.findElement(By.id("saveButton")).click();
            String message = driver.findElement(By.id("message")).getText();
            Assert.assertEquals("Fill the task description", message);
        } finally {
            driver.quit();
        }
    }

    @Test
    public void naoDeveSalvarTarefaSemData() throws MalformedURLException {
        WebDriver driver = acessarAplicacao();

        try {
            driver.findElement(By.id("addTodo")).click();
            driver.findElement(By.id("task")).sendKeys("Test via Selenium");
            driver.findElement(By.id("saveButton")).click();
            String message = driver.findElement(By.id("message")).getText();
            Assert.assertEquals("Fill the due date", message);
        } finally {
            driver.quit();
        }
    }

    @Test
    public void naoDeveSalvarTarefaComDataPassada() throws MalformedURLException {
        WebDriver driver = acessarAplicacao();

        try {
            driver.findElement(By.id("addTodo")).click();
            driver.findElement(By.id("task")).sendKeys("Test via Selenium");
            driver.findElement(By.id("dueDate")).sendKeys("01/01/2000");
            driver.findElement(By.id("saveButton")).click();
            String message = driver.findElement(By.id("message")).getText();
            Assert.assertEquals("Due date must not be in past", message);
        } finally {
            driver.quit();
        }
    }
}
