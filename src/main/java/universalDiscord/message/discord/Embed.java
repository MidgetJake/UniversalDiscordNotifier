package universalDiscord.message.discord;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class Embed {
    private Author author;
    private String title;
    private String url;
    private String description;
    private Image thumbnail;
    private Image image;
    private ArrayList<Field> fields;
}