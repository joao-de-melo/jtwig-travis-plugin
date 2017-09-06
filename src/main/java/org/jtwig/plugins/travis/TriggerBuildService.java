package org.jtwig.plugins.travis;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.jtwig.plugins.util.UrlBuilder;

public class TriggerBuildService {
    private final String baseUrl;
    private final HttpClient httpClient;

    public TriggerBuildService(String baseUrl, HttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
    }

    public void trigger(TriggerBuildRequest request) {
        HttpResponse response;
        try {
            StringEntity entity = new StringEntity(buildBody(request));
            String url = UrlBuilder.url(baseUrl)
                    .addToPath("repo")
                    .addToPathEscaped(request.getProject())
                    .addToPath("requests")
                    .build();
            HttpPost post = new HttpPost(url);
            post.addHeader("Travis-API-Version", "3");
            post.addHeader("Accept", "application/json");
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Authorization", String.format("token %s", request.getToken()));

            post.setEntity(entity);

            response = httpClient.execute(post);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Cannot trigger upstream project %s build", request.getProject()));
        }

        if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() >= 300)
            throw new RuntimeException(String.format("Cannot trigger upstream project %s build", request.getProject()));
    }

    private String buildBody(TriggerBuildRequest request) {
        return new JSONObject()
                .put("request",
                        new JSONObject()
                                .put("message", request.getMessage())
                                .put("config", new JSONObject()
                                        .put("env", new JSONObject()
                                                .put("TRIGGER_ORIGIN", request.getParentProject())
                                        )
                                )
                ).toString();
    }
}
