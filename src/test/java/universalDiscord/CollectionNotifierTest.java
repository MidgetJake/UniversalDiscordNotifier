package universalDiscord;

import org.junit.Test;
import universalDiscord.notifiers.CollectionNotifier;

import java.util.regex.Matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CollectionNotifierTest {


    @Test
    public void testCollectionLogRegex() {
        String[] itemNames = {
                "Xeric's talisman (inert)",
                "Jar of miasma",
                "Saradomin's light",
                "Craw's bow",
                "Thammaron's sceptre",
                "Chompy bird hat (ogre yeoman)",
                "Robe bottoms of the eye",
                "Bucket helm (g)",
                "Black d'hide body (g)",
                "Tzhaar-ket-om ornament kit",
        };

        for (String itemName : itemNames) {
            String chatLine = "New item added to your collection log: " + itemName;
            Matcher matcher = CollectionNotifier.COLLECTION_LOG_REGEX.matcher(chatLine);
            boolean finds = matcher.find();
            assertTrue(finds);
            assertEquals(itemName, matcher.group("itemName"));
        }
    }
}
