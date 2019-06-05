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
- H2

### Organizacja pracy
Wszystkie zadania przydzielane są na platformie [PivotalTracker](https://www.pivotaltracker.com/projects/2336446/)
Komunikacja przebiega na [naszym Slacku](http://zzpj-2019.slack.com).

# Instrukcje
## Baza danych
Baza danych została zmieniona w trkacie produkcji. Na potrzbeby pokazu zdecydowano, że użyjemy bazy danych **H2**. W razie konieczności pozwala na łatwą i bezbolesną zmianę na serwer zdalny.

## Api
Api łączy się przez port *8080*, docelowo wszystkie operacje rest, które ma wykonywać admin kryją się pod adresem */api/admin*, oczywiście cała reszta, tudzież dla klienta bądź niezalogowanego użytkownika pod adresem */api*.
Na potrzeby testów API od razu próbuje utworzyć konto administracyjne o loginie: admin oraz haśle: admin, ale tylko jeśli aktywny jest profil Springa **init**.
# Autorzy
- Adrian Fijałkowski,
- Dominik Lange, 
- Bartosz Goss,
- Kacper Pradzyński,
- Arkadiusz Grabowski.
