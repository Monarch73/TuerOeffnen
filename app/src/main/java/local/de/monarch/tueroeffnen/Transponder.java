package local.de.monarch.tueroeffnen;

import android.os.AsyncTask;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import local.de.monarch.tueroeffnen.R;

/**
 * Created by niels on 02.04.2016.
 */
public class Transponder extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            String md5Code = params[0];
            String md5devId = params[1];

            HttpResponse response = httpclient.execute(new HttpGet("http://www.monarch.de/tuer.php?code=" + md5Code + "&devId="+md5devId));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String responseString = out.toString();
                out.close();
                return "ok";
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                return statusLine.getReasonPhrase();
            }
        }
        catch (Exception ex)
        {
            return "Fehler: "+ ex.getMessage();
        }

    }
}
