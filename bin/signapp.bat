@echo off
rem * SignTool by L-ectron-X ( java-forum.org )
rem *
rem * Ablauf beim Signieren
rem *
rem * Erzeugen eines eigenen Schlüssels
rem * Mit KeyTool, einem Werkzeug aus dem SDK, einen neuen Schlüssel erzeugen:
rem * keytool -genkey -alias Signer -dname "cn=Dein Name, c=de"
rem * Gib anschließend Dein Passwort ein.
rem *
rem * Erzeugen eines Zertifikats
rem *	
rem * Mit KeyTool Zertifikat erzeugen:
rem * keytool -selfcert -alias Signer -dname "cn=Dein Name, c=de"
rem * Gib anschließend dein Passwort ein.
rem * keytool unterstützt weitere Angaben:
rem * CN=commonName
rem * OU=organizationUnit
rem * O=organizationName
rem * L=localityName
rem * S=stateName
rem * C=country
rem * 
rem * Signieren des Applets
rem * Signieren des Applets mit Hilfe des Tools jarsigner
rem * jarsigner signed.jar Signer
rem * Und noch einmal dein Passwort eingeben.

echo = SignTool =
echo Dieses Tool hilft beim Erzeugen von signierten jar-Dateien
echo.
echo Schritt 1: jar-Dateien erzeugen
echo -------------------------------

rem Pfad zum SDK setzen 
rem --> anpassen!
set java_home=.;C:\Programme\Java\jdk1.6.0\bin
set path=.;%path%;%java_home%

echo Manifestdatei erzeugen...
echo Manifest-Version: 1.0>manifest.mf 
echo Created-by: SignTool by L-ectron-X>>manifest.mf 

rem --> Anpassen, wenn eine Applikation statt eines Applets signiert werden soll!
rem echo Main-Class: package.MainClass>>manifest.mf

echo.>>manifest.mf

if exist *.jar goto key
echo jar-Datei mit angegebenen Parametern erzeugen...
rem --> anpassen!
rem * In folgendem Beispiel werden alle .class-Dateien und die Verzeichnisse bilder und etc
rem * mit ins jar-Archiv gepackt.
jar cfmv mctpo.jar manifest.mf com/atlan1/mctpo/*.class res com/atlan1/mctpo/WGen/*.class com/atlan1/mctpo/Texture/*.class com/atlan1/mctpo/Inventory/*.class com/atlan1/mctpo/Listener/*.class

:key
echo.
echo Schritt 2: Schluessel generieren
echo --------------------------------
rem --> anpassen!
keytool -genkey -alias Signer -dname "cn=Atlan1, c=de"

echo.
echo Schritt 3: Zertifikat erzeugen
echo ------------------------------
rem --> anpassen!
rem -validity 18250 (365 Tage x 50) erzeugt ein 50 Jahre gültiges Zertifikat
keytool -selfcert -validity 3650 -alias Signer -dname "cn=Atlan1, c=de"

if not exist *.jar goto error
echo.
echo Schritt 4: jar-Datei signieren
echo ------------------------------
echo jarsigner erwartet hier nochmals dein Passwort.
rem --> anpassen!
jarsigner mctpo.jar Signer

echo.
echo Schritt 5: Zertifikat testen
echo ----------------------------
rem --> anpassen!
jarsigner -verify -verbose -certs mctpo.jar
goto end

:error
echo.
echo Es wurde keine jar-Datei zum Signieren gefunden.
echo Die Erzeugung der jar-Datei ist moeglicherweise fehlgeschlagen.
echo Pruefe deine Eingaben in der Batchdatei!

:end
if not exist manifest.mf goto console
echo.
rem Manifest von Festplatte löschen 
del manifest.mf

:console
rem Console für Ausgaben noch geöffnet lassen
echo.
pause