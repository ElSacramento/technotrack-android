package com.example.clay.first_task;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() throws Exception {
        super(Application.class);
//        RussianNumbers rn = new RussianNumbers();
//        if (rn.toString(312356) != "триста двенадцать тысяч триста пятьдесят шесть") throw new Exception(rn.toString(312356));
    }
}