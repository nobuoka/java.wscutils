package info.vividcode.util.json;

import java.math.BigDecimal;

/**
 *JSON 文字列をパースする機能を提供するクラス.
 *<pre><code>  // パース対象の JSON 文字列
 *  String jsonStr = "[ 50, 200 ]";
 *  // パースする
 *  JsonValue jsonObj = JsonParser.parse( jsonStr );
 *  // 現在は JsonArray であることがわかっているので, JsonArray として扱う
 *  BigDecimal num1 = jsonObj.arrayValue().get( 0 ).numberValue();
 *  BigDecimal num2 = jsonObj.arrayValue().get( 1 ).numberValue();</code></pre>
 */
public class JsonParser {

    private JsonParser() {}

    private static class CodePointIterator {
        private String str;
        private int index;
        //private int numCP;
        private int length;
        public CodePointIterator(String str) {
            this.str = str;
            this.index = 0;
            //this.numCP = str.codePointCount( 0, str.length() );
            this.length = str.length();
        }
        public boolean hasNext() {
            //return index < numCP;
            return index < length;
        }
        public int next() {
            if (length <= index) {
                return -1;
            }
            int cp = str.codePointAt(index);
            index += Character.charCount(cp);
            return cp;
        }
        public int prev(int cp) {
            index -= Character.charCount(cp);
            return str.codePointAt(index);
        }
        public int viewNext() {
            return str.codePointAt(index);
        }
    }

    private static class Token {
        public TokenType type;
        public String tokenString;
        enum TokenType {
            BEGIN_ARRAY, // "["
            END_ARRAY, // "]"
            BEGIN_OBJECT, // "{"
            END_OBJECT, // "}"
            NAME_SEPARATER, // ":"
            VALUE_SEPARATER, // ","
            STRING,
            NUMBER,
            FALSE,
            TRUE,
            NULL,
        }
        private Token(TokenType type, String str) {
            this.type = type;
            this.tokenString = str;
        }
        static final public Token BEGIN_ARRAY = new Token(TokenType.BEGIN_ARRAY, null);
        static final public Token END_ARRAY = new Token(TokenType.END_ARRAY, null);
        static final public Token BEGIN_OBJECT = new Token(TokenType.BEGIN_OBJECT, null);
        static final public Token END_OBJECT = new Token(TokenType.END_OBJECT, null);
        static final public Token NAME_SEPARATER = new Token(TokenType.NAME_SEPARATER, null);
        static final public Token VALUE_SEPARATER = new Token(TokenType.VALUE_SEPARATER, null);
        static final public Token FALSE = new Token(TokenType.FALSE, null);
        static final public Token TRUE = new Token(TokenType.TRUE, null);
        static final public Token NULL = new Token(TokenType.NULL, null);
        static final public Token newStringToken(String str) {
            return new Token(TokenType.STRING, str);
        }
        static final public Token newNumberToken(String str) {
            return new Token(TokenType.NUMBER, str);
        }
    }

    private static class Tokenizer {
        private CodePointIterator cpi;
        public Tokenizer(String str) {
            cpi = new CodePointIterator(str);
        }
        public Token getNextToken() {
            return getNextTokenInternal(cpi);
        }

        static final private int[] FALSE_FOLLOWING_CPS = { 0x61, 0x6c, 0x73, 0x65 };
        static final private int[] TRUE_FOLLOWING_CPS  = { 0x72, 0x75, 0x65  };
        static final private int[] NULL_FOLLOWING_CPS  = { 0x75, 0x6c, 0x6c  };

        private Token getNextTokenInternal(CodePointIterator cpi) {
            // 最初の文字の Code point を取得
            int firstCharCP = -1;
            while (cpi.hasNext()) {
                int cp = cpi.next();
                if (!isWS(cp)) {
                    firstCharCP = cp;
                    break;
                }
            }
            // 次のトークンがない場合
            if (firstCharCP == -1) {
                return null;
            }
            // 最初の文字の CP によって処理を変える
            switch (firstCharCP) {
                case 0x5B /* [ */:
                    return Token.BEGIN_ARRAY;
                case 0x7B /* { */:
                    return Token.BEGIN_OBJECT;
                case 0x5D /* ] */:
                    return Token.END_ARRAY;
                case 0x7D /* } */:
                    return Token.END_OBJECT;
                case 0x3A /* : */:
                    return Token.NAME_SEPARATER;
                case 0x2C /* , */:
                    return Token.VALUE_SEPARATER;
                case 0x22 /* " */:
                    return getNextStringToken( cpi );
                case 0x66 /* f */:
                    checkNextLiteralToken( cpi, FALSE_FOLLOWING_CPS );
                    return Token.FALSE;
                case 0x6E /* n */:
                    checkNextLiteralToken( cpi, NULL_FOLLOWING_CPS );
                    return Token.NULL;
                case 0x74 /* t */:
                    checkNextLiteralToken( cpi, TRUE_FOLLOWING_CPS );
                    return Token.TRUE;
            }

            /* -, 0-9 */
            if (firstCharCP == 0x2D || (0x30 <= firstCharCP && firstCharCP <= 0x39)) {
                return getNextNumberToken(cpi, firstCharCP);
            }

            // TODO : 例外処理
            //return sb.toString();
            return null;
        }

