package ch.viascom.lusio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import ch.viascom.lusio.beans.RouletteBean;

public class RandomFieldTest {

    @Test
    public void testRandomFieldGenerator() {

        List<Number> numbers = new ArrayList<>();

        RouletteBean rouletteBean = new RouletteBean();

        int runs =  1000000;
        
        for (int i = 0; i < runs; i++) {
            int randomNumber = rouletteBean.calculateNumber();
            boolean found = false;

            for (Iterator iterator = numbers.iterator(); iterator.hasNext();) {
                Number number = (Number) iterator.next();
                if (number.number == randomNumber) {
                    number.counter += 1;
                    found = true;
                    break;
                }
            }

            if (!found) {
                Number number = new Number(randomNumber, 1);
                numbers.add(number);
            }
        }

        Collections.sort(numbers);

        for (Iterator iterator = numbers.iterator(); iterator.hasNext();) {
            Number number = (Number) iterator.next();
            double procent = number.counter;
            procent = procent / runs * 100;
            System.out.println(number.number + "\t: " + number.counter + " (" + procent + "%)");
        }
    }
}
