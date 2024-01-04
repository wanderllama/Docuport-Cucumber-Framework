package jw.demo.constants;

import java.util.Random;

public final class TestStrings {

    public static final String GENERAL_COMMENT = "This is a test comment";
    public static final String TEST = "test";
    public static final String EMPTY_STRING = "";
    public static final String NULL_STRING = "null";
    public static final String UNDEFINED = "undefined";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String HAS_OWN_PROPERTY = "hasOwnProperty";
    public static final String THEN = "then";
    public static final String BACKSLASH = "\\";
    public static final String ZERO = "0";
    public static final String MINUS_ZERO = "-0";
    public static final String ONE = "1";
    public static final String LEADING_ZEROS = "002";
    public static final String ZEROS = "000";
    public static final String ONE_DOT_00 = "1.00";
    public static final String ONE_DOLLAR = "$1.00";
    public static final String ONE_HALF = "1/2";
    public static final String SCI_1E2 = "1E2";
    public static final String SCI_1E02 = "1E02";
    public static final String SCI_1E_PLUS_02 = "1E+02";
    public static final String MINUS_ONE = "-1";
    public static final String MINUS_ONE_DOT_00 = "-1.00";
    public static final String MINUS_ONE_DOLLAR = "-$1.00";
    public static final String MINUS_ONE_HALF = "-1/2";
    public static final String SCI_MINUS_1E2 = "-1E2";
    public static final String MINUS_SCI_1E02 = "-1E02";
    public static final String MINUS_SCI_1E_PLUS_02 = "-1E+02";
    public static final String ONE_DIVIDED_BY_ZERO = "1/0";
    public static final String ZERO_DIVIDED_BY_ZERO = "0/0";
    public static final String MAX_INT = "-2147483648/-1";
    public static final String MAX_NUMBER = "-9223372036854775808/-1";
    public static final String NINES = "999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
    public static final String NAN = "NaN";
    public static final String INFINITY = "Infinity";
    public static final String INF = "INF";
    public static final String A_BAD_IDEA = "0xabad1dea";
    public static final String NUMBERS = "1234567890";
    public static final String ONE_THOUSAND = "1,000.00";
    public static final String ONE_MILLION = "1,000,000.00";
    public static final String SPECIAL_CHARACTERS = "!@#$%^&*()`~<>?:\\\"{}|_+,./;'[]\\\\\\\\-=";
    public static final String UNICODE = "\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\u000e\u000f";
    public static final String MATH = "Ω≈ç√∫˜µ≤≥÷∑⅛⅜⅝⅞⁰⁴⁵₀₁₂";
    public static final String SPECIAL_LETTERS = "åß∂ƒ©˙∆˚¬…æœ´®†¥¨ˆøπ“‘Œ„´‰ˇÁ¨ˆØ∏”’ÅÍÎÏ˝ÓÔÒÚÆ☃`⁄€‹›ﬁﬂ‡°·‚—±¸˛Ç◊ı˜Â¯˘¿¡™£¢∞§¶";
    public static final String ZALGO = "z̶̡̛̤̭͓̭̼̩͈̦̳͈̰̜̍͊͌̑̔̓͑̄͘̕͜͜ạ̶̡̢̻̝̲̺͎̌́̃̍͜ļ̷̼̱̮̝͍̰̙͉̙͓̟̺͉́̆͐̓͜͠g̸̡̛̭͇̖̪̻̹͖̓̏́̀̐̚͜͝͠ȍ̵̡̧̢̮̹̹̪̤͎͚͓̈́̽͜͝͠ͅด้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็ ด้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็ ด้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็";
    public static final String LANGUAGES = "田中さんにあげて下さい和製漢語사회과학원어𐐜𐐔𐐇𐐝𐐀𐐡𐐇𐐓ЁЂЃЄЅІЇЈГДЕЖЗИЙКЛОПРУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклтуфхцчшщъыьэюя نفس سقطت وبالتحديد،";
    public static final String HTML = "<foo val=“bar” />";
    public static final String KAOMOJI = "ヽ༼ຈل͜ຈ༽ﾉ(｡◕ ∀ ◕｡)｀ｨ(´∀｀∩ﾟ･✿ヾ╲(｡◕‿◕｡)╱✿･ﾟ(╯°□°）╯︵ ┻━┻)¯\\\\_(ツ)_/¯";
    public static final String CHAOS = "Ṱ̺̺̕o͞ ̷i̲̬͇̪͙n̝̗͕v̟̜̘̦͟o̶̙̰̠kè͚̮̺̪̹̱̤ ̖t̝͕̳̣̻̪͞h̼͓̲̦̳̘̲e͇̣̰̦̬͎ ̢̼̻̱̘h͚͎͙̜̣̲ͅi̦̲̣̰̤v̻͍e̺̭̳̪̰-m̢iͅn̖̺̞̲̯̰d̵̼̟͙̩̼̘̳ ̞̥̱̳̭r̛̗̘e͙p͠r̼̞̻̭̗e̺̠̣͟s̘͇̳͍̝͉e͉̥̯̞̲͚̬͜ǹ̬͎͎̟̖͇̤t͍̬̤͓̼̭͘ͅi̪̱n͠g̴͉ ͏͉ͅc̬̟h͡a̫̻̯͘o̫̟̖͍̙̝͉s̗̦̲.̨̹͈̣";
    public static final String MIRROR_TEXT = "˙ɐnbᴉlɐ ɐuƃɐɯ ǝɹolop ʇǝ ǝɹoqɐl ʇn ʇunpᴉpᴉɔuᴉ ɹodɯǝʇ poɯsnᴉǝ op pǝs 'ʇᴉlǝ ƃuᴉɔsᴉdᴉpɐ ɹnʇǝʇɔǝsuoɔ 'ʇǝɯɐ ʇᴉs ɹolop ɯnsdᴉ ɯǝɹo˥";
    public static final String quickBrownFox1 = "Ｔｈｅ ｑｕｉｃｋ ｂｒｏｗｎ ｆｏｘ ｊｕｍｐｓ ｏｖｅｒ ｔｈｅ ｌａｚｙ ｄｏｇ";
    public static final String quickBrownFox2 = "𝐓𝐡𝐞 𝐪𝐮𝐢𝐜𝐤 𝐛𝐫𝐨𝐰𝐧 𝐟𝐨𝐱 𝐣𝐮𝐦𝐩𝐬 𝐨𝐯𝐞𝐫 𝐭𝐡𝐞 𝐥𝐚𝐳𝐲 𝐝𝐨𝐠";
    public static final String quickBrownFox3 = "𝕿𝖍𝖊 𝖖𝖚𝖎𝖈𝖐 𝖇𝖗𝖔𝖜𝖓 𝖋𝖔𝖝 𝖏𝖚𝖒𝖕𝖘 𝖔𝖛𝖊𝖗 𝖙𝖍𝖊 𝖑𝖆𝖟𝖞 𝖉𝖔𝖌";
    public static final String quickBrownFox4 = "𝑻𝒉𝒆 𝒒𝒖𝒊𝒄𝒌 𝒃𝒓𝒐𝒘𝒏 𝒇𝒐𝒙 𝒋𝒖𝒎𝒑𝒔 𝒐𝒗𝒆𝒓 𝒕𝒉𝒆 𝒍𝒂𝒛𝒚 𝒅𝒐𝒈";
    public static final String quickBrownFox5 = "𝓣𝓱𝓮 𝓺𝓾𝓲𝓬𝓴 𝓫𝓻𝓸𝔀𝓷 𝓯𝓸𝔁 𝓳𝓾𝓶𝓹𝓼 𝓸𝓿𝓮𝓻 𝓽𝓱𝓮 𝓵𝓪𝔃𝔂 𝓭𝓸𝓰";
    public static final String quickBrownFox6 = "𝕋𝕙𝕖 𝕢𝕦𝕚𝕔𝕜 𝕓𝕣𝕠𝕨𝕟 𝕗𝕠𝕩 𝕛𝕦𝕞𝕡𝕤 𝕠𝕧𝕖𝕣 𝕥𝕙𝕖 𝕝𝕒𝕫𝕪 𝕕𝕠𝕘";
    public static final String quickBrownFox7 = "𝚃𝚑𝚎 𝚚𝚞𝚒𝚌𝚔 𝚋𝚛𝚘𝚠𝚗 𝚏𝚘𝚡 𝚓𝚞𝚖𝚙𝚜 𝚘𝚟𝚎𝚛 𝚝𝚑𝚎 𝚕𝚊𝚣𝚢 𝚍𝚘𝚐";
    public static final String quickBrownFox8 = "⒯⒣⒠ ⒬⒰⒤⒞⒦ ⒝⒭⒪⒲⒩ ⒡⒪⒳ ⒥⒰⒨⒫⒮ ⒪⒱⒠⒭ ⒯⒣⒠ ⒧⒜⒵⒴ ⒟⒪⒢";
    public static final String IMG = "<IMG SRC=&#106;&#97;&#40;&#39;&#88;&#83;&#83;&#39;&#41;>";
    public static final String ON_COPY = "<u oncopy=alert()> Copy me</u>";
    public static final String ON_WHEEL = "<i onwheel=alert(1)> Scroll over me </i>";
    public static final String ON_TEXTAREA = "</textarea><script>alert(123)</script>";
    public static final String SQL_INJECTION_1 = "1;DROP TABLE users";
    public static final String SQL_INJECTION_2 = "1'; DROP TABLE users-- 1";
    public static final String VERSION = "--version";
    public static final String USER = "$USER";
    public static final String LIST_FILES = "%x('ls -al /')";
    public static final String HOME = "$HOME";
    public static final String ENV_HOME = "$ENV{'HOME'}";
    public static final String FILE = "File:///";
    public static final String PASSWD = "../../../../../../../../../../../etc/passwd%00";
    public static final String HOSTS = "../../../../../../../../../../../etc/hosts";
    public static final String PLEASE_WAKE_UP = "If you're reading this, you've been in a coma for almost 20 years now. We're trying a new technique. We don't know where this message will end up in your dream, but we hope it works. Please wake up, we miss you.";
    public static final String ROSES_ARE_RED = "Roses are \u001b[0;31mred\u001b[0m, violets are \u001b[0;34mblue. Hope you enjoy terminal hue";
    public static final String PRINT = "{% print 'x' * 64 * 1024**3 %}";
    public static final String CLASSMRO = "{{ \"\".__class__.__mro__[2].__subclasses__()[40](\"/etc/passwd\").read() }}";
    public static final String JS_ALERT_01 = "<script>alert(123)</script>";
    public static final String JS_ALERT_02 = "&lt;script&gt;alert(&#39;123&#39;);&lt;/script&gt;";
    public static final String JS_ALERT_03 = "<img src=x onerror=alert(123) />";
    public static final String JS_ALERT_04 = "<svg><script>123<1>alert(123)</script>";
    public static final String JS_ALERT_05 = "\"><script>alert(123)</script>";
    public static final String JS_ALERT_06 = "'><script>alert(123)</script>";
    public static final String JS_ALERT_07 = "><script>alert(123)</script>";
    public static final String JS_ALERT_08 = "</script><script>alert(123)</script>";
    public static final String JS_ALERT_09 = "< / script >< script >alert(123)< / script >";
    public static final String JS_ALERT_10 = " onfocus=JaVaSCript:alert(123) autofocus";
    public static final String JS_ALERT_11 = "\" onfocus=JaVaSCript:alert(123) autofocus";
    public static final String JS_ALERT_12 = "' onfocus=JaVaSCript:alert(123) autofocus";
    public static final String JS_ALERT_13 = "＜script＞alert(123)＜/script＞";
    public static final String JS_ALERT_14 = "<sc<script>ript>alert(123)</sc</script>ript>";
    public static final String JS_ALERT_15 = "--><script>alert(123)</script>";
    public static final String JS_ALERT_16 = "\";alert(123);t=\"";
    public static final String JS_ALERT_17 = "';alert(123);t='";
    public static final String JS_ALERT_18 = "JavaSCript:alert(123)";
    public static final String JS_ALERT_19 = ";alert(123);";
    public static final String JS_ALERT_20 = "src=JaVaSCript:prompt(132)";
    public static final String JS_ALERT_21 = "\"><script>alert(123);</script x=\"";
    public static final String JS_ALERT_22 = "'><script>alert(123);</script x='";
    public static final String JS_ALERT_23 = "><script>alert(123);</script x=";
    public static final String JS_ALERT_24 = "\" autofocus onkeyup=\"javascript:alert(123)";
    public static final String JS_ALERT_25 = "' autofocus onkeyup='javascript:alert(123)";
    public static final String JS_ALERT_26 = "<script\\x20type=\"text/javascript\">javascript:alert(1);</script>";
    public static final String JS_ALERT_27 = "'`\"><\\x00script>javascript:alert(1)</script>";
    public static final String JS_ALERT_28 = "ABC<div style=\"x:expression\\x00(javascript:alert(1)\">DEF";
    public static final String JS_ALERT_29 = "ABC<div style=\"x:exp\\x00ression(javascript:alert(1)\">DEF";
    public static final String JS_ALERT_30 = "<a href=\"\\x0Bjavascript:javascript:alert(1)\" id=\"fuzzelement1\">test</a>";
    public static final String JS_ALERT_31 = "<a href=\"\\xC2\\xA0javascript:javascript:alert(1)\" id=\"fuzzelement1\">test</a>";
    public static final String JS_ALERT_32 = "<a href=\"\\xE1\\xA0\\x8Ejavascript:javascript:alert(1)\" id=\"fuzzelement1\">test</a>";
    public static final String JS_ALERT_33 = "\"`'><script>\\xEF\\xBB\\xBFjavascript:alert(1)</script>";
    public static final String JS_ALERT_34 = "<img src=x\\x00onerror=\"javascript:alert(1)\">";
    public static final String JS_ALERT_35 = "<img src=x\\x09onerror=\"javascript:alert(1)\">";
    public static final String JS_ALERT_36 = "<a href=java&#1&#2&#3&#4&#5&#6&#7&#8&#11&#12script:javascript:alert(1)>XXX</a>";
    public static final String JS_ALERT_37 = "<img src=\"x` `<script>javascript:alert(1)</script>\"` `>";
    public static final String JS_ALERT_38 = "<img src onerror /\" '\"= alt=javascript:alert(1)//\">";
    public static final String JS_ALERT_39 = "<title onpropertychange=javascript:alert(1)></title><title title=>";
    public static final String JS_ALERT_40 = "<a href=http://foo.bar/#x=`y></a><img alt=\"`><img src=x:x onerror=javascript:alert(1)></a>\">";
    public static final String JS_ALERT_41 = "<!--[if]><script>javascript:alert(1)</script -->";
    public static final String JS_ALERT_42 = "<IMG onmouseover=\"alert('xxs')\">";
    public static final String WRONG_DATE_FORMAT = "00/00/0000";

    private TestStrings() {
        throw new IllegalStateException("Constants class");
    }

    public static String longString(int length) {
        if (length <= 0) {
            return "";
        }
        final String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890,.!?;:'\"!@#$%^&*()_+-=[]{}/ ";
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 1; i <= length; i++) {
            sb.append(charset.charAt(rand.nextInt(charset.length() - 1)));
        }
        return sb.toString();
    }

    public static String longNumber(int length, boolean leadingZero) {
        if (length <= 0) {
            return "";
        }
        final String charset = "0123456789";
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 1; i <= length; i++) {
            if (leadingZero) {
                sb.append("0");
                leadingZero = false;
                continue;
            }
            sb.append(charset.charAt(rand.nextInt(charset.length() - 1)));
        }
        return sb.toString();
    }
}
