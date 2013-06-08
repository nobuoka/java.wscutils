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
     * 自分自身を返す.
     * このオブジェクトが JsonValue 型として扱われている場合に, キャストの代わりとして使用することができる.
     */
    @Override
    public JsonObject asObject() {
        return this;
    }

    @Override
    public JsonValue get(String key) {
        return super.get(key);
    }


    private String createErrorMessageOfMethodNotSupporting(String methodName) {
        return "JSON value of " + this.valueType().name() +
                " doesn't support “" + methodName + "” method.";
    }

    @Override
    public JsonArray asArray() throws JsonTypeException {
        throw new JsonTypeException(createErrorMessageOfMethodNotSupporting("asArray()"));
    }

    @Override
    public BigDecimal numberValue() {
        throw new JsonTypeException(createErrorMessageOfMethodNotSupporting("numberValue()"));
    }

    @Override
    public String stringValue() {
        throw new JsonTypeException(createErrorMessageOfMethodNotSupporting("stringValue()"));
    }

    @Override
    public Boolean booleanValue() {
        throw new JsonTypeException(createErrorMessageOfMethodNotSupporting("booleanValue()"));
    }

    @Override
    public JsonValue get(int index) {
        throw new JsonTypeException(createErrorMessageOfMethodNotSupporting("get(int)"));
    }

}
