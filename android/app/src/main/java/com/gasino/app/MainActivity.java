package com.gasino.app;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Expose a native billing bridge to the WebView for Cafe Bazaar IAB
        try {
            WebView webView = this.getBridge().getWebView();
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new BazaarBillingBridge(), "BazaarBridge");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class BazaarBillingBridge {
        
        @JavascriptInterface
        public void initiateBazaarPurchase(String productId) {
            // This is called by the React/TS UI when a user clicks the Buy button.
            // On a real Android device with Bazaar, the developer will initiate Cafe Bazaar In-App Purchase logic:
            // e.g. using billing client or standard Cafe Bazaar AIDL service.
            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "در حال اتصال به کافه بازار جهت خرید " + productId, Toast.LENGTH_LONG).show();
                
                // Let's notify the webview that checkout is initiated.
                // When payment succeeds, native Android code can call:
                // webView.evaluateJavascript("window.onBazaarPurchaseSuccess('" + purchaseToken + "')", null);
            });
        }

        @JavascriptInterface
        public boolean isBazaarAvailable() {
            // Check if Cafe Bazaar is installed on the user's device
            try {
                MainActivity.this.getPackageManager().getPackageInfo("com.farsitel.bazaar", 0);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @JavascriptInterface
        public void showNativeToast(String msg) {
            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            });
        }
    }
}
