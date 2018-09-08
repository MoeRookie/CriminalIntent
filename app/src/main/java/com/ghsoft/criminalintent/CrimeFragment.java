package com.ghsoft.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by mac on 2018/8/2.
 */

public class CrimeFragment extends Fragment {
    private static final String TAG = CrimeFragment.class.getSimpleName();
    private Crime mCrime;
    private File mPhotoFile;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private static final String AGR_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_PICTURE_DETAIL = "DialogPictureDetail";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO = 2;
    private CrimeLab mCrimeLab;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Callbacks mCallbacks;
    private int mPhotoViewWidth;
    private int mPhotoViewHeight;

    /**
     * 声明托管方activity的回调接口
     */
    public interface Callbacks{
        void onCrimeUpdated(Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    public static CrimeFragment newInstance(UUID crimeId){
        Bundle bundle = new Bundle();
        bundle.putSerializable(AGR_CRIME_ID,crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID crimeId = (UUID) getArguments().getSerializable(AGR_CRIME_ID);
        mCrimeLab = CrimeLab.get(getActivity());
        mCrime = mCrimeLab.getCrime(crimeId);
        mPhotoFile = CrimeLab.get(getActivity())
                .getPhotoFile(mCrime);
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    /**
     *
     * @param inflater
     * @param container 当前视图的父视图,通常需要父视图来正确配置组件?
     * @param savedInstanceState 用来存储以及恢复数据,可供该方法从保存状态下重建视图
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        // attachToRoot:告知inflater是否将生成的视图添加给父视图
        // false - 我将以activity代码的方式添加视图
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = view.findViewById(R.id.crime_title);
        // 显示crime的标题
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // s:用户输入内容,.toString()返回用来设置Crime标题的字符串.
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        mDateButton = view.findViewById(R.id.crime_date);
        // 格式化时间戳
        final Date date = mCrime.getDate();
        updateDate(date);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示对话框
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                dialog.show(manager,DIALOG_DATE);
            }
        });
        mSolvedCheckBox = view.findViewById(R.id.crime_solved);
        // 设置crime的解决状态
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 更新陋习的处理状态
                mCrime.setSolved(isChecked);
            }
        });
        mReportButton = view.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建发送消息的隐式intent
                ShareCompat.IntentBuilder sc = ShareCompat.IntentBuilder.from(getActivity());
                sc.setType("text/plain");
                sc.setText(getCrimeReport());
                sc.setSubject(getString(R.string.crime_report_subject));
                sc.createChooserIntent();
                sc.startChooser();
                // 创建发送消息的隐式intent
                // Intent intent = new Intent(Intent.ACTION_SEND);
                // intent.setType("text/plain");
                // intent.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                // intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
                // intent = Intent.createChooser(intent,getString(R.string.send_report));
                // startActivity(intent);
            }
        });
        mSuspectButton = view.findViewById(R.id.crime_suspect);
        // 创建打开联系人列表的intent
        final Intent pickContact = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
        // pickContact.addCategory(Intent.CATEGORY_HOME); 验证过滤器
        // 检查可响应任务的activity
        checkCanHandleCurrentTaskActivity(pickContact,mSuspectButton);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });
        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }
        mCallSuspectButton = view.findViewById(R.id.crime_call);
        // 创建拨打电话的隐式intent
        Intent callPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
        checkCanHandleCurrentTaskActivity(callPhone,mCallSuspectButton);
        mCallSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mCrime.getPhoneNumber()));
                startActivity(callPhone);
            }
        });
        mPhotoButton = view.findViewById(R.id.crime_camera);

        // 创建能够处理拍照任务的隐式intent
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto =
                mPhotoFile != null
                        &&
                        captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });
        mPhotoView = view.findViewById(R.id.crime_photo);
        // 获取mPhotoView的观察者对象以及mPhotoView的宽高值
        ViewTreeObserver observer = mPhotoView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 获取mPhotoView的宽高
                mPhotoViewWidth = mPhotoView.getWidth();
                mPhotoViewHeight = mPhotoView.getHeight();
                // 更新照片显示
                updatePhotoView(mPhotoViewWidth,mPhotoViewHeight);
            }
        });
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 弹出显示放大版缩略图的对话框
                FragmentManager manager = getFragmentManager();
                // 获取缩略图位图对象
                Bitmap bitmap = PictureUtils.getScaledBitmap(
                        mPhotoFile.getPath(), getActivity()
                );
                PictureDetailFragment dialog = PictureDetailFragment.newInstance(bitmap);
                dialog.show(manager,DIALOG_PICTURE_DETAIL);
            }
        });
        return view;
    }

    /**
     * 检查当前设备中是否存在能够处理当前任务的activity,如果不存在则设置启动目标应用的按钮无效
     * @param intent 封装定义了当前任务的intent
     * @param button 启动目标应用的button
     */
    private void checkCanHandleCurrentTaskActivity(Intent intent, Button button) {
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(intent,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            button.setEnabled(false);
        }
    }

    private void updateDate(Date date) {
        CharSequence currentDate = DateFormat.format("EEEE,MMMMdd,yyyy", date);
        mDateButton.setText(currentDate);
    }
    private String getCrimeReport(){
        String solvedString =
                mCrime.isSolved()
                        ?
                        getString(R.string.crime_report_solved)
                        :
                        getString(R.string.crime_report_unsolved);
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect() == null
                ?
                getString(R.string.crime_report_no_suspect)
                :
                getString(R.string.crime_report_suspect,mCrime.getSuspect());
        String report = getString(R.string.crime_report,
                mCrime.getTitle(),
                dateString,
                solvedString,
                suspect);
        return report;
    }

    private void updatePhotoView(int width,int height){
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), width,height
            );
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            // 设置crime的记录日期
            mCrime.setDate(date);
            // 刷新日期按钮的显示结果
            updateDate(date);
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            // 指定需要返回其值的字段
            String[] queryFields = {
                    Contacts.DISPLAY_NAME,
                    Contacts._ID
            };
            // 查询,contactUri类似这里的where条件
            Cursor cursor = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            try {
                // 检查是否真正的获取到了结果
                if (cursor.getCount() == 0) {
                    return;
                }
                // 获取到第一条记录的第一个字段值即为嫌疑人姓名
                cursor.moveToFirst();
                String suspect = cursor.getString(0);
                mCrime.setSuspect(suspect);
                String suspectId = cursor.getString(1);
                Log.e(TAG, "姓名:" + suspect + ",ID:" + suspectId);
                String phoneNumber = getPhoneNumberById(suspectId);
                mCrime.setPhoneNumber(phoneNumber);
                mSuspectButton.setText(suspect);
            }finally {
                cursor.close();
            }
        } else if (requestCode == REQUEST_PHOTO) {
            updatePhotoView(mPhotoViewWidth,mPhotoViewHeight);
        }
    }

