package com.alignedcookie88.fireclient.commandrunner;

public interface CommandRunnerResponse {

    String[] getExpr();

    void execute(String[] groups);

}
