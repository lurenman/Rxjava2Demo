package com.example.lurenman.rxjava2demo.activity;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.lurenman.rxjava2demo.R;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

/**
 * @author: baiyang.
 * Created on 2017/11/14.
 */

public class Rxjava2ActivityTwo extends BaseActivity {
    private static final String TAG = "Rxjava2ActivityTwo";
    private Button bt_Replay;
    private Button bt_ReplaySubject;
    private Button bt_PublishSubject;
    private Button bt_BehaviorSubject;
    private Button bt_AsyncSubject;
    private Button bt_ThrottleFirst;
    private Button bt_ThrottleLast;
    private Button bt_Debounce;
    private Button bt_Window;
    private Button bt_Delay;
    private Button bt_Thread;
    private Button bt_ceshi;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_rxjava2two);
        bt_Replay = (Button) findViewById(R.id.bt_Replay);
        bt_ReplaySubject = (Button) findViewById(R.id.bt_ReplaySubject);
        bt_PublishSubject = (Button) findViewById(R.id.bt_PublishSubject);
        bt_BehaviorSubject = (Button) findViewById(R.id.bt_BehaviorSubject);
        bt_AsyncSubject = (Button) findViewById(R.id.bt_AsyncSubject);
        bt_ThrottleFirst = (Button) findViewById(R.id.bt_ThrottleFirst);
        bt_ThrottleLast = (Button) findViewById(R.id.bt_ThrottleLast);
        bt_Debounce = (Button) findViewById(R.id.bt_Debounce);
        bt_Window = (Button) findViewById(R.id.bt_Window);
        bt_Delay = (Button) findViewById(R.id.bt_Delay);
        bt_Thread = (Button) findViewById(R.id.bt_Thread);
        bt_ceshi = (Button) findViewById(R.id.bt_ceshi);

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initEnvent() {
        super.initEnvent();
        bt_Replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectableObservable<Integer> connectableObservable = Observable.just(1, 2, 3, 4, 5).replay(2);//这个2
                connectableObservable.connect();//这个代表发送数据
               // connectableObservable.delaySubscription(2,TimeUnit.SECONDS)//延迟2秒订阅
                connectableObservable .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception {
                                Log.e(TAG, "Replay accept:1------- " + integer);
                            }
                        });

            }
        });
        bt_ReplaySubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ReplaySubject 可以缓存所有发射给他的数据。当一个新的订阅者订阅的时候，缓存的所有数据都会发射给这个订阅者。
                // 由于使用了缓存，所以每个订阅者都会收到所有的数据 , 说白了就是把发射数据都缓存起来，所有的订阅者都可以接收到
                ReplaySubject<Integer> replaySubject = ReplaySubject.create();
                replaySubject.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "ReplaySubject accept:1------- " + integer);
                    }
                });
                replaySubject.onNext(1);
                replaySubject.onNext(2);
                replaySubject.onNext(3);
                replaySubject.onComplete();
                replaySubject.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "ReplaySubject accept:2------- " + integer);
                    }
                });
 /*               replaySubject.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "ReplaySubject accept:3------- " + integer);
                    }
                });*/
