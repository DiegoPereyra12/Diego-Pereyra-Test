package com.example.axosnettestjava.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.axosnettestjava.retrofit.RetroInstance;
import com.example.axosnettestjava.retrofit.RetroService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateViewModel extends ViewModel {

    private final MutableLiveData<UpdateViewModel.ReceiptState> receiptState;

    RetroService retroService2 = RetroInstance.postRetrofit().create(RetroService.class);

    public enum ReceiptState {
        RECEIPT_UPDATED_SUCCESS,
        RECEIPT_UPDATED_FAILURE
    }

    public UpdateViewModel() {
        receiptState = new MutableLiveData<>();
    }

    public MutableLiveData<UpdateViewModel.ReceiptState> getReceiptStateObservable() {
        return receiptState;
    }

    public void updateReceipt(Integer id, String provider, String amount, String comment, String emissionDate, String currencyCode) {
        Call<Void> call = retroService2.updateReceipt(id, provider, comment, amount, emissionDate, currencyCode);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                receiptState.setValue(ReceiptState.RECEIPT_UPDATED_SUCCESS);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                receiptState.setValue(ReceiptState.RECEIPT_UPDATED_FAILURE);
            }
        });
    }

}
