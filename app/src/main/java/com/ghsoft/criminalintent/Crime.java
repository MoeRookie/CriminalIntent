package com.ghsoft.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by mac on 2018/8/1.
 */

public class Crime {
    private UUID mId; // crime的唯一标识符
    private String mTitle; // 陋习名称
    private Date mDate; // 陋习发生的时间
    private boolean mSolved; // 陋习的处理状态
    private String mSuspect; // 嫌疑人姓名
    private String mPhoneNumber; // 嫌疑人电话号码
    public Crime(){
        this(UUID.randomUUID());
    }
    public Crime(UUID id){
        // 生成唯一标识符
        mId = id;
        mDate = new Date(); // 陋习的发生时间默认为当前时间
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getPhotoFileName(){
        return "IMG_" + getId().toString() + ".jpg";
    }
}
