package Environments;

public final class Constants {
    // Object Repository properties path
    public static String SETTINGS_OR = "/src/main/java/Locators/Settings.properties";
    public static String COMMON_OR = "/src/main/java/Locators/Common.properties";
    public static String HOME_OR = "/src/main/java/Locators/Home.properties";
    public static String SIGNIN_OR = "/src/main/java/Locators/SignIn.properties";
    public static String SIGNUP_OR = "/src/main/java/Locators/signUp.properties";

    public static String EMAIL_OR = "/src/main/java/Locators/Email.properties";
    public static String AGENDA_OR = "/src/main/java/Locators/Agenda.properties";
    public static String TODO_OR = "/src/main/java/Locators/Todo.properties";
    public static String CONTACT_OR = "/src/main/java/Locators/Contact.properties";
    public static String NOTE_OR = "/src/main/java/Locators/Note.properties";
    public static String COLLECTION_OR = "/src/main/java/Locators/Collection.properties";

    // Data driven excel file
    public static String SIGNIN_UP_DATA = "./src/main/java/TestData/SIGNIN_UP_DATA.xlsx";
    public static String SETTINGS_DATA = "./src/main/java/TestData/SETTINGS_DATA.xlsx";
    public static String GENERAL_DATA = "./src/main/java/TestData/GENERAL_DATA.xlsx";
    public static String TODO_DATA = "./src/main/java/TestData/TODO_DATA.xlsx";
    public static String AGENDA_DATA = "./src/main/java/TestData/AGENDA_DATA.xlsx";
    public static String CONTACT_DATA = "./src/main/java/TestData/CONTACT_DATA.xlsx";
    public static String NOTE_DATA = "./src/main/java/TestData/NOTE_DATA.xlsx";
    public static String COLLECTION_DATA = "./src/main/java/TestData/COLLECTION_DATA.xlsx";

    // Screenshoot folder
    public static String SCREENSHOT_FOLDER = "screenshots";

    // Capabilities app path variables
    public static final String APP_PATH = "/Users/Bi_DATA/FileFloApp/FloQC_0.9.17_201908071430.app";
//    public static final String APP_PATH = "/Users/fw/Downloads/FloDEV-2.app";
//    public static final String APP_PATH = "/Users/nguyensunny/Desktop/FloApp/Flo iPhone.app";
//    public static final String APP_PATH = "/Users/lcl/Desktop/App/Flo_Internal_beta.app";

    // Others
//    public static String MAIL_SERVER = "@123flo.com";
    public static String MAIL_SERVER = "@flomail.net";
    public static int NUM_CHARS_LOG = 120;
    public static String JSON_FILE = "./src/main/java/Environments/jsonDevices";
    public static String EMAIL_FROM = "flo_automation@flomail.net";
    public static int COMMAND_TIMEOUT = 4000;

    // Email Report
    static String EMAIL_TO1 = "htmhanh2000@flomail.net";
    static String EMAIL_TO2 = "anhthuy@leftcoastlogic.com";
    static String EMAIL_CC1 = "automationqcflo@gmail.com";
    static String EMAIL_CC2 = "hai@flomail.net";
    public static String EMAIL_TO = EMAIL_TO1 +","+ EMAIL_TO2;
    public static String EMAIL_CC = EMAIL_CC1 +","+ EMAIL_CC2;

    // File Report
    public static String REPORT_PATH = System.getProperty("user.dir") + "/test-output/ReportFile";
    public static String CSS_FILE = System.getProperty("user.dir") + "/test-output/testng.css";

    // Run Single script
    public static String HTML_FILE = System.getProperty("user.dir") + "/test-output/Default Suite/FloAutomation.html";
    public static String HTML_XML_FILE = System.getProperty("user.dir") + "/test-output/Default Suite/FloAutomation.xml";

    // Run XML batch scripts
    public static String HTML_FILE_XML_PATH = System.getProperty("user.dir") + "/test-output/Regression test Suite/";

    // Device - Build Version
    public static String BUILD_VERSION = "1";
    public static String DEVICE = "iPhone 6s.12.2";
    public static int NUM_LOOP = 2000000000; // 2 million

    // XML variables
    public static boolean RUN_XML = true;

//     User Automation
//    public static String USERNAME = "flo_automation";
//    public static String USERNAME01 = "flo_automation01";
//    public static String USERNAME02 = "flo_automation02";
//    public static String USERNAME03 = "flo_automation03";
//    public static String USERNAME04 = "flo_automation04";
//    public static String USERNAME05 = "flo_automation05";
//    public static String USERNAME06 = "flo_automation06";
//    public static String USERNAME07 = "flo_automation07";
    public static String PASSWORD = "123456";

    // User Automation
    public static String USERNAME   = "flo_automationb1";
    public static String USERNAME01 = "flo_automationb2";
    public static String USERNAME02 = "flo_automationb3";
    public static String USERNAME03 = "flo_automationb4";
    public static String USERNAME04 = "flo_automationb5";
    public static String USERNAME05 = "flo_automationb6";
    public static String USERNAME06 = "flo_automationb7";
    public static String USERNAME07 = "flo_automationb8";

    // User Automation
//    public static String USERNAME   = "flo_automationk1";
//    public static String USERNAME01 = "flo_automationk2";
//    public static String USERNAME02 = "flo_automationk3";
//    public static String USERNAME03 = "flo_automationk4";
//    public static String USERNAME04 = "flo_automationk5";
//    public static String USERNAME05 = "flo_automationk6";
//    public static String USERNAME06 = "flo_automationk7";
//    public static String USERNAME07 = "flo_automationk8";
}
