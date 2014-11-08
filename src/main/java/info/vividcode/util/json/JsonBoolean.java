package info.vividcode.util.json;

import java.math.BigDecimal;

/**
 * JSON の boolean (true, false) を表すためのクラス.
 *
 * 対応する Java の値は {@link Boolean} クラスのインスタンスであり, {@link JsonBoolean#booleanValue()}
 * メソッドによって取得できる.
 *
 * コンストラクタはなく, {@link JsonBoolean#TRUE} フィールドまたは {@link JsonBoolean#FALSE}
 * フィールドによりインスタンスを取得することができる.
 * @author nobuoka
 *
 */
public class JsonBoolean implements JsonValue {

    private static final String CLASS_NAME  = "JsonBoolean";
    private static final String METHOD_NAME = "booleanValue()";

    private Boolean val;

    private JsonBoolean(Boolean b) {
        val = b;
    }

    /** JSON の true に対応する JsonBoolean オブジェクト */
    static final public JsonBoolean TRUE  = new JsonBoolean(true);
    /** JSON の false に対応する JsonBoolean オブジェクト */
    static final public JsonBoolean FALSE = new JsonBoolean(false);

    /**
     * 対応する JSON の値のタイプを返す.
     * このクラスは JSON の boolean に対応するため, 常に {@link JsonValue.ValueType}.BOOLEAN_VALUE を返す.
     */
    @Override
    public ValueType valueType() {
        return ValueType.BOOLEAN_VALUE;
    }

    /**
     * 常に, 例外 {@link UnsupportedOperationException} が投げられる.
     */
    @Override
    public JsonArray arrayValue() {
        throw new UnsupportedOperationException(
                "This object is a " + CLASS_NAME + " object. " +
                "if you want to get the value, please use the " + METHOD_NAME + " method instead." );
    }

    /**
     * 常に, 例外 {@link UnsupportedOperationException} が投げられる.
     */
    @Override
    public JsonObject objectValue() {
        throw new UnsupportedOperationException(
                "This object is a " + CLASS_NAME + " object. " +
                "if you want to get the value, please use the " + METHOD_NAME + " method instead." );
    }

    /**
     * 常に, 例外 {@link UnsupportedOperationException} が投げられる.
     */
    @Override
    public BigDecimal numberValue() {
        throw new UnsupportedOperationException(
                "This object is a " + CLASS_NAME + " object. " +
                "if you want to get the value, please use the " + METHOD_NAME + " method instead." );
    }

    /**
     * 常に, 例外 {@link UnsupportedOperationException} が投げられる.
     */
    @Override
    public String stringValue() {
        throw new UnsupportedOperationException(
                "This object is a " + CLASS_NAME + " object. " +
                "if you want to get the value, please use the " + METHOD_NAME + " method instead." );
    }

    /**
     * 対応する Boolean オブジェクトが返される.
     */
    @Override
    public Boolean booleanValue() {
        return val;
    }

    /**
     * この JsonBoolean オブジェクトを表す String オブジェクトを返す.
     * このオブジェクトが JSON の true を表す場合は "[JSON boolean : true]" に等しい文字列が,
     * そうでない場合は "[JSON boolean : false]" に等しい文字列が返される.
     */
    @Override
    public String toString() {
        return "[JSON boolean : " + ( val ? "true" : "false" ) + "]";
    }

    /**
     * オブジェクトが同値かどうかチェックする.
     * 引数が null ではなく, このオブジェクトと同じ Boolean オブジェクトをラップする JsonBoolean オブジェクトの場合に true を返す.
     * それ以外の場合は false.
     * @param o 比較対象のオブジェクト
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof JsonBoolean) {
            JsonBoolean oo = (JsonBoolean) o;
            return this.val.equals(oo.val);
        } else {
            return false;
        }
    }

}
