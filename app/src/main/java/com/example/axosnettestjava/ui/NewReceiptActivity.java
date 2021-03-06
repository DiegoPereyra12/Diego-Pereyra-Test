package com.example.axosnettestjava.ui;

import android.app.DatePickerDialog;
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
import com.example.axosnettestjava.databinding.ActivityNewReceiptBinding;
import com.example.axosnettestjava.viewmodel.SendViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewReceiptActivity extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();

    ActivityNewReceiptBinding binding;
    private SendViewModel viewModel;
    Spinner spinner;
    String currencyCodeS;
    private EditText provider, amount, comment, emissionDate;
    private Button btnAddReceipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewReceiptBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        init();
        btnAddReceipt.setOnClickListener(view1 -> checkTextView());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_code_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        DatePickerDialog.OnDateSetListener date = (view12, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        emissionDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(NewReceiptActivity.this, date, myCalendar
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
    }
    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getReceiptStateObservable().observe(this, receiptState -> {
            switch (receiptState) {
                case RECEIPT_ADDED_SUCCESS:
                    finish();
                    break;
                case RECEIPT_ADDED_FAILURE:
                    Toast.makeText(this, "Failed to add new receipt", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        emissionDate.setText(sdf.format(myCalendar.getTime()));
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
        }  else {
            viewModel.sendReceipt(providerS, amountS, commentS, emissionDateS, currencyCodeS.toUpperCase());
        }
    }
    private void init() {
        btnAddReceipt = binding.button;
        provider = binding.etProvider;
        amount = binding.etAmount;
        comment = binding.etComment;
        emissionDate = binding.etEmissionDate;
        spinner = binding.spinner;
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SendViewModel.class);
    }
}