package jw.demo.constants;

public class Constants {

    // Dynamic Data Redux Scripts
    public static final String VERSION_ID = "versionId";
    public static final String DYNAMIC_COMPONENT = "return window.store.getState().dynamicComponent";
    public static final String CURRENT_TASK_VERSION_ID = DYNAMIC_COMPONENT + ".data.versionId";
    public static final String RFI_VERSION_ID = DYNAMIC_COMPONENT + ".context.rfi.versionId";
    public static final String RFI_0_VERSION_ID = DYNAMIC_COMPONENT + ".context.rfis.0.versionId";
    public static final String RFI_1_VERSION_ID = DYNAMIC_COMPONENT + ".context.rfis.1.versionId";
    public static final String SCROLL_INTO_VIEW = "arguments[0].scrollIntoView({behavior: 'auto', block: 'center', inline: 'nearest'});";
    public static final String DOCUMENT_READY_STATE = "return document.readyState";
    public static final String BLUR_ACTIVE_ELEMENT = "document.activeElement.blur()";

    // DATE TIME

    @SuppressWarnings("SpellCheckingInspection")
    public static final String MODIFY_DATETIME = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    public static final String EASTERN_TIME_ZONE_ID = "America/New_York";
    public static final String TIMESTAMP = "timestamp";
    public static final String UTC = "UTC";
    public static final String DATE_FORMAT = "dd-MM-yyyy";

    // CONSTANT
    public static final String DATA = "data";
    public static final String COMPLETE = "complete";

    // ENDPOINT
    public static String AUTHORIZATION = "path/used/for/authorization";
    public static String TOKEN = "token";


}
