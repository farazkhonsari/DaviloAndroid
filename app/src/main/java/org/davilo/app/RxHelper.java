package org.davilo.app;


import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class RxHelper {
    public <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    public <T> ObservableTransformer<T, T> addRegularRetryAndDelay() {
//        if(MyLog.isDebugAble) {
//            return addRetryAndDelay(1, 3);
//        }else{
        return addRetryAndDelay(0, 2, 5, 5, 10);

//        }
    }

    public <T> ObservableTransformer<T, T> addRetryAndDelay(int retryCount, int delaySecond) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.retryWhen(errors -> {
                    return errors.zipWith(Observable.range(1, 1 + retryCount), (error, i) -> {
                        if (i >= 1 + retryCount) {
                            if (error instanceof Exception) {
                                throw ((Exception) error);
                            } else {
                                throw new Exception(error);
                            }
                        }
                        return i;

                    }).flatMap(i -> {
                        Log.e("addRetryAndDelay", "delay retry by " + delaySecond + " second(s)");
                        return Observable.timer(delaySecond, TimeUnit.SECONDS);
                    });
                });
            }

        };
    }

    public <T> ObservableTransformer<T, T> addRetryAndDelay(int... delay) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.retryWhen(errors -> {
                    return errors.zipWith(Observable.range(1, 1 + delay.length), (error, i) -> {
                        boolean forceThrow = false;
                        if (error instanceof HttpException) {
                            int code = ((HttpException) error).code();
                            if (code <= 501) {
                                forceThrow = true;
                            }
                        }
                        if (i >= 1 + delay.length || forceThrow) {
                            throw error;
                        }
                        return i;

                    }).flatMap(i -> {
                        Log.e("addRetryAndDelay", "delay retry by " + delay[i - 1] + " second(s)");
                        return Observable.timer(delay[i - 1], TimeUnit.SECONDS);
                    });
                });
            }

        };
    }

    public <T> ObservableTransformer<T, T> addRetryAndDelayMilliSecond(int retryCount, int delayMillis) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.retryWhen(errors -> {
                    return errors.zipWith(Observable.range(1, 1 + retryCount), (error, i) -> {
                        if (i >= 1 + retryCount) {
                            throw new Exception(error);
                        }
                        return i;

                    }).flatMap(i -> {
                        return Observable.timer(delayMillis, TimeUnit.MILLISECONDS);
                    });
                });
            }

        };
    }


    public <T> ObservableTransformer<T, T> addInfiniteRetryAndDelay(int delaySecond) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.retryWhen(errors -> {
                    return errors.flatMap(i -> {
                        Log.e("addInfinitRetryAndDelay", "delay retry by " + delaySecond + " second(s)");
                        return Observable.timer(delaySecond, TimeUnit.SECONDS);
                    });
                });
            }

        };
    }

    public <T> ObservableTransformer<T, T> addInfiniteRetryAndDelayMiliSecond(long miliSecond) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.retryWhen(errors -> {
                    return errors.flatMap(i -> {

                        return Observable.timer(miliSecond, TimeUnit.MILLISECONDS);
                    });
                });
            }

        };
    }


}
