package com.dicicip.starter.util.validator;

import java.util.ArrayList;
import java.util.Arrays;

public class ValidatorItem {

    public String name;
    public ArrayList<String> validations;

    public ValidatorItem(String name, ArrayList<String> validations) {
        this.name = name;
        this.validations = validations;
    }

    public ValidatorItem(String name, String... validations) {
        this.name = name;
        this.validations = new ArrayList<>(Arrays.asList(validations));
    }

}
