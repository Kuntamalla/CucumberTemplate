package com.kavinschool.quiz.steps;

import com.kavinschool.quiz.hooks.DriverFactory;
import com.kavinschool.quiz.utils.DriverUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import static org.junit.Assert.assertEquals;

public class QuizStepDefs {
    private final DriverUtils driverUtils;
    private WebDriver driver;
    private DriverFactory driverFactory;
    private QuizHomePage quizHomePage;

    public QuizStepDefs(DriverFactory driverFactory) {
        this.driverFactory = driverFactory;
        this.driver = driverFactory.getDriver();
        this.driverUtils = driverFactory.getDriverUtils();
        this.quizHomePage = PageFactory.initElements(driver, QuizHomePage.class);
    }

    @Given("I visit the kavin school quiz url {string}")
    public void iVisitTheKavinSchoolQuizUrl(String url) {
        System.out.println("QuizStepDefs.iVisitTheKavinSchoolQuizUrl");
        System.out.println("url = " + url);
        driver.get(url);
        assertEquals("Title check", QuizHomePage.EXPECTED_TITLE, quizHomePage.getTitle());
    }

    @When("I Select {string} from the dropdown")
    public void iSelectFromTheDropdown(String quizName) {
        System.out.println("QuizStepDefs.iSelectFromTheDropdown");
        System.out.println("quizName = " + quizName);
        quizHomePage.quizSelectDropdown(quizName);
    }

    @Then("I should see {string}")
    public void iShouldSee(String headerName) {
        System.out.println("QuizStepDefs.iShouldSee");
        System.out.println("headerName = " + headerName);
        assertEquals("Header Check", headerName, quizHomePage.getHeaderText());
    }

}
