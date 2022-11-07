package universalDiscord.message.discord;

import lombok.Data;

@Data
public class Field {
    private String name;
    private String value;
    private boolean inline = false;
}