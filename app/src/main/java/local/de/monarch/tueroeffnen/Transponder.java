package local.de.monarch.tueroeffnen;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

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
            HttpPost post = new HttpPost("http://www.monarch.de/tuer.php");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("code", md5Code));
            nameValuePairs.add(new BasicNameValuePair("devId", md5devId));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            //HttpResponse response = httpclient.execute(new HttpGet("http://www.monarch.de/tuer.php?code=" + md5Code + "&devId="+md5devId));
            HttpResponse response = httpclient.execute(post);
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
