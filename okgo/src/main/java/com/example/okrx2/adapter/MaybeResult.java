
package com.example.okrx2.adapter;


import com.example.okgo.adapter.AdapterParam;
import com.example.okgo.adapter.Call;
import com.example.okgo.adapter.CallAdapter;
import com.example.okgo.model.Result;

import io.reactivex.Maybe;

public class MaybeResult<T> implements CallAdapter<T, Maybe<Result<T>>> {
    @Override
    public Maybe<Result<T>> adapt(Call<T> call, AdapterParam param) {
        ObservableResult<T> observable = new ObservableResult<>();
        return observable.adapt(call, param).singleElement();
    }
}
