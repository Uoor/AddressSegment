package com.AddressSegment.logic.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public abstract class HttpRequestTemplate 
{
	private URL httpUrl;
	
	protected void setHttpUrl(String url) {
		try {
			httpUrl = new URL(url);
		} catch (MalformedURLException e) {
			System.err.println("Invalid Http Request Url Format.");
			throw new RuntimeException(e);
		}
	}
	
	private void setDefaultProperty (URLConnection httpConnection) {
		httpConnection.setRequestProperty("accept", "*/*");
		httpConnection.setRequestProperty("connection", "Keep-Alive");
		httpConnection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		httpConnection.setConnectTimeout(30000);
/*		try {
			httpConnection.setRequestMethod("POST");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			System.out.println("Set Request Method Failed.");
			e.printStackTrace();
		}*/
	}
	
	public void get(IHttpResponseHandler httpGetHandler) {
		InputStream responseStream = null;
		URLConnection httpConnection = null;
		//InputSource is = null;
		try {
			httpConnection = httpUrl.openConnection();
			setDefaultProperty(httpConnection);
			httpConnection.connect();
			responseStream = httpConnection.getInputStream();
			httpGetHandler.handleHttpResponse(
					httpConnection.getHeaderFields(), responseStream);
		} catch (IOException e) {
			//TODO
			e.printStackTrace();
		} finally {
			close(responseStream, httpConnection);
		}	
	}
	
	public void post(IHttpResponseHandler httpGetHandler, Map<String, String> urlParams) {
		InputStream responseStream = null;
		URLConnection httpConnection = null;
		//BufferedReader in = null;
		try {
			httpConnection = httpUrl.openConnection();
			setDefaultProperty(httpConnection);
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			PrintWriter outPrinter = new PrintWriter(httpConnection.getOutputStream());
			boolean first = true;
			for (Map.Entry<String, String> entry : urlParams.entrySet()) {
				if (!first) {
					outPrinter.append('&');
				}
				first = false;
				outPrinter.append(entry.getKey()).append('=').append(entry.getValue());
			}
			outPrinter.flush();
			responseStream = httpConnection.getInputStream();
			
			/******************************
			in = new BufferedReader(
                    new InputStreamReader(httpConnection.getInputStream()));
			String line, result = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
            //httpGetHandler.handleHttpResponse(
        	//		httpConnection.getHeaderFields(), is);
			/******************************/

			httpGetHandler.handleHttpResponse(
					httpConnection.getHeaderFields(), responseStream);
			outPrinter.close();
		} catch (IOException e) {
			//TODO
			e.printStackTrace();
		} finally {
			close(responseStream, httpConnection);
		}
	}
	
	private void close(InputStream responseStream, URLConnection httpConnection) {
		if (null != responseStream) {
			try {
				responseStream.close();
			} catch (IOException e) {
				System.err.println("Close HttpResponse Stream Failed.");
			} finally {
				httpConnection = null;
				httpUrl = null;
			}
		}
	}

}
