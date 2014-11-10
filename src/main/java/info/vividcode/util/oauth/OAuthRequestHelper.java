package info.vividcode.util.oauth;

import info.vividcode.util.Base64Encoder;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *OAuth 認証を用いた HTTP リクエストを手助けするためのクラス.
 *<p>
 *使用例は以下である.
 *<pre><code>  // Twitter に Request token を求める際の例
 *
 *  // 送信先 URL
 *  String url = "https://api.twitter.com/oauth/request_token";
 *  // リクエストメソッド
 *  String method = "POST";
 *  // Consumer key と secret
 *  String consumerKey    = "XXXXXXXXXX";
 *  String consumerSecret = "XXXXXXXXXX";
 *  // 今回は request token を求める例で token secret はないので空文字列
 *  String tokenSecret = "";
 *  // secrets 文字列 (Consumer secret と token secret を繋いだもの)
 *  String secrets = OAuthEncoder.encode(consumerSecret) + "&amp;" + OAuthEncoder.encode(tokenSecret)
 *  // OAuth 関係のパラメータ
 *  OAuthRequestHelper.ParamList paramList = new OAuthRequestHelper.ParamList(
 *        new String[][]{
 *          { "oauth_consumer_key", consumerKey },
 *          { "oauth_nonce", OAuthRequestHelper.getNonceString() },
 *          { "oauth_signature_method", "HMAC-SHA1" },
 *          { "oauth_timestamp", Long.toString( new Date().getTime() / 1000 ) },
 *          { "oauth_version", "1.0" },
 *          { "oauth_callback", "oob" },
 *        } );
 *  // OAuthRequestHelper のインスタンス化
 *  // 今回はクエリパラメータにもリクエストボディにも情報を載せないので, 後ろ 2 つの引数は null
 *  OAuthRequestHelper helper = new OAuthRequestHelper( url, method, secrets, paramList, null, null );
 *      // インスタンス化と同時にシグニチャ生成もされるので, あとは helper から情報を取って
 *      // リクエストを送信するだけ
 *
 *  //　リクエスト先への connection 生成
 *  URL u = new URL( helper.getUrlStringIncludeQueryParams() );
 *  HttpURLConnection conn = (HttpURLConnection)u.openConnection();
 *  // リクエストメソッドのセット
 *  conn.setRequestMethod( helper.getRequestMethod() );
 *  // Authorization ヘッダのセット
 *  conn.addRequestProperty( "Authorization", helper.getAuthorizationHeaderString("") );
 *  // 接続
 *  conn.connect();
 *      // 後はレスポンスを受け取って処理する</code></pre>
 *@author nobuoka
 */
public class OAuthRequestHelper {

    /**
     * リクエスト時に送信する単一のパラメータを表すクラス.
     * @author nobuoka
     *
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

    /**
     * パラメータ同士の比較を行うためのクラス.
     * OAuth 認証では, パラメータを並べ替える必要があり,
     * その際にこのクラスのインスタンスを使用する
     *
     * @author nobuoka
     *
     */
    public static class ParamComparator implements Comparator<Param> {
        private ParamComparator() {}
        @Override
        public int compare(Param o1, Param o2) {
            int r = o1.getKey().compareTo(o2.getKey());
            if( r == 0 ) {
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return r;
            }
        }
        static private ParamComparator instance = null;
        static public ParamComparator getInstance() {
            if (instance == null) {
                instance = new ParamComparator();
            }
            return instance;
        }
    }

