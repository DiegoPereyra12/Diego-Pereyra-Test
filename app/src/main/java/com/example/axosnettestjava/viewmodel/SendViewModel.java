package com.example.axosnettestjava.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.axosnettestjava.retrofit.RetroInstance;
import com.example.axosnettestjava.retrofit.RetroService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendViewModel extends ViewModel {

    private final MutableLiveData<SendViewModel.ReceiptState> receiptState;

    RetroService retroService2 = RetroInstance.postRetrofit().create(RetroService.class);

    public enum ReceiptState {
        RECEIPT_ADDED_SUCCESS,
        RECEIPT_ADDED_FAILURE
    }
    public SendViewModel() {
        receiptState = new MutableLiveData<>();
    }
    public MutableLiveData<SendViewModel.ReceiptState> getReceiptStateObservable() {
        return receiptState;
    }

    public void sendReceipt(String provider, String amount, String comment, String emissionDate, String currencyCode) {
        Call<Void> call = retroService2.addReceipt(provider, comment, amount, emissionDate, currencyCode);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                receiptState.setValue(SendViewModel.ReceiptState.RECEIPT_ADDED_SUCCESS);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                receiptState.setValue(SendViewModel.ReceiptState.RECEIPT_ADDED_FAILURE);
            }
        });
    }
}