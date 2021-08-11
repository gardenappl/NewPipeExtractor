package org.schabi.newpipe.extractor.services.tiktok;

import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import com.grack.nanojson.JsonReader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.io.StringReader;

public final class TikTokParsingHelper {
    /** TikTok does care about the user agent,
     *  returns empty response for empty user agent, and Captcha for suspicious user agents
     */
    public static final String USER_AGENT =
            "Mozilla/5.0 (X11; Linux x86_64; rv:90.0) Gecko/20100101 Firefox/90.0";

    public static JsonObject extractFromWebpage(String html) throws ExtractionException {
        //Might be a dumb optimization, but maybe we can avoid parsing the HTML?
        int jsonDataStart = html.indexOf("<script id=\"__NEXT_DATA__\"");
        jsonDataStart = html.indexOf(">{", jsonDataStart) + 1;

        int jsonDataEnd = html.indexOf("</script>", jsonDataStart);

        try {
            return JsonParser.object().from(html.substring(jsonDataStart, jsonDataEnd));
        } catch (JsonParserException e) {
            throw new ExtractionException("Error while parsing JSON info", e);
        }
    }
}
