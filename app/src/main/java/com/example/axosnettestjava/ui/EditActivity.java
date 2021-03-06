package com.example.axosnettestjava.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.axosnettestjava.R;
import com.example.axosnettestjava.databinding.ActivityEditBinding;
import com.example.axosnettestjava.viewmodel.UpdateViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();

    public static final String ID_PARAM = "id";
    public static final String PROVIDER_PARAM = "provider";
    public static final String AMOUNT_PARAM = "amount";
    public static final String COMMENT_PARAM = "coment";
    public static final String EMISSION_DATE_PARAM = "emissionDate";
    public static final String CURRENCY_CODE_PARAM = "currencyCode";

    ActivityEditBinding binding;
    private UpdateViewModel viewModel;
    private EditText provider, amount, comment, emissionDate;
    Spinner spinner;
    String currencyCodeS;
    private Button btnUpdateReceipt;
    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initViews();
        Intent intent = getIntent();
        String idS = intent.getStringExtra(ID_PARAM);
        id = Integer.parseInt(idS);
        String providerS = intent.getStringExtra(PROVIDER_PARAM);
        String amountS = intent.getStringExtra(AMOUNT_PARAM);
        String commentS = intent.getStringExtra(COMMENT_PARAM);
        currencyCodeS = intent.getStringExtra(CURRENCY_CODE_PARAM);
        String emissionDateS = intent.getStringExtra(EMISSION_DATE_PARAM);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_code_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        setTextViews(providerS, amountS, commentS, currencyCodeS, emissionDateS);

        DatePickerDialog.OnDateSetListener date = (view12, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        emissionDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(EditActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyCodeS = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnUpdateReceipt.setOnClickListener(view1 -> checkTextView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getReceiptStateObservable().observe(this, receiptState -> {
            switch (receiptState) {
                case RECEIPT_UPDATED_SUCCESS:
                    finish();
                    break;
                case RECEIPT_UPDATED_FAILURE:
                    Toast.makeText(this, "Failed to update receipt", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        emissionDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void setTextViews(String providerS, String amountS, String commentS, String currencyCodeS, String emissionDateS) {
        provider.setText(providerS);
        amount.setText(amountS);
        comment.setText(commentS);
        emissionDate.setText(emissionDateS);
        switch (currencyCodeS) {
            case "USD":
                spinner.setSelection(0);
                break;
            case "EUR":
                spinner.setSelection(2);
                break;
            case "MXN":
                spinner.setSelection(1);
                break;
        }

    }

    private void initViews() {
        btnUpdateReceipt = binding.button;
        provider = binding.etProvider;
        amount = binding.etAmount;
        comment = binding.etComment;
        emissionDate = binding.etEmissionDate;
        spinner = binding.spinner;
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(UpdateViewModel.class);
    }

    private void checkTextView() {
        String providerS = provider.getText().toString();
        String amountS = amount.getText().toString();
        String commentS = comment.getText().toString();
        String emissionDateS = emissionDate.getText().toString();

        if (TextUtils.isEmpty(providerS)) {
            provider.setError(getString(R.string.write_a_provider));
        } else if (TextUtils.isEmpty(amountS)) {
            amount.setError(getString(R.string.write_a_amount));
        } else if (TextUtils.isEmpty(emissionDateS)) {
            emissionDate.setError(getString(R.string.write_a_emission_date));
        } else {
            viewModel.updateReceipt(id, providerS, amountS, commentS, emissionDateS, currencyCodeS.toUpperCase());
        }
    }
}