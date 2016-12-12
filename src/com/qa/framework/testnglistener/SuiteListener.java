package com.qa.framework.testnglistener;

import com.qa.framework.bean.Method;
import com.qa.framework.cache.ResultCache;
import org.apache.log4j.Logger;
import org.testng.*;

import java.util.*;

import static com.qa.framework.ioc.IocHelper.findImplementClass;


/**
 * Created by Administrator on 2016/12/12.
 */
public class SuiteListener implements ISuiteListener {
    private static Logger logger = Logger.getLogger(SuiteListener.class);

    @Override
    public void onStart(ISuite iSuite) {
        logger.info("testContext Start");
    }

    @Override
    public void onFinish(ISuite iSuite) {
        logger.info("testContext Finish");

        // List of test results which we will delete later
        ArrayList<ITestResult> testsToBeRemoved = new ArrayList<ITestResult>();
        // collect all id's from passed test
        Set<Integer> passedTestIds = new HashSet<Integer>();
        for (String key : iSuite.getResults().keySet()) {
            ISuiteResult suiteResult = iSuite.getResults().get(key);
            ITestContext testContext = suiteResult.getTestContext();
            for (ITestResult passedTest : testContext.getPassedTests().getAllResults()) {
                logger.info("PassedTests = " + passedTest.getName());
                passedTestIds.add(getId(passedTest));
            }
            for (ITestResult skipTest : testContext.getSkippedTests().getAllResults()) {
                logger.info("SkipTest = " + skipTest.getName());
            }
            for (ITestResult failedTest : testContext.getFailedTests().getAllResults()) {
                logger.info("FailedTest = " + failedTest.getName());
            }

            HashMap<Integer, Method> map = ResultCache.get();
            if (map != null && map.size() > 0) {
                for (Map.Entry<Integer, Method> entry : map.entrySet()) {
                    if (entry.getValue().getStatus().equals("fail")) {
                        int currId = entry.getKey();
                        if (map.get(currId + 1) != null && map.get(currId + 1).getStatus().equals("pass") && map.get(currId + 1).getName().equals(map.get(currId).getName())) {
                            for (Iterator<ITestResult> iterator = testContext.getFailedTests().getAllResults().iterator(); iterator.hasNext(); ) {
                                ITestResult testResult = iterator.next();
                                if (entry.getValue().getHashCode() == getId(testResult)) {
                                    logger.info("Remove fail but retry pass test: " + entry.getValue().getName());
                                    iterator.remove();
                                    break;
                                }
                            }
                        }
                    }
                }

            }
        }
        logger.info("testContext Finish");
        Class<?> clazz = findImplementClass(ICustomTestListener.class);
        if (clazz != null) {
            ICustomTestListener testListenerImp = null;
            try {
                testListenerImp = (ICustomTestListener) clazz.newInstance();
                testListenerImp.onFinish(iSuite);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    private int getId(ITestResult result) {
        int id = result.getTestClass().getName().hashCode();
        id = id + result.getMethod().getMethodName().hashCode();
        id = id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
        return id;
    }
}


