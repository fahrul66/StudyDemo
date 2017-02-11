package com.work.wulei.Rxjava.java.ObserverPattern;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func1;
import rx.observers.SerializedObserver;
import rx.schedulers.Schedulers;

/**
 * Created by wulei on 2016/12/31.
 */

public class Client {

    public static void main(String[] args) {
//            Player player1 = new Player("小米");
//            Player player2 = new Player("小化");
//            Player player3 = new Player("小刚");
//            Player player4 = new Player("小明");
//            Player player5 = new Player("小李");
//
//          ConcreteAllyControlCenter center = new ConcreteAllyControlCenter("无敌");
//
//          center.join(player1);
//          center.join(player2);
//          center.join(player3);
//          center.join(player4);
//          center.join(player5);
//
////          player1.help();
////          player2.help();
//
//          player3.beAttacked(center);
//
//      Subscription subscription = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("1");
//                subscriber.onNext("2");
//
////                subscriber.onError(new Throwable("我是exception"));
//                subscriber.onNext("3");
//                subscriber.onNext("4");
//                subscriber.onCompleted();
//            }
//        }).subscribe(new Observer<String>() {
//            @Override
//            public void onCompleted() {
//                System.out.println("事件完毕");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                System.out.println("exception come out ");
//            }
//
//            @Override
//            public void onNext(String s) {
//                System.out.println(s);
//            }
//        });
//
//       System.out.println(subscription.isUnsubscribed());

        /**
         * 方式2
         */
        /*Observable.just("1","2","3","4").subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("事件完毕");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        });*/

        /**
         方式3
         *
         */
/*        String[] s = {"1","2","3","4"};
        Observable.from(s).subscribe(new Subscriber<String>() {

            @Override
            public void onStart() {
                super.onStart();
                System.out.println("i start 了 start 数据 清零重置");
            }

            @Override
            public void onCompleted() {
                System.out.println("事件完毕");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        });*/

        /**
         * 不完全调用，action
         */
  /*      Action1<String> action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        };
        Action1<Throwable> error = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
               System.out.println(throwable.toString());
            }
        } ;

        Action0 compeleted = new Action0() {
            @Override
            public void call() {
            System.out.println("事件发送完毕了，我是action");
            }
        };

        *//**
         * subscribe(action,,);原理接口回调，内部实例化一个Subscriber对象，在onnext,onerror,oncompeleted
         * 处调用相应的action.call;
         *//*
        Observable.just("1","2","3","4").subscribe(action1,error,compeleted);*/


        /**
         * rxjava的线程（scheduler）：1.scheduler.immediate(),当前线程
         *                           2.scheduler.newThread(),新开一个线程
         *                           3.scheduler.io(),io线程,进行数据库读取，网络等操作
         *                           4.scheduler.computation(),线程计算
         *                           5.AndroidScheduler.mainThread(),android特有的主线程
         *
         *        通过subscribeOn(..)指定订阅是，事件产生的线程，observerOn(..)指定事件处理回调的线程
         *
         */
 /*       Observable.just("1","2","3")
                  .map(new Func1<String, Integer>() {
                      @Override
                      public Integer call(String s) {
                          return Integer.parseInt(s);
                      }
                  }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println(integer);
            }
        });*/

        /**
         * 事件在发送过程中的变换，map（一对一），flatmap（一对多）
         */
        Course c = new Course(20);
        Course c1 = new Course(23);
        Course c2 = new Course(22);
        Course c3 = new Course(26);
        Course c4 = new Course(29);

        final List<Course> list = Arrays.asList(c, c1, c2, c3, c4);

        /**
         * onSubscriber是继承action1<Subscriber<? extends T>>,
         * 所以重写的call参数为subscriber对象
         *//*
        /*Observable.create(new Observable.OnSubscribe<Student>() {
            @Override
            public void call(Subscriber<? super Student> subscriber) {
                subscriber.onNext(new Student("武磊", list));
            }
        }).flatMap(new Func1<Student, Observable<Course>>() {
            @Override
            public Observable<Course> call(Student student) {
                //将数据重新生成observable对象，将老的subscriber替换为新的，一条条数据发送
                return Observable.from(student.getCourseInstance());
            }
        }).subscribe(new Action1<Course>() {
            @Override
            public void call(Course course) {
              int grades = course.getGrades();

                System.out.println("flatMap转换的数据:"+grades);

            }
        });*/

        /**
         * 自身变换Observable
         *
         */
        //compose方法
//        public <R> Observable<R> compose (Transformer < ? super T,?extends R > transformer){
//            return ((Transformer<T, R>) transformer).call(this);
//        }

        //Transformer接口
//         public interface Transformer<T, R> extends Func1<Observable<T>, Observable<R>> {
//         // cover for generics insanity
//         }


        /*class transformerMyObservable implements Observable.Transformer<String, Integer> {
            @Override
            public Observable<Integer> call(Observable<String> stringObservable) {
                return stringObservable.lift()
                                       .lift()
                                       .lift();
            }
        }

        Observable.just("1").compose(new transformerMyObservable()).subscribe();
        Observable.just("w").compose(new transformerMyObservable()).subscribe();
        Observable.just("e").compose(new transformerMyObservable()).subscribe();
        Observable.just("t").compose(new transformerMyObservable()).subscribe();*/

        /**
         * scheduler,其他用法，
         * 1.subscribeOn()方法，返回的Observable对象在OnSubscribe的时候切换线程，通知old Observable发送subscriber
         * 它指示Observable在一个指定的调度器上创建（只作用于被观察者创建阶段）。只能指定一次，如果指定多次则以第一次为准.
         * 2.observerOn()方法，返回的Observable对象在其new Observable的subscriber对象中切换线程，之后
         * 3.可以频繁的切换线程
         * 4.在subscribe()方法调用时，其中的onStart()会先被调用，但是不能指定线程，而是只能执行在 subscribe() 被调用时的线程。
         * 5.Observable.doOnSubscribe(),默认也是在subscribe()调用时的线程，在执行到subscribe()方法是时调用，但是如果此方法后面有subscribeOn()方法，则会在此指定
         * 的线程中调用。
         */

        class mapFunc implements Func1<String, Integer> {
            @Override
            public Integer call(String s) {
                return Integer.parseInt(s);
            }
        }

        class doAction implements Action0 {

            @Override
            public void call() {
                //here initialization
            }
        }

        class subAction1 implements Action1<String> {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        }

        Observable.just("2")       //ui线程，new 通知 old Observable 在onSubscribe切换
                .doOnSubscribe(new doAction()) //指定在ui线程中初始化
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread()) //在new 通知 old Observable 在新的Observable中切换
//                .map(new mapFunc())
                .subscribe(new subAction1());  //新线程


