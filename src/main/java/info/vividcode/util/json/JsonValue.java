package info.vividcode.util.json;

import java.math.BigDecimal;

/**
 * The {@link JsonValue} interface is the primary datatype for the
 * JavaScript Object Notation (JSON) structure. It represents a single
 * value in the JSON structure. Type of a value is one of following values:
 * string, number, boolean, null, object and array.
 *
 * @author nobuoka
 */
public interface JsonValue {

    /**
     * {@link ValueType} represents one of types of JSON value.
     */
    enum ValueType {
        /** JSON object */
        OBJECT_VALUE,
        /** JSON array */
        ARRAY_VALUE,
        /** JSON number */
        NUMBER_VALUE,
        /** JSON string */
        STRING_VALUE,
        /** JSON boolean ({@literal true} or {@literal false}) */
        BOOLEAN_VALUE,
        /** JSON null ({@literal null}) */
        NULL_VALUE,
    }

    JsonValue get(int index) throws JsonTypeException;
    JsonValue get(String key) throws JsonTypeException;

    /**
     * Return type of this JSON value.
     *
     * @return Type of this JSON value
     */
    ValueType valueType();

    /**
     * Return itself as {@link JsonArray} if it is a {@link JsonArray},
     * throw {@link JsonTypeException} exception otherwise.
     * This method can be used instead of explicit casting.
     *
     * @return Itself
     * @throws JsonTypeException Raised when this object is not a {@link JsonArray}
     */
    JsonArray asArray() throws JsonTypeException;

    /**
     * Return itself as {@link JsonObject} if it is a {@link JsonObject},
     * throw {@link JsonTypeException} exception otherwise.
     * This method can be used instead of explicit casting.
     *
     * @return Itself
     * @throws JsonTypeException Raised when this object is not a {@link JsonObject}
     */
    JsonObject asObject() throws JsonTypeException;

    /**
     * Return {@link BigDecimal} representing associated number value
     * if type of this value is number,
     * throw {@link JsonTypeException} exception otherwise.
     *
     * @return {@link BigDecimal} object representing associated number value
     * @throws JsonTypeException Raised when JSON type of this object is not number
     */
    BigDecimal numberValue() throws JsonTypeException;

    /**
     * Return {@link String} representing associated string value
     * if type of this value is string,
     * throw {@link JsonTypeException} exception otherwise.
     *
     * @return {@link String} object representing associated string value
     * @throws JsonTypeException Raised when JSON type of this object is not string
     */
    String stringValue() throws JsonTypeException;

    /**
     * Return {@link Boolean} representing associated boolean value
     * if type of this value is boolean,
     * throw {@link JsonTypeException} exception otherwise.
     *
     * @return {@link Boolean} object representing associated string value
     * @throws JsonTypeException Raised when JSON type of this object is not boolean
     */
    Boolean booleanValue() throws JsonTypeException;

}
