package com.example.okrx.adapter;


import com.example.okgo.adapter.AdapterParam;
import com.example.okgo.adapter.Call;
import com.example.okgo.adapter.CallAdapter;
import com.example.okgo.model.Response;

import rx.Observable;

public class ObservableResponse<T> implements CallAdapter<T, Observable<Response<T>>> {
    @Override
    public Observable<Response<T>> adapt(Call<T> call, AdapterParam param) {
        Observable.OnSubscribe<Response<T>> subscribe = AnalysisParams.analysis(call, param);
        return Observable.create(subscribe);
    }
}
