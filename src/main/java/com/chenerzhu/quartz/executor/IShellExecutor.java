package com.chenerzhu.quartz.executor;

import com.chenerzhu.quartz.entity.ExecuteResult;

public interface IShellExecutor {
    ExecuteResult executeCommand(String command, long timeout);
}