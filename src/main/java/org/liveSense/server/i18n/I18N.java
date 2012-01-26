package org.liveSense.server.i18n;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.Messages;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class I18N {

	private static final Map<String, Object> cache = new HashMap<String, Object>();
	public static boolean useCache = true;

	public static void resetCache() {
		cache.clear();
	}
	
	public static <T> T create(Class<T> itf) throws IOException {
		return create(itf, (Locale)null, (ClassLoader)null);
	}

	public static <T> T create(Class<T> itf, Locale locale) throws IOException {
		return create(itf, locale, (ClassLoader)null);		
	}

	public static Locale createLocaleFromLang(String lang) {
		return createLocaleFromLang(lang, null);
	}

	public static Locale createLocaleFromLang(String lang, Locale loc) {
		
		String[] locs = null;
		if (lang != null) locs = lang.split("_");
		if (locs !=null && locs.length > 2) return new Locale(locs[0], locs[1], locs[2]);
		if (locs !=null && locs.length > 1) return new Locale(locs[0], locs[1]);
		if (locs !=null && locs.length > 0) return new Locale(locs[0]);
		if (loc != null) return loc;
		return Locale.getDefault();
		
	}

	public static <T> T create(Class<T> itf, Locale locale, ClassLoader classLoader) throws IOException {
		String locStr = null;
		if (locale != null) {
			locStr = locale.getLanguage();
			if (locale.getCountry() != null && !"".equals(locale.getCountry())) {
				locStr += "_"+locale.getCountry();
			}
			if (locale.getVariant() != null && !"".equals(locale.getVariant())) {
				locStr += "_"+locale.getVariant();
			}
		}
		return create(itf, locStr, classLoader);
	}

	public static <T> T create(Class<T> itf, String lang) throws IOException {
		return create(itf, lang, (ClassLoader)null);
	}
		
	@SuppressWarnings("unchecked")
	public static <T> T create(Class<T> itf, String lang, ClassLoader classLoader) throws IOException {
		return create(itf, lang, classLoader, null);
	}

	@SuppressWarnings("unchecked")
	public static <T> T create(Class<T> itf, String lang, ClassLoader classLoader, String prefix) throws IOException {
		Locale locale = createLocaleFromLang(lang);
		final String key = ( (prefix == null) ? "" : prefix + "#" ) + itf.getName() + ((lang == null) ? "" : ("_" + lang));
		if (useCache) {
			Object o = cache.get(key);
    		T msg = null;
    		try {
    			msg = (T) cache.get(key);
    		} catch (Throwable th) {
			}
    		if (msg == null) {
    			msg = createProxy(itf, locale.getLanguage(), locale.getCountry(), locale.getVariant(), classLoader);
    			cache.put(key, msg);
    		}
            return msg;
		} else {
		    return createProxy(itf, locale.getLanguage(), locale.getCountry(), locale.getVariant(), classLoader);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T createProxy(Class<T> itf, String lang, String country, String variant, ClassLoader classLoader) throws IOException {
	    InvocationHandler ih;
        if (GenericX.isA(itf, Constants.class)) {
            ih = new GenericConstants(itf, lang, country, variant, classLoader);
        } else if (GenericX.isA(itf, Messages.class)) {
            ih = new GenericMessages(itf, lang, country, variant, classLoader);
        } else {
            ih = new GenericMessages(itf, lang, country, variant, classLoader);        
        }
        //    throw new InvalidParameterException("Class " + itf.getName() + " is not a GWT i18n subclass");
        //}
        return (T) Proxy.newProxyInstance(itf.getClassLoader(), new Class[] { itf }, ih);
	}

}
