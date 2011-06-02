package info.vividcode.util.oauth;

import info.vividcode.util.Base64Encoder;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class OAuthRequestHelper {
	
	static public class Param {
		private String mKey;
		private String mValue;
		public Param( String key, String value ) {
			mKey = key;
			mValue = value;
		}
		public String getKey() { return mKey; }
		public String getValue() { return mValue; }
	}
	
	static public class ParamComparator implements Comparator<Param> {
		private ParamComparator() {}
		@Override
		public int compare( Param o1, Param o2 ) {
			int r = o1.getKey().compareTo( o2.getKey() );
			if( r == 0 ) {
				return o1.getValue().compareTo( o2.getValue() );
			} else {
				return r;
			}
		}
		static private ParamComparator instance = null;
		static public ParamComparator getInstance() {
			if( instance == null ) {
				instance = new ParamComparator();
			}
			return instance;
		}
	}
	
	static public class ParamList extends ArrayList<Param> {
		private static final long serialVersionUID = -849036503227560868L;
		public ParamList() {
			super();
		}
		public ParamList( String[][] paramStrs ) {
			super();
			addAll( paramStrs );
		}
		public void addAll( String[][] paramStrs ) {
			for( String[] ps : paramStrs ) {
				if( ps.length != 2 ) {
					// TODO : 例外処理
					throw new RuntimeException( "配列の形式が不正です" );
				}
				add( new Param( ps[0], ps[1] ) );
			}
		}
	}
	
	static final private byte[] NONCE_SEED_BYTES = 
		"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
		.getBytes( Charset.forName( "US-ASCII" ) );
	static public String getNonceString() {
		return getNonceString( 16 );
	}
	static public String getNonceString( int length ) {
		SecureRandom rand = new SecureRandom();
		byte[] bytes = new byte[ length ];
		for( int i = 0; i < length; i++ ) {
			bytes[i] = NONCE_SEED_BYTES[ rand.nextInt( NONCE_SEED_BYTES.length ) ];
		}
        return new String( bytes, Charset.forName( "US-ASCII" ) );
	}
	
	private String mUrlStr;
	private String mMethodStr;
	private String mSecretsStr;
	private ParamList mOauthParams;
	private ParamList mUrlQueryParams;
	private ParamList mReqBodyParams;
	
	public OAuthRequestHelper( String urlStr, String method, String secretsStr,
			ParamList oauthParams, ParamList urlQueryParams, ParamList reqBodyParams ) 
	throws GeneralSecurityException {
		mUrlStr = urlStr;
		mMethodStr = method;
		mSecretsStr = secretsStr;
		mOauthParams = oauthParams;
		mUrlQueryParams = urlQueryParams;
		mReqBodyParams = reqBodyParams;
		
		sign();
	}
	
	private String toNormalizationString( Param[] params ) {
		StringBuilder sb = new StringBuilder();
		for( Param param : params ) {
			if( sb.length() != 0 ) {
				sb.append( "&" );
			}
			sb.append( OAuthEncoder.encode( param.getKey() ) + "=" );
			sb.append( OAuthEncoder.encode( param.getValue() ) );
		}
		return sb.toString();
	}
	
	private String createParameterNormalizationString() {
		ParamList paramList = new ParamList();
		if( mOauthParams    != null ) paramList.addAll( mOauthParams );
		if( mUrlQueryParams != null ) paramList.addAll( mUrlQueryParams );
		if( mReqBodyParams  != null ) paramList.addAll( mReqBodyParams );
		Param[] params = paramList.toArray( new Param[ paramList.size() ] );
		Arrays.sort( params, ParamComparator.getInstance() );
		String ns = toNormalizationString( params );
		return ns;
	}
	
	// TODO : 例外処理
	private void sign() throws GeneralSecurityException {
		String signatureBaseStr = 
			OAuthEncoder.encode( mMethodStr ) + '&' + 
			OAuthEncoder.encode( mUrlStr ) + '&' + 
			OAuthEncoder.encode( createParameterNormalizationString() );
		// TODO : 別のアルゴリズムへの対応
		String algorithmName = "HmacSHA1";
		SecretKeySpec sks = new SecretKeySpec( 
				mSecretsStr.getBytes( Charset.forName( "US-ASCII" ) ), algorithmName );
		Mac mac = Mac.getInstance( algorithmName );
		mac.init( sks );
		byte[] digest = mac.doFinal( signatureBaseStr.getBytes( Charset.forName( "US-ASCII" ) ) );
		String signatureStr = new String( Base64Encoder.encode( digest ), Charset.forName( "US-ASCII" ) );
		mOauthParams.add( new Param( "oauth_signature", signatureStr ) );
	}
	
	public String getUrlStringIncludeQueryParams() {
		return getUrlStringIncludeQueryParams( false );
	}
	
	public String getUrlStringIncludeQueryParams( boolean includeOAuthParams ) {
		ParamList paramList = new ParamList();
		if( includeOAuthParams && mOauthParams != null ) paramList.addAll( mOauthParams );
		if( mUrlQueryParams != null ) paramList.addAll( mUrlQueryParams );
		if( paramList.size() == 0 ) return mUrlStr;
		Param[] params = paramList.toArray( new Param[ paramList.size() ] );
		return mUrlStr + "?" + toNormalizationString( params );
	}
	
	public String getAuthorizationHeaderString( String realmStr ) {
		// OAuth ヘッダ
		StringBuilder sb = new StringBuilder();
		sb.append( "OAuth realm=\"" + realmStr + "\"" );
		for( Param p : mOauthParams ) {
			sb.append( ", " );
			sb.append( OAuthEncoder.encode( p.getKey() ) + "=\"" );
			sb.append( OAuthEncoder.encode( p.getValue() ) + "\"" );
		}
		return sb.toString();
	}
	
	/*
	public void connect() {
		String urlStr = "https://userstream.twitter.com/2/user.json";
		String method = "GET";
		String secretsStr = "tRJ5IJPbVtmT83Z5rIc11usVd0CS5HiUEPnORX97aw&GA2tiYSAWesCpbytiMizmYWAXdabcqnhpMRHLIJE";
		
		ArrayList<ReqParam> params = new ArrayList<ReqParam>();
		params.add( new ReqParam( "oauth_consumer_key", "9VOTgNEOD5asafMKffYl3Q" ) );
		params.add( new ReqParam( "oauth_nonce", "randomString" ) );
		params.add( new ReqParam( "oauth_signature_method", "HMAC-SHA1" ) );
		params.add( new ReqParam( "oauth_timestamp", Long.toString( new Date().getTime() / 1000 ) ) );
		params.add( new ReqParam( "oauth_token", "59411342-GC0DyjBY73x6bAYeT82xNKVgbXnMCv8hOqguXRKWM" ) );
		params.add( new ReqParam( "oauth_version", "1.0" ) );
		
		URL url = new URL( urlStr );
		URLConnection tmpConn = url.openConnection();
		System.out.println( tmpConn instanceof HttpURLConnection );
		conn = (HttpURLConnection)tmpConn;
		
		// consumer key : 9VOTgNEOD5asafMKffYl3Q
		StringBuilder sb = new StringBuilder();
		for( ReqParam p : params ) {
			if( sb.length() != 0 ) {
				sb.append( "&" );
			}
			sb.append( OAuthEncoder.encode( p.key ) + "=" + OAuthEncoder.encode( p.value ) );
		}
		String paramsStr = sb.toString();
		String signatureBaseStr = OAuthEncoder.encode( method ) + '&' + 
			OAuthEncoder.encode( urlStr ) + '&' + OAuthEncoder.encode( paramsStr );
		System.out.println( signatureBaseStr );
		
		// ====
		String signatureStr = null;
		try {
			String algorithmName = "HmacSHA1";
			SecretKeySpec sks = new SecretKeySpec( 
					secretsStr.getBytes( Charset.forName( "US-ASCII" ) ), algorithmName );
			Mac mac = Mac.getInstance( algorithmName );
			//TODO
			mac.init( sks );
			byte[] digest = mac.doFinal( signatureBaseStr.getBytes( "US-ASCII" ) );
			// TODO
			signatureStr = new String( Base64Encoder.encode( digest ), Charset.forName( "US-ASCII" ) );
			//MessageDigest md = MessageDigest.getInstance( "SHA-1" );
		} catch( NoSuchAlgorithmException err ) {
			// TODO Auto-generated catch block
			err.printStackTrace();
		} catch( InvalidKeyException err ) {
			// TODO Auto-generated catch block
			err.printStackTrace();
		}
		System.out.println( signatureStr );
		params.add( new ReqParam( "oauth_signature", signatureStr ) );
		
		// OAuth ヘッダ
		sb = new StringBuilder();
		sb.append( "OAuth realm=\"\"" );
		for( ReqParam p : params ) {
			sb.append( ", " );
			sb.append( OAuthEncoder.encode( p.key ) + "=\"" + OAuthEncoder.encode( p.value ) + "\"" );
		}
		String oauthHeaderStr = sb.toString();
		
		conn.setRequestProperty( "Authorization", oauthHeaderStr );
		System.out.println( oauthHeaderStr );
		
	}
	*/
	
}
