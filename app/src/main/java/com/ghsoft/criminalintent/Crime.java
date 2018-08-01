package com.ghsoft.criminalintent;

import java.util.UUID;

/**
 * Created by mac on 2018/8/1.
 */

public class Crime {
    private UUID mId; // crime的唯一标识符
    private String mTitle; // 陋习名称
    public Crime(){
        // 生成唯一标识符
        mId = UUID.randomUUID();
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
}
