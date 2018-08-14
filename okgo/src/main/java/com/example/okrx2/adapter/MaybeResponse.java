
package com.example.okrx2.adapter;


import com.example.okgo.adapter.AdapterParam;
import com.example.okgo.adapter.Call;
import com.example.okgo.adapter.CallAdapter;
import com.example.okgo.model.Response;

import io.reactivex.Maybe;

public class MaybeResponse<T> implements CallAdapter<T, Maybe<Response<T>>> {
    @Override
    public Maybe<Response<T>> adapt(Call<T> call, AdapterParam param) {
        ObservableResponse<T> observable = new ObservableResponse<>();
        return observable.adapt(call, param).singleElement();
    }
}
