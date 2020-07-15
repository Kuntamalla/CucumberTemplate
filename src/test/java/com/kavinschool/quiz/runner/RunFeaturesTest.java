package com.kavinschool.quiz.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

import static io.cucumber.junit.CucumberOptions.SnippetType.CAMELCASE;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/quiz_simple.feature",

        glue = {"classpath:com.kavinschool.quiz.steps",
                "classpath:com.kavinschool.quiz.hooks"},

        plugin = {
                "pretty",
                "html:target/cucumber-reports",
                "json:target/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml",
                "rerun:target/cucumber-reports/rerun.txt"
        },

        snippets= CAMELCASE,
        strict = true,
        //dryRun = true,
        monochrome = true,
        tags = {"@quiz"}
        //tags = {"@home-page"}
        //name = {"osticket"}
)
public class RunFeaturesTest {

}
