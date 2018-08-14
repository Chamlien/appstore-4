
package com.example.okrx.adapter;

import com.example.okgo.adapter.AdapterParam;
import com.example.okgo.adapter.Call;
import com.example.okgo.adapter.CallAdapter;
import com.example.okgo.model.Response;

import rx.Single;

public class SingleResponse<T> implements CallAdapter<T, Single<Response<T>>> {
    @Override
    public Single<Response<T>> adapt(Call<T> call, AdapterParam param) {
        ObservableResponse<T> body = new ObservableResponse<>();
        return body.adapt(call, param).toSingle();
    }
}
