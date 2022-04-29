package com.project.tests.Runner;


import com.saf.framework.MyTestNGBaseClass;
import io.cucumber.testng.CucumberFeatureWrapper;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleEventWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.apiguardian.api.API;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@CucumberOptions(

        features = "src/test/features/TransferAndPackageChange/NVDSLtoNDSL.feature",
        // tags="@JiraScenarioKey1, @JiraScenarioKey2, @Payment",
        // tags="@Payment",
        plugin = {"pretty", "io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm"},
        glue = {"com.project.stepdefs"})

public class TestRunnerHappy extends MyTestNGBaseClass {
    @API(
            status = API.Status.STABLE
    )
    private TestNGCucumberRunner testNGCucumberRunner;


    //String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    @BeforeClass(alwaysRun = true)
    public void setUpClass() {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @Test(groups = "cucumber", description = "Runs Cucumber Feature", dataProvider = "features")
    public void runScenario(PickleEventWrapper pickleWrapper, CucumberFeatureWrapper featureWrapper) throws Throwable {
        this.testNGCucumberRunner.runScenario(pickleWrapper.getPickleEvent());

    }

    @DataProvider
    public Object[][] features() {
        return testNGCucumberRunner.provideScenarios();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        testNGCucumberRunner.finish();
    }
}
