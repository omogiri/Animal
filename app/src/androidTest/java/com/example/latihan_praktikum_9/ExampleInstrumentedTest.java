package com.example.latihan_praktikum_9;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.latihan_praktikum_9.presentation.ui.MainActivity;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void testSuccessfulLogin() throws InterruptedException {
        onView(withId(R.id.email_input)).perform(typeText("samsudin123@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password_input)).perform(typeText("samsudin123"), closeSoftKeyboard());
        onView(withId(R.id.email_login_btn)).perform(click());

        Thread.sleep(3000);

        onView(withText("Selamat Datang di Aplikasi Animal")).check(matches(isDisplayed()));
        onView(withId(R.id.bottom_navigation)).perform(click());
    }
}
