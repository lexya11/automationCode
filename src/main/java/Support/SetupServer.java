package Support;

import Environments.Capabilities;
import Environments.Constants;
import Locators.ObjectRepository;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SetupServer{
    // Driver constant variables
    public static IOSDriver driver;
    public static WebDriverWait wait;
    public static ObjectRepository OR;
    public static SendEmailUtils sendEmail;

    // Log variables
    static Logger Log = Logger.getLogger(SetupServer.class);

    public SetupServer(){
        DOMConfigurator.configure("log4j.xml");
    }

    /**
     * Initialize the driver and orthers to execute testcase
     * @param capabilities
     */
    public static void SetUp (Capabilities capabilities){
        try {
            Log.info("Started SetUp keyword");
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", capabilities.getPlatformName());
            caps.setCapability("platformVersion", capabilities.getPlatformVersion());
            caps.setCapability("deviceName", capabilities.getDeviceName());
            caps.setCapability("automationName", capabilities.getAutomationName());
            caps.setCapability("showXcodeLog", capabilities.getShowXcodeLog());
            caps.setCapability("noReset", capabilities.getNoReset());
            caps.setCapability("app", Constants.APP_PATH);
            caps.setCapability("newCommandTimeout", Constants.COMMAND_TIMEOUT);

            Log.info("Initialized Appium URL");
            URL remoteUrl = new URL(capabilities.getURL());
            Log.info("Initialized Appium driver");
            driver = new IOSDriver(remoteUrl, caps);
            OR = new ObjectRepository();

            //Log.info("Initialized Slack report");
            //reporter = new RemoteReporterImpl(Constants.SERVICE, Constants.CHANNEL,Constants.SENDER);

            Log.info("Initialized email report");
            sendEmail = new SendEmailUtils();
        } catch(Exception e){
            Log.error("Can't set up" + e);
        }
    }

    /**
     * Wait for locator with time in second
     * @param second
     * @param locator
     */
    public void Wait(int second, By locator){
        int i = 0;
        while(i < second){
            try{
                driver.findElement(locator);
                Log.info("Wait - Found element");
                return ;
            }catch(Exception e){
               Wait(1);
               i++;
               Log.info("Waiting..." + i + " second(s)");
            }
        }
        Log.error("Could not found element with locator: " + locator.toString());
    }
    /**
     * Wait for locator with time in second
     * @param second
     * @param by
     */
    public MobileElement findElement(int second,By by){
        int i = 0;
        MobileElement element = null;
        while(i < second){
            try{
                element = (MobileElement) driver.findElement(by);
                Log.info("Wait - Found element");
                break;
            }catch(Exception e){
                Wait(1);
                i++;
                Log.info("Waiting..." + i + " second(s)");
            }
        }
        if(i == second){
            Log.error("Could not found element with locator: " + by.toString());
            captureScreenShot(by.toString());
        }
        return element;

    }
    /**
     * Wait for in second
     * @param second
     */
    public void Wait(int second){
        try{
            Thread.sleep(second * 1000);
        }catch(Exception e){
            Log.error("Wait error: " + e);
        }
    }
    /**
     * Wait for in second
     * @param second
     */
    public void waitElementVisible(int second, By locator){
        try{
            WebDriverWait wait = new WebDriverWait(driver, second);
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            Log.info("Element "+ locator+" is visible");
        }catch(Exception e){
            Log.error("Could not found element with locator: " + locator.toString());
        }
    }

    /**
     * Set timeout to find element in second
     * @param second
     */
    public void setTimeOut(int second){
        driver.manage().timeouts().implicitlyWait(second, TimeUnit.SECONDS);
    }


    /**
     * Find element by locator with capture screenshot if not found
     * @param by
     */
    public MobileElement findElement(By by)  {
        MobileElement element = null;
        try {
            element = (MobileElement) driver.findElement(by);
        } catch (Exception e) {
            // Find again element
//            element = (MobileElement) driver.findElement(by);
            captureScreenShot(by.toString());
            Log.error("Could not found element with locator: " + by.toString(), e);
        }
        return element;
    }

    /**
     * Find element by locator with throw exception if not found
     * @param by
     */
    public MobileElement findElementException (By by)  {
        MobileElement element = (MobileElement) driver.findElement(by);
        return element;
    }

    /**
     * Only check element Exist with throw exception if not found
     * @param by
     */
    public void isElementExist(By by)  {
        driver.findElement(by);
    }

    /**
     * Check element Exist and return true / false
     * @param by
     * @return true/false
     */
    public boolean isElementPresent(By by)  {

        if (driver.findElements(by).size() == 0)
            return false;
        else
            return true;
    }

    /**
     * Accept popup notification
     */
    public void acceptPopup(){
        driver.switchTo().alert().accept();
    }

    /**
     * Dismiss popup notification
     */
    public void dismissPopup(){
        driver.switchTo().alert().dismiss();
    }

    /**
     * Touch action swipe by 4 Coordinate
     * @param a coordinate x
     * @param b coordinate y
     * @param c coordinate of new location
     * @param d coordinate of new location
     */
    public void touchActionSwipe(int a, int b, int c, int d)  {
        Log.info("touchActionSwipe > "+ "Coordinate: " + a + "_" + b + "_" + c + "_" + d);
        try {
            new TouchAction(driver).press(PointOption.point(a,b)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(200))).moveTo(PointOption.point(c,d)).release().perform();
        }catch (Exception e){
            captureScreenShot("Coordinate_" + a + "_" + b + "_" + c + "_" + d);
            Log.error("Could not found Touch Action: " );
            throw e;
        }
        Log.info("touchActionSwipe - completed");
    }

    /**
     * Touch action swipe with param array with 4 coordinate
     * @param locator
     */
    public void touchActionSwipe(int[] locator)  {
        int a = locator[0];
        int b = locator[1];
        int c = locator[2];
        int d = locator[3];
        Log.info("touchActionSwipe > "+ "Coordinate: " + a + "_" + b + "_" + c + "_" + d);
        try {
            new TouchAction(driver).press(PointOption.point(a,b)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(350))).moveTo(PointOption.point(c,d)).release().perform();
        }catch (Exception e){
            captureScreenShot("Coordinate_" + a + "_" + b + "_" + c + "_" + d);
            Log.error("Could not found Touch Action: " );
            throw e;
        }
        Log.info("touchActionSwipe - completed");
    }

    /**
     * Tap the element with capture screenshot if not found
     */
    public void tapElement(By by)   {
        Log.info("tapElement > "+ by);
        try{
            WebElement el = driver.findElement(by);
            new TouchAction(driver).tap(ElementOption.element(el)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(300))).release().perform();
        } catch (Exception e) {
            captureScreenShot(by.toString());
            Log.info("Exception occurs:" + e + "");
            throw e;
        }
        Log.info("tapElement - completed");
    }

    /**
     * Long press element with capture screenshot if not found
     * @throws Exception
     */
    public void longPressElement(By by)   {
        Log.info("longPressElement > "+ by);
        try{
            WebElement el = driver.findElement(by);
            new TouchAction(driver).longPress(ElementOption.element(el)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000))).release().perform();
        } catch (Exception e) {
            captureScreenShot(by.toString());
            Log.info("Exception occurs:" + e + "");
            throw e;
        }
        Log.info("longPressElement - completed");
    }

    /**
     * Touch action tap by coordinate
     * @param x
     * @param y
     */
    public void tapCoordinate(int x, int y){
        Log.info("tapCoordinate > "+ "Coordinate: " + x + "_" + y);
         try {
             new TouchAction(driver).press(PointOption.point(x,y)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(300))).release().perform();
         } catch (Exception e){
             captureScreenShot("Coordinate_" + x + "_" + y);
             Log.error("Could not found Touch Action: ");
             throw e;
         }
        Log.info("tapCoordinate - completed");
     }

    /**
     * Touch action tap by Point
     * @param a
     */
    public void tapCoordinate(Point a)   {
        int x = a.x;
        int y = a.y;
        Log.info("tapCoordinate > "+ "Point: " + x + "_" + y);
        try {
            new TouchAction(driver).press(PointOption.point(x,y)).release().perform();
        } catch (Exception e){
            captureScreenShot("Coordinate_" + x + "_" + y);
            Log.error("Could not found Touch Action: ");
            throw e;
        }
    }

    /**
     * Keyword to capture screenshot of device.
     */
    public void captureScreenShot(String fileName) {
        Log.info("Started Captured the screenshot keyword");
        //If fileName > 255 character, just take 255 character (maximum file name character is 255)
        if(fileName.length()>255){
            fileName = fileName.substring(255);
        }
        String destDir = Constants.SCREENSHOT_FOLDER;

        // To capture screenshot.
        File scrFile = driver.getScreenshotAs(OutputType.FILE);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_aa");

        // To create folder to store screenshots
        new File(destDir).mkdirs();
        // Set file name with combination of test class name + date time.
        fileName = fileName.replace(": ","_").replace("//","_").replace("/","_");
        String destFile = dateFormat.format(new Date()) + "_" + fileName + ".png";

        try {
            // Store file at destination folder location
            FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
        } catch (IOException e) {
            Log.error("Can't store file at destination folder", e);
        }
    }

    // Unused will remove

    public MobileElement findElementByXpath(String xpath)   {
        try {
            MobileElement element = (MobileElement) driver.findElementByXPath(xpath);
            return element;
        } catch (Exception e) {
            Log.error("Could not found element with Xpath : " + xpath);
            throw e;
        }
    }

    public MobileElement findElementByName(String name)   {
        try {
            MobileElement element = (MobileElement) driver.findElementByName(name);
            return element;
        } catch (Exception e) {
            Log.error("Could not found element with Name : " + name);
            throw e;
        }
    }

    public void findTouchActionLongPress (Point a)  {
        try {
            int x = a.x;
            int y = a.y;
            TouchAction touchAction = new TouchAction(driver).longPress(PointOption.point(x,y)).release();
            touchAction.perform();
        } catch (Exception e){
            Log.error("Could not found Touch Action: ");
            throw e;
        }
    }
    public MobileElement doubleTap(By by){
        MobileElement element = null;
        try {
            element = (MobileElement) driver.findElement(by);
            TouchActions action = new TouchActions(driver);
            action.doubleTap(element);
            action.perform();
        } catch (Exception e) {
            // Find again element
            captureScreenShot(by.toString());
            Log.error("Could not found element with locator: " + by.toString(), e);
        }
        return element;
    }
}