package matcher;

import matcher.perform.wsj.WSJPerformManager;
import matcher.script.Script;
import matcher.url.UrlGenerator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.Executors;



public class Main {


    public static void main(String[] args){

        var config = new Configuration("config.ini");
        var script = Script.getRequired(config);
        assert script != null;
        script.run();
    }
}
