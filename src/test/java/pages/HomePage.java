package pages;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class HomePage extends BasePage {

    private Logger logger = LoggerFactory.getLogger(HomePage.class);


    @Override
    public HomePage load() {
        $(By.id("logo")).shouldBe(Condition.visible);
        return this;
    }





}
