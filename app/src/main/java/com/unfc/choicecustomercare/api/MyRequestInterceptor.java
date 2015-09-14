package com.unfc.choicecustomercare.api;

import com.unfc.choicecustomercare.utils.Constants;
import com.unfc.choicecustomercare.utils.CustomPreferences;

import retrofit.RequestInterceptor;

/**
 * Hai Nguyen - 8/27/15.
 */
public class MyRequestInterceptor implements RequestInterceptor {

	@Override
	public void intercept(RequestFacade requestFacade) {

		String accessToken = CustomPreferences.getPreferences(Constants.PREF_USER_TOKEN, "");
		requestFacade.addHeader("Authorization", "Bearer " + accessToken);
	}
}
