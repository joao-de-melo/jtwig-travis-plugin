package org.jtwig.plugins.travis;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.jtwig.plugins.util.UrlBuilder;

import java.io.IOException;

public class TriggerBuildService {
    private final String baseUrl;
    private final String githubToken;
    private final HttpClient httpClient;

    public TriggerBuildService(String baseUrl, String githubToken, HttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.githubToken = githubToken;
        this.httpClient = httpClient;
    }

    public String getToken () {
        HttpPost httpPost = new HttpPost(String.format("%s/auth/github", baseUrl));
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Travis");

        String content = new JSONObject()
                .put("github_token", githubToken)
                .toString();
        httpPost.setEntity(new ByteArrayEntity(content.getBytes()));
        try {
            HttpResponse response = httpClient.execute(httpPost);

            String result = IOUtils.toString(response.getEntity().getContent());
            try {
                return new JSONObject(result).getString("access_token");
            } catch (Exception e) {
                throw new RuntimeException("Unable to retrieve from "+result+content, e);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot get auth token", e);
        }
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
            post.addHeader("Authorization", String.format("token %s", getToken()));

            post.setEntity(entity);

            response = httpClient.execute(post);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Cannot trigger upstream project %s build", request.getProject()), e);
        }

        if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() >= 300)
            throw new RuntimeException(String.format("Cannot trigger upstream project %s build. Response with status: %s", request.getProject(), response));
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
