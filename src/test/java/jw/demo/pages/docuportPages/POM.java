package jw.demo.pages.docuportPages;

public class POM {

    private BasePage basePage;
    private HomePage loginPage;

    public BasePage basePage() {
        if (basePage == null) {
            basePage = new BasePage();
        }
        return basePage;
    }

    public HomePage homePage() {
        if (loginPage == null) {
            loginPage = new HomePage();
        }
        return loginPage;
    }
 }