    /**
     * パラメータ (Param オブジェクト) のリストを表すクラス.
     * 実装としては {@code ArrayList<Param>} であり,
     * パラメータの追加を行いやすいように 2 次元の
     * String 型配列を受け取るコンストラクタと addAll メソッドが追加されている.
     *
     * @author nobuoka
     *
     */
    public static class ParamList extends ArrayList<Param> {
        private static final long serialVersionUID = -849036503227560868L;
        public ParamList() {
            super();
        }
        public ParamList(String[][] paramStrs) {
            super(paramStrs.length);
            addAll(paramStrs);
        }
        private static final String MSG_ADD_ALL_ARG_ELEM_2_LEN =
                "`paramStrs`, argument of `addAll` method must be an array " +
                "which element is 2-length Array.";
        public void addAll(String[][] paramStrs) {
            List<Param> list = new ArrayList<Param>(paramStrs.length);
            for (String[] ps : paramStrs) {
                if (ps.length != 2) {
                    // TODO : 例外処理
                    throw new IllegalArgumentException(MSG_ADD_ALL_ARG_ELEM_2_LEN);
                }
                list.add( new Param(ps[0], ps[1]) );
            }
            this.addAll(list);
        }
    }

    private static final byte[] NONCE_SEED_BYTES =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            .getBytes( Charset.forName("US-ASCII") );
    private static final String MSG_NONCE_LEN_MUST_POS =
            "Length of nonce string must be positive integer.";

    /**
     *OAuth 認証に用いる nonce 文字列を生成する.
     *@return 長さ 16 の nonce 文字列
     */
    public static String getNonceString() {
        return getNonceString(16);
    }

