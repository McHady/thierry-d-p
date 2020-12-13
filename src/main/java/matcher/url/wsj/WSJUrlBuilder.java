package matcher.url.wsj;

import matcher.url.UrlBuilder;

public abstract class WSJUrlBuilder extends UrlBuilder {

    @Override
    public String secondPart(String countryCode, String prefix, String wsjCode) {

        return "?id=" + this.idPart(countryCode, prefix, wsjCode) +"&type=quotes_chart";
    }

    protected String idPart(String countryCode, String prefix, String wsjCode){

        return "%7B\"ticker\"%3A\"" + wsjCode + "\"%2C\"countryCode\"%3A\""+ countryCode +
                "\"%2C\"exchange\"%3A\"" + prefix + "\"%2C\"type\"%3A\"STOCK\"%2C\"" +
                "path\"%3A\"%2Fhk%2F" + this.firstPart(countryCode, prefix, wsjCode).replace("/", "%2F").toLowerCase() +"%2F388\"%7D";
    }
}

