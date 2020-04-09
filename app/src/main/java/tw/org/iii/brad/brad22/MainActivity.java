package tw.org.iii.brad.brad22;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        123);
        } else {
            init();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    private void init() {
        webView = findViewById(R.id.webView);
        initWebView();

    }
    private void initWebView(){
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new MyJS(), "broyo");

        webView.loadUrl("file:///android_asset/brad.html");
    }

    public class MyJS{
        @JavascriptInterface
        public void fromJS(){
            gotoScan();         //拉出去另外寫
        }
    }

    private void gotoScan(){
        Intent intent = new Intent(this,ScanActivity.class);
        startActivityForResult(intent, 321);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 321 && resultCode == RESULT_OK){
            webView.loadUrl(
                    String.format("javascript:showCode('%s')",data.getStringExtra("code")));
        }
    }
}
