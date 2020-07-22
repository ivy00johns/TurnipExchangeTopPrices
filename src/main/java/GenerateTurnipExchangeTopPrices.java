import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.*;

import static java.lang.Integer.*;

public class GenerateTurnipExchangeTopPrices {
    protected static final String TURNIP_EXCHANGE_URL        = "https://turnip.exchange/";
    protected static final Integer MIN_TURNIP_PRICE          = 600;

    protected static final String RESOURCES_PATH             = "/src/main/resources/";
    protected static final String CHROMEDRIVER_ZIP_FILE_NAME = "chromedriver_mac64_83.0.4103.39.zip";
    protected static final String SELENIUM_ZIP_FILE_NAME     = "selenium-server-standalone-3.141.59.jar.zip";

    protected static String CURRENT_WORKING_DIRECTORY;
    protected static String RESOURCES_CHROME_DRIVER_PATH;
    protected static String RESOURCES_SELENIUM_SERVER_STANDALONE_PATH;

    /**
     * Prints out the provided value using System.out.println().
     *
     * @param value - Print value
     */
    protected static void print(String value) {
        System.out.println(value);
    }

    /**
     * Disable the ChromeDriver and other warnings that are not relevant to the Test Suites.
     *
     * - Usage: DisableWarnings()
     * - Result: N/A
     */
    public static void DisableWarnings() {
        System.err.close();
        System.setErr(System.out);
    }

    /**
     * Generate the Full File Path to the necessary resources.
     *
     * - Usage: SetupFilePaths()
     * - Result: N/A
     */
    public static void SetupFilePaths() {
        CURRENT_WORKING_DIRECTORY                 = System.getProperty("user.dir");
        RESOURCES_CHROME_DRIVER_PATH              = CURRENT_WORKING_DIRECTORY + RESOURCES_PATH + CHROMEDRIVER_ZIP_FILE_NAME;
        RESOURCES_SELENIUM_SERVER_STANDALONE_PATH = CURRENT_WORKING_DIRECTORY + RESOURCES_PATH + SELENIUM_ZIP_FILE_NAME;
    }

    /**
     * Unzip the file at the provided FilePath. Export it to the provided DestinationPath.
     *
     * - Usage: UnzipFile(RESOURCES_CHROME_DRIVER_PATH, RESOURCES_PATH)
     * - Result: Files
     *
     * @param FilePath - Provided File Path
     * @param DestinationPath - Provided Destination Path
     */
    public static void UnzipFile(String FilePath, String DestinationPath) {
        File newFile;

        byte[] buffer = new byte[1024];

        try {
            // Create output directory is not exists.
            File folder = new File(DestinationPath);

            if(!folder.exists()){
                folder.mkdir();
            }

            // Get the zip file content.
            ZipInputStream zis = new ZipInputStream(new FileInputStream(FilePath));

            // Get the zipped file list entry.
            ZipEntry ze = zis.getNextEntry();

            while(ze != null){
                String fileName = ze.getName();

                newFile = new File(DestinationPath + File.separator + fileName);

                System.out.println("Unzip - " + newFile.getAbsoluteFile() + ": SUCCESS");

                // Create all non-exists folders.
                // Else you will hit FileNotFoundException for compressed folder.
                //new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
        } catch (IOException e) {}
    }

    /**
     * Unzip the necessary files. Start Selenium Standalone Server with ChromeDriver.
     *
     * - Usage: SetupAndStartSeleniumServerStandalone()
     * - Result: N/A
     *
     * @throws IOException -
     */
    public static void SetupAndStartSeleniumServerStandalone() throws IOException {
        print("--------------- Setup Selenium Server Standalone -----------\n");

        print("----------- Unzipping Selenium Server Standalone -----------");
        UnzipFile(RESOURCES_SELENIUM_SERVER_STANDALONE_PATH, CURRENT_WORKING_DIRECTORY + "/target/selenium");
        print("-------- Unzip Selenium Server Standalone: SUCCESS ---------");

        print("\n------------------- Unzipping ChromeDriver -----------------");
        UnzipFile(RESOURCES_CHROME_DRIVER_PATH, CURRENT_WORKING_DIRECTORY + "/target/selenium");
        print("---------------- Unzip ChromeDriver: SUCCESS ---------------\n");

        print("\n------------ Starting Selenium Server Standalone -----------");
        ProcessBuilder pb = new ProcessBuilder("/usr/bin/java", "-jar", "/target/selenium/selenium-server-standalone-3.141.59.jar");
        pb.start();

        print("------------- Selenium Server Standalone: RUNNING ----------\n");
    }

    public static ArrayList<Integer> getIntegerArray(ArrayList<String> stringArray) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(String stringValue : stringArray) {
            try {
                //Convert String to Integer, and store it into integer array list.
                result.add(parseInt(stringValue));
            } catch(NumberFormatException nfe) {
                //System.out.println("Could not parse " + nfe);
                print("Parsing failed! " + stringValue + " can not be an integer");
            }
        }
        return result;
    }

