package online.javalab.poly.storage;

import android.content.Context;

import java.io.IOException;
import java.security.GeneralSecurityException;

import online.javalab.poly.utils.Define;

public class DataStorageManager {


    private static final String KEY_TOKEN = "TOKEN";

    private static DataStorageManager instance;

    private MySharedPreference mySharedPreference;

    public static void init(Context context) throws GeneralSecurityException, IOException {
        instance = new DataStorageManager();
        instance.mySharedPreference = new MySharedPreference(context);


    }

    public static DataStorageManager getInstance() {
        if (instance == null) {
            instance = new DataStorageManager();
        }
        return instance;
    }


    public static String getStringValue(String key) {
        return DataStorageManager.getInstance().mySharedPreference.getStringValue(key);
    }

    public static void removeFromKey(String key) {
        DataStorageManager.getInstance().mySharedPreference.removeFromKey(key);
    }

    public static void removeAll() {
        DataStorageManager.getInstance().mySharedPreference.removeAll();
    }

    public static void putStringValue(String key, String value) {
        DataStorageManager.getInstance().mySharedPreference.putStringValue(key, value);
    }

    public static void putToken(String token) {
        DataStorageManager.getInstance().mySharedPreference.putStringValue(KEY_TOKEN, token);
    }

    public static void putIsCheckNotifi(boolean isCheck, String key) {
        DataStorageManager.getInstance().mySharedPreference.putBooleanValue(key, isCheck);
    }

    public static boolean getIsCheckNotifi() {
        return DataStorageManager.getInstance().mySharedPreference.getBooleanValueV3(Define.StorageKey.IS_CHECK_NOTIFICATION);
    }

    public static String getToken() {
        return DataStorageManager.getInstance().mySharedPreference.getStringValue(KEY_TOKEN);
    }

    public static String getUserName() {
        return DataStorageManager.getInstance().mySharedPreference.getStringValue(Define.StorageKey.USER_NAME);
    }

    public static void setUserName(String name) {
        DataStorageManager.getInstance().mySharedPreference.putStringValue(Define.StorageKey.USER_NAME, name);
    }

}
