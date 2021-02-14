import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CheckPricingCalculationResultTest {
    private WebDriver driver;

    @BeforeMethod (alwaysRun = true)
    public void browserSetup(){

        driver = new ChromeDriver();
    }

    @Test (description = "Verifying result in Estimate Result table")
    public void checkResultTable () {

        driver.manage().window().maximize();

        driver.get("https://cloud.google.com/");

        //Clicking the search button on the portal at the top of the page
        WebElement searchButton = driver
                .findElement(By.xpath("/html/body/section/devsite-header/div/div[1]/div/div/div[2]/devsite-search/form/div[1]/div/input"));
        searchButton.click();

        //Search the search text box and start a search on the "Google Cloud Platform Pricing Calculator"
        WebElement searchInput = driver.findElement(By.xpath("//input[@name='q']"));
        searchInput.sendKeys("Google Cloud Platform Pricing Calculator" + "\n");

        //Go to the calculator page
        WebElement searchResult = waitForElementLocatedBy(driver, By.linkText("Google Cloud Platform Pricing Calculator"));
        searchResult.click();

        // Switch iframes
        driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"cloud-site\"]/devsite-iframe/iframe")));
        driver.switchTo().frame(driver.findElement(By.xpath("//*[@id='myFrame']")));

        //Activate the COMPUTE ENGINE section at the top of the page
        driver.findElement(By.xpath("//*[@id=\"mainForm\"]/md-tabs/md-tabs-wrapper/md-tabs-canvas/md-pagination-wrapper/md-tab-item[1]")).click();
        // Enter number of instances:4
        driver.findElement(By.id("input_63")).sendKeys("4");

        //Clear Instances text field
        driver.findElement(By.id("input_64")).clear();

        //Select Operating System / Software: Free: Debian, CentOS, CoreOS, Ubuntu, or other User Provided OS
        openDropDown(driver, By.id("select_76"));
        WebElement operationSystem = waitForElementToBeClickable(driver, By.id("select_option_65"));
        operationSystem.click();

        //Select VM Class: Regular
        openDropDown(driver, By.id("select_80"));
        WebElement machineClass = waitForElementToBeClickable(driver, By.id("select_option_78"));
        machineClass.click();

        //Select Series n1
        openDropDown(driver, By.id("select_88"));
        WebElement series = waitForElementToBeClickable(driver, By.id("select_option_188"));
        series.click();

        //Select Instance type: n1-standard-8    (vCPUs: 8, RAM: 30 GB)
        openDropDown(driver, By.id("select_value_label_60"));
        WebElement machineType = waitForElementToBeClickable(driver, By.id("select_option_360"));
        machineType.click();

        // Check Add GPUs check-box
        WebElement addCPUCheckBox = driver.findElement(By.xpath("//*[@id=\"mainForm\"]/div[2]/div/md-card/md-card-content/div/div[1]/form/div[8]/div[1]/md-input-container/md-checkbox/div[1]"));
        if (!addCPUCheckBox.isSelected()) {
            addCPUCheckBox.click();
        }

        //Select Number of GPUs: 1
        openDropDown(driver, By.id("select_394"));
        WebElement numberOfCPU = waitForElementToBeClickable(driver, By.id("select_option_399"));
        numberOfCPU.click();

        //Select GPU type: NVIDIA Tesla V100
        openDropDown(driver, By.id("select_396"));
        WebElement typeOfCPU = waitForElementToBeClickable(driver, By.id("select_option_406"));
        typeOfCPU.click();


        // Select Local SSD: 2x375 Gb
        openDropDown(driver, By.id("select_value_label_354"));
        WebElement localSSD = waitForElementToBeClickable(driver, By.id("select_option_381"));
        localSSD.click();


        //Select Frankfurt (europe-west3) region of location
        openDropDown(driver, By.id("select_92"));
        WebElement dataCenterLocation = waitForElementToBeClickable(driver, By.id("select_option_205"));
        dataCenterLocation.click();


        //Select 1 Year commited usage
        openDropDown(driver, By.id("select_99"));
        WebElement committedUsage = waitForElementToBeClickable(driver, By.id("select_option_97"));
        committedUsage.click();

        //Click to Add to Estimate button
        List<WebElement> addToEstimatebuttons = driver.findElements(By.xpath("//button[@class='md-raised md-primary cpc-button md-button md-ink-ripple']"));
        addToEstimatebuttons.get(0).click();

        //Check VM Class in Result table is regular
        String vmClassResultTable = driver.findElement(By.xpath("//*[@id=\"compute\"]/md-list/md-list-item[2]/div")).getText();
        Assert.assertTrue(vmClassResultTable.contains("regular"),"VM class in result table is NOT regular");

        //Check Instance type in Result table is n1-standard-8
        String instanceTypeResultTable = driver.findElement(By.xpath("//*[@id=\"compute\"]/md-list/md-list-item[3]/div")).getText();
        Assert.assertTrue(instanceTypeResultTable.contains("n1-standard-8"),"Instance type in result table is NOT n1-standard-8");

        //Check Region in Result table is Frankfurt
        String regionResultTable = driver.findElement(By.xpath("//*[@id=\"compute\"]/md-list/md-list-item[4]/div")).getText();
        Assert.assertTrue(regionResultTable.contains("Frankfurt"),"Region in result table is NOT Frankfurt");

        //Check local SSD in Result table is 2x375 GiB
        String localSSDResultTable = driver.findElement(By.xpath("//*[@id=\"compute\"]/md-list/md-list-item[5]/div")).getText();
        Assert.assertTrue(localSSDResultTable.contains("2x375 GiB"),"Local SSD in result table is NOT 2x375 GiB");

        // Check commitment term in Result table is 1year
        String commitmentTermResultTable = driver.findElement(By.xpath("//*[@id=\"compute\"]/md-list/md-list-item[6]/div")).getText();
        Assert.assertTrue(commitmentTermResultTable.contains("1 Year"),"Commitment term in result table is NOT 1 Year");

        //Check that the rental amount per month matches the amount received when manually passing the test
        String resultEstimation = driver.findElement(By.xpath("//*[@id=\"resultBlock\"]/md-card/md-card-content/div/div/div/h2/b")).getText().trim();
        Assert.assertTrue(resultEstimation.contains("USD 1,082.77"),"Result is NOT equal 1,082.77 per 1 month");

        //Switch to default content
        driver.switchTo().defaultContent();
    }

        @AfterMethod (alwaysRun = true)
        public void closeCreatedBrowserInstance() {
            driver.quit();
            driver=null;
    }
    private  static  WebElement waitForElementLocatedBy (WebDriver driver, By by){
        return new WebDriverWait(driver, 10)
                .until(ExpectedConditions
                        .presenceOfElementLocated(by));
    }

    private static  WebElement waitForElementToBeClickable (WebDriver driver,By by){
        return  new WebDriverWait(driver,10)
                .until(ExpectedConditions.elementToBeClickable(by));
    }
    private static void openDropDown (WebDriver driver,By by){
        driver.findElement(by).click();
    }
     }

