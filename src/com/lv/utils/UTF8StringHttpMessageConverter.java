package com.lv.utils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.*;

import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 解决由@ResponseBody
 * 带来的中文json数据乱码的问题
 * 
 * @author lvliang
 * 
 */
public class UTF8StringHttpMessageConverter extends StringHttpMessageConverter {

	private static final MediaType utf8 = new MediaType("text", "plain",
			Charset.forName("UTF-8"));
	private boolean writeAcceptCharset = true;

	protected MediaType getDefaultContentType(String dumy) {
		return utf8;
	}

	protected List<Charset> getAcceptedCharsets() {
		return Arrays.asList(utf8.getCharSet());
	}

	protected void writeInternal(String s, HttpOutputMessage outputMessage)
			throws IOException {
		if (this.writeAcceptCharset) {
			outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
		}
		Charset charset = utf8.getCharSet();
		FileCopyUtils.copy(s,new OutputStreamWriter(outputMessage.getBody(), charset));
	}

	public boolean isWriteAcceptCharset() {
		return writeAcceptCharset;
	}

	public void setWriteAcceptCharset(boolean writeAcceptCharset) {
		this.writeAcceptCharset = writeAcceptCharset;
	}

}
