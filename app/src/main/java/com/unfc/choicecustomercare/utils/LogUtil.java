/*
 *
 */
package com.unfc.choicecustomercare.utils;

import android.util.Log;

/**
 * Customize environment logger Turn log off when release app
 */
public class LogUtil {
	public static boolean DEBUG = true;

	/**
	 * @param message
	 *            The message you would like logged.
	 */
	public static void e(String message) {

		if (DEBUG) {

			try {

				String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
				String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
				String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
				int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
				Log.e(className + "." + methodName + " line : " + lineNumber, message);
			} catch (Exception ignored) {

			}
		}
	}
}
