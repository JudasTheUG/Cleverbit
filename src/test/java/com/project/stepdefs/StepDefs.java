package com.project.stepdefs;

import com.saf.framework.*;
import io.cucumber.core.api.Scenario;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.java.Before;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import java.util.*;
import java.util.Arrays;
import java.util.List;


import org.testng.Assert;
import org.testng.annotations.Listeners;
import utils.excelutils.ExcelUtils;


import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;




public class StepDefs extends MyTestNGBaseClass {
    ExcelUtils excelUtils = new ExcelUtils();
    CommonLib commonLib = new CommonLib();


    int timeout = 30;
    public String uuid = UUID.randomUUID().toString();
    public String object;
    public static HashMap<String, String> strings = new HashMap<String, String>();
    InputStream stringsis;
    TestUtils utils;

    @Before
    public void setReportName(Scenario scenario) {
        commonLib.startTest(scenario.getName());
    }

    @Given("^Open the (.*) URL$")
    public void openUrl(String URL) {
        CommonLib.navigateToURL(oDriver, URL);
    }


    @Then("^I see (.*) page$")
    public void seePage(String page) {
        commonLib.seePage(page);
    }

    @When("^(?:I )?click element: (\\w+(?: \\w+)*) at index (\\d+)")
    public boolean clickElement(String element, int index) {
        WebElement object = commonLib.findElement(element, index);
        boolean flag = false;
        try {
            if (object != null) {
                object.click();
                System.out.println("Clicked on object-->" + element);
                Allure.addAttachment("Element is clicked.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                reportResult("PASS", "I clicked the element: " + element, true);
                return true;
            }
        } catch (Exception e) {
            reportResult("FAIL", "I cannot clicked the element: " + element, true);
            Allure.addAttachment("Element is not clicked.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            Assert.fail("Could not clicked the element:" + element);
            flag = false;
        }
        return flag;
    }

    @Then("I choose {string} with the {string} tag")
    public boolean clickOnElement(String elem,String text) {
        WebElement object = commonLib.findElementFromText(elem,text);
        boolean flag = false;
        try {
            if (object != null) {
                object.click();
                System.out.println("Clicked on object-->" + elem);
                Allure.addAttachment("Element is clicked.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                reportResult("PASS", "I clicked the element: " + elem, true);
                return true;
            }
        } catch (Exception e) {
            reportResult("FAIL", "I cannot clicked the element: " + elem, true);
            Allure.addAttachment("Element is not clicked.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            Assert.fail("Could not clicked the element:" + elem);
            flag = false;
        }
        return flag;
    }


    @When("^(?:I )?have to verify the text for: (\\w+(?: \\w+)*) at index (\\d+)")
    public boolean verifyText(String element, int index) throws Exception {
        WebElement object = commonLib.findElement(element, index);
        boolean flag = false;
        try {
            if (object != null) {
                String xmlFileName = "strings.xml";
                stringsis = this.getClass().getClassLoader().getResourceAsStream(xmlFileName);
                utils = new TestUtils();
                strings = utils.parseStringXML(stringsis);

                object.click();
                String actualErrTxt = object.getText();
                if (element.contains("approve popup")) {
                    String expectedErrText = strings.get("approve popup");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("assign to pool popup")) {
                    String expectedErrText = strings.get("assign to pool popup");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                } else if (element.contains("cancel popup")) {
                    String expectedErrText = strings.get("cancel popup");
                    System.out.println("actual popup text - " + actualErrTxt + "\n" + "expected popup text - " + expectedErrText);
                    Assert.assertEquals(actualErrTxt, expectedErrText);
                    Allure.addAttachment("Verification completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                    reportResult("PASS", "Assertion is true." + element, true);
                    return true;
                }
            }
        } catch (Exception e) {
            Allure.addAttachment("Verification does not completed.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            reportResult("FAIL", "An error during assertion. " + element, true);
            Assert.fail("Could not clicked the element:" + element);
            flag = false;
        } finally {
            if (stringsis != null) {
                stringsis.close();
            }
        }
        return flag;
    }


    @Then("I refresh the page")
    public void iRefreshThePage() {
        oDriver.navigate().refresh();
    }


    @Then("^I enter \"([^\"]*)\" text to (.*) at index (\\d+)")
    public boolean enterText(String text, String element, int index) throws InterruptedException {
        WebElement object;
        object = commonLib.waitElement(element, timeout, index);
        boolean flag = false;
        try {
            if (object != null) {
                object.sendKeys(text);
                System.out.println("The text has been entered:" + text);
                Allure.addAttachment("The text has been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                reportResult("PASS", "I entered the text: " + text, true);

                return true;
            }
        } catch (Exception e) {
            Allure.addAttachment("The text has not been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            reportResult("FAIL", "I cannot entered the element: " + text, true);
            Assert.fail("Could not entered the text:" + text);
            flag = false;
        }
        return flag;
    }

    @Then("^I randomly enter \"([^\"]*)\" text to (.*) at index (\\d+)")
    public boolean randomenterText2(String text, String element, int index) throws InterruptedException {
        WebElement object;
        Random rand = new Random();

        // Generate random integers in range 0 to 999
        int rand_int1 = rand.nextInt(1000);
        String a = String.valueOf(rand_int1);

        object = commonLib.waitElement(element, timeout, index);
        boolean flag = false;
        try {
            if (object != null) {
                object.sendKeys(a);
                object.sendKeys(text);
                System.out.println("The text has been entered:" + text);
                Allure.addAttachment("The text has been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                reportResult("PASS", "I entered the text: " + text, true);

                return true;
            }
        } catch (Exception e) {
            Allure.addAttachment("The text has not been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            reportResult("FAIL", "I cannot entered the element: " + text, true);
            Assert.fail("Could not entered the text:" + text);
            flag = false;
        }
        return flag;
    }


    @Then("^I enter (.*) element text to (.*) element at index (\\d+)")
    public boolean enterText2(String element1, String element, int index) throws InterruptedException {
        WebElement object;
        object = commonLib.waitElement(element, timeout, index);
        WebElement object2;
        object2 = commonLib.waitElement(element1, timeout, index + 2);

        boolean flag = false;
        try {
            if (object != null) {
                object.sendKeys(element1);
                System.out.println("The text has been entered:" + object2);
                Allure.addAttachment("The text has been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                reportResult("PASS", "I entered the text: " + object2, true);

                return true;
            }
        } catch (Exception e) {
            Allure.addAttachment("The text has not been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            reportResult("FAIL", "I cannot entered the element: " + object2, true);
            Assert.fail("Could not entered the text:" + object2);
            flag = false;
        }
        return flag;
    }

    @Then("^I enter a email to (.*) at index (\\d+)")
    public boolean enterEmail(String element, int index) throws InterruptedException {
        WebElement object;
        object = commonLib.waitElement(element, timeout, index);
        boolean flag = false;
        String email = commonLib.getRandomString() + "@example.com";
        try {
            if (object != null) {
                object.sendKeys(email);
                System.out.println("The email has been entered:" + email);
                Allure.addAttachment("The email has been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                reportResult("PASS", "I entered the email: " + email, true);
                return true;
            }
        } catch (Exception e) {
            Allure.addAttachment("The email has not been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            reportResult("FAIL", "I cannot entered the element: " + email, true);
            Assert.fail("Could not entered the email:" + email);
            flag = false;
        }
        return flag;
    }


    @Then("I go to top of the site")
    public void topOfWebsite() {
        ((JavascriptExecutor) oDriver).executeScript("window.scrollTo(document.body.scrollHeight, 0)");
    }


    @Then("^I enter unique text to (.*) at index (\\d+)")
    public boolean uniqueText(String element, int index) throws InterruptedException {
        //mouseHover(element);
        WebElement object;
        object = commonLib.waitElement(element, timeout, index);
        String text = "automation" + uuid;
        boolean flag = false;
        try {
            if (object != null) {
                object.sendKeys(text);
                System.out.println("The text has been entered.");
                Allure.addAttachment("The text has been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                reportResult("PASS", "I entered the text: " + text, true);
                return true;
            }
        } catch (Exception e) {
            Allure.addAttachment("The text has not been entered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            reportResult("FAIL", "I cannot entered the element: " + text, true);
            Assert.fail("Could not entered the text:" + text);
            flag = false;
        }
        return flag;
    }


    @Then("^I clear text to (.*) at index (\\d+)")
    public boolean clearText(String element, int index) throws InterruptedException {
        WebElement object;
        object = commonLib.waitElement(element, timeout, index);
        boolean flag = false;
        try {
            if (object != null) {
                object.click();
                Thread.sleep(1000);
                object.sendKeys(Keys.CONTROL, "a");
                object.sendKeys(Keys.DELETE);
                Thread.sleep(1000);
                System.out.println("The text has been deleted.");
                Allure.addAttachment("The text has been deleted.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                reportResult("PASS", "The text has been deleted.", true);
                return true;
            }
        } catch (Exception e) {
            System.out.println("The text has not been deleted.");
            Allure.addAttachment("The text has not been deleted.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            reportResult("FAIL", "The text has not been deleted", true);
            Assert.fail("Waiting element is not found!");
            flag = false;
        }
        return flag;
    }


    @And("^I wait (.*) element (\\d+) seconds at index (\\d+)")
    public void waitElement(String element, int timeout, int index) throws InterruptedException {
        commonLib.waitElement(element, timeout, index);

    }


    @When("^(?:I )?select element: \"([^\"]*)\" under (\\w+(?: \\w+)*) at index (\\d+)")
    public boolean selectElement(String text, String element, int index) {
        WebElement object = commonLib.findElement(element, index);
        boolean flag = false;
        try {
            if (object != null) {
                object.click();
                System.out.println("Select the object type-->" + text + element);
                Select select = new Select(object);
                select.selectByVisibleText(text);
                Allure.addAttachment("The selection is done.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                reportResult("PASS", "The selection is done.", true);
                return true;
            }
        } catch (Exception e) {
            System.out.println("The selection cannot be done.");
            Allure.addAttachment("The selection cannot be done.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            reportResult("FAIL", "The selection cannot be done.", true);
            Assert.fail("The selection cannot be done!");
            flag = false;
        }
        return flag;
    }


    @And("^I need to just wait")
    public void justWait() throws InterruptedException {
        Thread.sleep(10000);
    }


    @And("^I need to wait for (\\d+) seconds")
    public void waitForIt(int seconds) throws InterruptedException {

        int secondsToMilis = seconds * 1000;
        Thread.sleep(secondsToMilis);

    }


    @Then("^(?:I )?get the text area information: (\\w+(?: \\w+)*) at index (\\d+)")
    public boolean getTextFromAttribute(String element, int index) {
        String object = commonLib.getTheItemValueFromAttribute(element, index);
        boolean flag = false;
        try {
            if (object != null) {
                System.out.println(object);
                Allure.addAttachment("Information gathered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                reportResult("PASS", "I got the information:" + object, true);
                return true;
            }
        } catch (Exception e) {
            System.out.println("Could not got the information.");
            Allure.addAttachment("Information could not be gathered.", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            reportResult("FAIL", "Could not got the information.", true);
            Assert.fail("Could not got the information.");
            flag = false;

        }
        return flag;
    }


    @When("^(?:I )?double click element: (\\w+(?: \\w+)*) at index (\\d+)")
    public void doubleClickElement(String element, int index) {
        WebElement object = commonLib.findElement(element, index);
        commonLib.doubleClickElement(object);
    }


    @Then("^(?:I )?get the item value: (\\w+(?: \\w+)*)")
    public void getTheItemValue(String element) {
        int index = 1;
        String object = commonLib.getTheItemValue(element, index);
    }


    @Then("^(?:I )?I need to checkbox verify for (\\w+(?: \\w+)*) at index (\\d+)")
    public boolean verifyCheckbox(String element, int index) {
        String value = commonLib.getTheItemValueFromAttribute(element, index);

        boolean flag = false;
        try {
            if (value.equals("on")) {
                System.out.println("The checkbox is selection state is : on");
                Allure.addAttachment("The checkbox is selection state is : on", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
                reportResult("PASS", "The checkbox is selection state is : on" + element, true);
                return true;
            }
        } catch (Exception e) {
            System.out.println("The checkbox is selection state is : off");
            Allure.addAttachment("The checkbox is selection state is : off", new ByteArrayInputStream(((TakesScreenshot) oDriver).getScreenshotAs(OutputType.BYTES)));
            reportResult("FAIL", "The checkbox is selection state is : off" + element, true);
            Assert.fail("The checkbox is selection state is : off:" + element);
            flag = false;
        }
        return flag;
    }

    @Then("^I press ESC button")
    public void i_press_ESC_button() {
        Actions act = new Actions(oDriver);
        act.sendKeys(Keys.ESCAPE).build().perform();
    }


    @Then("I confirm if element {string} exists at index {int}")
    public boolean iConfirmElementExists(String element, int index) throws InterruptedException {

        WebElement element2 = commonLib.waitElement(element, timeout, index);

        boolean result = commonLib.confirmElementExist(element2.getText());

        if (result) {
            System.out.println(element + " element exists");
            return true;
        } else System.out.println(element + " element doesnt exist");
        return false;
    }

    @Then("I confirm that element {string} doesn't exists")
    public void iConfirmElementNotExists(String element) {
        boolean result = commonLib.confirmElementNotExist(element);
        if (result) System.out.println(element + " bulunamadı");
        else System.out.println(element + " bulundu");
    }


    @Then("I switch to {string}")
    public void iSwitchTo(String frame) {
        if (Objects.equals(frame, "default")) oDriver.switchTo().defaultContent();
        else oDriver.switchTo().frame(commonLib.findElement(frame, 1));
    }

    @Then("I scroll to the {string}")
    public void scrollToElement(String element) throws InterruptedException {
        WebElement elementToSee = commonLib.findElement(element, 1);
        ((JavascriptExecutor) oDriver).executeScript("arguments[0].scrollIntoView(true);", elementToSee);
        Thread.sleep(1000);
    }

    @Then("I scroll to the {string} at index {int}")
    public void scrollToElement(String element, int index) throws InterruptedException {
        WebElement elementToSee = commonLib.findElement(element, index);
        ((JavascriptExecutor) oDriver).executeScript("arguments[0].scrollIntoView(true);", elementToSee);
        Thread.sleep(1000);
    }

    @Then("I quit driver")
    public void quitDriver() throws InterruptedException {
        oDriver.quit();
    }

    @Then("I click element: {string} at index {int}")
    public void i_click_element_at_index(String element, int index) throws InterruptedException {
        scrollToElement(element, index);
        clickElement(element, index);
    }

    @Then("I confirm if {string} selected at index {int}")
    public void i_confirm_if_selected_at_index(String element, int index) {
        Assert.assertTrue(commonLib.findElement(element, index).isSelected());
        System.out.println("Checkbox is selected");
    }

    @Then("I confirm if {string} unselected at index {int}")
    public void i_confirm_if_unselected_at_index(String element, int index) {
        Assert.assertFalse(commonLib.findElement(element, index).isSelected());
        System.out.println("Checkbox is unselected");
    }

    @Then("I navigate to the previous page")
    public void i_navigate_to_the_previous_page() {
        oDriver.navigate().back();
    }

    @Then("I confirm if {string} equals to {string} exists at index {int}")
    public void i_confirm_if_equals_to_exists_at_index(String parameter, String element, int index) {
        WebElement status = commonLib.findElement(element, index);
        String statusText = status.getText();
        if (!(statusText.equals(parameter)))
            Assert.assertEquals(statusText, parameter);
    }

}