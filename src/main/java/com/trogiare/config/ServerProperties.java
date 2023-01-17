package com.trogiare.config;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;


@Component
@ConfigurationProperties(prefix = "server.properties")
public class ServerProperties {

    String scheme;
    String host;
    int port;
    String path;

    public URI getURI(String urlPath, String query) throws URISyntaxException {
        URI uri = new URI(scheme, null, host, port, path, null, null);
        uri = UriComponentsBuilder.fromUri(uri).path(urlPath).query(query).build(true).toUri();

        return uri;
    }

    public String getURIPath(String urlPath, String query) throws URISyntaxException {
        String url = this.getURI(urlPath, query).toString();
        url = url.replace(":80/", "/").replace(":443/",	"/");
        return url;
    }

    public String getURIPath(String urlPath, Map<String, String> query) throws URISyntaxException {
        StringBuffer buff = new StringBuffer();
        if (query != null) {
            for (Map.Entry<String, String> en : query.entrySet()) {
                if (buff.length() > 0) {
                    buff.append("&");
                }

                buff.append(en.getKey());
                buff.append("=");
                try {
                    buff.append(URLEncoder.encode(en.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
        buff.trimToSize();
        return this.getURIPath(urlPath, buff.toString()).toString();
    }

    public String getScheme() {
        return scheme;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
