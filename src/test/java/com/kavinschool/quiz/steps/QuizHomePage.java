package com.kavinschool.quiz.steps;

import com.kavinschool.quiz.utils.DriverUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class QuizHomePage {
    public static final String EXPECTED_TITLE = "Quiz - Kavin School";

    private WebDriver driver;

    @FindBy(how = How.CSS, using = "select")
    @CacheLookup
    private WebElement quiz;

    @FindBy(xpath = "//div[@class='container']//div[2]/h3")
    private WebElement header;

    public QuizHomePage(WebDriver driver) {
        this.driver = driver;
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public void quizSelectDropdown(final String quizName) {
        new DriverUtils(driver).selectOption(quiz,quizName);
    }

    public String getHeaderText() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOf(header));
        return header.getText();
    }

}