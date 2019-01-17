import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GistFunctionalTest {

    private static ChromeDriverService service;
    private WebDriver driver;
    //    public static final String GITHUB_USERNAME = System.getProperty("username");
//    public static final String GITHUB_PASSWORD = System.getProperty("password");
    public static final String GITHUB_USERNAME = "lucraise";
    public static final String GITHUB_PASSWORD = "Roxas13!";


    public void login() {
        driver.get("https://github.com/login");
        WebElement usernameField = driver.findElement(By.id("login_field"));
        WebElement passwordField = driver.findElement(By.id("password"));
        usernameField.sendKeys(GITHUB_USERNAME);
        passwordField.sendKeys(GITHUB_PASSWORD);
        passwordField.submit();
    }


    @BeforeClass
    public static void createAndStartService() throws IOException {
        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File("/home/leoandika/Projects/gisttestexample/chromedriver"))
                .usingAnyFreePort()
                .build();
        service.start();
    }

    @AfterClass
    public static void createAndStopService() {
        service.stop();
    }

    @Before
    public void createDriverAndLoginToGithub() {
        driver = new RemoteWebDriver(service.getUrl(),
                DesiredCapabilities.chrome());
        login();
    }

    @After
    public void quitDriver() {
        driver.quit();
    }

    @Test
    public void testCreateNewGist_ValidGist_NewGistShouldBeCreated() {
        driver.get("https://gist.github.com");
        assertEquals("Create a new Gist", driver.getTitle());

        String expectedDescription = "Description of Test Gist";
        String expectedFilename = "testGist.txt";
        String expectedContent = "Hello Gist!";

        WebElement gistDescription = driver.findElement(By.name("gist[description]"));
        WebElement gistFilename = driver.findElement(By.name("gist[contents][][name]"));
        WebElement gistContent = driver.findElement(By.className("CodeMirror-code"));
        WebElement createPublicGistButton = driver.findElement(By.name("gist[public]"));

        gistDescription.sendKeys(expectedDescription);
        gistFilename.sendKeys(expectedFilename);
        gistContent.sendKeys(expectedContent);
        createPublicGistButton.submit();

        String actualFilename = driver.findElement(By.className("gist-blob-name")).getText();
        assertEquals(expectedFilename, actualFilename);

        String actualDescription = driver.findElement(By.xpath("//div[@itemprop='about']")).getText();
        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testViewGistList_ShouldReturnAllGist() {

    }

}
