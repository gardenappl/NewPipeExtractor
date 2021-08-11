package org.schabi.newpipe.extractor.services.tiktok;

import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.channel.ChannelExtractor;
import org.schabi.newpipe.extractor.comments.CommentsExtractor;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.kiosk.KioskList;
import org.schabi.newpipe.extractor.linkhandler.*;
import org.schabi.newpipe.extractor.localization.Localization;
import org.schabi.newpipe.extractor.playlist.PlaylistExtractor;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.services.tiktok.extractors.TikTokStreamExtractor;
import org.schabi.newpipe.extractor.services.tiktok.linkHandler.TikTokStreamLinkHandlerFactory;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.subscription.SubscriptionExtractor;
import org.schabi.newpipe.extractor.suggestion.SuggestionExtractor;

import java.util.List;

import static java.util.Arrays.asList;

public class TikTokService extends StreamingService {
    public TikTokService(int id) {
        super(id, "TikTok", asList(ServiceInfo.MediaCapability.VIDEO, ServiceInfo.MediaCapability.AUDIO));
    }

    @Override
    public String getBaseUrl() {
        return "https://tiktok.com";
    }

    @Override
    public LinkHandlerFactory getStreamLHFactory() {
        return new TikTokStreamLinkHandlerFactory();
    }

    @Override
    public ListLinkHandlerFactory getChannelLHFactory() {
        return null;
    }

    @Override
    public ListLinkHandlerFactory getPlaylistLHFactory() {
        return null;
    }

    @Override
    public SearchQueryHandlerFactory getSearchQHFactory() {
        return null;
    }

    @Override
    public ListLinkHandlerFactory getCommentsLHFactory() {
        return null;
    }

    @Override
    public SearchExtractor getSearchExtractor(SearchQueryHandler queryHandler) {
        return null;
    }

    @Override
    public SuggestionExtractor getSuggestionExtractor() {
        return null;
    }

    @Override
    public SubscriptionExtractor getSubscriptionExtractor() {
        return null;
    }

    @Override
    public KioskList getKioskList() throws ExtractionException {
        return null;
    }

    @Override
    public ChannelExtractor getChannelExtractor(ListLinkHandler linkHandler) throws ExtractionException {
        return null;
    }

    @Override
    public PlaylistExtractor getPlaylistExtractor(ListLinkHandler linkHandler) throws ExtractionException {
        return null;
    }

    @Override
    public StreamExtractor getStreamExtractor(LinkHandler linkHandler) throws ExtractionException {
        return new TikTokStreamExtractor(this, linkHandler);
    }

    @Override
    public CommentsExtractor getCommentsExtractor(ListLinkHandler linkHandler) throws ExtractionException {
        return null;
    }

    @Override
    public List<Localization> getSupportedLocalizations() {
        //Main page has locale selector, also list of alternate <link>s in head
        return Localization.listFrom(
                "ar", "bn-IN", "ceb-PH", "cs-CZ", "de-DE", "el-GR", "en", "es", "fi-FI", "fil-PH",
                "fr", "he-IL", "hi-IN", "hu-HU", "id-ID", "it-IT", "ja-JP", "jv-ID", "km-KH",
                "ko-KR", "ms-MY", "my-MM", "nl-NL", "pl-PL", "pt-BR", "ro-RO", "ru-RU", "sv-SE",
                "th-TH", "tr-TR", "uk-UA", "ur", "vi-VN", "zh-Hant-TW"
        );
    }
}
