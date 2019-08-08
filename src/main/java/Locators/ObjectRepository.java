package Locators;

import Environments.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import java.awt.*;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.Properties;

public class ObjectRepository {
    private Properties OR;

    // Log variables
    static Logger Log = Logger.getLogger(ObjectRepository.class);

    // All ORs variables
    public static ObjectRepository signin;
    public static ObjectRepository signup;
    public static ObjectRepository home;
    public static ObjectRepository settings;
    public static ObjectRepository email;
    public static ObjectRepository common;
    public static ObjectRepository agenda;
    public static ObjectRepository todo;
    public static ObjectRepository contact;
    public static ObjectRepository note;
    public static ObjectRepository collection;

    public ObjectRepository(){
        signin = new ObjectRepository("Sign In");
        signup = new ObjectRepository("Sign Up");
        home = new ObjectRepository("Home");
        settings = new ObjectRepository("Settings");
        email = new ObjectRepository("Email");
        common = new ObjectRepository("Common");
        agenda = new ObjectRepository("Agenda");
        todo = new ObjectRepository("Todo");
        contact = new ObjectRepository("Contact");
        note = new ObjectRepository("Note");
        collection = new ObjectRepository("Collection");
    }

    /**
     * initiate Object Repository for each screen
     * @param screen
     */
    public ObjectRepository(String screen)
    {
        Log.info("Initialized OR screen "+ screen);
        try{
            //Declaring String variable for storing Object Repository path
            String Path_OR = "";
            //Creating file system object for Object Repository text/property file
            final String dir = System.getProperty("user.dir");
            switch(screen)
            {
                case "Sign In":
                    //Declaring String variable for storing Object Repository path
                    Path_OR = Constants.SIGNIN_OR;
                    break;
                case "Sign Up":
                    Path_OR = Constants.SIGNUP_OR;
                    break;
                case "Home":
                    Path_OR = Constants.HOME_OR;
                    break;
                case "Settings":
                    Path_OR = Constants.SETTINGS_OR;
                    break;
                case "Common":
                    Path_OR = Constants.COMMON_OR;
                    break;
                case "Email":
                    Path_OR = Constants.EMAIL_OR;
                    break;
                case "Agenda":
                    Path_OR = Constants.AGENDA_OR;
                    break;
                case "Todo":
                    Path_OR = Constants.TODO_OR;
                    break;
                case "Contact":
                    Path_OR = Constants.CONTACT_OR;
                    break;
                case "Note":
                    Path_OR = Constants.NOTE_OR;
                    break;
                case "Collection":
                    Path_OR = Constants.COLLECTION_OR;
                    break;
            }
            FileInputStream fs = new FileInputStream("." + Path_OR);
            //Log.info("Repository file = " + dir + Path_OR);
            //Creating an Object of properties
            OR = new Properties(System.getProperties());
            //Loading all the properties from Object Repository property file in to OR object
            OR.load(fs);
        } catch (Exception e) {
            Log.error("Class ObjectRepository | Exception desc :  " + e.getMessage());
        }
    }

    /**
     * get locator of element with Id/Name/Css/Link/Tag/Xpath
     * @param key
     * @return By
     */
    public By getLocator(String key)
    {
        String locatorProperty = OR.getProperty(key);
        //System.out.println(locatorProperty);
        By locator = null;
        if(locatorProperty != null) {
            String locatorType = locatorProperty.split(":", 2)[0];
            String locatorValue = locatorProperty.split(":", 2)[1];
            switch (locatorType) {
                case "Id":
                    locator = By.id(locatorValue);
                    break;
                case "Name":
                    locator = By.name(locatorValue);
                    break;
                case "CssSelector":
                    locator = By.cssSelector(locatorValue);
                    break;
                case "LinkText":
                    locator = By.linkText(locatorValue);
                    break;
                case "PartialLinkText":
                    locator = By.partialLinkText(locatorValue);
                    break;
                case "TagName":
                    locator = By.tagName(locatorValue);
                    break;
                case "Xpath":
                    locator = By.xpath(locatorValue);
                    break;
            }
        }else
            Log.info("Can't get locator from property");
        return locator;
    }

    /**
     * get locator of dynamic element by passing argument
     * @param key
     * @param values
     * @return
     */
    public By getLocatorDynamic(String key, String[] values)
    {
        String locatorProperty = OR.getProperty(key);
//        System.out.println(locatorProperty);
        By locator = null;
        if(locatorProperty != null) {
            String locatorType = locatorProperty.split(":", 2)[0];
            String locatorValue = MessageFormat.format(locatorProperty.split(":", 2)[1], values);
//            System.out.println(locatorValue);
            switch (locatorType) {
                case "Id":
                    locator = By.id(locatorValue);
                    break;
                case "Name":
                    locator = By.name(locatorValue);
                    break;
                case "CssSelector":
                    locator = By.cssSelector(locatorValue);
                    break;
                case "LinkText":
                    locator = By.linkText(locatorValue);
                    break;
                case "PartialLinkText":
                    locator = By.partialLinkText(locatorValue);
                    break;
                case "TagName":
                    locator = By.tagName(locatorValue);
                    break;
                case "Xpath":
                    locator = By.xpath(locatorValue);
                    break;
            }
        }else
            Log.info("Can't get locator from property");
        return locator;
    }

    /**
     * get locator of element with coordinates
     * @param key
     * @return Point
     */
    public Point getCoordinate(String key) {
        Point locator = new Point(0, 0);
        String locatorProperty = OR.getProperty(key);
        //System.out.println(locatorProperty);
        if(locatorProperty != null) {
            String locatorType = locatorProperty.split(":")[0];
            int X = Integer.parseInt(locatorProperty.split(":")[1]);
            int Y = Integer.parseInt(locatorProperty.split(":")[2]);
            //System.out.println(X +" "+Y);
            if (locatorType.equals("Coordinates"))
                locator.setLocation(X, Y);
        }else
            Log.info("Can't get locator from property");
        return locator;
    }

    /**
     * get coordinates locator of swipe action
     * @param key
     * @return int[]
     */
    public int[] getSwipeCoordinate(String key) {
        String locatorProperty = OR.getProperty(key);
        //System.out.println(locatorProperty);
        int[] locator = new int[4];
        if(locatorProperty != null) {
            String locatorType = locatorProperty.split(":")[0];
            int x = Integer.parseInt(locatorProperty.split(":")[1]);
            int y = Integer.parseInt(locatorProperty.split(":")[2]);
            int a = Integer.parseInt(locatorProperty.split(":")[3]);
            int b = Integer.parseInt(locatorProperty.split(":")[4]);
//        System.out.println(x +" "+y+" "+a+" "+b);
            if (locatorType.equals("Swipe")) {
                locator[0] = x;
                locator[1] = y;
                locator[2] = a;
                locator[3] = b;
            }
        }else
            Log.info("Can't get locator from property");
        return locator;
    }
}
