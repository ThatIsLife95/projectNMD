package com.example.demo.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Common {
    public static String getToken() {
        return UUID.randomUUID().toString();
    }

    public static Map<String, Object> getTemplateModel(String link, String token, String greeting, String text, String button) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("link", link + "?token=" + token);
        templateModel.put("greeting", greeting);
        templateModel.put("text", text);
        templateModel.put("button", button);
        return  templateModel;
    }


}
