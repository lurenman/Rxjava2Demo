package com.example.lurenman.rxjava2demo.activity;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lurenman.rxjava2demo.R;
import com.example.lurenman.rxjava2demo.entity.FlatMapClass;
import com.example.lurenman.rxjava2demo.entity.MapClass;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: baiyang.
 * Created on 2017/11/13.
 * 参考
 * https://github.com/amitshekhariitbhu/RxJava2-Android-Samples
 * http://www.open-open.com/lib/view/open1487647155844.html#articleHeader11
 * http://www.cnblogs.com/liushilin/p/7081715.html#_label1_7
 * http://blog.csdn.net/maplejaw_/article/details/52396175
 */

public class Rxjava2ActivityOne extends BaseActivity {
    private static final String TAG = "Rxjava2ActivityOne";

    private Button bt_Simple;
    private Button bt_Map;
    private Button bt_FlatMap;
    private Button bt_Filter;
    private Button bt_Interval;
    private Button bt_Zip;
    private Button bt_disposables;

    private MapClass mapclass;
    private FlatMapClass flatMapclass;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private Button bt_Take;
    private Button bt_Timer;
    private Button bt_SingleObserver;
    private Button bt_completable;
    private Button bt_Reduce;
    private Button bt_buffer;
    private Button bt_skip;
    private Button bt_scan;
    private Button bt_Concat;
    private Button bt_Merge;
    private Button bt_Defer;
    private Button bt_Distinct;
    private Button bt_last;


    @Override
    protected void initViews() {
        setContentView(R.layout.activity_rxjava2one);
        bt_Simple = (Button) findViewById(R.id.bt_Simple);
        bt_Map = (Button) findViewById(R.id.bt_Map);
        bt_FlatMap = (Button) findViewById(R.id.bt_FlatMap);
        bt_Filter = (Button) findViewById(R.id.bt_Filter);
        bt_Interval = (Button) findViewById(R.id.bt_Interval);
        bt_Zip = (Button) findViewById(R.id.bt_Zip);
        bt_disposables = (Button) findViewById(R.id.bt_disposables);
        bt_Take = (Button) findViewById(R.id.bt_Take);
        bt_Timer = (Button) findViewById(R.id.bt_Timer);
        bt_SingleObserver = (Button) findViewById(R.id.bt_SingleObserver);
        bt_completable = (Button) findViewById(R.id.bt_completable);
        bt_Reduce = (Button) findViewById(R.id.bt_Reduce);
        bt_buffer = (Button) findViewById(R.id.bt_buffer);
        bt_skip = (Button) findViewById(R.id.bt_skip);
        bt_scan = (Button) findViewById(R.id.bt_scan);
        bt_Concat = (Button) findViewById(R.id.bt_Concat);
        bt_Merge = (Button) findViewById(R.id.bt_Merge);
        bt_Defer = (Button) findViewById(R.id.bt_Defer);
        bt_Distinct = (Button) findViewById(R.id.bt_Distinct);
        bt_last = (Button) findViewById(R.id.bt_last);


    }

    @Override
    protected void initVariables() {
        mapclass = new MapClass();
        mapclass.setFlat("Map转换");
        flatMapclass = new FlatMapClass();
        flatMapclass.setFlat("FlatMap转换");
    }

    @Override
    protected void initEnvent() {
        super.initEnvent();
        bt_Simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new Observer<String>() 这个不是rxjava中new Action1<String>()
                Observable.just("haha").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                                Log.e(TAG, "onSubscribe: " + d.isDisposed());
                            }

                            @Override
                            public void onNext(String s) {
                                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.toString());

                            }

                            @Override
                            public void onComplete() {
                                Log.e(TAG, "onComplete: Simple");
                            }
                        });
            }
        });
        bt_Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.just(mapclass).map(new Function<MapClass, String>() {
                    @Override
                    public String apply(@NonNull MapClass mapClass) throws Exception {
                        String flat = mapClass.getFlat();
                        return flat;
                    }
                }).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });
        bt_FlatMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.just(flatMapclass).flatMap(new Function<FlatMapClass, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull FlatMapClass flatMapClass) throws Exception {
                        return Observable.just(flatMapClass.getFlat().toString());
                    }
                }).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });
        bt_Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.just(10).filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer > 0;
                    }
                }).subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Toast.makeText(getApplicationContext(), "Filter:" + integer, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });
        bt_Interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.interval(2,8, TimeUnit.SECONDS).take(3).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Long aLong) {
                                //aLong这个代表是第几次执行，技术重0开始
                                Log.e(TAG, "onNext: " + aLong);
                                Toast.makeText(getApplicationContext(), "Interval相应", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        bt_Zip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        /*        Observable.zip(Observable.just(3), Observable.just(3),
                        new BiFunction<Integer, Integer, Integer>() {
                            @Override
                            public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                                if (integer.equals(integer2))
                                {
                                    return integer;
                                }else {
                                    return 100;
                                }

                            }
                        }).subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Toast.makeText(getApplicationContext(), "Zip:"+integer, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*/
                //zip就是哪两个Observable中的数据进行一些操作而已，个人认为用的场景不多
                int[] a1 = {1, 2, 3};
                int[] a2 = {3, 4, 5};
                Observable.zip(Observable.fromArray(a1), Observable.fromArray(a2), new BiFunction<int[], int[], Integer>() {
                    @Override
                    public Integer apply(@NonNull int[] ints, @NonNull int[] ints2) throws Exception {
                        for (int i = 0; i < ints.length; i++) {
                            for (int j = 0; j < ints2.length; j++) {
                                if (ints[i] == ints2[j]) {
                                    return ints[i];
                                }
                            }
                        }
                        return 100;
                    }
                }).subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Toast.makeText(getApplicationContext(), "Zip:" + integer, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });
        bt_disposables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //下面这么做还是像开启线程池那样里等线程任务销毁按返回键才好使，里指定这个 .subscribeOn(Schedulers.io())
/*                DisposableObserver<String> disposableObserver = new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, "onNext:------- "+s);
                        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete:------ ");
                    }
                };
                getDisposables().subscribe(disposableObserver);

                disposables.add(disposableObserver);*/

                disposables.add(getDisposables()
                        .subscribeOn(Schedulers.io())//这块一定要指定线程要不那个返回建你懂得
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<String>() {
                            @Override
                            public void onNext(String s) {
                                Log.e(TAG, "onNext:------- " + s);
                                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                Log.e(TAG, "onComplete:------ ");
                            }
                        }));

            }
        });
        bt_Take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Observable.just(1, 2, 3, 4, 5).take(3).subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Toast.makeText(getApplicationContext(), integer.toString(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onNext:take " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });
        bt_Timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.timer(2, TimeUnit.SECONDS).subscribeOn(Schedulers.io())//这个可以不指定
                        .observeOn(AndroidSchedulers.mainThread())//这块必须指定要不不会更新UI
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Long aLong) {
                                Log.e(TAG, "Timer onNext: " + aLong);
                                Toast.makeText(getApplicationContext(), "Timer", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.toString());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        bt_SingleObserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这个just("Single")只能传单值
//                Single   参考http://www.jianshu.com/p/66a55abbadef
//                只发射一条单一的数据，或者一条异常通知，不能发射完成通知，其中数据与通知只能发射一个。
                Single.just("Single").subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        });
        bt_completable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Completable
