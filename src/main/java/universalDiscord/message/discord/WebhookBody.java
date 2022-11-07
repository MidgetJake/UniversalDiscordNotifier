package universalDiscord.message.discord;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class WebhookBody {
    private String content;
    private List<Embed> embeds = new ArrayList<>();
}
