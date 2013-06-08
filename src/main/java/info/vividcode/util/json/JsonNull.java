package info.vividcode.util.json;

/**
 * JSON の null を表すためのクラス.
 *
 * 対応する Java の値は null であり, {@link JsonNull#arrayValue()} メソッドや
 * {@link JsonNull#booleanValue()} メソッドなど,
 * 値を取得するためのメソッドでは常に null を返す.
 *
 * コンストラクタはなく, {@link JsonNull#VALUE} フィールドによりインスタンスを取得することができる.
 * @author nobuoka
 *
 */
public class JsonNull extends AbstractJsonLeafValue {

    /** {@link JsonNull} の唯一のインスタンス */
    public static final JsonNull VALUE = new JsonNull();

    private JsonNull() {}

    /**
     * 対応する JSON の値のタイプを返す.
     * このクラスは JSON の null に対応するため,
     * 常に {@link info.vividcode.util.json.JsonValue.ValueType}.NULL_VALUE を返す.
     */
    @Override
    public ValueType valueType() {
        return ValueType.NULL_VALUE;
    }

    /**
     * このオブジェクトの文字列表現.
     * @return "[JSON null]" という String オブジェクト
     */
    @Override
    public String toString() {
        return "[JSON null]";
    }

}
