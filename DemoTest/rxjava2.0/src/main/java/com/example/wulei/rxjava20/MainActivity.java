package com.example.wulei.rxjava20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

//        mTextView = (TextView) findViewById(R.id.tv);

        mTextView.setText("butterknife");


        /**                                                       不支持backpressure
         * Observable(abstract) 父类 ObservableSource(interface)  -------------------> Observer
         *
         */
   /*     Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("1");
                e.onNext("2");
                e.onNext("3");
                e.onNext("4");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //onstart,取消订阅
                        mTextView.append("开始");

//                        d.dispose(); 取消订阅
                    }

                    @Override
                    public void onNext(String s) {
                        mTextView.append(s + ".....next");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mTextView.append("completed");
                    }
                });*/
        /**
         * 不完全调用action0->action , action1 ->Consumer , action2 ->BiConsumer
         * Function -> Function , Function2 -> BiFunction
         *
         * fromArray(T[] t) 是一次性发送一个数组 在ObservableFromArray中重写了方法subscribeActual
         */
/*        Consumer<int[]> onNext = new Consumer<int[]>() {
            @Override
            public void accept(int[] integer) throws Exception {
                mTextView.append("**"+ Arrays.toString(integer)+"**");
            }
        };

        Consumer<Throwable> onError = new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mTextView.append("**"+throwable.toString()+"**");
            }
        };

        Consumer<Disposable> onSubscribe = new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                mTextView.append("**"+disposable.isDisposed()+"**");
            }
        };

        final Action onCompeleted = new Action() {
            @Override
            public void run() throws Exception {
                  mTextView.append("**onCompeleted 调用了**");
            }
        };

        int[] s = {1,2,3,4};
        Observable.fromArray(s)
                .subscribe(onNext,onError,onCompeleted,onSubscribe);*/

        /**
         * 一个一个发送just
         */
/*        Observable.just("1","2","3","4")
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) throws Exception {
                        return Integer.parseInt(s);
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mTextView.append("\r\n"+integer);
                    }
                });*/

        /**
         *
         * Flowable (abstract) 父类 Publisher(interface)--------> Subscriber(观察者)
         * 支持backpressure,指定Backpressure类型，通过在onSunscribe()（相当于onstart）调用request(n)告诉上流发送数据量，并且
         * 会立即发送，不会执行request后面的代码，最后执行。
         *
         * flowable--------------observable都有各自的相应的类和接口
         * 父：publisher(subscriber)          observableSource(observer)
         * 观察者：subscriber(onSubscribe(Subscription s))      observer(onSubscribe(Disposable d))
         *
         * public interface Subscription {                      public interface Disposable {
         * public void request(long n); (支持backpressure)       void dispose();
         * public void cancel();  (取消订阅)                      boolean isDisposed();
         * }                                                      }
         */
//        Flowable.create(new FlowableOnSubscribe<String>() {
//            @Override
//            public void subscribe(FlowableEmitter<String> e) throws Exception {
//                e.onNext("1");
//                e.onNext("2");
//                e.onNext("3");
//                e.onNext("4");
//                e.onNext("5");
//                e.onNext("6");
//                e.onComplete();
//            }
//        }, BackpressureStrategy.BUFFER)
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        //初始化
//                        mTextView.append("\r\nonSubscribe start");
//
//                        s.request(Long.MAX_VALUE);//不使用backpressure
//
//                        mTextView.append("\r\nonSubscribe end");
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        mTextView.append("\r\nonNext :" + s);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        mTextView.append("\r\nonComplete");
//                    }
//                });

        Flowable.range(1, 5)
                //并不会取消，除非后面有真正取消事件如onCancel,dispose.
                //使用take(n)操作符，会在onNext执行n个事件后，取消。
                .doOnCancel(new Action() {
                    @Override
                    public void run() throws Exception {
                        mTextView.append("\r\nonCancel");
                    }
                })
                .take(2)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        //初始化
                        mTextView.append("\r\nonSubscribe start");

                        s.request(Long.MAX_VALUE);//不使用backpressure

                        mTextView.append("\r\nonSubscribe end");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        mTextView.append("\r\nonNext :" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        mTextView.append("\r\nonComplete");
                    }
                });
        /**
         * subscribe(Observer&&Subscriber) 订阅方法的参数为observer或者subscriber时返回为void，1.0为subscription
         * 而subscribe(Consumer) 参数为Consumer时，返回的是Disposable对象，用来dispose取消订阅事件，避免OOM
         * 而Observer，Subscriber参数里面onSubscribe(Disposable&&Subscription) 可以内部取消订阅
         * 想要外部取消，需要使用subscribeWith()返回的是Subscriber或者observer对象，
         *
         * 所以要使用Observer的子类ResourceObserver(也实现了Disposable),Subscriber的子类ResourceSubscriber(也实现了Disposable)
         *
         *  然后使用 CompsiteDispoasble.add()，subscriberWith返回的值，进行统一取消订阅。
         */
    }
}
