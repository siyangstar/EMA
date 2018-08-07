package com.cqsynet.ema.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cqsynet.ema.R;
import com.cqsynet.ema.adapter.AddImageGridAdapter;
import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.db.ErrorAppearanceDao;
import com.cqsynet.ema.model.ErrorAppearanceObject;
import com.cqsynet.ema.model.ResponseObject;
import com.cqsynet.ema.network.OkgoRequest;
import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提交报修
 */
public class SubmitReportActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEtMessage;
    private TextView mTvDevice;
    private TextView mTvLocation;
    private TextView mTvPriority;
    private TextView mTvAppearance;
    private TextView mTvDepartment;
    private TextView mTvPerson;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mImageList;
    private AddImageGridAdapter mImageAdapter;
    private ArrayList<ErrorAppearanceObject> mErrorAppearanceList;
    private String mErrorAppearanceId;
    private String mDeviceId;
    private int mImageClickPosition; //添加图片的位置
    private static int MAX_IMAGE_NUMBER = 6;
    private static int REQUEST_CODE_OPEN_GALLARY = 1;
    private static int REQUEST_CODE_SCAN = 2;
    private static int REQUEST_CODE_DEVICE = 3;
    private boolean mHasPermission = false;
    private static String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_report);

        TextView tvTitle = findViewById(R.id.tvTitle_titlebar);
        ImageButton ibtnQuit = findViewById(R.id.ibtnLeft_titlebar);
        ImageButton ibtnScan = findViewById(R.id.ibtnRight_titlebar);
        mEtMessage = findViewById(R.id.etMessage_activity_submit_report);
        mTvDevice = findViewById(R.id.tvDevice_activity_submit_report);
        mTvLocation = findViewById(R.id.tvLocation_activity_submit_report);
        mTvPriority = findViewById(R.id.tvPriority_activity_submit_report);
        mTvAppearance = findViewById(R.id.tvAppearance_activity_submit_report);
        mTvDepartment = findViewById(R.id.tvDepartment_activity_submit_report);
        mTvPerson = findViewById(R.id.tvPerson_activity_submit_report);
        mRecyclerView = findViewById(R.id.rvImage_activity_submit_report);

        tvTitle.setText(R.string.submit_report);
        ibtnQuit.setVisibility(View.VISIBLE);
        ibtnQuit.setImageResource(R.drawable.btn_back_selector);
        ibtnQuit.setOnClickListener(this);
        ibtnScan.setVisibility(View.VISIBLE);
        ibtnScan.setImageResource(R.drawable.scan);
        ibtnScan.setOnClickListener(this);
        findViewById(R.id.llSelect_device_activity_submit_report).setOnClickListener(this);
        findViewById(R.id.llPriority_activity_submit_report).setOnClickListener(this);
        findViewById(R.id.llAppearance_activity_submit_report).setOnClickListener(this);
        findViewById(R.id.btnSubmit_activity_submit_report).setOnClickListener(this);

        mImageList = new ArrayList<>();
        mImageList.add("");
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mImageAdapter = new AddImageGridAdapter(R.layout.item_recycler_add_image_grid, mImageList);
        mRecyclerView.setAdapter(mImageAdapter);
        mImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                mHasPermission = AndPermission.hasPermissions(SubmitReportActivity.this, PERMISSIONS);
                if (!mHasPermission) {
                    AndPermission.with(SubmitReportActivity.this)
                            .runtime()
                            .permission(PERMISSIONS)
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> permissions) {
                                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(intent, REQUEST_CODE_OPEN_GALLARY);
                                    mImageClickPosition = position;
                                }
                            })
                            .onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(@NonNull List<String> permissions) {
                                    if (AndPermission.hasAlwaysDeniedPermission(SubmitReportActivity.this, permissions)) {
                                        ToastUtils.showLong("您需要允许权限才可以正常使用嘿快的全部功能, 请到手机系统的设置界面打开权限");
                                    }
                                }
                            })
                            .start();
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_CODE_OPEN_GALLARY);
                    mImageClickPosition = position;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtnLeft_titlebar:
                onBackPressed();
                break;
            case R.id.ibtnRight_titlebar:
                mHasPermission = AndPermission.hasPermissions(SubmitReportActivity.this, Manifest.permission.CAMERA);
                if (!mHasPermission) {
                    AndPermission.with(SubmitReportActivity.this)
                            .runtime()
                            .permission(Manifest.permission.CAMERA)
                            .onGranted(new Action<List<String>>() {
                                @Override
                                public void onAction(List<String> permissions) {
                                    Intent intent = new Intent(SubmitReportActivity.this, CaptureActivity.class);
                                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                                }
                            })
                            .onDenied(new Action<List<String>>() {
                                @Override
                                public void onAction(@NonNull List<String> permissions) {
                                    if (AndPermission.hasAlwaysDeniedPermission(SubmitReportActivity.this, permissions)) {
                                        ToastUtils.showLong("您需要允许摄像头的权限才可以正常使用嘿快的全部功能, 请到手机系统的设置界面打开权限");
                                    }
                                }
                            })
                            .start();
                } else {
                    Intent intent = new Intent(this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                }
                break;
            case R.id.llSelect_device_activity_submit_report:
                Intent intent = new Intent(SubmitReportActivity.this, DeviceListActivity.class);
                startActivityForResult(intent, REQUEST_CODE_DEVICE);
                break;
            case R.id.llPriority_activity_submit_report:
                new MaterialDialog.Builder(this)
                        .title(R.string.priority)
                        .dividerColorRes(R.color.divider)
                        .items(R.array.priority)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                mTvPriority.setText(getResources().getStringArray(R.array.priority)[which]);
                            }
                        })
                        .show();
                break;
            case R.id.llAppearance_activity_submit_report:
                mErrorAppearanceList = ErrorAppearanceDao.getInstance(SubmitReportActivity.this).queryErrorAppearance();
                ArrayList<String> errorValueList = new ArrayList<>();
                for (ErrorAppearanceObject errorAppearanceObject : mErrorAppearanceList) {
                    errorValueList.add(errorAppearanceObject.gzdm_ms);
                }
                new MaterialDialog.Builder(this)
                        .title(R.string.error_apprearance)
                        .dividerColorRes(R.color.divider)
                        .items(errorValueList)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                mTvAppearance.setText(mErrorAppearanceList.get(which).gzdm_ms);
                                mErrorAppearanceId = mErrorAppearanceList.get(which).gzdm_bm;
                            }
                        })
                        .show();
                break;
            case R.id.btnSubmit_activity_submit_report:
                submitReport();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == REQUEST_CODE_OPEN_GALLARY && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            c.close();
            setImage(imagePath);
        } else if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK && data != null) {
            //处理扫描结果
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                String result = bundle.getString(CodeUtils.RESULT_STRING);
                Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
            } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CODE_DEVICE && resultCode == Activity.RESULT_OK && data != null) {
            mTvDevice.setText(data.getStringExtra("deviceName"));
            mTvLocation.setText(data.getStringExtra("deviceLocation"));
            mDeviceId = data.getStringExtra("deviceId");
        }
    }

    /**
     * 设置图片
     *
     * @param imagePath
     */
    private void setImage(String imagePath) {
        mImageList.set(mImageClickPosition, imagePath);
        if (mImageAdapter.getItemCount() != MAX_IMAGE_NUMBER && mImageClickPosition == mImageAdapter.getItemCount() - 1) {
            mImageList.add("");
        }
        mImageAdapter.notifyDataSetChanged();
    }

    /**
     * 提交报修
     */
    private void submitReport() {
        if (TextUtils.isEmpty(mDeviceId)) {
            ToastUtils.showShort("请选择设备");
        } else if (TextUtils.isEmpty(mTvPriority.getText().toString().trim())) {
            ToastUtils.showShort("请选择优先级");
        } else if (TextUtils.isEmpty(mErrorAppearanceId)) {
            ToastUtils.showShort("请选择故障现象");
        }
        mProgressDialog.setMessage(getString(R.string.logining));
        mProgressDialog.show();

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("gdMs", mEtMessage.getText().toString().trim());
        paramMap.put("sbBm", mDeviceId);
        paramMap.put("yxj", mTvPriority.getText().toString().trim());
        paramMap.put("gzxxBm", mErrorAppearanceId);
        paramMap.put("bgr", mTvPerson.getText().toString().trim());
        paramMap.put("imgList", "");

        OkgoRequest.excute(this, AppConstants.URL_SUBMIT_REPORT, paramMap, new OkgoRequest.IResponseCallback() {
            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                if (response != null) {
                    Gson gson = new Gson();
                    ResponseObject responseObj = gson.fromJson(response, ResponseObject.class);
                    if (responseObj != null) {
                        if (AppConstants.RET_OK.equals(responseObj.ret)) {
                            ToastUtils.showShort(R.string.submit_success);
                        } else {
                            ToastUtils.showShort(responseObj.msg);
                        }
                    }
                }
            }

            @Override
            public void onErrorResponse() {
                ToastUtils.showShort(R.string.submit_failed);
                mProgressDialog.dismiss();
            }
        });
    }
}
