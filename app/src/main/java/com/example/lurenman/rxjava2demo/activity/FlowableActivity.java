package com.example.lurenman.rxjava2demo.activity;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.lurenman.rxjava2demo.R;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: baiyang.
 * Created on 2017/11/14.
 * 参考http://www.jianshu.com/p/ff8167c1d191  这篇文章有个关于不丢数据的背压解决
 * ERROR,BUFFER,DROP,LATEST,MISSING 个人觉得这种东西虽然可以解决背压但是在清理缓存的时候会有数据丢失
 */

public class FlowableActivity extends BaseActivity {
    private static final String TAG = "FlowableActivity";
    private Button bt_ERROR;
    private Button bt_DROP;
    private Button bt_LATEST;
    private Button bt_BUFFER;
    private Button bt_MISSING;
    private Button bt_Subscription;
    private Subscription mSubscription;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_flowable);
        bt_ERROR = (Button) findViewById(R.id.bt_ERROR);
        bt_DROP = (Button) findViewById(R.id.bt_DROP);
        bt_LATEST = (Button) findViewById(R.id.bt_LATEST);
        bt_BUFFER = (Button) findViewById(R.id.bt_BUFFER);
        bt_MISSING = (Button) findViewById(R.id.bt_MISSING);
        bt_Subscription = (Button) findViewById(R.id.bt_Subscription);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initEnvent() {
        super.initEnvent();
    /*  在此策略下，如果放入Flowable的异步缓存池中的数据超限了，
        则会抛出MissingBackpressureException异常。*/
        bt_ERROR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        //上线128
                        for (int i = 1; i <= 129; i++) {
                            e.onNext(i);
                        }
                        e.onComplete();
                    }
                }, BackpressureStrategy.ERROR)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<Integer>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                s.request(Long.MAX_VALUE);
                            }

                            @Override
                            public void onNext(Integer integer) {
                                //SystemClock.sleep(2000);
                                Log.e(TAG, "BackpressureStrategy.ERROR onNext: " + integer);
                            }

                            @Override
                            public void onError(Throwable t) {
                                //io.reactivex.exceptions.MissingBackpressureException: create: could not emit value due to lack
                                Log.e(TAG, "BackpressureStrategy.ERROR onError: " + t.toString());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        //在此策略下，如果Flowable的异步缓存池满了，会丢掉将要放入缓存池中的数据。
        //这个缓存池指的是 .observeOn(Schedulers.newThread())下游接收的那个，当尺子满的那个时候会丢掉数据
        bt_DROP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        Log.e(TAG, "subscribe: " + "开始发射数据---------");
                        for (int i = 1; i <= 500; i++) {
                            Log.e(TAG, "发射数据:" + i);
                            e.onNext(i);
                            SystemClock.sleep(100);
                        }
                        e.onComplete();
                        Log.e(TAG, "subscribe: " + "结束发射数据---------");
                    }
                }, BackpressureStrategy.DROP)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<Integer>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                s.request(Long.MAX_VALUE);
                            }

                            @Override
                            public void onNext(Integer integer) {
                                SystemClock.sleep(300);
                                Log.e(TAG, "BackpressureStrategy.DROP onNext: " + integer);
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {
                                Log.e(TAG, "BackpressureStrategy.DROP onComplete" + "接收完成---------");
                            }
                        });
            }
        });
    /*    与Drop策略一样，如果缓存池满了，会丢掉将要放入缓存池中的数据，不同的是，
        不管缓存池的状态如何，LATEST都会将最后一条数据强行放入缓存池中。*/
        bt_LATEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        Log.e(TAG, "subscribe: " + "开始发射数据---------");
                        for (int i = 1; i <= 500; i++) {
                            Log.e(TAG, "发射数据:" + i);
                            e.onNext(i);
                            SystemClock.sleep(100);
                        }
                        e.onComplete();
                        Log.e(TAG, "subscribe: " + "结束发射数据---------");
                    }
                }, BackpressureStrategy.LATEST)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<Integer>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                s.request(Long.MAX_VALUE);
                            }

                            @Override
                            public void onNext(Integer integer) {
                                SystemClock.sleep(300);
                                Log.e(TAG, "BackpressureStrategy.LATEST onNext: " + integer);
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {
                                Log.e(TAG, "BackpressureStrategy.LATEST onComplete" + "接收完成---------");
                            }
                        });
            }
        });
      /*  此策略下，Flowable的异步缓存池同Observable的一样，没有固定大小，可以无限制向里添加数据，
        不会抛出MissingBackpressureException异常，但会导致OOM。
        说白了就是上下缓存池没有上线限制
        */
        bt_BUFFER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        for (int i = 1; i <= 200; i++) {
                            e.onNext(i);
                        }
                        e.onComplete();

                    }
                }, BackpressureStrategy.BUFFER)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<Integer>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                s.request(150);//这个东西就是要请求的东西
                            }

                            @Override
                            public void onNext(Integer integer) {
                                SystemClock.sleep(100);
                                Log.e(TAG, "BackpressureStrategy.BUFFER onNext: " + integer);

                            }

                            @Override
                            public void onError(Throwable t) {
                                t.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                Log.e(TAG, "onComplete:---------");
                            }
                        });

            }
        });
    /*    MISSING
        此策略表示，通过Create方法创建的Flowable没有指定背压策略，不会对通过OnNext发射的数据做缓存或丢弃处理，
        需要下游通过背压操作符（onBackpressureBuffer()/onBackpressureDrop()/onBackpressureLatest()）指定背压策略。*/
        bt_MISSING.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        //io.reactivex.exceptions.MissingBackpressureException: Queue is full?!
                        //这种模式下发送最大129超过抛出上面异常
                        for (int i = 1; i <= 129; i++) {
                            e.onNext(i);
                        }
                        e.onComplete();
                    }
                }, BackpressureStrategy.MISSING)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<Integer>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                s.request(129);
                            }

                            @Override
                            public void onNext(Integer integer) {
                                SystemClock.sleep(100);
                                Log.e(TAG, "BackpressureStrategy.MISSING onNext: " + integer);

                            }

                            @Override
                            public void onError(Throwable t) {
                                t.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                Log.e(TAG, "onComplete:---------");
                            }
                        });
            }
        });
        bt_Subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        for (int i = 0; i < 200; i++) {
                            e.onNext(i);
                        }
                        e.onComplete();
                    }
                }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<Integer>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                s.request(Long.MAX_VALUE);
                                mSubscription = s;
                            }

                            @Override
                            public void onNext(Integer integer) {
                                SystemClock.sleep(100);
                                Log.e(TAG, "onNext: --------" + integer);
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {
                                Log.e(TAG, "onComplete: -------");
                            }
                        });
            }
        });

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //进行取消操作
        if (mSubscription != null) {
            mSubscription.cancel();
        }
    }
}
