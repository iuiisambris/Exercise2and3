package com.company;

public class NameValueArgumentParser {
    private final String expression;
    private String key;
    private String value;
    private boolean parsed;

    public NameValueArgumentParser(String expression) {
        this.expression = expression;
        parsed = false;
    }

    public String getKey(){
        if (!parsed)
            parse();

        return key;
    }

    public String getValue(){
        if (!parsed)
            parse();

        return value;
    }

    private void parse(){
        String [] tokens = expression.split("=");
        key = tokens[0];
        value = tokens[1].replace("'", "");
        parsed = true;
    }
}
