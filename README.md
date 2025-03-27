# lab4_GUI_JP
Lab4 - GUI (Swing, JavaFX)

## Treść programowa

Zaprojektowanie i implementacja aplikacji z graficznym interfejsem użytkownika (z wykorzystaniem podstawowych komponentów do budowy formularzy).

# Cel zajęć

Celem zajęć jest doskonalenie umiejętności programowania obiektowego oraz opanowanie technik budowania aplikacji oferujących graficzny interfejs użytkownika.

<aside>
💡 Od wersji Java SE 9 pakiet `javax.swing` został przeniesiony do modułu `java.desktop`.

</aside>

<aside>
💡 JavaFX została usunięta z oficjalnego SDK od wersji 11.

</aside>

Domyślnie implementacja może wykorzystywać:

- pakiet `javax.swing` (Java SE ≤ 8),
- moduł `java.desktop` (JavaSE  ≥ 9) - zalecane,
- JavaFX (Java SE ≤ 11),
- Zewnętrzna [biblioteka OpenJFX](https://openjfx.io/)

Niezależnie od wykorzystanej technologi aplikacja powinna być umieszczona w pojedynczym i wykonywalnym pliku JAR. Tworząc aplikację należy w szczególności przemyśleć kwestię usuwania obiektów (jak powinny zachować się powiązane encje) oraz kwestię zapisywania danych. Dodatkowo potrzebna okaże się umiejętność dołączenia zewnętrznych plików JAR (np. do tworzenia PDF lub generowania wizualizacji).

<aside>
💡

**Wzorzec MVC (Model-View-Controller)**

Wzorzec MVC ułatwia zarządzanie złożonością aplikacji poprzez oddzielenie logiki biznesowej od warstwy prezentacji, co zwiększa modularność, testowalność i możliwość łatwej modyfikacji poszczególnych komponentów bez wpływu na pozostałe. W Swingu pomaga to w reagowaniu na zdarzenia użytkownika bez obciążania widoku logiką aplikacyjną.

W kontekście aplikacji GUI to sposób organizacji kodu, który rozdziela odpowiedzialności między trzy główne komponenty:

1. **Model**: Reprezentuje dane i logikę aplikacji, niezależną od interfejsu użytkownika.
2. **View**: Odpowiada za prezentację danych użytkownikowi.
3. **Controller**: Zarządza interakcjami użytkownika, aktualizuje Model i zmienia widoki.

➡️ Szablon aplikacji MVC: https://replit.com/@nkozlowski/PWR-Swing-MVC-Template?v=1

</aside>

<aside>
💡 W przypadku obu grup należy przemyśleć relacje między obiektami. W szczególności co powinno wydarzyć się w momencie usunięcia powiązanego obiektu (np. składnika posiłku który jest zawarty w diecie).
</aside>

## Grupa A

Celem projektu jest stworzenie aplikacji typu **Fitness Tracker** posiadających następujące funkcjonalności:

- użytkownik może tworzyć, edytować i usuwać treningi wybierając grupując odpowiednie ćwiczenia, trening powinien być odpowiednio sparametryzowany (ilość *obwodów*, *powtórzeń*, itp),
- użytkownik może logować, edytować i usuwać poszczególne sesje treningowe (treningi rozszerzone o zmienną jak np. *obciążenie* czy *czas*),
- użytkownik może tworzyć, edytować i usuwać swoje cele treningowe (w odniesieniu do konkretnego ćwiczenia), cel treningowy powinen być także określany przez czas (deadline),
- użytkownik może wizualizować (na podstawie wykresu) swoje postępy w docelowym ćwiczeniu w danym okresie czasowym.
  
---

![image](https://github.com/user-attachments/assets/de0f23b1-1dd1-48e6-9a36-7130b5ab97f8)

