# HttpClient

## HttpGet

```java
public TeamLinkResultModel doUserInfoByTeamLink(String authCode, String accessToken){

        TeamLinkResultModel result = new TeamLinkResultModel();
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            URIBuilder builder = new URIBuilder(userinfo_uri);
            builder.setParameter("xxxx", authCode);
            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.addHeader("X-token", accessToken);

            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != 200) {
                log.error("Get userinfo error");
                new Exception("Get userinfo error");
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // EntityUtils to get the response content
                String content =  EntityUtils.toString(entity);
                if (entity != null) {
                    result  = JSON.parseObject(content, TeamLinkResultModel.class);
                }
            }
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

```

## HttpPost

```java
public String doPostTeamLink(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(accesstoken_uri);

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("client_id",client_id));
        params.add(new BasicNameValuePair("scope",scope));
        httpPost.addHeader(authorizationTL, authorizationCode);
        StringBuilder accessToken = new StringBuilder();
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                log.error("Get accessToken error");
                new Exception("Get accessToken error");
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // EntityUtils to get the response content
                String content =  EntityUtils.toString(entity);
                if (entity != null) {
                    AccessTokenModel token = JSON.parseObject(content, AccessTokenModel.class);
                    accessToken.append(token.getAccess_token());
                }
            }
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken.toString();
    }
```

