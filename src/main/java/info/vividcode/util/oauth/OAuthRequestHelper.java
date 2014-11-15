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

import info.vividcode.util.Base64Encoder;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Helper for the client of OAuth 1.0 Protocol.
 * <p>
 * Following code is the example of the way to use this class.
 * <pre><code>  // Example: Way to retrieve the temporary credentials from Twitter
 *
 *  // URL.
 *  String url = "https://api.twitter.com/oauth/request_token";
 *  // Request method.
 *  String method = "POST";
 *  // Client identifier (a.k.a. consumer key) and client shared-secret (a.k.a. consumer secret).
 *  String clientIdentifier = "YOUR_CONSUMER_KEY";
 *  String clientSharedSecret = "YOUR_CONSUMER_SECRET";
 *
 *  // Key: the concatenated values of the encoded client shared-secret, an "&amp;" character,
 *  //     and the encoded token shared-secret, which is empty string in this case.
 *  String key = OAuthEncoder.encode(clientSharedSecret) + '&amp;';
 *  // OAuth parameters.
 *  OAuthRequestHelper.ParamList paramList = OAuthRequestHelper.ParamList.fromArray(
 *        new String[][]{
 *          { "oauth_consumer_key", clientIdentifier },
 *          { "oauth_nonce", OAuthRequestHelper.generateNonce() },
 *          { "oauth_signature_method", "HMAC-SHA1" }, // Currently only supports HTAC-SHA1.
 *          { "oauth_timestamp", Long.toString(new Date().getTime() / 1000) },
 *          { "oauth_version", "1.0" },
 *          { "oauth_callback", "oob" },
 *        } );
 *
 *  // Instantiate OAuthRequestHelper.
 *  // In this case, no URI query parameter and no HTTP request body parameters is used,
 *  // so last two parameters are null.
 *  OAuthRequestHelper helper = new OAuthRequestHelper(url, method, key, paramList, null, null);
 *      // Before instantiation of the OAuthRequestHelper is finished, the signature process
 *      // will be done.
 *      // Therefore you can send request just after instantiation.
 *
 *  // Create HttpURLConnection object.
 *  URL u = new URL(helper.getUrlStringIncludeQueryParams());
 *  HttpURLConnection conn = (HttpURLConnection) u.openConnection();
 *  // Set HTTP request method.
 *  conn.setRequestMethod(helper.getRequestMethod());
 *  // Set HTTP Authorization header.
 *  conn.addRequestProperty("Authorization", helper.getAuthorizationHeaderString(""));
 *  // Connect.
 *  conn.connect();</code></pre>
 *
 * @author NOBUOKA Yu
 */
public class OAuthRequestHelper {

    /**
     * Key-value pair.
     * This class is used for representing an OAuth parameter, a URI query parameter,
     * or a "application/x-www-form-urlencoded" entity-body parameter.
     */
    public static class Param {
        private final String mKey;
        private final String mValue;
        public Param(String key, String value) {
            if (key == null)
                throw new IllegalArgumentException("`key` must not be null.");
            mKey = key;
            mValue = value; // TODO `mValue` may be `null`?
        }
        public final String getKey() { return mKey; }
        public final String getValue() { return mValue; }
    }

    public static class ParamComparator implements Comparator<Param> {
        private static ParamComparator instance = null;
        public static synchronized ParamComparator getInstance() {
            if (instance == null) instance = new ParamComparator();
            return instance;
        }

        private ParamComparator() {}

        /**
         * Compares two parameters lexicographically.
         * If two parameters have different keys, then two key strings are
         * compared lexicographically, and the result is returned.
         * If two keys are same, then two value strings are compared
         * lexicographically, and the result is returned.
         */
        @Override
        public int compare(Param o1, Param o2) {
            int r = o1.getKey().compareTo(o2.getKey());
            if (r == 0) {
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return r;
            }
        }
    }

    /**
     * The list of parameters, which is based on {@code ArrayList<Param>}.
     */
    public static class ParamList extends ArrayList<Param> {
        private static final long serialVersionUID = -849036503227560868L;

        public static ParamList fromArray(String[][] params) {
            ParamList list = new ParamList(params.length);
            list.addAll(params);
            return list;
        }

        public ParamList() {
            super();
        }

        public ParamList(int initialCapacity) {
            super(initialCapacity);
        }

        /**
         * Deprecated. Use {@link ParamList#fromArray(String[][])} instead.
         */
        @Deprecated
        public ParamList(String[][] paramStrs) {
            super(paramStrs.length);
            addAll(paramStrs);
        }

        private static final String MSG_ADD_ALL_ARG_ELEM_2_LEN =
                "`paramStrs`, argument of `addAll` method must be an array " +
                "which element is 2-length Array.";

