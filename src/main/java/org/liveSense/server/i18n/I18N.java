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

	public final static Map<String, Object> cache = new HashMap<String, Object>();
	public static boolean useCache = true;

	public static <T> T create(Class<T> itf) throws IOException {
		return create(itf, (Locale)null, (ClassLoader)null);
	}

	public static <T> T create(Class<T> itf, Locale locale) throws IOException {
		return create(itf, locale, (ClassLoader)null);		
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
		final String key = itf.getName() + (lang == null ? "" : ("_" + lang));
		if (useCache) {
    		T msg = (T) cache.get(key);
    		if (msg == null) {
    			msg = createProxy(itf, lang, classLoader);
    			cache.put(key, msg);
    		}
            return msg;
		} else {
		    return createProxy(itf, lang, classLoader);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T createProxy(Class<T> itf, String lang, ClassLoader classLoader) throws IOException {
	    InvocationHandler ih;
        if (GenericX.isA(itf, Constants.class)) {
            ih = new GenericConstants(itf, lang, classLoader);
        } else if (GenericX.isA(itf, Messages.class)) {
            ih = new GenericMessages(itf, lang, classLoader);
        } else {
            throw new InvalidParameterException("Class " + itf.getName() + " is not a GWT i18n subclass");
        }
        return (T) Proxy.newProxyInstance(itf.getClassLoader(), new Class[] { itf }, ih);
	}
}
