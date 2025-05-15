package com.alignedcookie88.fireclient.sdk;

public abstract class SDKFunction {

    public abstract String getId();

    public void execute(String argString) {

        String[] rawArgs = argString.split(SDKArgument.SPLITTER);
        SDKArgument<?>[] expectedArgs = getArguments();
        int providedArgs = argString.isEmpty() ? 0 : rawArgs.length;

        if (providedArgs != expectedArgs.length) {
            throw new IllegalStateException(
                    "Incorrect argument count supplied to function %s, expected %s arguments, found %s."
                            .formatted(getId(), expectedArgs.length, providedArgs)
            );
        }

        for (int i = 0; i < rawArgs.length; i++) {
            expectedArgs[i].parse(rawArgs[i]);
        }

        onExecute();
    }

    public abstract void onExecute();

    public abstract SDKArgument<?>[] getArguments();

    public boolean executeSynchronously() {
        return false;
    }

}
