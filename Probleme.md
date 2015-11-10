# Verwaltung der Games am Server #
_Frage: Wird nun tatsächlich nur ein Spiel zur gleichen Zeit verwaltet, oder gibt es Fälle wo bspw. ein Spiel gestartet wird, während für ein zweites schon Spieler registriert werden können?_

# Spread Konfiguration #
_Frage: Wie werden nun die Server in der Spread Config am schlauesten konfiguriert?_

# Refactoring: IAdvancedListener Methoden in ServerImplementation #
_Wahrscheinlich wäre es logischer die IAdvancedListener Implementierung auch in die RegistryServerImplementation Klasse zu verschieben, damit funktionell der ganze Server in dieser einen Klasse abgebildet ist, und RegistryServer nur mehr für RMI und SPread Startup zuständig ist._

# PlayerID's #
_Derzeit werden für jeden neuen Player von einem Sequencer einfach aufsteigende Nummern vergeben, sodass kein Spieler die gleiche Player ID bekommt. Spätestens nach dem Spielstart müssen die PlayerID's der 4 Spieler aber neu (von 0 bis 3) durchnummeriert werden, weil die Alcatraz API dies erwartet. Oder?_

**_Stimmt so - siehe Kommentar unten_**

# Spiele-Start-Ablauf #
_Brauchen wir noch eine createPlayer() Methode_

**Siehe PaketeAufteilung**

# Nötige Änderungen nach Zusammenlegung von createPlayer & registerPlayer #

_Mir ist nach der Änderungen folgendes aufgefallen:_

**Man muss jetzt bei registerPlayer den clientstub mitübergeben, was etwas sinnfrei ist, da man den auch abfragen kann?!**

**_Den Client-Stub kann man leider nicht abfragen weil wir für die Clients keine eigene Registry verwenden sondern die ClientStubs per RMI an den Server übergeben, der wiederum verteilt die Stubs beim Spielstart auf alle anderen Clients!_**

**In der Methode Server.addPlayer wird auf gleiche IDs überprüft, da wir aber keine mehr vergeben, kann das wegfallen oder?**