
package com.example.okrx.adapter;


import com.example.okgo.adapter.AdapterParam;
import com.example.okgo.adapter.Call;
import com.example.okgo.adapter.CallAdapter;

import rx.Single;

public class SingleBody<T> implements CallAdapter<T, Single<T>> {
    @Override
    public Single<T> adapt(Call<T> call, AdapterParam param) {
        ObservableBody<T> body = new ObservableBody<>();
        return body.adapt(call, param).toSingle();
    }
}
