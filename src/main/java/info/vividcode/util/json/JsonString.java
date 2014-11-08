package info.vividcode.util.json;

import java.math.BigDecimal;

/**
 * JSON の string を表すためのクラス.
 *
 * 対応する Java の値は {@link String} であり, {@link JsonString#stringValue()}
 * メソッドで取得できる.
 *
 * @author nobuoka
 *
 */
public class JsonString implements JsonValue {

    private static final String CLASS_NAME  = "JsonString";
    private static final String METHOD_NAME = "stringValue()";

    private String val;

    /**
     * 指定の String オブジェクトに対応する JsonString オブジェクトを生成する.
     * @param str 表現対象となる JSON string に対応する String オブジェクト
     */
    public JsonString(String str) {
        this.val = str;
    }

    /**
     * 対応する JSON の値のタイプを返す.
     * このクラスは JSON の string に対応するため,
     * 常に {@link JsonValue.ValueType}.STRING_VALUE を返す.
     */
    @Override
    public ValueType valueType() {
        return ValueType.STRING_VALUE;
    }

    /**
     * 常に, 例外 {@link UnsupportedOperationException} が投げられる.
     */
    @Override
    public JsonArray arrayValue() {
        throw new UnsupportedOperationException(
                "This object is a " + CLASS_NAME + " object. " +
                "If you want to get the value, please use the " + METHOD_NAME + " method instead." );
    }

    /**
     * 常に, 例外 {@link UnsupportedOperationException} が投げられる.
     */
    @Override
    public JsonObject objectValue() {
        throw new UnsupportedOperationException(
                "This object is a " + CLASS_NAME + " object. " +
                "If you want to get the value, please use the " + METHOD_NAME + " method instead." );
    }

    /**
     * 常に, 例外 {@link UnsupportedOperationException} が投げられる.
     */
    @Override
    public BigDecimal numberValue() {
        throw new UnsupportedOperationException(
                "This object is a " + CLASS_NAME + " object. " +
                "If you want to get the value, please use the " + METHOD_NAME + " method instead." );
    }

    /**
     * 対応する String オブジェクトが返される.
     */
    @Override
    public String stringValue() {
        return val;
    }

    /**
     * 常に, 例外 {@link UnsupportedOperationException} が投げられる.
     */
    @Override
    public Boolean booleanValue() {
        throw new UnsupportedOperationException(
                "This object is a " + CLASS_NAME + " object. " +
                "If you want to get the value, please use the " + METHOD_NAME + " method instead." );
    }

    /**
     * この JsonString オブジェクトを表す String オブジェクトを返す.
     * この JsonString オブジェクトに対応する String オブジェクトを s とし,
     * s を JSON 文字列の形式に変換した String オブジェクトを ss とすると,
     * "[JSON string : " + ss + "]" に等しい文字列が返される.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        JsonSerializer.serializeJString(sb, val);
        return "[Json string : " + sb.toString() + "]";
    }

    /**
     * このオブジェクトと指定のオブジェクトが同値かどうかチェックする.
     * 引数が null ではなく, このオブジェクトと同値な String オブジェクトをラップする
     * JsonString オブジェクトの場合に true を返す.
     * それ以外の場合は false.
     * @param o 比較対象のオブジェクト
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof JsonString) {
            JsonString oo = (JsonString) o;
            return this.val.equals(oo.val);
        } else {
            return false;
        }
    }

}
