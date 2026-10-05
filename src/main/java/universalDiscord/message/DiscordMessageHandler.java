package universalDiscord.message;

import static net.runelite.http.api.RuneLiteAPI.GSON;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.annotations.EverythingIsNonNull;
import universalDiscord.UniversalDiscordPlugin;
import universalDiscord.Utils;
import universalDiscord.message.discord.Image;

import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@Slf4j
public class DiscordMessageHandler {
    private final UniversalDiscordPlugin plugin;

    @Inject
    public DiscordMessageHandler(UniversalDiscordPlugin plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(MessageBuilder messageBuilder) {
        messageBuilder.setPlayerAsAuthorForEmbeds();

        MultipartBody.Builder reqBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (messageBuilder.sendScreenImage) {
            plugin.drawManager.requestNextFrameListener(image -> {
                BufferedImage bufferedImage = (BufferedImage) image;
                byte[] imageBytes;
                String filename = "screenshot.jpg";
                try {
                    imageBytes = Utils.convertImageToByteArray(bufferedImage);
                    reqBodyBuilder.addFormDataPart("file", filename,
                            RequestBody.create(MediaType.parse("image/jpg"), imageBytes));


                    // Puts the image inside the embed block
                    messageBuilder.webhookBody.getEmbeds().stream().findFirst().ifPresent((embed -> embed.setImage(new Image("attachment://" + filename))));
                } catch (IOException e) {
                    log.warn("There was an error creating bytes from captured image", e);
                    // Still send the message even if the image cannot be created
                }
                sendToWebhooks(messageBuilder, reqBodyBuilder);
            });

        } else {
            sendToWebhooks(messageBuilder, reqBodyBuilder);
        }
    }

    private void sendToWebhooks(MessageBuilder messageBuilder, MultipartBody.Builder reqBodyBuilder) {
        reqBodyBuilder.addFormDataPart("payload_json", GSON.toJson(messageBuilder.webhookBody));
        sendToMultiple(webhooks(), reqBodyBuilder);
    }


    private ArrayList<HttpUrl> webhooks() {
        ArrayList<HttpUrl> urlList = new ArrayList<>();
        String webhookUrl = plugin.config.discordWebhook();
        if (Strings.isNullOrEmpty(webhookUrl)) {
            return urlList;
        }
        String[] strList = webhookUrl.split("\n");
        for (String urlString : strList) {
            if (Objects.equals(urlString, "")) {
                continue;
            }
            urlList.add(HttpUrl.parse(urlString));
        }
        return urlList;
    }

    private void sendToMultiple(ArrayList<HttpUrl> urls, MultipartBody.Builder requestBody) {
        for (HttpUrl url : urls) {
            sendMessage(url, requestBody);
        }
    }

    private void sendMessage(HttpUrl url, MultipartBody.Builder requestBody) {
        RequestBody body = requestBody.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        plugin.httpClient.newCall(request).enqueue(new Callback() {
            @Override
            @EverythingIsNonNull
            public void onFailure(Call call, IOException e) {
                log.warn("There was an error sending the webhook message", e);
            }

            @Override
            @EverythingIsNonNull
            public void onResponse(Call call, Response response) {
                response.close();
            }
        });
    }
}