    public static void Get_Top_Turnip_Prices() throws IOException {
        WebDriver driver;

        print("\n---------------- Start of Bearer Token Logic ---------------");
        print("------------------ Configuring ChromeDriver ----------------\n");

        ProcessBuilder pb = new ProcessBuilder("/bin/chmod", "777", CURRENT_WORKING_DIRECTORY + "/target/selenium/chromedriver");
        pb.start();

        // Point ChromeDriver to the executable
        System.setProperty("webdriver.chrome.driver", CURRENT_WORKING_DIRECTORY + "/target/selenium/chromedriver");
        System.setProperty("webdriver.chrome.silentOutput", "true");

        // Add the ChromeDriver Options
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--headless",
                "--disable-gpu",
                "--window-size=1920,1200",
                "--ignore-certificate-errors",
                "--silent"
        );

        driver = new ChromeDriver(options);

        // Start the WebDriver
        JavascriptExecutor js = ((JavascriptExecutor) driver);

        // Set the Timeout properties for the WebDriver
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);

            print("\nGoto the 'Turnip.Exchange' homepage.");
            driver.get(TURNIP_EXCHANGE_URL);
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//p[contains(text(), 'Support the community')]")));

            print("Close the 'Cookie' banner.");
            driver.findElement(By.cssSelector(".Cookie__button")).click();
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//p[contains(text(), 'Support the community')]")));

            print("Click on 'Islands'.");
            driver.findElement(By.cssSelector(".note-list")).click();
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".note")));

            print("Click on 'All Islands'.");
            driver.findElement(By.xpath("//button[contains(text(), 'All Island')]")).click();
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[contains(@class, 'bg-secondary-200') and contains(text(), 'All Islands')]")));

            print("Click on 'Turnips'.");
            driver.findElement(By.xpath("//button[contains(text(), 'Turnips')]")).click();

            List<WebElement> elements = driver.findElements(By.xpath("//div[contains(@class, 'note')]//p[contains(@class, 'ml')]"));
            print("\nNumber of Islands: " + elements.size() + "\n");

            ArrayList<String> initial_prices_list = new ArrayList<String>();

            for (WebElement element : elements) {
                String StarterText = element.getText().replace(" Bells", "");
                initial_prices_list.add(StarterText);
            }

            // TODO: Grab price, description and URL.
            // TODO: Print necessary info.

            Collections.sort(getIntegerArray(initial_prices_list));

            print("Turnip.Exchange prices over 600 :bells:");
            print("-------------------------------------------");

            for (String counter: initial_prices_list) {
                if (Integer.parseInt(counter) >= MIN_TURNIP_PRICE) {
                    print("- :ac-turnip: price: " + counter + " :bells:");
                }
            }

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            print("-------------------------------------------");
            print("Time: " + timestamp);
        } catch (Exception e) {
            // Take a Screenshot at the end of the Test (Useful if it fails).
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File("./target/staging_login_failure_screenshot.png"));
        }

        // Kill the WebDriver
        driver.quit();
    }

    public static void main(String[] args) throws IOException {
        DisableWarnings();
        SetupFilePaths();
        SetupAndStartSeleniumServerStandalone();
        Get_Top_Turnip_Prices();
    }
}