        /**
         * @throws NullPointerException if the specified array is {@code null}.
         * @throws IllegalArgumentException if some of the specified array's elements are not 2-length array..
         */
        public void addAll(String[][] paramStrs) {
            List<Param> list = new ArrayList<Param>(paramStrs.length);
            for (String[] ps : paramStrs) {
                if (ps == null || ps.length != 2) {
                    throw new IllegalArgumentException(MSG_ADD_ALL_ARG_ELEM_2_LEN);
                }
                list.add(new Param(ps[0], ps[1]));
            }
            this.addAll(list);
        }
    }

    private static final byte[] NONCE_SEED_BYTES =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            .getBytes(Charset.forName("US-ASCII"));
    private static final String MSG_NONCE_LEN_MUST_POS =
            "Length of nonce string must be positive integer.";

    /**
     * Deprecated. Use {@link #generateNonce()} instead.
     */
    @Deprecated
    public static String getNonceString() {
        return generateNonce();
    }

    /**
     * Deprecated. Use {@link #generateNonce(int)} instead.
     */
    @Deprecated
    public static String getNonceString(int length) {
        return generateNonce(length);
    }

    /**
     * Generate a random string, which can be used as a nonce for OAuth 1.0 Protocol.
     * @return Generated random string, length of which is 16.
     */
    public static String generateNonce() {
        return generateNonce(16);
    }

