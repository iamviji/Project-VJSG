#include <SoftwareSerial.h>
#include <ESP8266wifi.h>

// Auther Giridhar RP
// Copy Right 2016
// Version 1.0
//---Trasnport module base class
class TrasnportModule
{

  public:
    TrasnportModule()
    {

    }
    virtual void configure() = 0;
    virtual void sendData(char aInData[], int aInLen = 0) = 0;
    virtual void receiveData() = 0;
};

//-----------Constants for Wifi Trasnport Module----
#define ANDROID_APP_IP "192.168.1.3"
#define ANDROID_APP_TCP_PORT "5432"
#define ANDROID_APP_UDP_PORT "5432"
#define MAX_RETRY       20
#define BOARD_RESET_PIN   10

#define sw_serial_rx_pin 2 //  Connect this pin to TX on the esp8266
#define sw_serial_tx_pin 3 //  Connect this pin to RX on the esp8266
#define esp8266_reset_pin 4 // Connect this pin to CH_PD on the esp8266, not reset. (let reset be unconnected)
#define MY_SSID_AP "Giri"
#define MY_AP_PASSWORD "12345678901234"

class Wifi : public TrasnportModule
{
    static ESP8266wifi pWifi;
    static SoftwareSerial pSwSerial;
    char  pAndroidIp[30];
    char  pTcpPort[6];
    char  pUdpPort[6];
  public:
    Wifi()
    {
      sprintf(pAndroidIp, "%s", ANDROID_APP_IP);
      sprintf(pTcpPort, "%s", ANDROID_APP_TCP_PORT);
      sprintf(pUdpPort, "%s", ANDROID_APP_UDP_PORT);
    }

    void configure()
    {
      bool lRet = false;
      int lRetryCount = 1;
      pSwSerial.begin(9600);
      Serial.println("Disconnect from Access point ");

      //Make sure to exit from AP and reconnect it
      pWifi.disconnectFromAP();
      delay(4000);

      Serial.println("Connecting to Access poing ");
      //wifi.setTransportToTCP();// this is also default
      pWifi.setTransportToUDP();//Will use UDP when connecting to server, default is TCP

      pWifi.endSendWithNewline(true); // Will end all transmissions with a newline and carrage return ie println.. default is true

      pWifi.begin();
      Serial.println("Wifi started ");
      do
      {
        lRet = pWifi.connectToAP(MY_SSID_AP, MY_AP_PASSWORD);
        delay(2000);

        if ( false == lRet )
        {
          Serial.println("Failed to connect to Access Point ");
          lRetryCount++;
          delay(1000);
          //retry after 1 sec
        }
        char *lIp = pWifi.getIP();
        if ( '\0' == lIp[0] )
        {
          Serial.println("Failed to connect to Access Point. Retrying ");
          delay(2000);
        } else
        {
          Serial.println(" ");
          Serial.print("Got IP from Access Point:");
          Serial.println(lIp);
        }
      } while ( lRet == false && lRetryCount < MAX_RETRY );
      Serial.println("Successfully connected to Access Point ");

      lRetryCount = 1;
      if ( false == lRet )
      {
        //still not connected. Wait after some time.
        //reboot board;
        Serial.println("MAX retry failed to connect to AP. Rebooting Board... ");
        return;
      }

      if (!pWifi.isConnectedToServer())
      {
        Serial.println("Connecting to Android APP... ");
        //Connect to android app
        do
        {
          lRet = pWifi.connectToServer(ANDROID_APP_IP, ANDROID_APP_UDP_PORT);
          lRet = true;
          if ( false == lRet )
          {
            Serial.println("Failed to connect to Android APP ");
            delay(1000);
            //retry after 1 sec
            lRetryCount++;
          }
        } while ( lRet == false && lRetryCount < MAX_RETRY );

        if ( false == lRet )
        {
          //still not connected. Wait after some time.
          //reboot board;
          Serial.println("MAX retry failed to connect Android APP. Rebooting Board... ");
        }
      } else
      {
        Serial.println("Already connected to Android APP... ");
      }
    }//configure Ends

    //send data
    virtual void sendData(char aInData[], int aInLen = 0)
    {
      pWifi.send(SERVER, aInData);
    }//sendData Ends

    void receiveData()
    {

    }//end receiveData

};

