## Smart-ui-framework - a light, robust Web/Android/IOS UI automation framework

Smart-ui-framework is a light, robust Web/Android/IOS UI automation framework based on [Webdriver](http://seleniumhq.org/), [Appium](http://appium.io/) and [TestNG](http://testng.org/doc/index.html).

* Tags: Selenium, Appium, Webdriver, TestNG, Automation

## Why
This framework pulled out of the common components such as Driver,TestNG,http/database library, which is needed in all of automation projects, let you focus on the important business and improve development efficiency.

## Features
* Support page object design pattern and extend page factory support highlight element, log action, screenshot action automatically and so on
* Support identify Toast and PopupWindow for appium through [android-automation-library](https://github.com/lwfwind/android-automation-library)
* Good support concurrent testing with multi webdriver
* Re-run failed test cases and capture screenshot automatically when testcase fails

## Example
##### 1. Add maven dependency
```xml
<dependency>
    <groupId>com.github.lwfwind.automation</groupId>
    <artifactId>smart-ui-framework</artifactId>
    <version>3.2</version>
</dependency>
```

##### 2. Create Page level Class, which extends PageBase Class

```java
public class SearchPage extends PageBase {

    String url = PropConfig.getWebPath();
    @FindBy(id = "kw")
    private WebElement searchTestBox;

    public void searchFor(String text) {
        open(url);
        searchTestBox.sendKeys(text);
        searchTestBox.submit();
    }
}
```

##### 3. Create Service Level Class, which extends ServiceBase Class

```java
public class SearchService extends ServiceBase {
    @AutoInject
    SearchPage searchPage;

    public void search(String content){
        searchPage.searchFor(content);
        searchPage.verifyResult();
    }
}
```

##### 4. Create TestCase Level Class, which extends TestCaseBase Class

```java
public class SearchWithServiceTest extends TestCaseBase {
    @AutoInject
    SearchService searchService;

    @Test(dataProviderClass = TestCaseData.class, dataProvider = "searchData", description = "搜索测试")
    public void pageFactoryTest(String content) {
        searchService.search(content);
    }
}
```

##### 5. Create test listener Class to add business log on success/fail/skip, which implement ICustomTestListener Class

```java
public class TestListener implements ICustomTestListener {

    @Override
    public void onStart(ITestContext testContext) {
        // TODO
    }
    
    @Override
    public void onTestSuccess(ITestResult tr){
        // TODO
    }
    
    @Override
    public void onTestFailure(ITestResult tr) {
        // TODO
    }
    
    @Override
    public void onTestSkipped(ITestResult tr){
        // TODO
    }

    @Override
    public void onFinish(ITestContext testContext) {
        // TODO
    }

}
```

Detail please refer to [smart-ui-automation-example](https://github.com/lwfwind/smart-ui-automation-example)


