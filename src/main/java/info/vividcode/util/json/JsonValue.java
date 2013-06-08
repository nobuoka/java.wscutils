package info.vividcode.util.json;

import java.math.BigDecimal;

/**
 * JSON の各種値 (object, array, string, number, boolean, null)
 * に対応する Java のオブジェクトによって実装されるインターフェイス.
 * @author nobuoka
 *
 */
public interface JsonValue {

    /**
     * JSON の各種値の型を表す列挙型
     * @author nobuoka
     *
     */
    enum ValueType {
        OBJECT_VALUE,
        ARRAY_VALUE,
        NUMBER_VALUE,
        STRING_VALUE,
        BOOLEAN_VALUE,
        NULL_VALUE
    }

    JsonValue get(int index) throws JsonTypeException;
    JsonValue get(String key) throws JsonTypeException;

    /**
     * このオブジェクトが表す JSON の値のタイプを返す.
     */
    ValueType valueType();

    /**
     * JSON の array を表すオブジェクトの場合に,
     * 対応する Java のオブジェクト ({@link JsonArray} オブジェクト) を返す.
     * @return 対応する JsonArray オブジェクト
     */
    JsonArray asArray() throws JsonTypeException;

    /**
     * JSON の object を表すオブジェクトの場合に,
     * 対応する Java のオブジェクト ({@link JsonObject} オブジェクト) を返す.
     * @return 対応する JsonObject オブジェクト
     */
    JsonObject asObject() throws JsonTypeException;

    /**
     * JSON の number を表すオブジェクトの場合に,
     * 対応する Java のオブジェクト ({@link BigDecimal} オブジェクト) を返す.
     * @return 対応する BigDecimal オブジェクト
     */
    BigDecimal numberValue() throws JsonTypeException;

    /**
     * JSON の object を表すオブジェクトの場合に,
     * 対応する Java のオブジェクト ({@link String} オブジェクト) を返す
     * @return 対応する String オブジェクト
     */

    String stringValue() throws JsonTypeException;

    /**
     * JSON の object を表すオブジェクトの場合に,
     * 対応する Java のオブジェクト ({@link Boolean} オブジェクト) を返す.
     * @return 対応する Boolean オブジェクト
     */
    Boolean booleanValue() throws JsonTypeException;

}
