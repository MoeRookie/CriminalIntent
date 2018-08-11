package com.ghsoft.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by mac on 2018/8/10.
 */

public class DatePickerFragment extends DialogFragment {
    private static final String ARG_DATE = "date";
    private static final String EXTRA_DATE = "com.ghsoft.criminalintent.date";
    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);
        // 初始化DatePicker
        // 从当前fragment的args中获取传过来的date
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        // 通过Calendar类加工并从中获取初始化datePicker所需的年、月、日
        Calendar calendar = Calendar.getInstance();
        // 将date配置给calendar
        calendar.setTime(date);
        // 从calendar中获取初始化datePicker所需的年、月、日
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker = view.findViewById(R.id.dialog_date_date_picker);
        // 初始化datePicker
        mDatePicker.init(year,month,day,null);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取用户当前选中日期的信息并封装成date
                        // 获取用户当前选中日期的信息
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        // 封装成date
                        Date date = new GregorianCalendar(year, month, day).getTime();
                        // 发送结果到crimeFragment中
                        sendResult(Activity.RESULT_OK,date);
                    }
                })
                .create();
    }

    /**
     * 发送date给目标fragment - crimeFragment
     * @param resultCode 结果码
     * @param date 用户选择的日期
     */
    private void sendResult(int resultCode,Date date){
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
