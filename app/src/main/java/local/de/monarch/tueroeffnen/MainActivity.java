package local.de.monarch.tueroeffnen;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    TextView code;
    String deviceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnClick = (Button) findViewById(R.id.button) ;
        btnClick.setOnClickListener(this);
        this.code = (TextView) findViewById(R.id.textBoxCode);
        this.deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @Override
    public void onClick(View view) {
        String code= this.code.getText().toString();
        String md5Code = this.md5(code);
        String md5devId = this.md5(this.deviceId);

        Transponder t1 = new Transponder();
        t1.execute(md5Code, md5devId);
        try {
            String result = t1.get();
            if (result!=null)
            {
                TextView text1 = (TextView)findViewById(R.id.textViewStatus);
                text1.setText("Ergebnis:" + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    private String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
