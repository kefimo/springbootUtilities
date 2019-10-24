package com.kefi.controller;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.RequestLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class TesttController {
	
	@GetMapping("/hello2")
    public ResponseEntity<String> hello() {
		  try (CloseableHttpClient httpclient = createAcceptSelfSignedCertificateClient()) {
	            HttpGet httpget = new HttpGet("https://example.com");
	            //System.out.println("Executing request " + );
	            RequestLine response = httpget.getRequestLine();
	            httpclient.execute(httpget);
	            System.out.println("----------------------------------------");
	            return new ResponseEntity<String>(response.toString(),HttpStatus.OK);
	        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException e) {
	            throw new RuntimeException(e);
	        }
				
    }
	
	 private CloseableHttpClient createAcceptSelfSignedCertificateClient()
	            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

	        // use the TrustSelfSignedStrategy to allow Self Signed Certificates
	        SSLContext sslContext = SSLContextBuilder
	                .create()
	                .loadTrustMaterial(new TrustSelfSignedStrategy())
	                .build();

	        // we can optionally disable hostname verification. 
	        // if you don't want to further weaken the security, you don't have to include this.
	        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
	        
	        // create an SSL Socket Factory to use the SSLContext with the trust self signed certificate strategy
	        // and allow all hosts verifier.
	        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);
	        
	        // finally create the HttpClient using HttpClient factory methods and assign the ssl socket factory
	        return HttpClients
	                .custom()
	                .setSSLSocketFactory(connectionFactory)
	                .build();
	    }
}