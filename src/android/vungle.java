package cordova.plugin.vungle.ad;
import android.content.Context;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.vungle.warren.Vungle;
import com.vungle.warren.AdConfig;              // Custom ad configurations
import com.vungle.warren.InitCallback;          // Initialization callback
import com.vungle.warren.PlayAdCallback;        // Play ad callback
import com.vungle.warren.VungleNativeAd;        //  Flex-Feed ad
import com.vungle.warren.VungleSettings;

/**
 * This class echoes a string called from JavaScript.
 */
public class vungle extends CordovaPlugin {
 Context context = this.cordova.getActivity().getApplicationContext();
         final String LOG_TAG = "VungleAdd";
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        
        if(action.equals("initSDK")){
            this.initSDK(args,callbackContext);
            return true;
        }
        return false;
    }

   private void initSDK(JSONArray args, CallbackContext callback){
           //args.getJSONObject(0).getString("param1");
           callback.success("Rewarded");
     try{
       final String appId = "5d8bd33efbcaba00189b410f";

        final long MEGABYTE = 1024L * 1024L;
        final VungleSettings vungleSettings =
                new VungleSettings.Builder()
                        .setMinimumSpaceForAd(20 * MEGABYTE)
                        .setMinimumSpaceForInit(21 * MEGABYTE)
                        .build();

        Vungle.init(appId, context, new InitCallback() {
            @Override
            public void onSuccess() {   
                makeToast("Vungle SDK initialized");
                callback.success("Rewarded");
                onAdLoad();
                Log.d(LOG_TAG, "InitCallback - onSuccess");
                Log.d(LOG_TAG, "Vungle SDK Version - " + com.vungle.warren.BuildConfig.VERSION_NAME);
                Log.d(LOG_TAG, "Valid placement list:");

            }

            @Override
            public void onError(Throwable throwable) {
                if (throwable != null) {
                    Log.d(LOG_TAG, "InitCallback - onError: " + throwable.getLocalizedMessage());
                } else {
                    Log.d(LOG_TAG, "Throwable is null");
                }
            }

            @Override
            public void onAutoCacheAdAvailable(final String placementReferenceID) {
                Log.d(LOG_TAG, "InitCallback - onAutoCacheAdAvailable" +
                        "\n\tPlacement Reference ID = " + placementReferenceID);


            }
        }, vungleSettings);

   }catch(Exception ex){
    callback.error("Something went wrong" + ex);
   }
}

    public void onAdLoad() {
        final String placementReferenceID="DEFAULT-3324354";
        Log.d(LOG_TAG,"LoadAdCallback - onAdLoad" +
                "\n\tPlacement Reference ID = " + placementReferenceID);


            if (Vungle.isInitialized()) {
                if (Vungle.canPlayAd(placementReferenceID)) {
                    final AdConfig adConfig = getAdConfig();
                    // Play Vungle ad
                             Vungle.playAd(placementReferenceID, adConfig, vunglePlayAdCallback);

                } else {
                  makeToast("Vungle ad not playable for " + placementReferenceID);
                }
            } else {
                makeToast("Vungle SDK not initialized");
            }


    }

      private final PlayAdCallback vunglePlayAdCallback = new PlayAdCallback() {
        final String placementReferenceID="DEFAULT-3324354";
        @Override

        public void onAdStart(final String placementReferenceID) {
          Toast.makeText(context,"Ad Start",Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "PlayAdCallback - onAdStart" +
                    "\n\tPlacement Reference ID = " + placementReferenceID);

            if (Vungle.isInitialized()) {
                if (Vungle.canPlayAd(placementReferenceID)) {
                    final AdConfig adConfig = getAdConfig();
                    // Play Vungle ad
                    Vungle.playAd(placementReferenceID, adConfig, vunglePlayAdCallback);

                } else {
                   makeToast("Vungle ad not playable for " + placementReferenceID);
                }
            } else {
              makeToast("Vungle SDK not initialized");
            }
        }

        @Override
        public void onAdEnd(final String placementReferenceID, final boolean completed, final boolean isCTAClicked) {
          Toast.makeText(context,"Ad End",Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "PlayAdCallback - onAdEnd" +
                    "\n\tPlacement Reference ID = " + placementReferenceID +
                    "\n\tView Completed = " + completed + "" +
                    "\n\tDownload Clicked = " + isCTAClicked);
//             if(completed){
//                  callback.success("Rewarded");
//             }
        }

        @Override
        public void onError(final String placementReferenceID, Throwable throwable) {
            Log.d(LOG_TAG, "PlayAdCallback - onError" +
                    "\n\tPlacement Reference ID = " + placementReferenceID +
                    "\n\tError = " + throwable.getLocalizedMessage());

            makeToast(throwable.getLocalizedMessage());

        }
    };


    private AdConfig getAdConfig() {
        AdConfig adConfig = new AdConfig();

        adConfig.setBackButtonImmediatelyEnabled(true);
        adConfig.setAutoRotate(false);
        adConfig.setMuted(false);
        adConfig.setOrdinal(5);

        return adConfig;
    }

    private void setCustomRewardedFields() {
        Vungle.setIncentivizedFields("TestUser", "RewardedTitle", "RewardedBody", "RewardedKeepWatching", "RewardedClose");
    }




    private void makeToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

 

}
