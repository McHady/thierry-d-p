package matcher.perform.wsj;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import matcher.ExcelManager;
import org.jsoup.Jsoup;

import java.io.IOException;

public abstract class WSJPerformer<T> {

    protected abstract T getJsonData(JsonObject jsonObject);
    public void perform(String url, ExcelManager excelManager, String country, String wsjCode, String tableKey) throws IOException {
        var responseString = Jsoup.connect(url).ignoreContentType(true).get().text();
        var json = (JsonObject) new JsonParser().parse(responseString);
        var data = this.getJsonData(json);
        this.writeData(excelManager, data, country, wsjCode, tableKey);
    }

    protected abstract void writeData(ExcelManager excelManager, T data, String country, String wsjCode, String tableKey);
}


