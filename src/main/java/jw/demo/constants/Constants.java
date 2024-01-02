package jw.demo.constants;

public class Constants {

    // Dynamic Data Redux Scripts
    public static final String VERSION_ID = "versionId";
    public static final String DYNAMIC_COMPONENT = "return window.store.getState().dynamicComponent";
    public static final String CURRENT_DYNAMIC_COMPONENT_ENTITY_TYPE = DYNAMIC_COMPONENT + ".data.entityType";
    public static final String CURRENT_TASK_VERSION_ID = DYNAMIC_COMPONENT + ".data.versionId";
    public static final String RFI_VERSION_ID = DYNAMIC_COMPONENT + ".context.rfi.versionId";
    public static final String RFI_0_VERSION_ID = DYNAMIC_COMPONENT + ".context.rfis.0.versionId";
    public static final String RFI_1_VERSION_ID = DYNAMIC_COMPONENT + ".context.rfis.1.versionId";

    // DATE TIME

    @SuppressWarnings("SpellCheckingInspection")
    public static final String MODIFY_DATETIME = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    public static final String EASTERN_TIME_ZONE_ID = "America/New_York";
    public static final String TIMESTAMP = "timestamp";
    public static final String UTC = "UTC";
    public static final Object DATE_FORMAT = "dd-MM-yyyy";

    // ENDPOINT
    public static String AUTHORIZATION = "path/used/for/authorization";
    public static String TOKEN = "token";


}
