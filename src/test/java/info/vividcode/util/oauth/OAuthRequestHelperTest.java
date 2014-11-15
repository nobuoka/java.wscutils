/*
Copyright 2011-2014 NOBUOKA Yu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package info.vividcode.util.oauth;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import org.junit.Test;

public class OAuthRequestHelperTest {

    @Test
    public void test_generateNonce() {
        String nonceStr;

        nonceStr = OAuthRequestHelper.generateNonce();
        assertEquals("Default length of nonce is 16", 16, nonceStr.length());

        nonceStr = OAuthRequestHelper.generateNonce(1);
        assertEquals(1, nonceStr.length());

        nonceStr = OAuthRequestHelper.generateNonce(5);
        assertEquals(5, nonceStr.length());

        nonceStr = OAuthRequestHelper.generateNonce(200);
        assertEquals(200, nonceStr.length());
    }

    @Test( expected = IllegalArgumentException.class )
    public void test_generateNonce_withZeroLength() {
        OAuthRequestHelper.generateNonce(0);
    }

    @Test( expected = IllegalArgumentException.class )
    public void test_generateNonce_withNegativeLength() {
        OAuthRequestHelper.generateNonce(-1);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void test_getNonceString() {
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
    @SuppressWarnings("deprecation")
    public void test_getNonceString_withZeroLength() {
        OAuthRequestHelper.getNonceString(0);
    }

    @Test( expected = IllegalArgumentException.class )
    @SuppressWarnings("deprecation")
    public void test_getNonceString_withNegativeLength() {
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
        OAuthRequestHelper.ParamList paramList = OAuthRequestHelper.ParamList.fromArray(
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
        assertTrue("`OAuth realm=\"rlm\"` で始まる", authHeaderStr.startsWith(PREF));
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

    @Test
    public void test_parametersNormalization() throws GeneralSecurityException {
        // Test case from RFC 5849.
        // See: https://tools.ietf.org/html/rfc5849#section-3.4.1.3.2

        OAuthRequestHelper.ParamList oauthParams = OAuthRequestHelper.ParamList.fromArray(new String[][] {
                { "oauth_consumer_key", "9djdj82h48djs9d2" },
                { "oauth_token", "kkk9d7dh3k39sjv7" },
                { "oauth_signature_method", "HMAC-SHA1" },
                { "oauth_timestamp", "137131201" },
                { "oauth_nonce", "7d8f3e4a" },
        });
        OAuthRequestHelper.ParamList queryParams = OAuthRequestHelper.ParamList.fromArray(new String[][] {
                { "b5", "=%3D" },
                { "a3", "a" },
                { "c@", "" },
                { "a2", "r b" },
                { "c2", "" },
                { "a3", "2 q" },
        });

        OAuthRequestHelper helper = new OAuthRequestHelper("http://example.com/", "GET", "&", oauthParams, queryParams, null);
        String normalizedParamsStr = helper.createParameterNormalizationString();

        assertEquals("a2=r%20b&a3=2%20q&a3=a&b5=%3D%253D&c%40=&c2=&oauth_consumer_key=9dj" +
                "dj82h48djs9d2&oauth_nonce=7d8f3e4a&oauth_signature_method=HMAC-SHA1" +
                "&oauth_timestamp=137131201&oauth_token=kkk9d7dh3k39sjv7", normalizedParamsStr);
    }

}
