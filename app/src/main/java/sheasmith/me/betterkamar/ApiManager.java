package sheasmith.me.betterkamar;

import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.dataModels.SettingsObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;

/**
 * Created by TheDiamondPicks on 5/09/2018.
 */

public class ApiManager {

    public static String TOKEN;
    public static String ID;
    public static String URL = "https://kamarportal.jameshargest.school.nz/api/api.php";

    private static XmlParserCreator parserCreator = new XmlParserCreator() {
        @Override
        public XmlPullParser createParser() {
            try {
                return XmlPullParserFactory.newInstance().newPullParser();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };

    static GsonXml gsonXml = new GsonXmlBuilder()
            .setXmlParserCreator(parserCreator)
            .create();

    /**
     * Logon to KAMAR
     * @param username Username to use
     * @param password Password to use
     * @param callback Callback, returning a LogonResults object
     */
    public static void login(final String username, final String password, final ApiResponse<LoginObject.LogonResults> callback) {
        Thread webThread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "Command=Logon&Key=vtku&Username=" + username + "&Password=" + password);
                Request request = new Request.Builder()
                        .url(URL)
                        .post(body)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("User-Agent", "KAMAR Plus 2.0")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String xml = response.body().string();

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    factory.setValidating(false);
                    factory.setIgnoringElementContentWhitespace(true);
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
                    Element resultNode = (Element) doc.getElementsByTagName("LogonResults").item(0);
                    if (resultNode.getElementsByTagName("Success") != null) {
                        LoginObject.LogonResults login = gsonXml.fromXml(xml, LoginObject.LogonResults.class);

                        TOKEN = login.getKey();
                        ID = username;
                    }
                    else {
                        String error = resultNode.getElementsByTagName("Error").item(0).getTextContent();
                        if (error.equalsIgnoreCase("The Username & Password do not appear to match a user on record. Please re-enter your Username and Password.")) {
                            callback.error(new Exceptions.InvalidUsernamePassword());
                        }
                        else {
                            callback.error(new Exceptions.UnknownServerError());
                        }
                    }
                }
                catch (Exception e) {
                    callback.error(e);
                }
            }
        });
        webThread.start();

    }

    public static void getSettings(final String url, final ApiResponse<SettingsObject.SettingsResults> callback) {
        Thread webThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                    RequestBody body = RequestBody.create(mediaType, "Command=GetSettings&Key=vtku");
                    Request request = new Request.Builder()
                            .url(url + "/api/api.php")
                            .post(body)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .addHeader("User-Agent", "KAMAR Plus 2.0")
                            .build();

                    Response response = client.newCall(request).execute();
                    if (response.code() == 200) {
                        String xml = response.body().string();
                        SettingsObject.SettingsResults login = gsonXml.fromXml(xml, SettingsObject.SettingsResults.class);
                        callback.success(login);

                        URL = request.url().toString();
                    }
                    else {
                        callback.error(new Exceptions.InvalidServer());
                    }
                }
                catch (Exception e) {
                    callback.error(e);
                }
            }
        });
        webThread.start();
    }

}
