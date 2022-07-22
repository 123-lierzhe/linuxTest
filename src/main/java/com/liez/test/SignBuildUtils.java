package com.liez.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public class SignBuildUtils {
	public static String signBuild(String input, int length) {
		String sign = null;
		if (length == 0) {
			sign = MD5Util.encodeToMD5(input);
		} else {
			sign = MD5Util.encodeToMD5(input).substring(0, length);
		}
		return sign;
	}

	public static String sign(RequestResponseBody request, String signKey) {
		return sign(request.getHead(), request.getBody(), signKey);
	}

	public static String sign(Head head, String body, String signKey) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(TyptConstant.PARTNERNO, head.getPartnerNo());
		params.put(TyptConstant.REQUESTID, head.getRequestId());
		params.put(TyptConstant.TIMESTAMP, head.getTimestamp());
		params.put(TyptConstant.VERSION, head.getVersion());
		params.put(TyptConstant.SIGNKEY, signKey);
		params.put(TyptConstant.BODY, body);
		return signBuild(getSignContent(params), 0);
	}

	public static String getSignContent(Map<String, Object> sortedParam) {
		LinkedHashMap<Object, Object> map = new LinkedHashMap<Object, Object>();
		List<String> keys = new ArrayList<String>(sortedParam.keySet());
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = sortedParam.get(key).toString();
			map.put(key, value);
		}
		return map.toString();
	}
}
