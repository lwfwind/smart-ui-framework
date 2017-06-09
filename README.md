## Smart-ui-framework - a light, robust Web/Android/IOS UI automation framework

Smart-ui-framework is a light, robust Web/Android/IOS UI automation framework based on [Webdriver](http://seleniumhq.org/), [Appium](http://appium.io/) and [TestNG](http://testng.org/doc/index.html).

* Tags: Selenium, Appium, Webdriver, TestNG, Automation

## Features
* Support page object design pattern and extend page factory support highlight element, log action, screenshot action automatically and so on
* Good support concurrent testing with multi webdriver
* Re-run failed test cases and capture screenshot automatically when testcase fails
* Support identify Toast and PopupWindow for appium through [android-automation-library](https://github.com/lwfwind/android-automation-library)

## Example
##### 1. Add maven dependency
```xml
<dependency>
    <groupId>com.github.lwfwind.automation</groupId>
    <artifactId>smart-ui-framework</artifactId>
    <version>3.6</version>
</dependency>
```

##### 2. Create Page level Class, which annotated by @Page

```java
@Page
public class SearchPage{

    @Autowired
    private WebDriver driver;
    @Value("webPath")
    private String url;
    @FindBy(id = "kw")
    private WebElement searchTestBox;

    public void searchFor(String text) {
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.get(url);
        searchTestBox.sendKeys(text);
        searchTestBox.submit();
    }
}
```

##### 3. Create Service Level Class, which annotated by @Service

```java
@Service
public class SearchService {
    @Autowired
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
    @Autowired
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
    public void onStart(ISuite iSuite){
        // TODO
    }
    
    @Override 
    public void onFinish(ISuite iSuite){
        // TODO
    }
    
    @Override
    public void onStart(ITestContext testContext){
        // TODO
    };
    
    @Override
    public void onFinish(ITestContext testContext) {
        // TODO
    }

}
```

Detail please refer to [smart-ui-automation-example](https://github.com/lwfwind/smart-ui-automation-example)


