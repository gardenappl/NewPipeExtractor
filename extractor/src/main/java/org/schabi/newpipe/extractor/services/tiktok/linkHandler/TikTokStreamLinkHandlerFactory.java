package org.schabi.newpipe.extractor.services.tiktok.linkHandler;

import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.ServiceList;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;
import org.schabi.newpipe.extractor.linkhandler.LinkHandlerFactory;
import org.schabi.newpipe.extractor.services.tiktok.TikTokParsingHelper;
import org.schabi.newpipe.extractor.utils.Parser;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TikTokStreamLinkHandlerFactory extends LinkHandlerFactory {
    private static final Pattern LONG_URL_PATTERN =
            Pattern.compile("^https?://(?:m\\.|www\\.)?tiktok\\.com/(?:[^/]*/video|v)/([^?.]*)");
    private static final Pattern SHORT_URL_PATTERN =
            Pattern.compile("^https?://vm\\.tiktok\\.com/([^?.]*)");

    private String tryGetIdFromLongUrl(String url) {
        final Matcher m = LONG_URL_PATTERN.matcher(url);
        return m.find() ? m.group(1) : null;
    }

    @Override
    public String getId(String url) throws ParsingException {
        String id = tryGetIdFromLongUrl(url);
        if (id != null)
            return id;

        // 301 Moved Permanently
        try {
            String longUrl = NewPipe.getDownloader().get(url).getHeader("Location");
            return Objects.requireNonNull(tryGetIdFromLongUrl(longUrl));
        } catch (IOException | ReCaptchaException e) {
            throw new ParsingException("Could not resolve short video link", e);
        }
    }

    @Override
    public String getUrl(String id) throws ParsingException {
        return ServiceList.TikTok.getBaseUrl() + "/video/" + id;
    }

    @Override
    public boolean onAcceptUrl(String url) throws ParsingException {
        return Parser.isMatch(LONG_URL_PATTERN, url) || Parser.isMatch(SHORT_URL_PATTERN, url);
    }
}
