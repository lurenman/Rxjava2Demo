package com.example.lurenman.rxjava2demo.activity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lurenman.rxjava2demo.R;
import com.example.lurenman.rxjava2demo.entity.RxbusEntity;
import com.example.lurenman.rxjava2demo.utils.RxBus;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author: baiyang.
 * Created on 2017/11/15.
 * 这个东西就是操作符PublishSubject
 */

public class RxBusActivity extends BaseActivity {
    private Button bt_rxbus;
    private Disposable mDisposable;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_rxbus);
        bt_rxbus = (Button) findViewById(R.id.bt_rxbus);
    }

    @Override
    protected void initVariables() {
        //这个东西必须取消订阅否则重进几次会执行多次，可以查看操作符可以理解
 /*       PublishSubject<Object> objectObservable = (PublishSubject<Object>) RxBus.getInstance().toObservable();
          objectObservable.subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (o instanceof RxbusEntity)
                {
                    String message = ((RxbusEntity) o).getMessage();
                    Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        //这块开始注册订阅
        PublishSubject<Object> objectObservable = (PublishSubject<Object>) RxBus.getInstance().toObservable();
        objectObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }
            @Override
            public void onNext(Object o) {
                if (o instanceof RxbusEntity) {
                    String message = ((RxbusEntity) o).getMessage();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    protected void initEnvent() {
        super.initEnvent();
        bt_rxbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送事件
                RxBus.getInstance().send(new RxbusEntity("我是Rxbus"));

            }
        });
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //执行订阅取消操作 哈哈妙解
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
