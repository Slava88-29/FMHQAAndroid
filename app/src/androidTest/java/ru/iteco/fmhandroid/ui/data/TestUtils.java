package ru.iteco.fmhandroid.ui.data;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.TreeIterables;

public class TestUtils {
    public static ViewInteraction waitView(Matcher<View> matcher) {
        onView(isRoot()).perform(waitElement(matcher, 12000));
        return onView((matcher));

    }
    public static ViewAction waitElement(final Matcher<View> viewMatcher, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                // Действие применяется к корневому view
                return ViewMatchers.isRoot();
            }

            @Override
            public String getDescription() {
                return "Ожидание появления view " + viewMatcher.toString() + " в течение " + millis + " мс.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                long startTime = System.currentTimeMillis();
                long endTime = startTime + millis;
                do {
                    // Проходим по всем дочерним view
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        if (viewMatcher.matches(child)) {
                            // Элемент найден, выходим из метода
                            return;
                        }
                    }
                    // Ожидание перед следующим проходом
                    uiController.loopMainThreadForAtLeast(50);
                } while (System.currentTimeMillis() < endTime);
                // Если элемент не найден, выбрасываем исключение
                throw new AssertionError("View " + viewMatcher.toString() + " не найдено за " + millis + " мс.");
            }
        };
    }
}
