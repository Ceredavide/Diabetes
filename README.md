# Mobile Programming Projekt - Diabetes
Davide Ceresa, Marc Federspiel

# 1 Einführung
## 1.1 Überblick
Diese App ist für Diabetiker entwickelt worden und ermöglicht es, Produkte manuell oder über Barcode zu erfassen. 
Züsatzlich können weiter Profile erstellt werden, um diese App auch für andere Personen verwenden zu können.
Für jedes Produkt werden der Name und die Kohlenhydrate pro 100 Gramm gespeichert. Der Benutzer kann Mahlzeiten zusammenstellen, indem er die Menge der verwendeten Produkte angibt. Die App berechnet die benötigte Insulindosis und speichert diese sowie die Produktinformationen in einer Room-Datenbank.

## 1.2 Ziele
Einfaches Erfassen von Produkten und deren Nährwertangaben.
Zusammenstellen von Mahlzeiten und Berechnung der benötigten Insulindosis.
Speichern und Verwalten von Produkten und Insulindosen in einer Room-Datenbank.
Zielgruppe

# 2 Nutzung
## 2.1 Produkte Hinzufügen
Navigieren Sie zum Abschnitt "Products" und geben Sie den Produktnamen und die Kohlenhydrate pro 100 Gramm ein.
Über Barcode: Nutzen Sie die Barcode-Scanner-Funktion, um Produktinformationen zu erfassen.

## 2.2 Mahlzeiten zusammenstellen
Fügen Sie die gewünschten Produkte zur Mahlzeit hinzu und geben Sie die Menge in Gramm an.
Die App berechnet automatisch die benötigte Insulindosis.

# 3 Funktionen
# 3.1 Produkte erfassen
Manuell: 
Der Benutzer gibt Produktname und Kohlenhydrate pro 100 Gramm ein.
Über Barcode: 
Benutzer scannt Barcode und die App erfasst automatisch die Produktinformationen, und
anschliessen hat der User die möglichkeit, diese noch anzupassen.
# 3.2 Mahlzeiten zusammenstellen
Der Benutzer fügt Produkte zur Mahlzeit hinzu und gibt die Menge in Gramm an.
Die App berechnet die Insulindosis basierend auf den eingegebenen Werten für 10 Gramm Kohlenhydrate,
1 mmol/L Glucose und dem aktuellen Blutzucker.

# 4 Persistenz
Berechnete Insulindosen ung hinzugefügte Produkte werden in der Room-Datenbank gespeichert.
Die Daten können bei Bedarf abgerufen und angezeigt werden.
Die Informationen über die User werden in den Shared-Preferences hinterlegt.

# 5. Formel zur Berechnung
Mit den angegebenen Werten ist es möglich die Insulindosis wie folgt zu berechnen:

$$Insulindosis = \frac{Insulindosis\frac{10g}{Kohlenhydrate}}{10} * Kohlenhydrate + Korrektur $$

Wobei die Korrektur wie folgt des aktuellen Blutzuckerspiegels Brechnet werden kann:

Im Falle eines zu hohen Blutzuckerspiegels:

$$Korrektur = +Insulindosis\frac{1mmol}{L} * (Blutzuckerspiegel - 5.5) $$

Im Falle eines zu tiefen Blutzuckerspiegels:

$$Korrektur = -Insulindosis\frac{1mmol}{L} * (5.5 - Blutzuckerspiegel) $$

Der Wert 5.5 wurde hier gewählt, da ein normaler Blutzuckerspiegel zwischen 4.0 und 7.0mmol/L liegt.