//                只发射一条完成通知，或者一条异常通知，不能发射数据，其中完成通知与异常通知只能发射一个
     /*           Completable completable = Completable.timer(2, TimeUnit.SECONDS);
                completable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onComplete() {
                                Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });*/
                Completable.create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(CompletableEmitter e) throws Exception {
                        // SystemClock.sleep(2000); 这里要是指定io线程用这个会崩
                        //   只发射一条完成通知，或者一条异常通知
                        // e.onComplete();//发送完成通知
                        e.onError(new Exception("Completable测试异常"));
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onComplete() {
                                Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        //reduce 在本例子的工作的流程
        // integer->1  integer2->0
        // integer->1  integer2->2
        // integer->3  integer2->3
        //integer->6   integer2->4
        //return 6+4=10
        bt_Reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.just(1, 2, 3, 4).reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                }).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Toast.makeText(getApplicationContext(), integer.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
//        buffer(count, skip)` 从定义就差不多能看出作用了，
//        将 observable 中的数据按 skip（步长）分成最长不超过 count 的 buffer，然后生成一个 observable
        bt_buffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "看log", Toast.LENGTH_SHORT).show();
                Observable.just(1, 2, 3, 4, 5).buffer(3, 2).subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(@NonNull List<Integer> integers) throws Exception {
                        Log.e(TAG, "开始-----------------");
                        for (Integer integer : integers) {
                            Log.e(TAG, integer.toString());
                        }
                        Log.e(TAG, "结束-----------------");
                    }
                });
            }
        });
        bt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "看log", Toast.LENGTH_SHORT).show();
//                看名字也知道是干嘛的了，跳过一些数据，例子中跳过的是数据量，也可以跳过时间 skip(long time, TimeUnit unit)
                Observable.just(1, 2, 3, 4, 5).skip(2).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "skip accept: " + integer);
                    }
                });

            }
        });
        bt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "看log", Toast.LENGTH_SHORT).show();
                //scan 操作符作用和上面的 reduce 一致，唯一区别是 reduce 是个只追求结果的坏人，而 scan 会始终如一地把每一个步骤都输出。
                Observable.just(1, 2, 3, 4, 5).scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                }).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "scan accept: " + integer);
                    }
                });
            }
        });
        bt_Concat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //很简单的就是连接两个Observable
                Observable.concat(Observable.just(1, 2, 3), Observable.just(4, 5, 6))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception {
                                Log.e(TAG, "Concat accept: " + integer);
                                SystemClock.sleep(2000);
                            }
                        });
            }
        });
        bt_Merge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //注意它和 concat 的区别在于，不用等到 发射器 A 发送完所有的事件再进行发射器 B 的发送。
                //意思就是merge被观察者们发送事件都是并发的呗
                Observable.merge(Observable.just(1, 2, 3), Observable.just(4, 5, 6))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception {
                                Log.e(TAG, "Merge accept: " + integer);
                                SystemClock.sleep(2000);
                            }
                        });
            }
        });
        bt_Defer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //简单地时候就是每次订阅都会创建一个新的Observable，并且如果没有被订阅，就不会产生新的Observable
                Observable.defer(new Callable<ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> call() throws Exception {
                        return Observable.just(1, 2, 3, 4, 5);
                    }
                }).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "Defer accept: " + integer);
                    }
                });
            }
        });
        bt_Distinct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //去重操作符，简单的作用就是去重。
                Observable.just(1, 2, 3, 4, 4, 3, 2, 6, 6, 5).distinct().subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.e(TAG, "Distinct accept: " + integer);
                    }
                });
            }
        });
        bt_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这个last的意思就是取最后一个值，last(100)这个代表取得默认值
                Observable.just(1, 2, 3, 4, 5).last(100).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Toast.makeText(getApplicationContext(), integer.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }


    private Observable<String> getDisposables() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                if (!e.isDisposed()) {
                    Log.e(TAG, "create:------- ");
                    SystemClock.sleep(3000);
                    e.onNext("disposables");
                    e.onComplete();

                }
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