        private Token getNextStringToken(CodePointIterator cpi) {
            StringBuilder sb = new StringBuilder();
            while (cpi.hasNext()) {
                int cp = cpi.next();
                if (cp == 0x22 /* " */) {
                    // end
                    return Token.newStringToken(sb.toString());
                }
                if (cp != 0x5C) {
                    // unescaped
                    sb.appendCodePoint(cp);
                } else {
                    // escaped
                    if (!cpi.hasNext()) throw new RuntimeException(); // TODO
                    cp = cpi.next();
                    /*
                    %x22 /          ; "    quotation mark  U+0022
                    %x5C /          ; \    reverse solidus U+005C
                    %x2F /          ; /    solidus         U+002F
                    %x62 /          ; b    backspace       U+0008
                    %x66 /          ; f    form feed       U+000C
                    %x6E /          ; n    line feed       U+000A
                    %x72 /          ; r    carriage return U+000D
                    %x74 /          ; t    tab             U+0009
                    %x75 4HEXDIG )  ; uXXXX                U+XXXX
                    */
                    switch (cp) {
                        case 0x22:
                            sb.append("\""); break;
                        case 0x5C:
                            sb.append("\\"); break;
                        case 0x2F:
                            sb.append("/"); break;
                        case 0x62:
                            sb.appendCodePoint(0x08); break;
                        case 0x66:
                            sb.appendCodePoint(0x0C); break;
                        case 0x6E:
                            sb.appendCodePoint(0x0A); break;
                        case 0x72:
                            sb.appendCodePoint(0x0D); break;
                        case 0x74:
                            sb.appendCodePoint(0x09); break;
                        case 0x75:
                            StringBuilder sb2 = new StringBuilder();
                            // TODO : error 処理
                            for (int i = 0; i < 4; i++) sb2.appendCodePoint(cpi.next());
                            sb.append( (char)Integer.parseInt(sb2.toString(), 16) );
                            break;
                        default:
                            throw new RuntimeException(); // TODO
                    }
                }
            }
            throw new RuntimeException("string の終端 (\") が見つかりません"); // TODO
        }

        private Token getNextNumberToken(CodePointIterator cpi, int cp) {
            // number = [minus] int [frac] [exp]
            // minus  = U+002D ; -
            // plus   = U+002B ; +
            // int    = zero | non-zero-digit digit*
            // zero   = U+0030 ; 0
            // non-zero-digit = U+0031 - U+0039 ; 1 - 9
            // digit  = zero | non-zero-digit
            // frac   = decimal-point digit+
            // decimal-point = U+002E ; .
            // exp    = e [ minus | plus ] digit+
            // e      = U+0065 | 0x0045 ; e | E
            // TODO : cpi.next() の例外処理
            //int cp = firstCharCP;
            StringBuilder sb = new StringBuilder();
            // 先頭が - の場合の処理
            if (cp == 0x2D) {
                sb.appendCodePoint(cp);
                cp = cpi.next();
            }
            // cp は int の先頭
            if (cp == 0x30 /* 0 */) {
                sb.appendCodePoint(cp);
                // 0 が先頭ならば, 続くのは数字ではない
                cp = cpi.next();
                if (0x30 <= cp && cp <= 0x39) throw new RuntimeException("unexpected character");
            } else if (0x31 /* 1 */ <= cp && cp <= 0x39 /* 9 */) {
                while (true) {
                    if (0x30 <= cp && cp <= 0x39) {
                        sb.appendCodePoint(cp);
                        cp = cpi.next();
                    } else {
                        break;
                    }
                }
            } else {
                throw new RuntimeException("unexpected character");
            }
            // cp は decimal-point か e か number ではない次のトークンの開始のはず
            if (cp == 0x2E) {
                sb.appendCodePoint(cp);
                cp = cpi.next();
                // cp は数字のはず
                if (cp < 0x30 || 0x39 < cp) throw new RuntimeException("unexpected character");
                while (true) {
                    if (0x30 <= cp && cp <= 0x39) {
                        sb.appendCodePoint(cp);
                        cp = cpi.next();
                    } else {
                        break;
                    }
                }
            }
            // cp は e か number ではない次のトークンの開始のはず
            if (cp == 0x45 || cp == 0x65) {
                sb.appendCodePoint(cp);
                cp = cpi.next();
                // minus or plus?
                if (cp == 0x2B || cp == 0x2D) {
                    sb.appendCodePoint(cp);
                    cp = cpi.next();
                }
                // cp は数字のはず
                if (cp < 0x30 || 0x39 < cp) throw new RuntimeException("unexpected character");
                while (true) {
                    if (0x30 <= cp && cp <= 0x39) {
                        sb.appendCodePoint(cp);
                        cp = cpi.next();
                    } else {
                        break;
                    }
                }
            }
            // cp は number ではない次のトークンの開始のはず
            cpi.prev(cp);
            // TODO : チェック

            return Token.newNumberToken(sb.toString());
        }

