package info.vividcode.util.json;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * JSON の object を表すためのクラスであり, String オブジェクトと JsonValue
 * オブジェクトの関係を保持する {@link java.util.Map} でもある.
 *
 * @author nobuoka
 *
 */
public class JsonObject extends HashMap<String,JsonValue> implements JsonValue {

    private static final long serialVersionUID = -898362213984847287L;
    private static final String CLASS_NAME  = "JsonObject";
    private static final String METHOD_NAME = "objectValue()";
    final Object ID;

    /**
     * 何も含まない状態の JsonObject オブジェクトを生成する.
     */
    public JsonObject() {
        super();
        ID = new Object();
    }

    /**
     * 対応する JSON の値のタイプを返す.
     * このクラスは JSON の object に対応するため, 常に {@link JsonValue.ValueType}.OBJECT_VALUE を返す.
     */
    @Override
    public ValueType valueType() {
        return ValueType.OBJECT_VALUE;
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
     * 自分自身を返す.
     * このオブジェクトが JsonValue 型として扱われている場合に, キャストの代わりとして使用することができる.
     */
    @Override
    public JsonObject objectValue() {
        return this;
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
     * 常に, 例外 {@link UnsupportedOperationException} が投げられる.
     */
    @Override
    public Boolean booleanValue() {
        throw new UnsupportedOperationException(
                "This object is a " + CLASS_NAME + " object. " +
                "if you want to get the value, please use the " + METHOD_NAME + " method instead." );
    }

}
