package com.testshop.webapp.utility;

public class JsonData
{
    public static String GetProdData()
    {
        String JsonData =
                "{\n" +
                        "  \"idProdotto\": 1,\n" +
                        "  \"nome\": \"Anello Diamante Elegance\",\n" +
                        "  \"descrizione\": \"Anello in oro bianco 18kt con diamante taglio brillante\",\n" +
                        "  \"prezzo\": 2499.99,\n" +
                        "  \"quantita_in_magazzino\": 10,\n" +
                        "  \"materiale\": \"Oro bianco\",\n" +
                        "  \"tipo_gemma\": \"Diamante\",\n" +
                        "  \"peso\": 5.2,\n" +
                        "  \"carati\": 1.2,\n" +
                        "  \"dettagli_ordine\": [],\n" +
                        "  \"fornitori\": null,\n" +
                        "  \"categorie\": null\n" +
                        "}";

        return JsonData;
    }

    public static String GetTestProdData()
    {
        String JsonData =
                "{\n" +
                        "  \"idProdotto\": 5,\n" +
                        "  \"nome\": \"Ciondolo Cuore di Zaffiro\",\n" +
                        "  \"descrizione\": \"Ciondolo con zaffiro blu e incastonatura in platino\",\n" +
                        "  \"prezzo\": 1599.0,\n" +
                        "  \"quantita_in_magazzino\": 4,\n" +
                        "  \"materiale\": \"Platino\",\n" +
                        "  \"tipo_gemma\": \"Zaffiro\",\n" +
                        "  \"peso\": 4.8,\n" +
                        "  \"carati\": 1.0,\n" +
                        "  \"dettagli_ordine\": [],\n" +
                        "  \"fornitori\": null,\n" +
                        "  \"categorie\": null\n" +
                        "}";

        return JsonData;
    }

}
