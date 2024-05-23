# Mobile Programming Projekt - Diabetes
Davide Ceresa, Marc Federspiel

# 1. Einführung und Ziele
Unsere App befasst sich damit, einer Person mit Diabetes zu helfen, die notwendige Insulindosis für eine Mahlzeit zu berechnen.
Dazu muss der User wissen, wie viele Einheiten Insulin er/sie benötigt für 10 Gramm Kohlenhydrate und auch 1 mmol/L, um einem zu 
hohen Blutzucker/tiefen Blutzuckerspiegel(Hypoglykämie) entgegen zu wirken.
Es ist möglich mehrere Profille mehrere User, oder auch den selben User zu hinterlegen. Der Grund dafür ist, dass eine 
Person, welche selbst Diabetes hat, und auch ein Kind hat, welches zu jung ist eine solche App zu bedienen, auch ein Profil für sein
Kind anlegen kann. Ebenfalls könnte ein User ein zweites Profil für sich selbst anlegen, bei dem eine andere Insulindosis berechnet
wird, da diese abhängig davon ist wie viel körperliche Anstrengung der User in der Zeit haben wird, in der das Insulin wirkt(ca. 2h lang).

# 2. Use-Case
Diese App soll es Diabetikern ermöglichen, auf einfache Art und Weise, die benötigte Insulindosis für eine selbst zusammengestellte
Mahlzeit, bestehend aus erfassten Produkten/Lebensmitteln, zu berechnen. Dies soll eine zuverlässige und komfortable Methode bieten 
diese Berechnung vorzunehmen und menschliche Fehler dabei zu minimieren. 

# 3. Umsetzung
Um eine solche App zu entwickeln, müssen gewisse Informationen über User und Produkte/Lebensmittel abgespeichert werden.
Informationen über den User werden mittels shared preferences abgespeichert, hingegen Produkte und Blutzuckermesswerte werden
mittels RoomDB in einer lokalen Datenbank hinterleget, wobei Blutzuckerwerte welche älter als 90 Tage alt sind wiedergelöscht werden.
Diese Werte werden auf den Home Screen für den aktuellen User in einem Grafen dargestellt, damit dieser den Verlauf nachverfolgen kann.
Produkte können manuell in die Datenbank eingefügt werden, oder via Barcodescanner über das Internet bezogen werden, und vor dem 
Einfügen noch angepasst werden, falls die bezogenen Nährwertinformationen nicht korrekt sind. 

# 4. Vorgehen der Berechnung der Inuslindosis
Bei der korrekten Berechnung der benötigten Insulindosis sind sich Ärzte(Diabetologen) nicht einig, und es gibt verschiedene Methoden,
diese zu Berechnen. Wir haben uns dafür enteschieden diese anhand der benötigten Insulindosis pro 10 Gramm Kohlenhydarte und 1mmol/L
Glucose zu berechnen, da aus der Erfahrung von Marc Federspiel(selbst Diabetiker) dies eine zuverlässige Methode ist, die es dem 
User ermöglicht flexibel zu bleiben in hinsicht auf wie viele Kohelnhydrate er/sie zu sich nehmen wird.

# 5. Formel zur Berechnung
Mit den angegebenen Werten ist es möglich die Insulindosis wie folgt zu berechnen:

$$Insulindosis = \frac{Insulindosis\frac{10g}{Kohlenhydrate}}{10} * Kohlenhydrate + Korrektur $$

Wobei die Korrektur wie folgt des aktuellen Blutzuckerspiegels Brechnet werden kann:

Im Falle eines zu hohen Blutzuckerspiegels:

$$Korrektur = +Insulindosis\frac{1mmol}{L} * (Blutzuckerspiegel - 5.5) $$

Im Falle eines zu tiefen Blutzuckerspiegels:

$$Korrektur = -Insulindosis\frac{1mmol}{L} * (5.5 - Blutzuckerspiegel) $$

Der Wert 5.5 wurde hier gewählt, da ein normaler Blutzuckerspiegel zwischen 4.0 und 7.0mmol/L liegt.
