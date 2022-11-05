package com.uottawa.seg2105.grp1.mealer.lib;

import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.NonNull;

import org.checkerframework.checker.units.qual.A;

import java.util.regex.Pattern;

//Stores utility methods useful to several classes
public class Utility {

    //Strings set to public for more flexibility
    //i.e.: can call isValidField("Aname", Utility.NAME)
    //            or isValidField("email@email.com", Utility.EMAIL)

    //A name starts with a capital letter followed by lowercases
    public static final String NAME = "^[A-Z][A-Za-z]*([\\-' ][A-Za-z]+)*$";

    //Email regex provided by baeldung
    //https://www.baeldung.com/java-email-validation-regex
    public static final String EMAIL = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    //Address regex provided by Jayakumari Arumugham from StackOverflow
    //https://stackoverflow.com/questions/11456670/regular-expression-for-address-field-validation
    public static final String ADDRESS = "^[#\\.0-9a-zA-Z\\s,-']+$";

    //Using convention that a credit card num. is a 16-digit number
    public static final String CREDITCARD = "^\\d{16}$";

    //Following MM/YY format, i.e.: 02/25
    //NOTE: Only validates, does not check whether it is expired
    //Courtesy of ryanp from StackOverflow
    //https://stackoverflow.com/questions/11528949/validate-the-credit-card-expiry-date-using-java
    public static final String CREDITCARDEXPIRY = "^(?:0[1-9]|1[0-2])/[0-9]{2}$";


    //Returns whether a string matches a provided regex pattern
    public static boolean isValidField(String field, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(field)
                .matches();
    }

    //Checks if a particular @text is empty
    public static boolean isEmpty(@NonNull EditText text) {
        String str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }


}
