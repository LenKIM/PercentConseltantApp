package com.hellmoney.thca.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hellmoney.thca.R;
import com.hellmoney.thca.model.Item;
import com.hellmoney.thca.model.ItemRes;
import com.hellmoney.thca.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MortgageItemFragment extends Fragment {
    public static final String TAG = MortgageItemFragment.class.getName();
    public static final String NAME = "주택담보대출";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Context mContext;
    private List<Item> mItems;
    private ItemAdapter mItemAdapter;

    @BindView(R.id.mortgage_item_recycler_view)
    protected RecyclerView mRecyclerView;

    public MortgageItemFragment() {
        // Required empty public constructor
    }

    public static MortgageItemFragment newInstance(String param1, String param2) {
        MortgageItemFragment fragment = new MortgageItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mortgage_item, container, false);
        ButterKnife.bind(this, view);
        mItems = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO : agentId 로그인 완성되면 대체
        String agentId = "agent1@naver.com";
        mItemAdapter = new ItemAdapter(mContext, mItems);
        mRecyclerView.setAdapter(mItemAdapter);
        Call<ItemRes> geItemsByAgentId = NetworkManager.service.getItemsByAgentId(agentId);
        geItemsByAgentId.enqueue(new Callback<ItemRes>() {
            @Override
            public void onResponse(Call<ItemRes> call, Response<ItemRes> response) {
                if(response.isSuccessful()) {
                    ItemRes results = response.body();
                    Log.d(TAG, results.getItems().toString());
                    mItems.clear();
                    mItems.addAll(results.getItems());
                    mItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ItemRes> call, Throwable t) {
                Log.e(TAG, "NETWORKING_ERROR");
            }
        });
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
        private final String TAG = ItemAdapter.class.getName();
        private final Context mContext;
        private final List<Item> mItems;

        ItemAdapter(Context context, List<Item> items) {
            this.mContext = context;
            this.mItems = items;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_item, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            final Item item = mItems.get(position);

            holder.mItemNameTextView.setText(item.getItemName());
            holder.mItemInterestRateTypeTextView.setText(item.getInterestRateType());
            holder.mItemRepaymentTypeTextView.setText(item.getRepaymentType());
            holder.mItemMinInterestRateTextView.setText(item.getMinInterestRate().toString() + "%");
            holder.mItemMaxInterestRateTextView.setText(item.getMaxInterestRate().toString() + "%");
            holder.itemView.setTag(item.getItemId());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailActivity.EXTRA_ITEM_ID, item.getItemId());
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        class ItemHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_name_text_view)
            protected TextView mItemNameTextView;
            @BindView(R.id.item_interest_rate_type_text_view)
            protected TextView mItemInterestRateTypeTextView;
            @BindView(R.id.item_repayment_type_text_view)
            protected TextView mItemRepaymentTypeTextView;
            @BindView(R.id.item_min_interest_rate_text_view)
            protected TextView mItemMinInterestRateTextView;
            @BindView(R.id.item_max_interest_rate_text_view)
            protected TextView mItemMaxInterestRateTextView;

            ItemHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}