/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mightybot.christopher.execution;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author wiesbob
 */
public class WorkExecutioner {
    private final ExecutorService workingQueue;

    public WorkExecutioner() {
        workingQueue = Executors.newSingleThreadExecutor();
    }
    
    public Future addToQueue(Runnable task){
        return workingQueue.submit(task);
    }
    
}
