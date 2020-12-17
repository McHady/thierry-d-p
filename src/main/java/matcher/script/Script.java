package matcher.script;

import matcher.Configurationable;
import matcher.FileManager;
import matcher.url.UrlGenerator;

public abstract class Script {

    protected final Configurationable config;

    public Script(Configurationable config){

        this.config = config;
    }

    public void run(){

        var urlGenerator = this.getUrlGenerator();
        var fileManager = new FileManager(this.config, this.ignoreFileNameParts());

        this.process(urlGenerator, fileManager);
    }

    protected abstract void process(UrlGenerator urlGenerator, FileManager fileManager);

    protected abstract Boolean ignoreFileNameParts();

    protected abstract UrlGenerator getUrlGenerator();

    public static Script getRequired(Configurationable config){

        var scriptType = ScriptType.getByCode(config.getScriptCode());
        switch (scriptType) {
            case WSJ:
                return new WSJCreateNewFileFileScript(config);
            case INVESTING:
                return new InvestingScript(config);
        }

        return null;
    }
}