        /**
         * backpressure:是一种在异步环境下的一种策略，通过下数据流告诉上数据流，要发送多少数据的策略。
         * 问题：如果在异步中，Observable发送数据的速度大于observer处理的速度，则数据就会堆积，在rxjava
         * 1.0时，会出现MissingBackpressureException,而在rxjava2.0中则不会出现异常，知道数据堆积过多，没有及时处理
         * 知道出现OOM内存溢出。怎么办？backpressure就是为了解决这个问题的。
         *
         */

        //  Caused by: rx.exceptions.MissingBackpressureException
          Observable.interval(1, TimeUnit.MILLISECONDS)
//                    .just("2")
                    .observeOn(Schedulers.newThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long s) {
                            try {
                                Thread.sleep(1000);
                                System.out.println(s++);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        /**
         * 在Rxjava1.0时,
         * 1.Hot Observable(只要创建observable，就会发送数据)如interval，不支持backpressure
         * onBackpressureBuffer():将数据缓存，调用request()时，发数据。
         * onBackpressureDrop():只有在subscribe方法中调用request()时，才会发送数据，其他时间清除数据
         * 2.cool Observable(只有在subscribe的时候，才会发送)，平时基本使用，支持backpressures
         *
         * 在Rxjava2.0时，就是为了解决backpressure的问题
         */
    }
}
