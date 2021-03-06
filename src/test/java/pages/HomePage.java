package pages;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends BasePage {

	@FindBy(id = "nav-link-accountList")
	WebElement accountAndLists;

	@FindBy(xpath = "//*[@id=\'nav-flyout-ya-signin\']/a")
	WebElement btnSignIn;

	@FindBy(id = "nav-link-accountList-nav-line-1")
	WebElement helloName;

	@FindBy(id = "nav-item-signout")
	WebElement signOut;

	@FindBy(id = "searchDropdownBox")
	WebElement categoriesDropDown;

	@FindBy(id = "twotabsearchtextbox")
	WebElement txtSearch;

	@FindBy(id = "nav-flyout-searchAjax")
	WebElement suggestionsBox;

	@FindBy(xpath = "//div[@id='suggestions']//div[@class='s-suggestion' and @data-alias='aps']")
	List<WebElement> suggestionItems;

	@FindBy(id = "nav-cart")
	WebElement shoppingCart;

	@FindBy(id = "nav-cart-count")
	WebElement cartCount;

	@FindBy(id = "WLHUC_viewlist")
	WebElement btnViewWL;

	@FindBy(xpath = "//div[@id='anonCarousel1']//img")
	List<WebElement> carouselItemImages;

	@FindBy(xpath = "//a[@class='a-carousel-goto-prevpage']")
	WebElement btnPrevCarousel;

	@FindBy(xpath = "//a[@class='a-carousel-goto-nextpage']")
	WebElement btnNextCarousel;

	@FindBy(id = "nav-hamburger-menu")
	WebElement hMenu;

	@FindBy(xpath = "//div[@id='hmenu-content']//a[text()='Customer Service']")
	WebElement hMenuCustomerService;

	public HomePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public void refresh() {
		driver.navigate().refresh();
	}

	public void clickOnAccountAndListsThenSignIn() {
		accountAndLists.click();
	}

	public String getSignedInUserName() {
		return helloName.getText();
	}

	public boolean clickSignOut() {

		try {
			Actions actions = new Actions(driver);
			actions.moveToElement(accountAndLists).moveToElement(signOut).click().build().perform();
			return true;
		} catch (ElementNotInteractableException e) {
			return false;
		}
	}

	public String getCurrentSearchCategory() {
		Select categories = new Select(categoriesDropDown);
		return categories.getFirstSelectedOption().getText();
	}

	public List<String> allSearchCategories() {
		Select categories = new Select(categoriesDropDown);
		return categories.getOptions().stream().map(item -> item.getText()).collect(Collectors.toList());
	}

	public void selectThisSearchCategory(String category) {
		Select categories = new Select(categoriesDropDown);
		categories.selectByVisibleText(category);
	}

	public String getTextOfSearchBox() {
		return txtSearch.getAttribute("value");
	}

	public String getPlaceholderOfSearchBox() {
		return txtSearch.getAttribute("placeholder");
	}

	public List<String> searchForThisKeyword(Logger logger, char input) throws InterruptedException, TimeoutException {

		txtSearch.sendKeys(Character.toString(input));

		if (txtSearch.getAttribute("value").length() == 1) {
			wait = new WebDriverWait(driver, 5);
			logger.info("waiting for suggestions box...");
			wait.until(ExpectedConditions.visibilityOf(suggestionsBox));
		} else {
			Thread.sleep(2300);
		}
		return suggestionItems.stream().map(item -> item.getText()).collect(Collectors.toList());
	}

	public void searchForThisProduct(String input) {
		txtSearch.sendKeys(input);
		txtSearch.submit();
	}

	public void goToShoppingCart() {
		shoppingCart.click();
	}

	public int getItemsInCartCount() {
		return Integer.parseInt(cartCount.getText());
	}

	public void goToWishList() {
		btnViewWL.click();
	}

	public Map<String, String> getCarouselItemNamesAndVisibility() {

		Map<String, String> carouselItemNamesAndVisibility = new LinkedHashMap<String, String>();

		carouselItemImages.forEach(item -> {
			carouselItemNamesAndVisibility.put(item.getAttribute("alt"), item.getCssValue("visibility"));
		});
		return carouselItemNamesAndVisibility;
	}

	public List<String> getCarouselItemNames() {
		return carouselItemImages.stream().map(image -> image.getAttribute("alt")).collect(Collectors.toList());
	}

	public String getVisibleCarouselItemName() {

		return carouselItemImages.stream().filter(image -> image.getCssValue("visibility").equals("visible"))
				.collect(Collectors.toList()).get(0).getAttribute("alt");
	}

	public boolean isCarouselPrevButtonDisplayed() {
		return btnPrevCarousel.isDisplayed();
	}

	public boolean isCarouselNextButtonDisplayed() {
		return btnNextCarousel.isDisplayed();
	}

	public void clickPreviousCarousel() {
		btnPrevCarousel.click();
	}

	public void clickNextCarousel() {
		btnNextCarousel.click();
	}

	public void clickHMenu() {
		hMenu.click();
	}

	public void clickHMenuCustomerService() {
		clickHMenu();
		wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.elementToBeClickable(hMenuCustomerService));
		hMenuCustomerService.click();
	}
}