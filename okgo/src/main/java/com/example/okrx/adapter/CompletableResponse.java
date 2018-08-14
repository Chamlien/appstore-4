package com.example.okrx.adapter;


import com.example.okgo.adapter.AdapterParam;
import com.example.okgo.adapter.Call;
import com.example.okgo.adapter.CallAdapter;

import rx.Completable;

public class CompletableResponse<T> implements CallAdapter<T, Completable> {
    @Override
    public Completable adapt(Call<T> call, AdapterParam param) {
        ObservableResponse<T> body = new ObservableResponse<>();
        return body.adapt(call, param).toCompletable();
    }
}