        private void checkNextLiteralToken(CodePointIterator cpi, int[] cps) {
            for (int cp : cps) {
                if (cp != cpi.next()) throw new RuntimeException("unexpected token");
            }
            // 続く文字が空白か "]", "}", "," のいずれかであればよい
            int cp = cpi.viewNext();
            if (!isWS(cp)) {
                switch (cp) {
                    case 0x5D /* ] */:
                    case 0x7D /* } */:
                    case 0x2C /* , */:
                        // ok
                        break;
                    default:
                        throw new RuntimeException("unexpected character [code point:" + cp + "]");
                }
            }
        }

        private boolean isWS(int cp) {
            return cp == 0x20 || cp == 0x09 || cp == 0x0A || cp == 0x0D;
        }
    }

    /*
     * s: "[" -> 1, "{" -> 5
     *
     * 1: "]" -> e, value -> 2
     * 2: "]" -> e, "," -> 3
     * 3: value -> 2
     *
     * 5: "}" -> e, string -> 6
     * 6: ":" -> 7
     * 7: value -> 8
     * 8: "}" -> e, "," -> 9
     * 9: string -> 6
     *
     * vs: "[" -> 1, "{" -> 5, ( string, number, boolean, null ) -> e
     */

    static private JsonValue parseValue(Tokenizer t, Token token) {
        switch (token.type) {
            case BEGIN_ARRAY:
                return parseArray(t, new JsonArray());
            case BEGIN_OBJECT:
                return parseObject(t, new JsonObject());
            case STRING:
                return new JsonString(token.tokenString);
            case NUMBER:
                return new JsonNumber(new BigDecimal(token.tokenString));
            case TRUE:
                return JsonBoolean.TRUE;
            case FALSE:
                return JsonBoolean.FALSE;
            case NULL:
                return JsonNull.VALUE;
            default:
                throw new RuntimeException("unexpected token");
        }
    }

    static private JsonObject parseObject(Tokenizer t, JsonObject jobject) {
        Token token = t.getNextToken();
        if (token.type == Token.TokenType.END_OBJECT) {
            return jobject;
        }
        while (true) {
            // string
            if (token.type != Token.TokenType.STRING)
                throw new RuntimeException("unexpected token");
            String key = token.tokenString;
            // name separater
            token = t.getNextToken();
            if (token.type != Token.TokenType.NAME_SEPARATER)
                throw new RuntimeException("unexpected token");
            // value
            token = t.getNextToken();
            JsonValue value = parseValue(t, token);
            jobject.put(key, value);
            // "," or "}"?
            token = t.getNextToken();
            if (token.type == Token.TokenType.END_OBJECT) {
                break;
            } else if (token.type == Token.TokenType.VALUE_SEPARATER) {
                token = t.getNextToken();
            } else {
                throw new RuntimeException("unexpected token [token type:" +
                        token.type + ", token string:" + token.tokenString + "]");
            }
        }
        return jobject;
    }

    static private JsonArray parseArray(Tokenizer t, JsonArray jarray) {
        Token token = t.getNextToken();
        if (token.type == Token.TokenType.END_ARRAY) {
            return jarray;
        }
        while (true) {
            // value
            JsonValue value = parseValue(t, token);
            jarray.add(value);
            // "," or "]"?
            token = t.getNextToken();
            if (token.type == Token.TokenType.END_ARRAY) {
                break;
            } else if (token.type == Token.TokenType.VALUE_SEPARATER) {
                token = t.getNextToken();
            } else {
                throw new RuntimeException("unexpected token");
            }
        }
        return jarray;
    }

    /**
     *JSON 文字列をパースして, 結果を JsonValue として返す.
     *@param jsonStr パース対象の JSON 文字列
     *@return jsonStr をパースした結果の JSON オブジェクト
     */
    static public JsonValue parse(String jsonStr) {
        // array or obj?
        Tokenizer t = new Tokenizer(jsonStr);
        Token token = t.getNextToken();
        if (token == null) {
            throw new InvalidJsonException("invalid JSON string (\"" + jsonStr + "\")");
        }
        JsonValue val = null;
        if (token.type == Token.TokenType.BEGIN_ARRAY) {
            val = parseArray(t, new JsonArray());
        } else if (token.type == Token.TokenType.BEGIN_OBJECT) {
            val = parseObject(t, new JsonObject());
        } else {
            throw new InvalidJsonException("The passed string is invalid [" + jsonStr + "]");
        }
        if ((token = t.getNextToken()) != null) {
            throw new RuntimeException();
        }
        return val;
    }

}
