package com.example.okrx.adapter;

import com.example.okgo.adapter.AdapterParam;
import com.example.okgo.adapter.Call;
import com.example.okgo.adapter.CallAdapter;
import com.example.okgo.model.Response;
import com.example.okgo.model.Result;
import com.example.okrx.subscribe.ResultOnSubscribe;

import rx.Observable;


public class ObservableResult<T> implements CallAdapter<T, Observable<Result<T>>> {
    @Override
    public Observable<Result<T>> adapt(Call<T> call, AdapterParam param) {
        Observable.OnSubscribe<Response<T>> subscribe = AnalysisParams.analysis(call, param);
        ResultOnSubscribe<T> resultSubscribe = new ResultOnSubscribe<>(subscribe);
        return Observable.create(resultSubscribe);
    }
}
