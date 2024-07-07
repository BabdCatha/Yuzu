package net.sokato.game.yuzu;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.billingclient.api.Purchase;

public class PreferencesManager {

    private Context context;
    private SharedPreferences Prefs;
    private SharedPreferences.Editor PrefsEditor;

    public PreferencesManager(Context context){
        this.context = context;
        this.Prefs = context.getSharedPreferences("PreferencesManager", Context.MODE_PRIVATE);
        this.PrefsEditor = this.Prefs.edit();
    }

    public boolean isFirstLaunch(){
        return Prefs.getBoolean("isFirstLaunch", true);
    }

    public void resetIsFirstLaunch(){
        PrefsEditor.putBoolean("isFirstLaunch", true).apply();
    }

    public void firstLaunchDone(){
        PrefsEditor.putBoolean("isFirstLaunch", false).apply();
    }

    public boolean WantsTailoredAds(){
        return Prefs.getBoolean("TailoredAdsWanted", false);
    }

    public void premiumBought(Purchase purchase){
        PrefsEditor.putBoolean("Premium", true).apply();
    }

    public boolean isPremium(){
        return Prefs.getBoolean("Premium", false);
    }

    public void SetTailoredAds(boolean TailoredAds){
        PrefsEditor.putBoolean("TailoredAdsWanted", TailoredAds).apply();
    }

    public boolean SendCrashReports(){
        return Prefs.getBoolean("SendCrashReports", false);
    }

    public void SetCrashReports(boolean Send){
        PrefsEditor.putBoolean("SendCrashReports", Send).apply();
    }

}
