#include <Arduino.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>
#include <LiquidCrystal.h>

/* ===== НАСТРОЙКИ ===== */
#define WIFI_SSID "Wokwi-GUEST"
#define WIFI_PASS ""

#define SERVER_URL "http://192.168.1.5:8080"
#define USER_SUBSCRIPTION_ID 1

#define BTN_CHECK 4
#define BTN_CONFIG 2
#define BUZZER_PIN 15

#define LCD_RS 16
#define LCD_E 17
#define LCD_D4 5
#define LCD_D5 18
#define LCD_D6 19
#define LCD_D7 21

/* ===== АНТИДРЕБЕЗГ ===== */
bool lastBtnState = HIGH;
unsigned long lastDebounceTime = 0;
const unsigned long debounceDelay = 50;

/* ===== LCD ===== */
LiquidCrystal lcd(LCD_RS, LCD_E, LCD_D4, LCD_D5, LCD_D6, LCD_D7);

/* ===== ПРОТОТИПЫ ===== */
void connectWiFi();
void alertBuzzer();
bool checkSubscriptionFromServer();
void updateLCD(const char* text);

void setup() {
  Serial.begin(115200);

  pinMode(BTN_CHECK, INPUT_PULLUP);
  pinMode(BTN_CONFIG, INPUT_PULLUP);
  pinMode(BUZZER_PIN, OUTPUT);

  lcd.begin(16, 2);
  lcd.print("Initializing...");

  connectWiFi();

  lcd.clear();
  lcd.print("ESP32 Ready");
}

void loop() {
  bool reading = digitalRead(BTN_CHECK);

  if (reading != lastBtnState) lastDebounceTime = millis();

  if ((millis() - lastDebounceTime) > debounceDelay) {
    if (reading == LOW) {
      bool active = checkSubscriptionFromServer();
      if (!active) alertBuzzer();
      delay(500);
    }
  }

  lastBtnState = reading;

  if (digitalRead(BTN_CONFIG) == LOW) {
    updateLCD("CONFIG Pressed");
    alertBuzzer();
    delay(500);
  }

  delay(10);
}

void connectWiFi() {
  Serial.print("Connecting to WiFi...");
  WiFi.begin(WIFI_SSID, WIFI_PASS);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("\nWiFi connected!");
  Serial.println(WiFi.localIP());
  lcd.clear();
  lcd.print("WiFi Connected");
}

void alertBuzzer() {
  digitalWrite(BUZZER_PIN, HIGH);
  delay(200);
  digitalWrite(BUZZER_PIN, LOW);
}

bool checkSubscriptionFromServer() {
  if (WiFi.status() != WL_CONNECTED) {
    updateLCD("WiFi Error");
    return false;
  }

  HTTPClient http;
  String url = String(SERVER_URL) + "/subscriptions/user/" + String(USER_SUBSCRIPTION_ID);
  Serial.println("GET " + url);
  http.begin(url);

  int httpCode = http.GET();
  if (httpCode != 200) {
    Serial.printf("HTTP Error: %d\n", httpCode);
    updateLCD("HTTP Error");
    http.end();
    return false;
  }

  String payload = http.getString();
  http.end();

  DynamicJsonDocument doc(256);
  DeserializationError err = deserializeJson(doc, payload);
  if (err) {
    updateLCD("JSON Error");
    return false;
  }

  const char* status = doc["status"];
  updateLCD(status);

  return strcmp(status, "ACTIVE") == 0;
}

void updateLCD(const char* text) {
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print(text);
}
