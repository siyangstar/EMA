package com.cqsynet.ema.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cqsynet.ema.R;

/**
 * 资产模块
 */
public class AssetFragment extends BaseFragment implements View.OnClickListener {

    private AssetListFragment mListFragment;
    private TextView mTvSort;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset, container, false);
        TextView tvTitle = view.findViewById(R.id.tvTitle_titlebar);
        tvTitle.setText(R.string.asset);
        mTvSort = view.findViewById(R.id.tvRight_titlebar);
        mTvSort.setVisibility(View.VISIBLE);
        mTvSort.setText(R.string.sort);
        mTvSort.setOnClickListener(this);

        getFragmentManager().beginTransaction().add(R.id.flRecyclerView_fragment_asset, mListFragment).commitAllowingStateLoss();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListFragment = new AssetListFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRight_titlebar:
                new MaterialDialog.Builder(mContext)
                        .titleGravity(GravityEnum.CENTER)
                        .title(R.string.sort)
                        .dividerColorRes(R.color.divider)
                        .itemsGravity(GravityEnum.CENTER)
                        .items(R.array.asset_sort)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                                mTvSort.setText(getResources().getStringArray(R.array.asset_sort)[which]);
                                String value = getResources().getStringArray(R.array.asset_sort_value)[which];
                                mListFragment.setOrderByParam(value);
                            }
                        })
                        .show();
                break;
        }
    }
}
