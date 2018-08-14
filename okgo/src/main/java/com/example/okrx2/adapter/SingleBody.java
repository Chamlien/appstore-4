
package com.example.okrx2.adapter;


import com.example.okgo.adapter.AdapterParam;
import com.example.okgo.adapter.Call;
import com.example.okgo.adapter.CallAdapter;

import io.reactivex.Single;

public class SingleBody<T> implements CallAdapter<T, Single<T>> {
    @Override
    public Single<T> adapt(Call<T> call, AdapterParam param) {
        ObservableBody<T> observable = new ObservableBody<>();
        return observable.adapt(call, param).singleOrError();
    }
}
