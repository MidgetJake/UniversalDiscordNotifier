package universalDiscord.message.discord;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Author {
    private String name;
    private String url;
    private String icon_url;
}