package in.jaaga.thebachaoproject;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by amar on 22-02-2015.
 */
public class TheBachaoProject extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "7qXoZPaj5Xv29czial8ZRZ1aIerUOVqrPxcDHMpN", "MimNVJQiVr5XxrDBRIenwTM7v2LenEVVOXeGnpFA");
    }
}
