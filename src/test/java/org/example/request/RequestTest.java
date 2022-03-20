package org.example.request;

import org.junit.Test;

import java.io.IOException;

public class RequestTest {


    @Test
    public void build() throws IOException {

        RequestBuilder builder = new RequestBuilder();
        Request request = builder.getInstance();

        bui

        System.out.println(request.toString());
        System.out.println(request.send());
    }

}
