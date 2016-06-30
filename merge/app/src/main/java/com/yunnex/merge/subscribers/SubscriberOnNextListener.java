package com.yunnex.merge.subscribers;

public interface SubscriberOnNextListener<T> {
    void onNext(T t);
}
