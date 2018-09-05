package com.ghsoft.criminalintent;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

/**
 * Created by mac on 2018/8/10.
 */

public class PictureDetailFragment extends DialogFragment {
    private static final String ARG_PICTURE_DETAIL = "picture_detail";

    public static PictureDetailFragment newInstance(Bitmap bitmap){
        Bundle args = new Bundle();
        // bitmap已经实现了parcelable接口
        args.putParcelable(ARG_PICTURE_DETAIL,bitmap);
        PictureDetailFragment fragment = new PictureDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_picture, null);
        // 从当前fragment的args中获取传递过来的bitmap并加载到imageView上
        Bitmap bitmap = getArguments()
                .getParcelable(ARG_PICTURE_DETAIL);
        ImageView pictureDetail = view.findViewById(R.id.dialog_picture_detail);
        pictureDetail.setImageBitmap(bitmap);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.picture_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
