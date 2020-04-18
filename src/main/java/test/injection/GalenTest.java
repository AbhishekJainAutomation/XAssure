package test.injection;

import java.util.LinkedList;
import java.util.List;

import org.testng.annotations.Test;

import com.galenframework.reports.GalenTestInfo;
import com.xassure.framework.driver.EnvironmentSetup;
import com.xassure.galen.GalenDriver;

public class GalenTest extends EnvironmentSetup {
	WelcomePage _welcomePage;
	LoginPage _loginPage;
	GalenDriver _galenDriver;

	@Test
	private void SampleUiTest() {
		List<GalenTestInfo> tests = new LinkedList<GalenTestInfo>();
		try {
			System.out.println(System.getProperty("java.classpath"));
			_welcomePage = injector.getInstance(WelcomePage.class);
			GalenTestInfo welcomePageTest = _welcomePage.launchApp("http://testapp.galenframework.com",
					"welcomePage.spec");
			tests.add(welcomePageTest);

			GalenTestInfo loginPageTest = _welcomePage.navigateToLoginPage("loginPage.spec");

			tests.add(loginPageTest);
			_loginPage = injector.getInstance(LoginPage.class);
			GalenTestInfo myNotePageTest = _loginPage.login("testuser@example.com", "test123", "myNotesPage.spec");
			tests.add(myNotePageTest);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			_galenDriver = new GalenDriver();
			_galenDriver.generateReport(tests);
		}
	}

}
