package com.example.okrx.subscribe;


import com.example.okgo.exception.HttpException;
import com.example.okgo.model.Response;

import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.exceptions.OnCompletedFailedException;
import rx.exceptions.OnErrorFailedException;
import rx.exceptions.OnErrorNotImplementedException;
import rx.plugins.RxJavaHooks;

public final class BodyOnSubscribe<T> implements OnSubscribe<T> {
    private final OnSubscribe<Response<T>> upstream;

    public BodyOnSubscribe(OnSubscribe<Response<T>> upstream) {
        this.upstream = upstream;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        upstream.call(new BodySubscriber<>(subscriber));
    }

    private static class BodySubscriber<R> extends Subscriber<Response<R>> {

        private final Subscriber<? super R> subscriber;
        private boolean subscriberTerminated;

        BodySubscriber(Subscriber<? super R> subscriber) {
            super(subscriber);
            this.subscriber = subscriber;
        }

        @Override
        public void onNext(Response<R> response) {
            if (response.isSuccessful()) {
                subscriber.onNext(response.body());
            } else {
                subscriberTerminated = true;
                Throwable t = new HttpException(response);
                try {
                    subscriber.onError(t);
                } catch (OnCompletedFailedException | OnErrorFailedException | OnErrorNotImplementedException e) {
                    RxJavaHooks.getOnError().call(e);
                } catch (Throwable inner) {
                    Exceptions.throwIfFatal(inner);
                    RxJavaHooks.getOnError().call(new CompositeException(t, inner));
                }
            }
        }

        @Override
        public void onError(Throwable throwable) {
            if (!subscriberTerminated) {
                subscriber.onError(throwable);
            } else {
                Throwable broken = new AssertionError("This should never happen! Report as a bug with the full stacktrace.");
                //noinspection UnnecessaryInitCause Two-arg AssertionError constructor is 1.7+ only.
                broken.initCause(throwable);
                RxJavaHooks.getOnError().call(broken);
            }
        }

        @Override
        public void onCompleted() {
            if (!subscriberTerminated) {
                subscriber.onCompleted();
            } else {
                Throwable broken = new AssertionError("This should never happen! Report as a bug with the full stacktrace.");
                RxJavaHooks.getOnError().call(broken);
            }
        }
    }
}
