package com.sakewiz.android.utils;

import rx.Scheduler;

/**
 * We use a scheduler to make testing our presenters easier
 * @author zsiegel
 */
public interface IScheduler {

    Scheduler mainThread();

    Scheduler backgroundThread();
}
