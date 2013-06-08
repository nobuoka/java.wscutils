package info.vividcode.util.json;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * JSON の array を表すためのクラスであり, JsonValue オブジェクトを格納するための
 * {@link java.util.List} でもある.
 *
 * @author nobuoka
 *
 */
public class JsonArray extends ArrayList<JsonValue> implements JsonValue {

    private static final long serialVersionUID = -8694573603186652059L;
    final Object ID;

    /**
     * 何も含まない状態の JsonArray オブジェクトを生成する.
     */
    public JsonArray() {
        super();
        ID = new Object();
    }

    /**
     * 対応する JSON の値のタイプを返す.
     * このクラスは JSON の array に対応するため, 常に {@link JsonValue.ValueType}.ARRAY_VALUE を返す.
     */
    @Override
    public ValueType valueType() {
        return ValueType.ARRAY_VALUE;
    }

    /**
     * 自分自身を返す.
     * このオブジェクトが JsonValue 型として扱われている場合に, キャストの代わりとして使用することができる.
     */
    @Override
    public JsonArray asArray() {
        return this;
    }


    private String createErrorMessageOfMethodNotSupporting(String methodName) {
        return "JSON value of " + this.valueType().name() +
                " doesn't support “" + methodName + "” method.";
    }

    @Override
    public JsonObject asObject() throws JsonTypeException {
        throw new JsonTypeException(createErrorMessageOfMethodNotSupporting("asObject()"));
    }

    @Override
    public BigDecimal numberValue() throws JsonTypeException {
        throw new JsonTypeException(createErrorMessageOfMethodNotSupporting("numberValue()"));
    }

    @Override
    public String stringValue() throws JsonTypeException {
        throw new JsonTypeException(createErrorMessageOfMethodNotSupporting("stringValue()"));
    }

    @Override
    public Boolean booleanValue() throws JsonTypeException {
        throw new JsonTypeException(createErrorMessageOfMethodNotSupporting("booleanValue()"));
    }

    @Override
    public JsonValue get(String key) throws JsonTypeException {
        throw new JsonTypeException(createErrorMessageOfMethodNotSupporting("get(String)"));
    }

}
