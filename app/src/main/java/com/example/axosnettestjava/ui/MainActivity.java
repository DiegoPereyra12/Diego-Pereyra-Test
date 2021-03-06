package com.example.axosnettestjava.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.axosnettestjava.R;
import com.example.axosnettestjava.ReceiptModel;
import com.example.axosnettestjava.adapter.ReceiptAdapter;
import com.example.axosnettestjava.databinding.ActivityMainBinding;
import com.example.axosnettestjava.viewmodel.MainViewModel;

import java.util.List;

import static com.example.axosnettestjava.ui.EditActivity.AMOUNT_PARAM;
import static com.example.axosnettestjava.ui.EditActivity.COMMENT_PARAM;
import static com.example.axosnettestjava.ui.EditActivity.CURRENCY_CODE_PARAM;
import static com.example.axosnettestjava.ui.EditActivity.EMISSION_DATE_PARAM;
import static com.example.axosnettestjava.ui.EditActivity.ID_PARAM;
import static com.example.axosnettestjava.ui.EditActivity.PROVIDER_PARAM;

public class MainActivity extends AppCompatActivity implements ReceiptAdapter.ItemClickListener {


    private MainViewModel viewModel;
    ActivityMainBinding binding;
    private ReceiptAdapter adapter;
    private List<ReceiptModel> receiptModelList;
    private Button btnAdd, edit;
    private TextView tvProvider, tvAmount, tvEmissionDate, tvCurrencyCode, tvComment, tvId;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        init();
        initView();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshRecycler();
            swipeRefreshLayout.setRefreshing(false);
        });
        refreshRecycler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvProvider.setText("");
        tvAmount.setText("");
        tvComment.setText("");
        tvCurrencyCode.setText("");
        tvEmissionDate.setText("");
        tvId.setText("");
        edit.setVisibility(View.GONE);
        viewModel.getReceiptListObservable().observe(this, itemList -> {
            if (itemList != null) {
                receiptModelList = itemList;
                adapter.setReceiptList(itemList);
            }
        });
        refreshRecycler();
    }

    private void refreshRecycler() {
        viewModel.makeApiCall();
    }

    private void initView() {
        btnAdd.setOnClickListener(view -> {
            Intent myIntent = new Intent(view.getContext(), NewReceiptActivity.class);
            startActivity(myIntent);
        });
        edit.setOnClickListener(view -> {
            Intent myIntent = new Intent(view.getContext(), EditActivity.class);
            myIntent.putExtra(ID_PARAM, tvId.getText());
            myIntent.putExtra(PROVIDER_PARAM, tvProvider.getText());
            myIntent.putExtra(AMOUNT_PARAM, tvAmount.getText());
            myIntent.putExtra(EMISSION_DATE_PARAM, tvEmissionDate.getText());
            myIntent.putExtra(CURRENCY_CODE_PARAM, tvCurrencyCode.getText());
            myIntent.putExtra(COMMENT_PARAM, tvComment.getText());
            startActivity(myIntent);
        });
    }

    private void init() {
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        RecyclerView recyclerView = binding.recyclerReceipts;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReceiptAdapter(receiptModelList, this);
        recyclerView.setAdapter(adapter);
        btnAdd = binding.btnAdd;
        edit = binding.btnEdit;
        tvProvider = binding.tvProviderInfo;
        tvAmount = binding.tvAmountInfo;
        tvComment = binding.tvCommentInfo;
        tvCurrencyCode = binding.tvCurrencyCodeInfo;
        tvEmissionDate = binding.tvEmissionDateInfo;
        tvId = binding.tvId;
        swipeRefreshLayout = binding.swipeRefresh;

    }

    @Override
    public void onReceiptClick(ReceiptModel receiptModel) {
        tvProvider.setText(receiptModel.getProvider());
        tvAmount.setText(receiptModel.getAmount().toString());
        tvComment.setText(receiptModel.getComment());
        tvCurrencyCode.setText(receiptModel.getCurrency_code());
        tvEmissionDate.setText(receiptModel.getEmission_date());
        tvId.setText(receiptModel.getId().toString());

        if (TextUtils.isEmpty(tvId.toString())) {
            edit.setVisibility(View.GONE);
        } else {
            edit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDelete(int position, int id) {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(MainActivity.this);
        myDialog.setTitle(R.string.delete_dialog);

        myDialog.setPositiveButton(R.string.yes_dialog, (dialog, which) -> {
            viewModel.deleteReceipt(id);
            refreshRecycler();
            viewModel.getReceiptStateObservable().observe(this, receiptState -> {
                switch (receiptState) {
                    case RECEIPT_DELETED_SUCCESS:
                        break;
                    case RECEIPT_DELETED_FAILURE:
                        Toast.makeText(this, "Failed to deleted receipt", Toast.LENGTH_SHORT).show();
                        break;
                }
            });
        });
        myDialog.setNegativeButton(R.string.cancel_dialog, (dialog, which) -> {
            dialog.cancel();
            refreshRecycler();
        });
        myDialog.show();
    }
}