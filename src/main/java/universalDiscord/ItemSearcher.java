package universalDiscord;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
final public class ItemSearcher {
    @Inject
    private OkHttpClient okHttpClient;
    private Map<String, Integer> nameAndIds = new HashMap<>();

    public void loadItemIdsAndNames() {
        Request request = new Request.Builder().url("https://static.runelite.net/cache/item/names.json").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.warn("Failed to load item names and ids.");
            }

            @Override
            public void onResponse(Call call, Response response) {
                Gson gson = new Gson();
                try {
                    Map<String, String> items = gson.fromJson(response.body().charStream(), Map.class);
                    filterNotedItems(items);
                } catch (JsonSyntaxException | JsonIOException e) {
                    log.error("UniversalDiscord failed to load names");
                }
            }
        });
    }

    private void filterNotedItems(Map<String, String> items) {
        Request request = new Request.Builder().url("https://static.runelite.net/cache/item/notes.json").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.warn("Failed to remove noted items.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                try {
                    Map<String, String> notes = gson.fromJson(response.body().charStream(), Map.class);
                    notes.keySet().forEach(items::remove);
                } catch (JsonSyntaxException | JsonIOException e) {
                    log.error("UniversalDiscord failed to filter noted items");
                }
                // Always set the items even when failing to filter noted items.
                nameAndIds = items
                        .entrySet()
                        .stream()
                        .collect(
                                Collectors.toMap(Map.Entry::getValue, (e) -> Integer.parseInt(e.getKey()), (Integer a, Integer b) -> a)
                        );
            }
        });
    }

    public Integer findItemId(String name) {
        return nameAndIds.get(name);
    }
}