    /**
     * Generate a random string, which can be used as a nonce for OAuth 1.0 Protocol.
     * @param length The length of generated string.
     * @return Generated random string, length of which is specified as a parameter.
     */
    public static String generateNonce(int length) {
        if (!(length > 0)) throw new IllegalArgumentException(MSG_NONCE_LEN_MUST_POS);

        SecureRandom rand = new SecureRandom();
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = NONCE_SEED_BYTES[rand.nextInt(NONCE_SEED_BYTES.length)];
        }
        return new String(bytes, Charset.forName("US-ASCII"));
    }

    private String mUrlStr;
    private String mMethodStr;
    private String mSecretsStr;
    private ParamList mOauthParams;
    private ParamList mUrlQueryParams;
    private ParamList mReqBodyParams;
    private String mSignature;

    /**
     * Create an {@code OAuthRequestHelper} object, which has the information of
     * the HTTP request using OAuth 1.0 protocol, such as an URI including request
     * parameters, OAuth parameters, and request body parameters.
     * The signature for OAuth 1.0 protocol is generated in the instantiation process.
     * See: <a href="https://tools.ietf.org/html/rfc5849#section-3.4">RFC 5849 (3.4. Signature)</a>
     *
     * @param urlStr The base string URI. It must not include request parameters.
     * @param method The HTTP request method in uppercase. For example: "HEAD", "GET", "POST", etc.
     * @param secretsStr The concatenated values of the encoded client shared-secret, an "&amp;" character, and the encoded token shared-secret.
     * @param oauthParams The OAuth parameters except a signature.
     * @param urlQueryParams The URI query parameters. May be null.
     * @param reqBodyParams The "application/x-www-form-urlencoded" entity-body parameter. May be null.
     * @throws GeneralSecurityException thrown when generating a signature is failed.
     */
    public OAuthRequestHelper(String urlStr, String method, String secretsStr,
            ParamList oauthParams, ParamList urlQueryParams, ParamList reqBodyParams)
    throws GeneralSecurityException {
        mUrlStr = urlStr;
        mMethodStr = method;
        mSecretsStr = secretsStr;
        mOauthParams = oauthParams;
        mUrlQueryParams = urlQueryParams;
        mReqBodyParams = reqBodyParams;

        sign();
    }

    private String toNormalizationString(ParamList params) {
        StringBuilder sb = new StringBuilder();
        for (Param param : params) {
            if (sb.length() != 0) sb.append('&');
            sb.append(OAuthEncoder.encode(param.getKey())).append('=')
                    .append(OAuthEncoder.encode(param.getValue()));
        }
        return sb.toString();
    }

    private static void addEncodedKeyValuePairsToParamList(ParamList src, ParamList dist) {
        for (Param p : src) {
            dist.add(new Param(OAuthEncoder.encode(p.getKey()), OAuthEncoder.encode(p.getValue())));
        }
    }

    String createParameterNormalizationString() {
        ParamList paramList = new ParamList();
        if (mOauthParams    != null) addEncodedKeyValuePairsToParamList(mOauthParams, paramList);
        if (mUrlQueryParams != null) addEncodedKeyValuePairsToParamList(mUrlQueryParams, paramList);
        if (mReqBodyParams  != null) addEncodedKeyValuePairsToParamList(mReqBodyParams, paramList);
        Collections.sort(paramList, ParamComparator.getInstance());

        StringBuilder sb = new StringBuilder();
        for (Param param : paramList) {
            if (sb.length() != 0) sb.append('&');
            sb.append(param.getKey()).append('=').append(param.getValue());
        }
        return sb.toString();
    }

    // TODO : 例外処理
    private void sign() throws GeneralSecurityException {
        // See: https://tools.ietf.org/html/rfc5849#section-3.4.1
        final String signatureBaseStr =
            OAuthEncoder.encode(mMethodStr) + '&' +
            OAuthEncoder.encode(mUrlStr) + '&' +
            OAuthEncoder.encode(createParameterNormalizationString());

        // TODO : 別のアルゴリズムへの対応
        final String algorithmName = "HmacSHA1";
        Key key = new SecretKeySpec(
                mSecretsStr.getBytes(Charset.forName("US-ASCII")), algorithmName);
        Mac mac = Mac.getInstance(algorithmName);
        mac.init(key);
        byte[] digest = mac.doFinal(signatureBaseStr.getBytes(Charset.forName("US-ASCII")));
        mSignature = Base64Encoder.encode(digest);
    }

    /**
     * Return the URL string including query parameters.
     * If you want that the OAuth parameters are included,
     * use the {@link #getUrlStringIncludeQueryParams(boolean)} method.
     *
     * @return The URL string including query parameters.
     */
    public String getUrlStringIncludeQueryParams() {
        return getUrlStringIncludeQueryParams(false);
    }

    /**
     * Return the URL string including query parameters.
     * It generated from the URL specified when this object is instantiated,
     * which is not included query parameters and
     * the values of a {@link ParamList} object that specified
     * as request body parameters when this object is instantiated.
     * The OAuth parameters will be included in query parameters if you want.
     *
     * @param includeOAuthParams tells whether the OAuth parameters are included in query parameters or not
     * @return The URL string including query parameters.
     */
    public String getUrlStringIncludeQueryParams(boolean includeOAuthParams) {
        ParamList paramList = new ParamList();
        if (includeOAuthParams) {
            if (mOauthParams != null) paramList.addAll(mOauthParams);
            paramList.add(new Param("oauth_signature", mSignature));
        }
        if (mUrlQueryParams != null) paramList.addAll(mUrlQueryParams);
        if (paramList.size() == 0) return mUrlStr;
        return mUrlStr + "?" + toNormalizationString(paramList);
    }

    /**
     * Return the string to send as a request body.
     * If you want that the OAuth parameters are included,
     * use the {@link #getRequestBodyString(boolean)} method.
     *
     * @return The string to send as a request body.
     */
    public String getRequestBodyString() {
        return getRequestBodyString(false);
    }

    /**
     * Return the string to send as a request body.
     * It generated from the values of a {@link ParamList} object that specified
     * as request body parameters when this object is instantiated.
     * The OAuth parameters will be included if you want.
     *
     * @param includeOAuthParams tells whether the OAuth parameters are included or not
     * @return The string to send as a request body.
     */
    public String getRequestBodyString(boolean includeOAuthParams) {
        ParamList paramList = new ParamList();
        if (includeOAuthParams) {
            if (mOauthParams != null) paramList.addAll(mOauthParams);
            paramList.add(new Param("oauth_signature", mSignature));
        }
        if (mReqBodyParams != null) paramList.addAll(mReqBodyParams);
        if (paramList.size() == 0) return "";
        return toNormalizationString(paramList);
    }

    /**
     * Return the string to send as a value of an HTTP Authorization header.
     * Following string is the example of the return value.
     * <pre><code>OAuth realm="...", oauth_timestamp="...", oauth_verifier="...", ...</code></pre>
     *
     * @param realm The realm value, which is may be an empty string. Must not be {@code null}.
     * @return The string to send as a value of an HTTP Authorization header.
     */
    public String getAuthorizationHeaderString(String realm) {
        StringBuilder sb = new StringBuilder();
        sb.append("OAuth realm=\"").append(realm).append('"');
        for (Param p : mOauthParams) {
            sb.append(", ");
            sb.append(OAuthEncoder.encode(p.getKey()));
            sb.append("=\"").append(OAuthEncoder.encode(p.getValue())).append('"');
        }
        sb.append(", ");
        sb.append("oauth_signature");
        sb.append("=\"").append(OAuthEncoder.encode(mSignature)).append('"');
        return sb.toString();
    }

    /**
     * Return the request method.
     * @return The request method that specified when this object is instantiated.
     */
    public String getRequestMethod() {
        return mMethodStr;
    }

}
