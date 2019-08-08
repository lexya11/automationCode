package Environments;

public interface Capabilities {
    public String getPlatformName();
    public String getPlatformVersion();
    public String getDeviceName();
    public String getAutomationName();
    public String getOrientation();
    public Boolean getNoReset();
    public Boolean getShowXcodeLog();
    public String getURL();

    public void setPlatformName(String platformName);
    public void setPlatformVersion(String platformVersion);
    public void setDeviceName(String deviceName);
    public void setAutomationName(String automationName);
    public void setOrientation(String orientation);
    public void setNoReset(Boolean noReset);
    public void setShowXcodeLog(Boolean showXcodeLog);
    public void setUrlAppium(String url);
}
