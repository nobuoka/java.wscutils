package info.vividcode.util.json;

import java.math.BigDecimal;

/**
 * JSON の number を表すためのクラス.
 *
 * 対応する Java の値は {@link BigDecimal} であり, {@link JsonNumber#numberValue()}
 * メソッドで取得できる.
 *
 * @author nobuoka
 *
 */
public class JsonNumber extends AbstractJsonLeafValue {

    private final BigDecimal val;

    /**
     * 指定の BigDecimal オブジェクトに対応する JsonNumber オブジェクトを生成する.
     * @param val JsonNumber に対応する BigDecimal オブジェクト
     */
    public JsonNumber(BigDecimal val) {
        this.val = val;
    }

    /**
     * 指定の double 型の数値に対応する JsonNumber オブジェクトを生成する.
     * 内部で BigDecimal オブジェクトを生成する.
     * @param val JsonNumber に対応する数値
     */
    public JsonNumber(double val) {
        this(new BigDecimal(val));
    }

    /**
     * 対応する JSON の値のタイプを返す.
     * このクラスは JSON の number に対応するため, 常に {@link JsonValue.ValueType}.NUMBER_VALUE を返す.
     */
    @Override
    public ValueType valueType() {
        return ValueType.NUMBER_VALUE;
    }

    /**
     * 対応する BigDecimal オブジェクトを返す.
     */
    @Override
    public BigDecimal numberValue() {
        return val;
    }

    /**
     * この JsonNumber オブジェクトを表す String オブジェクトを返す.
     * 対応する BigDecimal オブジェクトを b とすると, "[JSON number : " + b.toString() + "]"
     * に等しい文字列が返される.
     */
    @Override
    public String toString() {
        return "[JSON number : " + val.toString() + "]";
    }

    /**
     * 指定されたオブジェクトがこのオブジェクトと同値かどうかチェックする.
     * 指定されたオブジェクトが, このオブジェクトが内包する BigDecimal オブジェクトと同値の
     * BigDecimal オブジェクトをラップする JsonNumber オブジェクトの場合に true を返す.
     * それ以外の場合は false.
     * @param o 比較対象のオブジェクト
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof JsonNumber) {
            JsonNumber oo = (JsonNumber) o;
            return this.val.equals(oo.val);
        } else {
            return false;
        }
    }

}
