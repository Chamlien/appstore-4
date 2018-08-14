/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.okrx.subscribe;


import com.example.okgo.adapter.Call;
import com.example.okgo.callback.Callback;
import com.example.okgo.model.Progress;
import com.example.okgo.model.Response;
import com.example.okgo.request.base.Request;

import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.exceptions.Exceptions;


public final class CallEnqueueOnSubscribe<T> implements OnSubscribe<Response<T>> {
    private final Call<T> originalCall;

    public CallEnqueueOnSubscribe(Call<T> originalCall) {
        this.originalCall = originalCall;
    }

    @Override
    public void call(final Subscriber<? super Response<T>> subscriber) {
        // Since Call is a one-shot type, clone it for each new subscriber.
        Call<T> call = originalCall.clone();
        final CallArbiter<T> arbiter = new CallArbiter<>(call, subscriber);
        subscriber.add(arbiter);
        subscriber.setProducer(arbiter);

        call.execute(new Callback<T>() {
            @Override
            public T convertResponse(okhttp3.Response response) throws Throwable {
                // okrx 使用converter转换，不需要这个解析方法
                return null;
            }

            @Override
            public void onStart(Request<T, ? extends Request> request) {
            }

            @Override
            public void onSuccess(Response<T> response) {
                arbiter.emitNext(response);
            }

            @Override
            public void onCacheSuccess(Response<T> response) {
                arbiter.emitNext(response);
            }

            @Override
            public void onError(Response<T> response) {
                Throwable throwable = response.getException();
                Exceptions.throwIfFatal(throwable);
                arbiter.emitError(throwable);
            }

            @Override
            public void onFinish() {
                arbiter.emitComplete();
            }

            @Override
            public void uploadProgress(Progress progress) {
            }

            @Override
            public void downloadProgress(Progress progress) {
            }
        });
    }
}
