
package test.injection;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.xassure.dbControls.ControlsDatabaseBinding;
import com.xassure.framework.driver.ControlsBinding;
import com.xassure.framework.driver.EnvironmentSetup;

public class TestClass extends EnvironmentSetup {
	PageObjectClass poClass;
	PageObject2 poClass2;

	// @BeforeTest
	// private void beforeTest() {
	// injector = Guice.createInjector(new ControlsBinding());
	// }

	@Test
	public void firstTest() {
		try {
			poClass = injector.getInstance(PageObjectClass.class);
			poClass2 = injector.getInstance(PageObject2.class);

			poClass2.testmethod();
			poClass.testmethod();
			// Injector dbInjector = Guice.createInjector(new
			// ControlsDatabaseBinding());
			// TestDb tDb = dbInjector.getInstance(TestDb.class);
			// tDb.testing();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void secondTest() {
		try {
			poClass = injector.getInstance(PageObjectClass.class);
			poClass2 = injector.getInstance(PageObject2.class);

			poClass2.testmethod();
			poClass.testmethod();
			// Injector dbInjector = Guice.createInjector(new
			// ControlsDatabaseBinding());
			// TestDb tDb = dbInjector.getInstance(TestDb.class);
			// tDb.testing();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void thirdTest() {
		Injector dbInjector = Guice.createInjector(new ControlsDatabaseBinding());
		TestDb tDb = dbInjector.getInstance(TestDb.class);
		tDb.testing();
	}
}