SoftwareSerial Wifi::pSwSerial(sw_serial_rx_pin, sw_serial_tx_pin);
ESP8266wifi Wifi::pWifi(pSwSerial, pSwSerial, esp8266_reset_pin, Serial);

//Sensor Base class
class Sensor
{
  public:
    virtual void configure() = 0;

    virtual void getData(char aOutDataBuf[]) = 0;
    //Make sure you send the data in below format Or Sync with Andorid APP for a different set of Protocol
    //2char:2char:value
    //Px:P1:200
    //Px --> Proxomity Sensor
    //P1---> Positon of the sensor. P-> parking and 1-> position in the parking
    //200-> is the value measured

    virtual void whoAmI(char aOutBuf[]) = 0;

};


//-------US Proxomity US sensor pins-------------
#define TRIG_PIN  6
#define ECHO_PIN  7
#define OUT_OF_RANGE 500

class Ultrasonic : public Sensor
{
  private:
    int pTrigPin;
    int pEchoPin;
    char pType[5];
    char pPosition[5];
  public:
    Ultrasonic()
    {
      pTrigPin =  TRIG_PIN;
      pEchoPin = ECHO_PIN;
      strncpy(pType, "Px", 2);
      pType[2] = '\0';
      strncpy(pPosition, "P1", 2);
      pPosition[2] = '\0';
    }

    void whoAmI(char aOutBuf[])
    {
      sprintf(aOutBuf, "%s:%s", pType, pPosition);
    }

    void configure()
    {
      pinMode(pEchoPin, INPUT);
      pinMode(pTrigPin, OUTPUT);
    }

    void getData(char aOutDataBuf[])
    {
      int lDistance = 0;
      int lTravelTime = 0;

      digitalWrite(pTrigPin, LOW);
      delayMicroseconds(2);


      digitalWrite(pTrigPin, HIGH);
      delayMicroseconds(10); // Added this line
      digitalWrite(pTrigPin, LOW);

      lTravelTime = pulseIn(pEchoPin, HIGH);
      lDistance = (lTravelTime / 2) / 29.1;
      if ( lDistance <= 0 )
      {
        lDistance = OUT_OF_RANGE;
      }
      sprintf(aOutDataBuf, "%s:%s:%d:", pType, pPosition, lDistance);
      Serial.print("Seding this[");
      Serial.print(aOutDataBuf);
      Serial.println("]Done");
    }
};

Sensor *gSensor = NULL;
TrasnportModule *gTransport = NULL;

void configureSensor()
{
  //Get a factory class to get
  //instnace of Sensor Object
  // as of now am just doing UltraSonic Sensor
  gSensor = new Ultrasonic();
  gSensor->configure();
}

void configureTransport()
{
  //Get a factory class to get
  //instnace of Transport Object
  // as of now am just doing Wifi
  gTransport = new Wifi();
  gTransport->configure();
}

void setup()
{
  Serial.begin(9600);
  while (!Serial)
    ;
  Serial.println("Setup In progress...");
  configureTransport();
  configureSensor();
}

char gBuf[40] = {""};
void loop()
{

  //As on just connect to Server/Androidapp
  Serial.println("Inside Loop, ");
  if (gSensor != NULL && gTransport != NULL )
  {
    gSensor->getData(gBuf);
    gTransport->sendData(gBuf);
    //Serial.println("Data Sent plz check ");
  }
  delay(1000);

#if 0
  //Send a ping once in a while..
  if (millis() > nextPing) {
    wifi.send(SERVER, "Ping ping..");
    nextPing = millis() + 10000;
  }


  //Listen for incoming messages and echo back, will wait until a message is received, or max 6000ms..
  WifiMessage in = wifi.listenForIncomingMessage(6000);
  if (in.hasData) {
    if (in.channel == SERVER)
      Serial.println("Message from the server:");
    else
      Serial.println("Message a local client:");
    Serial.println(in.message);
    //Echo back;
    wifi.send(in.channel, "Echo:", false);
    wifi.send(in.channel, in.message);
    nextPing = millis() + 10000;
  }
#endif
  //If you want do disconnect from the server use:
  // wifi.disconnectFromServer();

}

//Listen for serial input from the console
void serialEvent() {
  //in Future we need it
}

