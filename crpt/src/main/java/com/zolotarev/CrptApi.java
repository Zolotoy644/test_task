package com.zolotarev;

import com.google.gson.Gson;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

public class CrptApi {
    private final Gson gson;
    private final RateLimiter rateLimiter;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.rateLimiter = RateLimiter.create((double) requestLimit / (timeUnit.toSeconds(1) / 1000.0));
    }

    public void sendMessage(JsonObject document, String description) throws IOException, InterruptedException {
        rateLimiter.acquire();
        String jsonString = gson.toJson(document);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://ismp.crpt.ru/api/v3/lk/documents/create"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        System.out.println(response.statusCode());
    }

    // Сделал доп метод для создания документа JSON указанного вида.
    // В задании не совсем понятно объект подается в метод уже готовый или мы его должны формировать
    // в методе написаны случайные значения, если что можно поменять
    public JsonObject makeJsonDoc() {
        // Создаем основной JSON объект
        JsonObject mainObject = new JsonObject();

        // Добавляем описание
        JsonObject description = new JsonObject();
        description.addProperty("participantInn", "1234567890");
        mainObject.add("description", description);

        // Добавляем остальные поля
        mainObject.addProperty("doc_id", "ABC123");
        mainObject.addProperty("doc_status", "active");
        mainObject.addProperty("doc_type", "LP_INTRODUCE_GOODS");
        mainObject.addProperty("importRequest", true);
        mainObject.addProperty("owner_inn", "0987654321");
        mainObject.addProperty("participant_inn", "1234567890");
        mainObject.addProperty("producer_inn", "1122334455");
        mainObject.addProperty("production_date", "2020-01-23");
        mainObject.addProperty("production_type", "type1");

        // Создаем массив продуктов
        JsonArray products = new JsonArray();

        // Создаем продукт
        JsonObject product = new JsonObject();
        product.addProperty("certificate_document", "Cert123");
        product.addProperty("certificate_document_date", "2020-01-23");
        product.addProperty("certificate_document_number", "CertNum123");
        product.addProperty("owner_inn", "0987654321");
        product.addProperty("producer_inn", "1122334455");
        product.addProperty("production_date", "2020-01-23");
        product.addProperty("tnved_code", "010203");
        product.addProperty("uit_code", "UIT123");
        product.addProperty("uitu_code", "UITU123");

        // Добавляем продукт в массив
        products.add(product);

        // Добавляем массив продуктов в основной объект
        mainObject.add("products", products);

        // Добавляем дату регистрации и номер
        mainObject.addProperty("reg_date", "2020-01-23");
        mainObject.addProperty("reg_number", "RegNum123");

        return mainObject;
    }

}