    /**
     * 根据suspectId从CommonDataKinds.Phone表中查询当前嫌疑人的电话号码
     * @param suspectId 联系人ID
     * @return 联系人电话号码
     */
    private String getPhoneNumberById(String suspectId) {
        Cursor cursor = getActivity().getContentResolver()
                .query(Phone.CONTENT_URI,
                        new String[]{Phone.NUMBER},
                        Phone.CONTACT_ID + "= ?",
                        new String[]{suspectId},
                        null);
        String phoneNumber = null;
        try{
            // 检查是否真正的获取到了结果
            if (cursor.getCount() == 0) {
                return null;
            }
            // 获取到第一条记录的第一个字段值即为嫌疑人电话号码
            cursor.moveToFirst();
            phoneNumber = cursor.getString(0);
            Log.e(TAG, "phoneNumber = " + phoneNumber);
        }finally {
            cursor.close();
        }
        return phoneNumber;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater); // 约定:任何超类定义的菜单项在子类方法中同样获得应用
        // 解析菜单定义,将菜单项填充到menu对象中
        inflater.inflate(R.menu.fragment_crime,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.menu_item_delete_crime:
                // 调用crimeLab的delete(UUID crimeId)方法,删除当前的crime
                mCrimeLab.deleteCrime(mCrime.getId());
                // 关闭crimeFragment的托管方activity
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
