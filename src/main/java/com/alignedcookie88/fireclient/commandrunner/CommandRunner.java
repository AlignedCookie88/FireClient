package com.alignedcookie88.fireclient.commandrunner;

import com.alignedcookie88.fireclient.CommandQueue;
import com.alignedcookie88.fireclient.FireClient;

public abstract class CommandRunner {

    private final CommandRunnerResponse response;

    private final String command;

    public CommandRunner(String[] regex, String command) {

        this.command = command;

        CommandRunner runner = this;
        this.response = new CommandRunnerResponse() {
            @Override
            public String[] getExpr() {
                return regex;
            }

            @Override
            public void execute(String[] groups) {
                runner.execute(groups);
            }
        };
    }


    protected abstract void execute(String[] groups);

    protected void run(String data) {
        if (data!=null && !data.isEmpty()) {
            CommandQueue.queueCommand(command+" "+data);
        } else {
            CommandQueue.queueCommand(command);
        }
        FireClient.addCommandRunnerResponse(response);
    }

    protected boolean shouldRun() {
        return CommandQueue.getQueueLength() < 5;
    }
}
