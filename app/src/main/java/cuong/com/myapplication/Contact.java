package cuong.com.myapplication;

import java.io.Serializable;

/**
 * Created by USER on 19/10/2017.
 */

public class Contact implements Serializable {
    private int mId;
    private String mName;
    private String mPhone;
    private String mAddress;
    private String mDate;
    private String mTime;
    private String mGender;

    public Contact() {
    }

    public Contact(int mId, String mName, String mPhone, String mAddress, String mDate, String mTime, String mGender) {
        this.mId = mId;
        this.mName = mName;
        this.mPhone = mPhone;
        this.mAddress = mAddress;
        this.mDate = mDate;
        this.mTime = mTime;
        this.mGender = mGender;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmGender() {
        return mGender;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }
}
