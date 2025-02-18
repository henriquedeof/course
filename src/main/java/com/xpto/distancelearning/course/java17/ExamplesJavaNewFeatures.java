package com.xpto.distancelearning.course.java17;

//import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ExamplesJavaNewFeatures {

    //@PostConstruct
    public void switchExample(){
        Month month = Month.JUNE;

        // Switch case that returns a value
        int numberOfDays = switch (month)
                {
                    case JANUARY, MARCH, MAY, JULY, AUGUST, OCTOBER, DECEMBER -> 31;
                    case APRIL, JUNE, SEPTEMBER, NOVEMBER -> 30;
                    case FEBRUARY -> 28;
                };
        System.out.println("number of days: " + numberOfDays);
    }

    //@PostConstruct
    public void textBlocksExample(){
        String html = """
            <html>
              <body>
                <p>Hello World!</p>
              </body>
            </html> """;
        System.out.println(html);
    }

//    //@PostConstruct
//    public void patternMatchingForSwitch(){
//        Object o = Double.valueOf(100);
//        double d = switch (o) {
//            case Integer i -> i.doubleValue();
//            case Float f -> f.doubleValue();
//            default -> 0d;
//        };
//        System.out.println("Result: " + d);
//    }
}