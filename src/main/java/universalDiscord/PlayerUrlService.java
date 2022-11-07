package universalDiscord;

public enum PlayerUrlService {
    NONE,
    OSRS_HISCORE,
    CRYSTAL_MATH_LABS,
    TEMPLEOSRS,
    WISEOLDMAN;

    public String playerUrl(String playerName) {
        switch (this) {
            case OSRS_HISCORE:
                return "https://secure.runescape.com/m=hiscore_oldschool/hiscorepersonal.ws?user1=" + playerName;
            case WISEOLDMAN:
                return "https://wiseoldman.net/players/" + playerName;
            case CRYSTAL_MATH_LABS:
                return "https://crystalmathlabs.com/track.php?player=" + playerName;
            case TEMPLEOSRS:
                return "https://templeosrs.com/player/overview.php?player=" + playerName;
            case NONE:
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case OSRS_HISCORE:
                return "OSRS HiScore";
            case WISEOLDMAN:
                return "Wise Old Man";
            case CRYSTAL_MATH_LABS:
                return "Crystal Math Labs";
            case TEMPLEOSRS:
                return "Temple OSRS";
            case NONE:
                return "None";
            default:
                return null;
        }
    }
}
