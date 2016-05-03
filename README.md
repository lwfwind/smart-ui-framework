## Smart-ui-framework - a light, robust Web/Android/IOS UI automation framework

Smart-ui-framework is a light, robust Web/Android/IOS UI automation framework based on [Webdriver](http://seleniumhq.org/), [Appium](http://appium.io/) and [TestNG](http://testng.org/doc/index.html).

* Tags: Selenium, Appium, Webdriver, TestNG, Automation

## Features

* Easy to learn
* Support page object design pattern and extend page factory support highlight element, log action, screenshot action automatically and so on
* Support multi-thread testing with selenium grid2
* Re-run failed test cases
* Capture screenshot automatically when testcase fails
* Support dependency injection
* Provide Element class extend for WebElement for web

## Example

##### 1. Create Page level Class, which extends PageBase Class

```java
package test.page;

import com.qa.framework.PageBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SearchPage Level extends PageBase {

    @FindBy(id = "kw")
    private WebElement searchTestBox;

    public void searchFor(String text) {
        driver.get("http://www.baidu.com/");
        searchTestBox.sendKeys(text);
        searchTestBox.submit();
    }
}
```

##### 2. Create Service Level Class, which extends ServiceBase Class (Notes: The Sevice Level is not necessary if the automation project is not complex )

```java
package test.service;

import com.qa.framework.ServiceBase;
import com.qa.framework.ioc.annotation.AutoInject;
import test.page.SearchPage;

public class SearchService extends ServiceBase {
    @AutoInject
    SearchPage searchPage;

    public void search(String content){
        searchPage.searchFor(content);
        searchPage.verifyResult();
    }
}
```

##### 3. Create TestCase Level Class, which extends TestCaseBase Class

```java
package test.testcase;

import com.qa.framework.TestCaseBase;
import com.qa.framework.ioc.annotation.AutoInject;
import org.testng.annotations.Test;
import test.datamodel.TestCaseData;
import test.service.SearchService;

public class SearchWithServiceTest extends TestCaseBase {
    @AutoInject
    SearchService searchService;

    @Test(dataProviderClass = TestCaseData.class, dataProvider = "searchData", description = "搜索测试")
    public void pageFactoryTest(String content) {
        searchService.search(content);
    }
}
```

##### 4. Create test listener Class to add business log on success/fail/skip, which implement ICustomTestListener Class

```java
package test.testnglistener;

import com.qa.framework.testnglistener.ICustomTestListener;
import org.apache.log4j.Logger;
import org.testng.ITestResult;

public class TestListener implements ICustomTestListener {

    public void onTestFailure(ITestResult tr) {
        // TODO
    }

    public void onTestSkipped(ITestResult tr){
        // TODO
    }

    public void onTestSuccess(ITestResult tr){
        // TODO
    }

}
```

Detail please refer to [smart-ui-automation-example](https://github.com/lwfwind/smart-ui-automation-example)


