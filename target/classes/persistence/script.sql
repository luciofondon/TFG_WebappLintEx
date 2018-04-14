
/* 
    Document   : 	Tablas para el SGBD Postgre SQL.
    Created on : 	18-ago-2016, 11:00:00
    Author     : 	Lucio David Fondon Terron.
    Description:	Tablas para almacenar los datos recuperados de los dispositivos.
*/

CREATE TABLE AUDIO(
  	ID              			SERIAL           		NOT NULL,
  	IMEI						VARCHAR(50)				NOT NULL,
  	DATE_SYNCHRONIZE			TIMESTAMP				NOT NULL,
  	NAME						VARCHAR(200)      		NOT NULL,
	PATH_JAVA             		VARCHAR(500)      		NOT NULL,
	PATH_WEB             		VARCHAR(500)      		NOT NULL,
	PRIMARY KEY (ID, IMEI)	
);

CREATE TABLE BEACON (
  	ID              			SERIAL            		NOT NULL,
  	IMEI	      				VARCHAR(50)				NOT NULL,
 	DATE_SYNCHRONIZE           	VARCHAR(50)       		NOT NULL,
  	LATITUDE         			REAL					NOT NULL,
  	LONGITUDE        			REAL					NOT NULL,
 	TYPE_BEACON       			INT               		NOT NULL,
  	PRIMARY KEY (ID, IMEI)
);

CREATE TABLE BROWSER (
  	ID              			SERIAL            		NOT NULL,
 	IMEI	      				VARCHAR(50)				NOT NULL,
 	DATE_ANDROID				TIMESTAMP				NOT NULL,
  	DATE_SYNCHRONIZE			TIMESTAMP				NOT NULL,
   	TITLE         				VARCHAR(9999)     		NOT NULL,
  	TYPE_BROWSER       			INT               		NOT NULL,
  	URL     					VARCHAR(9999999)       	NOT NULL, 
  	PRIMARY KEY (ID, IMEI)	
);

CREATE TABLE CALL (
	ID							SERIAL					NOT NULL,
  	IMEI            			VARCHAR(50)       		NOT NULL,
  	DATE_ANDROID				TIMESTAMP				NOT NULL,
  	DATE_SYNCHRONIZE			TIMESTAMP				NOT NULL,
	DURATION_HOUR       		INT       				,
  	DURATION_MINUTE       		INT       				,
  	DURATION_SECOND       		INT      				,
  	NAME_CONTACT            	VARCHAR(50)				NOT NULL,
  	NAME_FILE            		VARCHAR(50)				,
  	PATH_JAVA            		VARCHAR(500)      		,
  	PATH_WEB             		VARCHAR(500)      		,
  	PHONE_NUMBER     			VARCHAR(50)       		NOT NULL,
  	TYPE_CALL        			INT               		NOT NULL,
  	PRIMARY KEY (ID, IMEI)					
);

CREATE TABLE CONTACT (
  	ID              			SERIAL           		NOT NULL,
  	IMEI	      				VARCHAR(50)				NOT NULL,
  	DATE_SYNCHRONIZE			TIMESTAMP				NOT NULL,
  	EMAIL           			VARCHAR(1000)			,
  	NAME            			VARCHAR(1000)			NOT NULL, 
  	PHONE_NUMBER     			VARCHAR(1000)			NOT NULL,
 	PRIMARY KEY (ID, IMEI)	
);

