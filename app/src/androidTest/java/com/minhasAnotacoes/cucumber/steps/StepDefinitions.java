package com.minhasAnotacoes.cucumber.steps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.os.PowerManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.minhasAnotacoes.LoginActivity;
import com.minhasAnotacoes.MainActivity;
import com.minhasAnotacoes.R;
import com.minhasAnotacoes.cucumber.pages.BasePage;
import com.minhasAnotacoes.cucumber.pages.LoginPage;
import com.minhasAnotacoes.cucumber.pages.WelcomePage;
import com.minhasAnotacoes.util.ActivityFinisher;
import com.minhasAnotacoes.util.CountingIdlingResourceListenerImpl;

import org.junit.Rule;
import org.junit.runner.RunWith;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.pt.Dado;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.minhasAnotacoes.cucumber.steps.HelperSteps.takeScreenshot;
import static org.junit.Assert.assertNotNull;

/**
 * This defines all the translations from Gherkin (semi-English) sentences to Java
 */
@SuppressWarnings("JUnitTestCaseWithNoTests")
@RunWith(AndroidJUnit4.class)
public class StepDefinitions {

    @SuppressWarnings("unused")
    public static final String TAG = StepDefinitions.class.getSimpleName();
    @SuppressWarnings("unused")
    private Context mInstrumentationContext;
    @SuppressWarnings("unused")
    private Context mAppContext;
    private BasePage mCurrentPage;
    private Activity mActivity;
    private PowerManager.WakeLock mFullWakeUpLock;
    private CountingIdlingResourceListenerImpl mCountingIdlingResourceListener;

    @Rule
    private ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class,
            false, false);

    @Before
    public void setUp() throws Exception {
        mInstrumentationContext = InstrumentationRegistry.getContext();
        mAppContext = InstrumentationRegistry.getTargetContext();
        registerIdlingResources();
        mActivity = mActivityRule.launchActivity(new Intent()); // Start Activity before each test scenario
        assertNotNull(mActivity);
        turnOnScreenOfTestDevice();
    }

    private void registerIdlingResources() {
        mCountingIdlingResourceListener = new CountingIdlingResourceListenerImpl("ButtonAnimationStarter");
        MainActivity.setIdlingNotificationListener(mCountingIdlingResourceListener);
        Espresso.registerIdlingResources(mCountingIdlingResourceListener.getCountingIdlingResource());
    }

    private void turnOnScreenOfTestDevice() {
        final PowerManager powerManager = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
        //noinspection deprecation
        mFullWakeUpLock = powerManager.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "FULL WAKE UP LOCK");
        // This will turn on the screen during the test (lock screen still needs to be always disabled)
        mFullWakeUpLock.acquire();
    }

    /**
     * All the clean up of application's data and state after each scenario must happen here
     * The last call of this method should always be the call to parent's tear down method
     */
    @After
    public void tearDown() throws Exception {
        MainActivity.setIdlingNotificationListener(null);
        Espresso.unregisterIdlingResources(mCountingIdlingResourceListener.getCountingIdlingResource());
        ActivityFinisher.finishOpenActivities(); // Required for testing App with multiple activities
        letScreenOfTestDeviceTurnOff();
    }

    private void letScreenOfTestDeviceTurnOff() {
        if (mFullWakeUpLock != null) {
            mFullWakeUpLock.release();
        }
    }

    /**
     * Wait for the debugger to be manually attached to this running process.
     * Use this to debug test execution by adding this step to your test scenario and
     * when the test is running in Android Studio choose menu "Run - Attach debugger to Android process",
     * finally select the name of your app package from the list of processes displayed.
     */
    @Given("^I take a screenshot$")
    public void i_take_a_screenshot() {
        takeScreenshot("screenshot");
    }



    @Given("^I wait for manual attachment of the debugger$")
    public void wait_for_manual_attachment_of_debugger() throws InterruptedException {
        while (!Debug.isDebuggerConnected()) {
            Thread.sleep(1000);
        }
    }

    @Given("^I see the login page$")
    public void i_see_the_login_page() {
        mCurrentPage = new LoginPage();

    }

    @When("^I login with user name \"(.+)\" and password \"(.+)\"$")
    public void i_login_with_username_and_password(final String userName, final String password) {
        mCurrentPage = mCurrentPage.is(LoginPage.class).doLogin(userName, password);
    }

    @Then("^I see the welcome page$")
    public void i_see_the_welcome_page() {
        mCurrentPage.is(WelcomePage.class);
    }

    @And("^the title is \"(.+)\"$")
    public void the_title_is(final String title) {
        mCurrentPage.is(WelcomePage.class).checkTitle(title);


    }

    @When("^eu vejo a tela princial$")
    public void eu_vejo_a_tela_princial() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        Espresso.onView(withId(R.id.btn_Salvar)).check(matches(isDisplayed()));
    }

//    @And("^Eu vejo se vai dar certo$")
//    public void euVejoSeVaiDarCerto() throws Throwable {
////        Espresso.onView(withId(R.id.login_button)).check(matches(isDisplayed()));
//    }
//
//    @Dado("^que vejo a tela principal$")
//    public void queVejoATelaPrincipal() throws Throwable {
//        Espresso.onView(withId(R.id.btn_Salvar)).check(matches(isDisplayed()));
//    }
//
//    @When("^eu vejo a tela princial$")
//    public void euVejoATelaPrincial() throws Throwable {
//        Espresso.onView(withId(R.id.btn_Salvar)).check(matches(isDisplayed()));
//
//    }
}