//                replaySubject.onNext(4);
//                replaySubject.onComplete();

            }
        });
        bt_PublishSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //根据自己的理解最初始的观察者所有的onNext都会通知该观察者，在此期间新订阅的观者者之后接收
                //订阅之后的onNext.  貌似观察者是并发处理的
                PublishSubject<Integer> publishSubject = PublishSubject.create();
                publishSubject.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "PublishSubject accept:1------- " + integer);
                    }
                });
                publishSubject.onNext(1);
                publishSubject.onNext(2);
                publishSubject.onNext(3);
                // publishSubject.onComplete();
                publishSubject.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "PublishSubject accept:2------- " + integer);
                    }
                });
                publishSubject.onNext(4);
                publishSubject.onNext(5);
                publishSubject.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "PublishSubject accept:3------- " + integer);
                    }
                });
                publishSubject.onNext(6);
                publishSubject.onComplete();
            }
        });

        bt_BehaviorSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //BehaviorSubject 的最后一次 onNext() 操作会被缓存，然后在 subscribe() 后立刻推给新注册的 Observer
                //个人理解也就是在订阅观察者的时候前一个onNext会被缓存，也就是会通知之后订阅的观察者

                BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.create();
                behaviorSubject.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "BehaviorSubject accept:1------- " + integer);
                    }
                });
                behaviorSubject.onNext(1);
                behaviorSubject.onNext(2);
                behaviorSubject.onNext(3);
                behaviorSubject.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "BehaviorSubject accept:2------- " + integer);
                    }
                });
                behaviorSubject.onNext(4);
                behaviorSubject.onNext(5);
                behaviorSubject.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "BehaviorSubject accept:3------- " + integer);
                    }
                });
                behaviorSubject.onNext(6);
                behaviorSubject.onComplete();
            }
        });
        bt_AsyncSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查看文档，关于 AsyncObject 的介绍，在 调用 onComplete() 之前，
                // 除了 subscribe() 其它的操作都会被缓存，在调用 onComplete() 之后只有最后一个 onNext() 会生效
                //说白了就是最后一个onNext()有效，其它被都被缓存不知道搞毛用，不调用onComplete() 没反应结果
                AsyncSubject<Integer> asyncSubject = AsyncSubject.create();
                asyncSubject.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "AsyncSubject accept:1------- " + integer);
                    }
                });
                asyncSubject.onNext(1);
                asyncSubject.onNext(2);
                asyncSubject.onNext(3);
                asyncSubject.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "AsyncSubject accept:2------- " + integer);
                    }
                });
                asyncSubject.onNext(4);
                asyncSubject.onComplete();

            }
        });
        bt_ThrottleFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ThrottleFirst这个东西可以这么理解throttleFirst(1, TimeUnit.SECONDS)，在一次onNext响应事件
                //间隔1秒里不接收onNext响应事件
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        e.onNext(0);
                        Thread.sleep(200);
                        e.onNext(1);
                        Thread.sleep(600);
                        e.onNext(2);
                        Thread.sleep(300);
                        e.onNext(3);
                        Thread.sleep(1100);
                        e.onNext(4);
                        Thread.sleep(3000);
                        e.onComplete();
                    }
                }).throttleFirst(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception {
                                Log.e(TAG, "ThrottleFirst accept: " + integer);
                            }
                        });

            }
        });
        //这个和前面的对比就是第一个不算e.onNext(0);感觉也就是重哪记时的问题
        bt_ThrottleLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        e.onNext(0);
                        Thread.sleep(200);
                        e.onNext(1);
                        Thread.sleep(600);
                        e.onNext(2);
                        Thread.sleep(300);
                        e.onNext(3);
                        Thread.sleep(1100);
                        e.onNext(4);
                        Thread.sleep(3000);
                        e.onComplete();
                    }//throttleLast() 就是 sample() 换了个名字而已
                }).throttleLast(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception {
                                Log.e(TAG, "ThrottleLast accept: " + integer);
                            }
                        });
            }
        });
        bt_Debounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这个东西可以这么理解在被观察者发送onNext(1)事件之后sleep(1000)，这个debounce(500, TimeUnit.MILLISECONDS)
                //定义是500，他的意思就是观察者在接收一个事件间隔小于500的不执行，大于的就执行，也就相当与事件的处理时间小于500就不执行
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        e.onNext(1);
                        Thread.sleep(1000);
                        e.onNext(2);
                        Thread.sleep(400);
                        e.onNext(3);
                        Thread.sleep(1000);
                        e.onNext(4);
                        Thread.sleep(400);
                        e.onNext(5);
                        Thread.sleep(400);
                        e.onNext(6);
                        Thread.sleep(1000);
                        e.onComplete();
                    }
                }).debounce(500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception {
                                Log.e(TAG, "Debounce accept: " + integer);
                            }
                        });
            }
        });
        bt_Window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //定期将来自Observable的数据分拆成一些Observable窗口，然后发射这些窗口，而不是每次发射一项。
                Observable.interval(1, TimeUnit.SECONDS)
                        .take(9)
                        .window(3, TimeUnit.SECONDS).subscribe(new Consumer<Observable<Long>>() {
                    @Override
                    public void accept(@NonNull Observable<Long> longObservable) throws Exception {
                        longObservable.subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(@NonNull Long aLong) throws Exception {
                                Log.e(TAG, "Window accept: " + aLong);
                            }
                        });
                    }
                });
            }
        });
        bt_Delay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //延时发射Observable的结果。即让原始Observable在发射每项数据之前都暂停一段指定的时间段。
                // 效果是Observable发射的数据项在时间上向前整体平移了一个增量（除了onError，它会即时通知）。
                //发现没timer 和interval 都不能发送数据，这俩只是告诉观察者什么时候搞事情.
                Observable.just("Delay", "Delay1", "Delay2").delay(3, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(@NonNull String s) throws Exception {
                                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Delay accept: " + s);
                            }
                        });
            }
        });
        //线程测试
        bt_Thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
  /*              Observable.just(1).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        String name = Thread.currentThread().getName();
                        // Thread---------main
                        Log.e(TAG, "Thread---------" + name);
                    }
                });*/
/*                Observable.just(1).subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        String name = Thread.currentThread().getName();
                        // Thread---------RxCachedThreadScheduler-1
                        Log.e(TAG, "Thread---------" + name);
                    }
                });*/
    /*            Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        e.onNext(1);
                        String name = Thread.currentThread().getName();
                        // create Thread---------RxCachedThreadScheduler-1
                        Log.e(TAG, "create Thread---------" + name);
                    }
                }).subscribeOn(Schedulers.io()).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        String name = Thread.currentThread().getName();
                        //  subscribe Thread---------RxCachedThreadScheduler-1
                        Log.e(TAG, "subscribe Thread---------" + name);
                    }
                });*/

/*                Observable.just("Delay").delay(1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(@NonNull String s) throws Exception {
                                String name = Thread.currentThread().getName();
                                //什么也不指定  subscribe Thread---------RxComputationThreadPool-1
                                //指定Schedulers.io() subscribe Thread---------RxComputationThreadPool-1
                                Log.e(TAG, "subscribe Thread---------" + name);
                                Log.e(TAG, "Delay accept: " + s);
                            }
                        });*/
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        e.onNext("Delay");
                        e.onComplete();
                        String name = Thread.currentThread().getName();
                        //Schedulers.io()  Thread---------RxCachedThreadScheduler-1
                        Log.e(TAG, "create Thread---------" + name);
                    }
                }).delay(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        String name = Thread.currentThread().getName();
                        //Schedulers.io() subscribe Thread---------RxComputationThreadPool-1
                        Log.e(TAG, "subscribe Thread---------" + name);
                        Log.e(TAG, "Delay accept: " + s);
                    }
                });

            }
        });
        bt_ceshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        for (int i = 1; i <=200; i++) {
                            e.onNext(i);
                        }
                        e.onComplete();
                    }
                }).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception {
                                SystemClock.sleep(100);
                                Log.e(TAG, "ceshi accept: "+integer );
                            }
                        });
            }
        });


    }

    @Override
    protected void loadData() {

    }
}