CREATE TABLE DEVICE (
	IMEI            			VARCHAR(50)       		NOT NULL,
 	IP							VARCHAR(50)       		NOT NULL,
 	ADDRESS          			VARCHAR(50)       		NOT NULL,
 	AUDIO_RECORD				BOOLEAN					NOT NULL,
 	BATTERY          			INT      				NOT NULL,
 	BLUETOOTH_STATE				VARCHAR(50)       		NOT NULL,
 	CALL_STATE 					VARCHAR(200)				,
 	CONNECTION_INTERNET 		VARCHAR(50)       		NOT NULL,
 	CONT_LAST_IMAGE				INT						,
 	COUNTRY          			VARCHAR(50)       		NOT NULL,
 	DEVICE_STATE				BOOLEAN					NOT NULL,
 	EMAIL						VARCHAR(500)			NOT NULL,
 	GPS_STATE          			VARCHAR(50)       		NOT NULL,
 	LAST_CONNECTION    			TIMESTAMP       		NOT NULL,
 	LAST_SYNCHRONIZATION   		TIMESTAMP       		NOT NULL,
 	LOCALITY          			VARCHAR(50)       		NOT NULL,
 	LATITUDE_GPS         		REAL					,
  	LONGITUDE_GPS         		REAL					,
  	LATITUDE_NETWORK         	REAL					,
  	LONGITUDE_NETWORK        	REAL					,
  	LATITUDE_PASSIVE         	REAL					,
  	LONGITUDE_PASSIVE        	REAL					,
  	MOBILE_STATE 				VARCHAR(50)       		,
 	MODEL						VARCHAR(50)       		,	
 	NAME_COMPANY				VARCHAR(50)       		NOT NULL,
 	NFC_STATE 					VARCHAR(50)       		,
  	PHONE_NUMBER				VARCHAR(50)      		NOT NULL,
  	POWER_STATE          		VARCHAR(50)       		NOT NULL,
 	START_BEACON_GPS			BOOLEAN					,
  	START_BEACON_NETWORK		BOOLEAN					,
  	START_BEACON_PASSIVE		BOOLEAN					,
  	VERSION						VARCHAR(10)       		NOT NULL,
  	VIDEO_RECORD				BOOLEAN					,
 	WIFI_STATE          		VARCHAR(50)       		NOT NULL,
  	PRIMARY KEY (IMEI)	
);

CREATE TABLE IMAGE(
  	ID              			SERIAL           		NOT NULL,
  	IMEI						VARCHAR(50)				NOT NULL,
  	DATE_ANDROID				TIMESTAMP				,
  	DATE_SYNCHRONIZE			TIMESTAMP				NOT NULL,
  	HEIGHT						INT						,
  	LATITUDE         			REAL					,
  	LONGITUDE        			REAL					,
  	NAME            			VARCHAR(900)       		NOT NULL,
	PATH_JAVA            		VARCHAR(500)      		NOT NULL,
  	PATH_WEB             		VARCHAR(500)      		NOT NULL,  
  	SIZE						BIGINT					,
  	TYPE_IMAGE       			INT               		NOT NULL,
  	WIDTH            			INT       				,
  	PRIMARY KEY (ID, IMEI)		
);

CREATE TABLE RESOURCE (
  	ID              			SERIAL           		NOT NULL,
  	IMEI            			VARCHAR(50)       		NOT NULL,
  	DATE_SYNCHRONIZE           	TIMESTAMP				NOT NULL,
  	NAME            			VARCHAR(900)       		NOT NULL,
 	PATH_ANDROID				VARCHAR(900)       		,
 	PATH_JAVA					VARCHAR(900)       		,
 	PATH_WEB					VARCHAR(900)       		,
 	SIZE						BIGINT					NOT NULL,
  	TYPE_RESOURCE       		INT               		NOT NULL,
    PRIMARY KEY (ID, IMEI)			
);

CREATE TABLE SMS (
  	ID              			SERIAL            		NOT NULL,
  	IMEI	      				VARCHAR(50)				NOT NULL,
  	DATE_ANDROID				TIMESTAMP				NOT NULL,
  	DATE_SYNCHRONIZE			TIMESTAMP				NOT NULL,
  	MESSAGE         			VARCHAR(1000)     		NOT NULL,
	NAME            			VARCHAR(50)				NOT NULL,
  	TYPE_SMS         			INT               		NOT NULL,
 	PHONE_NUMBER     			VARCHAR(50)       		NOT NULL, 
  	PRIMARY KEY (ID, IMEI)		
);

CREATE TABLE USER_WEB (
  	ID              		SERIAL           			NOT NULL,
  	ENABLED          		INT							NOT NULL,
  	LAST_NAME            	VARCHAR(50)       			NOT NULL,
  	NAME            		VARCHAR(50)       			NOT NULL,
  	NICK            		VARCHAR(50)       			NOT NULL,
  	PASSWORD				VARCHAR(50)       			NOT NULL,
  	THEME					VARCHAR(50)					NOT NULL,
 	PRIMARY KEY (ID)	
);

INSERT INTO USER_WEB VALUES (1, 1, 'Fondón Terrón', 'Lucio David', 'luciofondon', 'admin', 'dark-hive');

CREATE TABLE VIDEO(
  	ID              			SERIAL 					NOT NULL,   
  	IMEI						VARCHAR(50)				NOT NULL,
  	DATE_SYNCHRONIZE        	TIMESTAMP				NOT NULL,
 	NAME						VARCHAR(200)      		NOT NULL,
	PATHJAVA             		VARCHAR(500)      		NOT NULL,
	PATHWEB             		VARCHAR(500)      		NOT NULL,
	PRIMARY KEY (NAME, IMEI)	
);

