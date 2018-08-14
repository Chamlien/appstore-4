
package com.example.okrx2.adapter;


import com.example.okgo.adapter.AdapterParam;
import com.example.okgo.adapter.Call;
import com.example.okgo.adapter.CallAdapter;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class FlowableBody<T> implements CallAdapter<T, Flowable<T>> {
    @Override
    public Flowable<T> adapt(Call<T> call, AdapterParam param) {
        ObservableBody<T> observable = new ObservableBody<>();
        return observable.adapt(call, param).toFlowable(BackpressureStrategy.LATEST);
    }
}
