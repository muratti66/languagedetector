# Language Detector

Language Detector is a natural language processing application used in machine learning. It learns and classifies the input text files and tells the text language over them.

### Features :
  - It works quickly and easily because a socket program
  - Exemplary data are in the project
  - Most commonly used six language supported (Deutsche, English, French, Italian, Russian, Turkish)
  - Lowest resource alocation
  - IPv6 and IPv4 supported

### Supported OS :
  - Linux
  - Mac OS X
  
### Preperation :
  - First you need to have java 1.8 and netbeans on your system
  - Download the project and open it with NetBeans
  - Generated following files copy in to "/opt/LanguageDetector" path
    - config.cfg
    - langdetect.service
    - languageTxts/ folder (with the files in it)
    - target/LanguageDetector-1.0-SNAPSHOT.jar 
    - target/dependency/ folder (with the files in it)
  - and run it :
```sh
java -jar /opt/LanguageDetector/LanguageDetector-1.0-SNAPSHOT.jar -Xms128m -Xmx256m
```
   - or if you have systemd on your system :
```sh
ln -s /opt/LanguageDetector/langdetect.service /usr/lib/systemd/system/
systemctl daemon-reload
systemctl start langdetect
```
  - Check the logs in "/var/log/languagedetector.log" log file
  - Finally port control : 
```sh
$ netstat -ant| grep 8181
    tcp46      0      0  *.8181                 *.*                    LISTEN 
```

### Usage :
Send your data directly (text) with the socket operation.
Example with netcat :
```sh
$ echo "hello how are you ? what is your name, im file" | nc 127.0.0.1 8181
100;;English;;0.9934262621714024
echo "merhaba dünya istanbul ankara nasıl araba" | nc 127.0.0.1 8181
100;;Turkish;;0.8612241376734291
$ echo "Привет, как вы, ваше имя?" | nc 127.0.0.1 8181
100;;Russian;;0.9699016241097704
$ echo "Ciao, come stai, il tuo nome?" | nc 127.0.0.1 8181
100;;Italian;;0.9945782711354506
```
The returned data is seperating with ";;".
First space is return code. These codes correspond to the following situations:
   -  0 - if sended data is "quit", returned this value
   -  100 - query is success, returned the language and score
   -  900 - program has a any error, check the logs

Second space is return the language name. Language name is the file name in the "languageTxts" folder.

Third space is return the language double score. Double 0.9934262621714024 value is equal 99,34...% percent.
This score value is low if you do not belong to any language or lowest size data (one ore two basic word).
We are thinking of acceptable score 80% and above

### Specific configuration changes :
You can do these changes in the file "/opt/LanguageDetector/config.cfg".
Changes :
   -  ld.language_files_path : If you need a specific path for language files.
   -  ld.max_number_of_threads : Thread count for Datumbox ML framework
   -  sck.port_number : Socket port number
   -  sck.client_so_timeout_milsec : Client connection timeout (millisecond) 
   -  sck.client_debug : If you will recording the connection and other transactions.

### Used Other Projects and Resources : 
   - [Datumbox Machine Learning Framework](https://github.com/datumbox/datumbox-framework)
   - [Wikipedia Deutsche](https://de.wikipedia.org/wiki/Deutsche_Sprache)
   - [Wikipedia English](https://en.wikipedia.org/wiki/English_language)
   - [Wikipedia French](https://fr.wikipedia.org/wiki/Français)
   - [Wikipedia Italian](https://it.wikipedia.org/wiki/Lingua_italiana)
   - [Wikipedia Russian](https://ru.wikipedia.org/wiki/Русский_язык)
   - [Wikipedia Turkish](https://tr.wikipedia.org/wiki/Türkçe)

### To do :
   - Socket address static mapping add to config file
   - Socket server will be multithread architecture
   - More language support
   - Bug fix operation as usual..

License
----

GNU