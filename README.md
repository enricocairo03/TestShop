# 🛒 TestShop - Backend Java Spring Boot

**TestShop** è un'applicazione backend e-commerce realizzata in **Java Spring Boot**, progettata per la gestione di prodotti, clienti, fornitori, ordini e pagamenti.  
Il progetto utilizza **PostgreSQL** come database e può essere eseguito in locale o containerizzato tramite **Docker**.  
Include API RESTful complete e una suite di test automatizzati per la validazione del sistema.

## 🚀 Tecnologie Utilizzate

- Java 17
- Spring Boot 3
  - Spring Web
  - Spring Data JPA
  - Spring Validation
- PostgreSQL
- Docker / Docker Compose
- JUnit 5
- MockMvc
- Maven
- Lombok

## 📦 Entità Gestite

Le entità JPA si trovano nel package `com.testshop.webapp.entities`:

- `Prodotti`
- `Categorie`
- `Fornitori`
- `Clienti`
- `Ordini`
- `Dettagli_ordine`
- `Pagamenti`

Tutte le entità sono collegate tra loro tramite relazioni `@OneToMany`, `@ManyToOne` ecc., rispettando il modello ER.

## 🧪 Test Automatizzati

I test si trovano nei package:

- `ControllerTests`:
  - `InsertProdTest.java` → test di inserimento, modifica, cancellazione e validazione errori.
  - `SelectProdTest.java` → test di ricerca prodotti per ID, codice, descrizione, EAN.
- `RepositoryTests`:
  - `ProdottiRepositoryTest.java` → test CRUD a basso livello sul repository JPA.

I test usano `MockMvc` per simulare le richieste HTTP e validare i risultati.

 ## 🔐 Sicurezza
- L'applicazione implementa un sistema di sicurezza tramite Spring Security con autenticazione e autorizzazione basate su ruoli.

- Sono definiti due ruoli principali:

- USER: può visualizzare le API di consultazione (es. ricerca prodotti).

- ADMIN: ha accesso completo, inclusa la possibilità di modificare, inserire o cancellare dati tramite API protette.

- Le API di modifica (POST, PUT, DELETE) sono protette e accessibili solo agli utenti con ruolo ADMIN.
- Le API di sola lettura (GET) sono protette per utenti autenticati con ruolo USER o superiore.



## 🔗 Esempi di Endpoint

```http
GET    /api/prodotti/cerca/codice/{id}
GET    /api/prodotti/descrizione/{desc}
POST   /api/prodotti/inserisci
PUT    /api/prodotti/modifica
DELETE /api/prodotti/elimina/{id}



