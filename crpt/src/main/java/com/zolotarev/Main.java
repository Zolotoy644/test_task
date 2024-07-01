package com.zolotarev;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        CrptApi api = new CrptApi(TimeUnit.SECONDS, 1000);
        api.sendMessage(api.makeJsonDoc(), "document");

    }
}