/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exercise.tiger.mylearnapplication.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

public class BuildUtil {

    /****sdk>=2.2******/
	public static boolean hasFroyo() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}
	/****sdk>=2.3******/
	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	/****sdk>=2.3.3******/
	public static boolean hasGingerbreadMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1;
	}

	/****sdk>=3.0******/
	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	/****sdk>=3.1******/
	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	/****sdk>=4.0******/
	public static boolean hasICS() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}

	/****sdk>=4.1******/
	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= 16/*(Build.VERSION_CODES.JELLY_BEAN)*/;
	}
	/****sdk>=4.2******/
	public static boolean hasJellyBeanMR1() {
		return Build.VERSION.SDK_INT >= 17/*(Build.VERSION_CODES.JELLY_BEAN_MR1)*/;
	}
	
	public static boolean hasJELLY_BEAN_MR2(){
	    return Build.VERSION.SDK_INT >= 18;
	}

    public static boolean hasKITKAT() {
        return Build.VERSION.SDK_INT >= 19;
    }

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean isHoneycombTablet(Context context) {
		return hasHoneycomb() && isTablet(context);
	}

	public static boolean isGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
				&& Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1;
	}

	/**
	 * M及以后的版本需要在使用权限时获取权限
	 * @return
	 */
	public static boolean hasMarshMallow(){
		return Build.VERSION.SDK_INT>=Build.VERSION_CODES.M;
	}

	/**
	 * N及以后的版本需要从FileProvider获取Uri
	 * @return
	 */
	public static boolean hasNougat(){
		return Build.VERSION.SDK_INT>=Build.VERSION_CODES.N;
	}
}
