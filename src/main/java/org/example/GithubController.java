package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

@RestController
public class GithubController {

    @GetMapping("/repositories/{username}")
    public ResponseEntity<String> getRepositories(@PathVariable String username) {
        String url = "https://api.github.com/users/" + username + "/repos";
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "application/json");
        try {
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 404) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", statusCode);
                jsonResponse.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse.toString());
            }
            JSONArray repositories = new JSONArray(EntityUtils.toString(response.getEntity()));
            JSONArray nonForkRepositories = new JSONArray();
            for (int i = 0; i < repositories.length(); i++) {
                JSONObject repo = repositories.getJSONObject(i);
                if (!repo.getBoolean("fork")) {
                    JSONObject repoInfo = new JSONObject();
                    repoInfo.put("Repository Name", repo.getString("name"));
                    repoInfo.put("Owner Login", repo.getJSONObject("owner").getString("login"));
                    JSONArray branches = getBranches(repo.getString("name"), repo.getJSONObject("owner").getString("login"));
                    repoInfo.put("Branches", branches);
                    nonForkRepositories.put(repoInfo);
                }
            }
            return ResponseEntity.ok().body(nonForkRepositories.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + e.getMessage());
        }
    }

    private JSONArray getBranches(String repoName, String ownerLogin) throws Exception {
        String url = "https://api.github.com/repos/" + ownerLogin + "/" + repoName + "/branches";
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "application/json");
        HttpResponse response = httpClient.execute(request);
        JSONArray branches = new JSONArray();
        if (response.getStatusLine().getStatusCode() == 200) {
            JSONArray branchesInfo = new JSONArray(EntityUtils.toString(response.getEntity()));
            for (int i = 0; i < branchesInfo.length(); i++) {
                JSONObject branchInfo = branchesInfo.getJSONObject(i);
                JSONObject branch = new JSONObject();
                branch.put("Branch Name", branchInfo.getString("name"));
                branch.put("Last Commit SHA", branchInfo.getJSONObject("commit").getString("sha"));
                branches.put(branch);
            }
        }
        return branches;
    }
}
