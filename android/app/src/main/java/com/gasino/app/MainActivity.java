package com.gasino.app;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    // کلید عمومی پرداخت درون‌برنامه‌ای کافه بازار برای احراز اصالت و اعتبارسنجی خریدها
    public static final String BAZAAR_PUBLIC_KEY = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwClquaLAedQqYXaL/MallnaDw1NE3QT7hZwxVkqrKEolbKVlz4cTiso01+lVonL0hEkgacQAI7mCdp4qiicjIHHkZnQ7naRCbqbQjhW+m6RkKg1LU+HbWwRzyPLSU2q46yMAkVybD9320wVqkDBG9UDA3bY64zBBNDM98YagaefMy5NQdVrs+5fs1dc2yXsB1gFtCAY7dmpB6AwyUNeLa2p+UrKfX5UzmdmopmgMkUCAwEAAQ==";

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
