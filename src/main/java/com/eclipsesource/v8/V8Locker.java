/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.v8;

/**
 * Represents a lock for a V8Runtime that can be moved between
 * threads. When instantiated, the lock is automatically assigned
 * to the current thread. If another thread wishes to acquire the
 * lock, it must first be released.
 */
public class V8Locker {

    private Thread thread = null;

    V8Locker() {
        acquire();
    }

    /**
     * Returns the current thread associated with locker.
     *
     * @return The currently locked thread.
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * Acquire the lock if it's currently not acquired by another
     * thread. If it's currently held by another thread, an
     * Error will be thrown.
     */
    public synchronized void acquire() {
        if ((thread != null) && (thread != Thread.currentThread())) {
            throw new Error("Invalid V8 thread access: current thread is " + Thread.currentThread() + " while the locker has thread " + thread);
        }
        thread = Thread.currentThread();
    }

    /**
     * Acquire the lock if it's currently not acquired by another
     * thread. If it's currently held by another thread, tryAcquire
     * will return false, otherwise true is returned.
     *
     * @return Returns true if the lock was acquired, false otherwise.
     */
    public synchronized boolean tryAcquire() {
        if ((thread != null) && (thread != Thread.currentThread())) {
            return false;
        }
        thread = Thread.currentThread();
        return true;
    }

    /**
     * Release the lock if it's currently held by the calling thread.
     * If the current thread does not hold the lock, and error will be
     * thrown.
     */
    public synchronized void release() {
        checkThread();
        thread = null;
    }

    /**
     * Checks if the locker has access to the current thread.
     * If the locker holds a different thread, than an Error
     * is thrown.
     */
    public void checkThread() {
        if ((thread != Thread.currentThread())) {
            throw new Error("Invalid V8 thread access: current thread is " + Thread.currentThread() + " while the locker has thread " + thread);
        }
    }

    /**
     * Check if the current thread holds this lock.
     *
     * @return Returns true if the current thread holds the lock,
     * false otherwise.
     */
    public boolean hasLock() {
        return thread == Thread.currentThread();
    }

}
