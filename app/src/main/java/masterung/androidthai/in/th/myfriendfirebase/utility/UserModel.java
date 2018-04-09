package masterung.androidthai.in.th.myfriendfirebase.utility;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable{

    private String emailString, displayNameString,
            photoUrlString, uidUserString;

    public UserModel() {
    }   // Constructor getter

    public UserModel(String emailString, String displayNameString,
                     String photoUrlString, String uidUserString) {
        this.emailString = emailString;
        this.displayNameString = displayNameString;
        this.photoUrlString = photoUrlString;
        this.uidUserString = uidUserString;
    }   // Constructor setter

    protected UserModel(Parcel in) {
        emailString = in.readString();
        displayNameString = in.readString();
        photoUrlString = in.readString();
        uidUserString = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getEmailString() {
        return emailString;
    }

    public void setEmailString(String emailString) {
        this.emailString = emailString;
    }

    public String getDisplayNameString() {
        return displayNameString;
    }

    public void setDisplayNameString(String displayNameString) {
        this.displayNameString = displayNameString;
    }

    public String getPhotoUrlString() {
        return photoUrlString;
    }

    public void setPhotoUrlString(String photoUrlString) {
        this.photoUrlString = photoUrlString;
    }

    public String getUidUserString() {
        return uidUserString;
    }

    public void setUidUserString(String uidUserString) {
        this.uidUserString = uidUserString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(emailString);
        dest.writeString(displayNameString);
        dest.writeString(photoUrlString);
        dest.writeString(uidUserString);
    }
}   // Main Class
