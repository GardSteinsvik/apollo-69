package no.ntnu.idi.apollo69;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		@SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
		System.out.println("Android device ID: " + deviceId);
		initialize(new Apollo69(deviceId), config);
	}
}
