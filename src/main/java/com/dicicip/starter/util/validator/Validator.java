package com.dicicip.starter.util.validator;

import com.dicicip.starter.util.StringCaseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class Validator<D> {

    private D requestBody;
    private List<ValidatorItem> validatorItems;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private HashMap<String, Object> errorsList = new HashMap<>();

    public Validator(D requestBody, ValidatorItem... validatorItems) {

        this.requestBody = requestBody;
        this.validatorItems = Arrays.asList(validatorItems);

        HashMap<String, Object> body = objectMapper.convertValue(this.requestBody, HashMap.class);

        for (ValidatorItem validatorItem : this.validatorItems) {

            if (validatorItem.validations.contains("required")) {

                if (body.get(validatorItem.name) == null || body.get(validatorItem.name).equals("")) {
                    this.errorsList.put(validatorItem.name, new ArrayList<String>() {{
                        add(String.format("The %s field is required", StringCaseUtil.snakeToTitleCase(validatorItem.name)));
                    }});
                }
            }

        }

    }

    public boolean valid() {
        return this.errorsList.size() < 1;
    }

    public HashMap<String, Object> getErrorsList() {
        return this.errorsList;
    }

}
