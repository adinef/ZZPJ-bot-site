# ZZPJ-slack-discord-bot-integration [![Build Status](https://travis-ci.org/adinef/ZZPJ-bot-site.svg?branch=master)](https://travis-ci.org/adinef/ZZPJ-bot-site)
Projekt na zajęcia z ZZPJ
## Cel
Aplikacja polega na integracji kilku API w jednym miejscu.
### Planowane funkcjonalności
- Integracja API Slack i Discord,
- Edytor tekstu z mozliwością szkicy i publikowania (tablica),
- Kalendarz pozwalający uzytkownikowi planować powiadomienia.

### Technologie
Apliakcja tworzona jest w języku Java, obecnie stos technologiczny to:
- Spring Framework,
- MongoDB

### Organizacja pracy
Wszystkie zadania przydzielane są na platformie [PivotalTracker](https://www.pivotaltracker.com/projects/2336446/)
Komunikacja przebiega na [naszym Slacku](http://zzpj-2019.slack.com).

# Instrukcje
## Baza danych
Standardowo, dla testów, plik konfiguracyjny łączy się z podaną w :application.properties" baza danych.
Konfiguracja połączenia:
- Host: localhost
- Port: 27017
- Dane logowania: podstawowe, puste

W przypadku adresu hosta oczywiście mogą istnieć różnice, stąd też albo konieczna jest zmiana,
albo mapowanie adresu, tj. na przykłąd przez plik *hosts*.
Zalecanym jednak rozwiązaniem jest stworzenie profilu uruchomieniowego z 
właściwym ustawieniem dla właściwości **spring.data.mongodb.host**.
Przykład dla IntelliJ IDEA:

![IntelliJ IDEA, konfiguracja dla hosta MongoDB](https://user-images.githubusercontent.com/19805658/57193864-258b7f80-6f40-11e9-9cfd-e74c29ed5e4a.png "IntelliJ IDEA, konfiguracja dla hosta MongoDB")
## Api
Api łączy się przez port *8080*, docelowo wszystkie operacje rest, które ma wykonywać admin kryją się pod adresem */api/admin*, oczywiście cała reszta, tudzież dla klienta bądź niezalogowanego użytkownika pod adresem */api*.
Na potrzeby testów API od razu próbuje utworzyć konto administracyjne o loginie: admin oraz haśle: admin, ale tylko jeśli aktywny jest profil Springa **init**.
# Autorzy
- Adrian Fijałkowski,
- Dominik Lange, 
- Bartosz Goss,
- Kacper Pradzyński,
- Arkadiusz Grabowski.
