package com.unfc.choicecustomercare.api;

import com.unfc.choicecustomercare.utils.Constants;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;

/**
 * Hai Nguyen - 8/27/15.
 */
public class BaseApi {

	protected ApiInterface mInterface;
	protected RestAdapter mRestAdapter;
	public BaseApi(boolean hasHeader) {

		RestAdapter.Builder builder = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL)
				.setLog(new AndroidLog("BaseApi:")).setEndpoint(Constants.BASE_URL);
		if (hasHeader) {

			builder.setRequestInterceptor(new MyRequestInterceptor());
		}

		mRestAdapter = builder.build();
		mInterface = mRestAdapter.create(ApiInterface.class);
	}

	public ApiInterface getInterface() {
		return mInterface;
	}
}
