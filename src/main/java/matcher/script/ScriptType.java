package matcher.script;

public enum ScriptType {
    WSJ(0),
    INVESTING(1),
    YAHOO(2);

    private final int code;

    ScriptType(int code){

        this.code = code;
    }

    public static ScriptType getByCode(int code){
        for (var value : ScriptType.values()){
            if (value.code == code) return value;
        }

        return ScriptType.WSJ;
    }
}
