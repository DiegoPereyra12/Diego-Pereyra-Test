package com.example.axosnettestjava.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.axosnettestjava.viewmodel.MainViewModel;
import com.example.axosnettestjava.R;
import com.example.axosnettestjava.ReceiptModel;

import java.util.List;


public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.MyViewHolder> {
    private List<ReceiptModel> receiptList;
    private ItemClickListener clickListener;
    ImageView delete;

    public ReceiptAdapter(List<ReceiptModel> receiptList, ItemClickListener clickListener) {
        this.receiptList = receiptList;
        this.clickListener = clickListener;
    }

    public void setReceiptList(List<ReceiptModel> receiptList) {
        this.receiptList = receiptList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ReceiptAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.receipt_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptAdapter.MyViewHolder holder, int position) {

        holder.itemView.setOnClickListener(v -> clickListener.onReceiptClick(receiptList.get(position)));

        holder.bind(receiptList.get(position));
    }

    @Override
    public int getItemCount() {
        if (this.receiptList != null) {
            return this.receiptList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView provider, amount, date, tvId;
        Integer id;

        public MyViewHolder(View itemView) {
            super(itemView);

            provider = itemView.findViewById(R.id.tvProvider);
            amount = itemView.findViewById(R.id.tvAmount);
            date = itemView.findViewById(R.id.tvDate);
            delete = itemView.findViewById(R.id.ivDelete);
            tvId = itemView.findViewById(R.id.tvId);
            delete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    clickListener.onDelete(position, id);
                }
            });
        }

        void bind(final ReceiptModel item) {
            provider.setText(item.getProvider());
            amount.setText(item.getAmount().toString());
            date.setText(item.getEmission_date());
            id = item.getId();
        }
    }

    public interface ItemClickListener {
        void onReceiptClick(ReceiptModel receiptModel);

        void onDelete(int position, int id);
    }
}