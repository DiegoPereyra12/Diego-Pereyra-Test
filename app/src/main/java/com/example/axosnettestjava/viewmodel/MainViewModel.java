package com.example.axosnettestjava.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.axosnettestjava.ReceiptModel;
import com.example.axosnettestjava.retrofit.RetroInstance;
import com.example.axosnettestjava.retrofit.RetroService;
import com.example.axosnettestjava.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
/** CONSTANTS*/
    private final String COMMENT = "comment";
    private final String EMISSION_DATE = "emission_date";
    private final String CURRENCY_CODE = "currency_code";
    private final String AMOUNT = "amount";
    private final String PROVIDER = "provider";
    private final String ID  = "id";

    private final MutableLiveData<List<ReceiptModel>> receiptList;
    private final MutableLiveData<MainViewModel.ReceiptState> receiptState;


    RetroService retroService = RetroInstance.getRetrofit().create(RetroService.class);
    RetroService retroService2 = RetroInstance.postRetrofit().create(RetroService.class);

    public enum ReceiptState {
        RECEIPT_DELETED_SUCCESS,
        RECEIPT_DELETED_FAILURE
    }


    public MainViewModel() {
        receiptList = new MutableLiveData<>();
        receiptState = new MutableLiveData<>();
    }

    public MutableLiveData<List<ReceiptModel>> getReceiptListObservable() {
        return receiptList;
    }
    public MutableLiveData<MainViewModel.ReceiptState> getReceiptStateObservable() {
        return receiptState;
    }

    public void makeApiCall() {
        Call<String> call = retroService.getAll();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String string = response.body();
                    List list = parseReceiptResult(string);
                    receiptList.postValue(list);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                receiptList.setValue(null);
            }
        });
    }

    private List<ReceiptModel> parseReceiptResult(String string) {
        List<ReceiptModel> receiptList = new ArrayList<>();
        String arReceiptList = Utils.removeFirstAndLastChar(string);
        String readyReceiptList = arReceiptList.replaceAll("\\\\", "");
        try {
            JSONArray receiptJSONArray = new JSONArray(readyReceiptList);
            for (int i = 0; i < receiptJSONArray.length(); i++) {
                JSONObject jObj = receiptJSONArray.getJSONObject(i);
                Integer id = jObj.getInt(ID);
                String provider = jObj.getString(PROVIDER);
                Double amount = jObj.getDouble(AMOUNT);
                String emission_date = jObj.getString(EMISSION_DATE);
                String comment = jObj.getString(COMMENT);
                String currency_code = jObj.getString(CURRENCY_CODE);

                ReceiptModel receiptModel = new ReceiptModel(id, provider, amount, comment, emission_date, currency_code);
                receiptList.add(receiptModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return receiptList;
    }

    public void deleteReceipt(Integer id) {
        Call<Void> call = retroService2.deleteReceipt(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                receiptState.setValue(ReceiptState.RECEIPT_DELETED_SUCCESS);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                receiptState.setValue(ReceiptState.RECEIPT_DELETED_FAILURE);
            }
        });
    }
}