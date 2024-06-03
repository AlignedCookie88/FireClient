package com.alignedcookie88.fireclient;

import java.util.ArrayList;
import java.util.List;

public class FireFunctionParser {
    public static String ARG_DENOTION = "\uF8F8";
    public void parseAndExecute(FireFunction function, String rawArgs) {
        List<String> stringArgs = parseStringArgs(rawArgs);
        List<Object> objectArgs = parseObjectArgs(stringArgs, function);
        function.execute(objectArgs.toArray());
    }

    private List<String> parseStringArgs(String rawArgs) {
        List<String> stringArgs = new ArrayList<>();
        String currentArgument = "";
        boolean parsingArgument = false;

        for (Character charr : rawArgs.toCharArray()) {
            if (charr.toString().equals(ARG_DENOTION)) {
                if (parsingArgument) {
                    stringArgs.add(currentArgument);
                } else {
                    currentArgument = "";
                }
                parsingArgument = !parsingArgument;
            } else if (parsingArgument) {
                currentArgument = currentArgument + charr;
            }
        }

        return stringArgs;
    }

    private List<Object> parseObjectArgs(List<String> stringArgs, FireFunction function) {
        List<Object> objectArgs = new ArrayList<>();

        int i = 0;
        for (String string : stringArgs) {
            FireArgument arg = function.getExpectedArguments().get(i); i++;
            objectArgs.add(arg.parse(string));
        }

        return objectArgs;
    }
}
