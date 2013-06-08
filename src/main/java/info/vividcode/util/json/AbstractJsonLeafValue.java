package info.vividcode.util.json;

import java.math.BigDecimal;

abstract class AbstractJsonLeafValue implements JsonValue {

    private String createErrorMessageOfMethodNotSupporting(String methodName) {
        return "JSON value of " + this.valueType().name() +
                " doesn't support “" + methodName + "” method.";
    }

    @Override
    public JsonValue get(int index) throws JsonTypeException {
        throw new JsonTypeException(createErrorMessageOfMethodNotSupporting("get(int)"));
    }

    @Override
    public JsonValue get(String key) throws JsonTypeException {
        throw new JsonTypeException(createErrorMessageOfMethodNotSupporting("get(String)"));
    }

    @Override
    public JsonArray asArray() throws JsonTypeException {
        throw new JsonTypeException(createErrorMessageOfMethodNotSupporting("asArray()"));
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

}
