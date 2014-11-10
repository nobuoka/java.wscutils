package info.vividcode.util.oauth;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import org.junit.Test;

public class OAuthRequestHelperTest {

    @Test
    public void testGetNonceString() {
        String nonceStr;

        nonceStr = OAuthRequestHelper.getNonceString();
        assertEquals("Default length of nonce is 16", 16, nonceStr.length());

        nonceStr = OAuthRequestHelper.getNonceString(1);
        assertEquals(1, nonceStr.length());

        nonceStr = OAuthRequestHelper.getNonceString(5);
        assertEquals(5, nonceStr.length());

        nonceStr = OAuthRequestHelper.getNonceString(200);
        assertEquals(200, nonceStr.length());
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetNonceStringWithZeroLength() {
        OAuthRequestHelper.getNonceString(0);
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetNonceStringWithNegativeLength() {
        OAuthRequestHelper.getNonceString(-1);
    }

    @Test
    public void testOAuthRequestHelper() throws GeneralSecurityException, MalformedURLException {
        String url = "https://photos.example.com/oauth/initiate";
        String method = "POST";
        String consumerKey    = "TEST_KEY==";
        String consumerSecret = "TEST_SECRET==";
        String tokenSecret = "";
        String secrets = OAuthEncoder.encode(consumerSecret) + "&" + OAuthEncoder.encode(tokenSecret);
        OAuthRequestHelper.ParamList paramList = new OAuthRequestHelper.ParamList(
              new String[][]{
                { "oauth_consumer_key", consumerKey },
                { "oauth_signature_method", "HMAC-SHA1" },
                { "oauth_nonce", "faQeA8ae22" },
                { "oauth_timestamp", "1368908614" },
                { "oauth_version", "1.0" },
                { "oauth_callback", "oob" },
              } );
        OAuthRequestHelper helper = new OAuthRequestHelper( url, method, secrets, paramList, null, null );

        // Authorization header field
        String authHeaderStr = helper.getAuthorizationHeaderString("rlm");
        final String PREF = "OAuth realm=\"rlm\",";
        authHeaderStr.startsWith(PREF);
        String s = authHeaderStr.substring(PREF.length(), authHeaderStr.length());
        String[] ss = s.split("\\s*,\\s*");
        for (int i = 0; i < ss.length; ++i) ss[i] = ss[i].trim();
        Arrays.sort(ss);
        assertArrayEquals(new String[] {
                "oauth_callback=\"oob\"",
                "oauth_consumer_key=\"TEST_KEY%3D%3D\"",
                "oauth_nonce=\"faQeA8ae22\"",
                "oauth_signature=\"e%2FDKfY2wLhBB9yqj5WtoJv2Ff6w%3D\"",
                "oauth_signature_method=\"HMAC-SHA1\"",
                "oauth_timestamp=\"1368908614\"",
                "oauth_version=\"1.0\"",
        }, ss);

        // URI
        String urlNotIncludingAuthQuery = helper.getUrlStringIncludeQueryParams();
        assertEquals(url, urlNotIncludingAuthQuery);

        // URI including OAuth parameters
        {
            String urlIncludingAuthQuery = helper.getUrlStringIncludeQueryParams(true);
            assertTrue("URL のクエリパラメータ直前まで", urlIncludingAuthQuery.startsWith(url + "?"));
            String queryParamsStr = urlIncludingAuthQuery.substring((url + "?").length());
            String[] params = queryParamsStr.split("&");
            Arrays.sort(params);
            assertArrayEquals(new String[] {
                    "oauth_callback=oob",
                    "oauth_consumer_key=TEST_KEY%3D%3D",
                    "oauth_nonce=faQeA8ae22",
                    "oauth_signature=e%2FDKfY2wLhBB9yqj5WtoJv2Ff6w%3D",
                    "oauth_signature_method=HMAC-SHA1",
                    "oauth_timestamp=1368908614",
                    "oauth_version=1.0",
            }, params);
        }

        // HTTP Request body including OAuth parameters
        {
            String reqBodyIncludingAuthQuery = helper.getRequestBodyString(true);
            String[] params = reqBodyIncludingAuthQuery.split("&");
            Arrays.sort(params);
            assertArrayEquals(new String[] {
                    "oauth_callback=oob",
                    "oauth_consumer_key=TEST_KEY%3D%3D",
                    "oauth_nonce=faQeA8ae22",
                    "oauth_signature=e%2FDKfY2wLhBB9yqj5WtoJv2Ff6w%3D",
                    "oauth_signature_method=HMAC-SHA1",
                    "oauth_timestamp=1368908614",
                    "oauth_version=1.0",
            }, params);
        }
    }

}
