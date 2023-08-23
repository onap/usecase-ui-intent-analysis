/*
 * Copyright (C) 2023 CMCC, Inc. and others. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onap.usecaseui.intentanalysis.Thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {

    @Bean("intentTaskExecutor")
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // corePoolSize
        executor.setCorePoolSize(2);
        // maxPoolSize
        executor.setMaxPoolSize(10);
        //queueCapacity
        executor.setQueueCapacity(60);
        //set  destroy time (seconds)
        executor.setKeepAliveSeconds(60);
        // thread name
        executor.setThreadNamePrefix("Intent_Thread_Excutor-");
        // Wait for all tasks to finish before closing the thread pool
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //rejectPolicy
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        //init pool
        executor.initialize();
        return executor;
    }

    @Bean("intentReportExecutor")
    public Executor getScheduledThreadPoolExecutor(){
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);
        executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("Report-task-%d").build());
        return executor;
    }
}

