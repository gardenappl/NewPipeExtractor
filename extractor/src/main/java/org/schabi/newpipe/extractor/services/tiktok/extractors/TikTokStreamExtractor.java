package org.schabi.newpipe.extractor.services.tiktok.extractors;

import com.grack.nanojson.JsonObject;
import org.schabi.newpipe.extractor.*;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.LinkHandler;
import org.schabi.newpipe.extractor.localization.DateWrapper;
import org.schabi.newpipe.extractor.services.tiktok.TikTokParsingHelper;
import org.schabi.newpipe.extractor.services.youtube.ItagItem;
import org.schabi.newpipe.extractor.stream.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.*;

public class TikTokStreamExtractor extends StreamExtractor {
    private JsonObject itemInfo;
    private JsonObject video;
    private JsonObject author;

    public TikTokStreamExtractor(StreamingService service, LinkHandler linkHandler) {
        super(service, linkHandler);
    }

    @Override
    public void onFetchPage(@Nonnull Downloader downloader) throws IOException, ExtractionException {
        String responseBody = downloader.get(
                getUrl(),
                singletonMap("User-Agent", singletonList(TikTokParsingHelper.USER_AGENT)),
                getExtractorLocalization()
        ).responseBody();

        itemInfo = TikTokParsingHelper.extractFromWebpage(responseBody)
                .getObject("props")
                .getObject("pageProps")
                .getObject("itemInfo")
                .getObject("itemStruct");
        video = itemInfo.getObject("video");
        author = itemInfo.getObject("author");
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        return itemInfo.getString("desc");
    }

    @Nullable
    @Override
    public String getTextualUploadDate() throws ParsingException {
        return null;
    }

    @Nullable
    @Override
    public DateWrapper getUploadDate() throws ParsingException {
        return new DateWrapper(OffsetDateTime.ofInstant(
                Instant.ofEpochSecond(itemInfo.getLong("createTime")),
                ZoneId.of("GMT")
        ));
    }

    @Nonnull
    @Override
    public String getThumbnailUrl() throws ParsingException {
        return video.getString("cover");
    }

    @Nonnull
    @Override
    public Description getDescription() throws ParsingException {
        return new Description(getName(), Description.PLAIN_TEXT);
    }

    @Override
    public int getAgeLimit() throws ParsingException {
        return 0;
    }

    @Override
    public long getLength() throws ParsingException {
        return video.getInt("duration");
    }

    @Override
    public long getTimeStamp() throws ParsingException {
        return 0;
    }

    @Override
    public long getViewCount() throws ParsingException {
        return itemInfo.getObject("stats").getInt("playCount");
    }

    @Override
    public long getLikeCount() throws ParsingException {
        return itemInfo.getObject("stats").getInt("diggCount");
    }

    @Override
    public long getDislikeCount() throws ParsingException {
        return -1;
    }

    @Nonnull
    @Override
    public String getUploaderUrl() throws ParsingException {
        return ServiceList.TikTok.getBaseUrl() + "/@" + getUploaderName();
    }

    @Nonnull
    @Override
    public String getUploaderName() throws ParsingException {
        return author.getString("uniqueId");
    }

    @Override
    public boolean isUploaderVerified() throws ParsingException {
        return author.getBoolean("verified");
    }

    @Nonnull
    @Override
    public String getUploaderAvatarUrl() throws ParsingException {
        return author.getString("avatarThumb");
    }

    @Nonnull
    @Override
    public String getSubChannelUrl() throws ParsingException {
        return "";
    }

    @Nonnull
    @Override
    public String getSubChannelName() throws ParsingException {
        return "";
    }

    @Nonnull
    @Override
    public String getSubChannelAvatarUrl() throws ParsingException {
        return "";
    }

    @Nonnull
    @Override
    public String getDashMpdUrl() throws ParsingException {
        return "";
    }

    @Nonnull
    @Override
    public String getHlsUrl() throws ParsingException {
        return "";
    }

    @Override
    public List<AudioStream> getAudioStreams() throws IOException, ExtractionException {
        return null;
    }

    @Override
    public List<VideoStream> getVideoStreams() throws IOException, ExtractionException {
        int width = video.getInt("width");
        int height = video.getInt("height");
        return singletonList(new VideoStream(video.getString("downloadAddr"),
                MediaFormat.getFromSuffix(video.getString("format")),
                Integer.toString(Math.min(width, height)) + 'p'));
    }

    @Override
    public List<VideoStream> getVideoOnlyStreams() throws IOException, ExtractionException {
        return null;
    }

    @Nonnull
    @Override
    public List<SubtitlesStream> getSubtitlesDefault() throws IOException, ExtractionException {
        return emptyList();
    }

    @Nonnull
    @Override
    public List<SubtitlesStream> getSubtitles(MediaFormat format) throws IOException, ExtractionException {
        return emptyList();
    }

    @Override
    public StreamType getStreamType() throws ParsingException {
        return StreamType.VIDEO_STREAM;
    }

    @Nullable
    @Override
    public InfoItemsCollector<? extends InfoItem, ? extends InfoItemExtractor> getRelatedItems() throws IOException, ExtractionException {
        return null;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }

    @Nonnull
    @Override
    public String getHost() throws ParsingException {
        return "";
    }

    @Override
    public Privacy getPrivacy() throws ParsingException {
        return Privacy.PUBLIC;
    }

    @Nonnull
    @Override
    public String getCategory() throws ParsingException {
        return "";
    }

    @Nonnull
    @Override
    public String getLicence() throws ParsingException {
        return "";
    }

    @Nullable
    @Override
    public Locale getLanguageInfo() throws ParsingException {
        return null;
    }

    @Nonnull
    @Override
    public List<String> getTags() throws ParsingException {
        return itemInfo.getArray("challenges").stream().map(obj ->
                ((JsonObject) obj).getString("title")
        ).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public String getSupportInfo() throws ParsingException {
        return "";
    }

    @Nonnull
    @Override
    public List<StreamSegment> getStreamSegments() throws ParsingException {
        return emptyList();
    }

    @Nonnull
    @Override
    public List<MetaInfo> getMetaInfo() throws ParsingException {
        return emptyList();
    }
}
