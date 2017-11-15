package com.example.lurenman.rxjava2demo.utils;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author: baiyang.
 * Created on 2017/11/15.
 *  创建单例
 */

public class RxBus {
    private PublishSubject<Object> bus = PublishSubject.create();

    private static final class Holder {
        private static final RxBus INSTANCE = new RxBus();
    }
    public static RxBus getInstance() {
        return Holder.INSTANCE;
    }

    public void send(Object o) {
        bus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