    /**
     *OAuth 認証に用いる nonce 文字列を, 指定の長さで生成する.
     *@param length 生成する nonce 文字列の長さ
     *@return 生成した nonce 文字列
     */
    public static String getNonceString(int length) {
        if (!(length > 0)) throw new IllegalArgumentException(MSG_NONCE_LEN_MUST_POS);

        SecureRandom rand = new SecureRandom();
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = NONCE_SEED_BYTES[ rand.nextInt(NONCE_SEED_BYTES.length) ];
        }
        return new String( bytes, Charset.forName("US-ASCII") );
    }

    private String mUrlStr;
    private String mMethodStr;
    private String mSecretsStr;
    private ParamList mOauthParams;
    private ParamList mUrlQueryParams;
    private ParamList mReqBodyParams;
    private String mSignature;

    /**
     *OAuth 認証を用いた HTTP リクエストに必要な情報を保持した
     *OAuthRequestHelper オブジェクトを生成する.
     *
     *@param urlStr リクエスト先の URL. クエリーパラメータは含めない
     *@param method リクエストメソッド. "PUT" や "GET" など
     *@param secretsStr Consumer secret と token secret を "&amp;" 記号で繋いだ文字列. token secret がない場合は空文字列を使う
     *@param oauthParams OAuth 認証のためのパラメータのリスト
     *@param urlQueryParams クエリーパラメータのリスト
     *@param reqBodyParams リクエストボディに含めるパラメータのリスト
     *@throws GeneralSecurityException OAuth 認証の署名ができなかった場合に発生する
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

    private String toNormalizationString(Param[] params) {
        StringBuilder sb = new StringBuilder();
        for (Param param : params) {
            if (sb.length() != 0) {
                sb.append("&");
            }
            sb.append( OAuthEncoder.encode(param.getKey()) + "=" );
            sb.append( OAuthEncoder.encode(param.getValue()) );
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
        paramList.sort(ParamComparator.getInstance());

        StringBuilder sb = new StringBuilder();
        for (Param param : paramList) {
            if (sb.length() != 0) sb.append('&');
            sb.append(param.getKey()).append('=').append(param.getValue());
        }
        return sb.toString();
    }

    // TODO : 例外処理
    private void sign() throws GeneralSecurityException {
        final String signatureBaseStr =
            OAuthEncoder.encode( mMethodStr ) + '&' +
            OAuthEncoder.encode( mUrlStr ) + '&' +
            OAuthEncoder.encode( createParameterNormalizationString() );

        // TODO : 別のアルゴリズムへの対応
        final String algorithmName = "HmacSHA1";
        Key key = new SecretKeySpec(
                mSecretsStr.getBytes(Charset.forName("US-ASCII")), algorithmName );
        Mac mac = Mac.getInstance(algorithmName);
        mac.init(key);
        byte[] digest = mac.doFinal(signatureBaseStr.getBytes(Charset.forName("US-ASCII")));
        mSignature = Base64Encoder.encode(digest);
    }

    /**
     *クエリパラメータを含んだ URL を文字列として返す.
     *OAuth 関係のパラメータはクエリパラメータに含めない.
     *@return クエリパラメータを含んだ URL の文字列表現
     */
    public String getUrlStringIncludeQueryParams() {
        return getUrlStringIncludeQueryParams(false);
    }

    /**
     *クエリパラメータを含んだ URL を文字列として返す.
     *OAuth 関係のパラメータを含めるかどうかは引数によって決める.
     *@param includeOAuthParams OAuth 関係のパラメータをクエリパラメータに含めるかどうか. 含める場合は true
     *@return クエリパラメータを含んだ URL の文字列表現
     */
    public String getUrlStringIncludeQueryParams(boolean includeOAuthParams) {
        ParamList paramList = new ParamList();
        if (includeOAuthParams) {
            if (mOauthParams != null) paramList.addAll(mOauthParams);
            paramList.add(new Param("oauth_signature", mSignature));
        }
        if (mUrlQueryParams != null) paramList.addAll(mUrlQueryParams);
        if (paramList.size() == 0) return mUrlStr;
        Param[] params = paramList.toArray( new Param[paramList.size()] );
        return mUrlStr + "?" + toNormalizationString(params);
    }

    /**
     *リクエストボディとして送信すべき値を文字列として取得する.
     *OAuth 関係のパラメータはクエリパラメータに含めない.
     *@return リクエストボディとして送信すべき文字列
     */
    public String getRequestBodyString() {
        return getRequestBodyString(false);
    }

    /**
     *リクエストボディとして送信すべき値を文字列として取得する.
     *OAuth 関係のパラメータを含めるかどうかは引数によって決める.
     * @param includeOAuthParams OAuth 関係のパラメータをクエリパラメータに含めるかどうか. 含める場合は true
     * @return リクエストボディとして送信すべき文字列
     */
    public String getRequestBodyString(boolean includeOAuthParams) {
        ParamList paramList = new ParamList();
        if (includeOAuthParams) {
            if (mOauthParams != null) paramList.addAll(mOauthParams);
            paramList.add(new Param("oauth_signature", mSignature));
        }
        if (mReqBodyParams != null) paramList.addAll(mReqBodyParams);
        if (paramList.size() == 0) return "";
        Param[] params = paramList.toArray( new Param[paramList.size()] );
        return toNormalizationString(params);
    }

    /**
     *HTTP リクエストの Authorization ヘッダとして送信すべき文字列を返す.
     *形式は以下のようなものである.
     *<pre>  OAuth realm="...", oauth_timestamp="...", oauth_verifier="...", ...</pre>
     *@param realmStr realm として含める文字列
     *@return HTTP リクエストの Authorization ヘッダとして送信すべき文字列
     */
    public String getAuthorizationHeaderString(String realmStr) {
        // OAuth ヘッダ
        StringBuilder sb = new StringBuilder();
        sb.append( "OAuth realm=\"" + realmStr + "\"" );
        for( Param p : mOauthParams ) {
            sb.append( ", " );
            sb.append( OAuthEncoder.encode(p.getKey()) + "=\"" );
            sb.append( OAuthEncoder.encode(p.getValue()) + "\"" );
        }
        sb.append(", ");
        sb.append("oauth_signature");
        sb.append("=\"").append(OAuthEncoder.encode(mSignature)).append('"');
        return sb.toString();
    }

    /**
     *リクエストメソッドを返す.
     *@return インスタンス生成時に指定したリクエストメソッド
     */
    public String getRequestMethod() {
        return mMethodStr;
    }

}
