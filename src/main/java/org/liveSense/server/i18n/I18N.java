package org.liveSense.server.i18n;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.LocaleUtils;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.Messages;

public class I18N {

	private static final Map<String,WeakReference<Object>> cache = new ConcurrentHashMap<String, WeakReference<Object>>();
	private static final Map<String,WeakReference<GenericX>> cacheHandlers = new ConcurrentHashMap<String, WeakReference<GenericX>>();

	public static boolean useCache = true;

	public static void resetCache() {
		cache.clear();
	}
	
	public static <T> T create(Class<T> itf) throws IOException {
		return create(itf, (Locale)null, (ClassLoader)null);
	}

	public static <T> T create(Class<T> itf, ResourceBundle bundle) throws IOException {
		return create(itf, (Locale)null, (ClassLoader)null, bundle);
	}

	public static <T> T create(Class<T> itf, Locale locale) throws IOException {
		return create(itf, locale, (ClassLoader)null);		
	}

	public static <T> T create(Class<T> itf, Locale locale, ResourceBundle bundle) throws IOException {
		return create(itf, locale, (ClassLoader)null, bundle);		
	}

	public static Locale createLocaleFromLang(String lang) {
		return createLocaleFromLang(lang, null);
	}

	public static Locale createLocaleFromLang(String lang, Locale loc) {
		Locale ret = LocaleUtils.toLocale(lang);
		if (ret == null) 
			ret = Locale.getDefault();
		return ret;
	}

	public static <T> T create(Class<T> itf, Locale locale, ClassLoader classLoader) throws IOException {
		return create(itf, locale, classLoader, (ResourceBundle)null);
	}

	
	public static <T> T create(Class<T> itf, Locale locale, ClassLoader classLoader, ResourceBundle bundle) throws IOException {
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
		return create(itf, locStr, classLoader, bundle);
	}

	public static <T> T create(Class<T> itf, String lang) throws IOException {
		return create(itf, lang, (ClassLoader)null);
	}

	public static <T> T create(Class<T> itf, String lang, ResourceBundle bundle) throws IOException {
		return create(itf, lang, (ClassLoader)null, bundle);
	}

	@SuppressWarnings("unchecked")
	public static <T> T create(Class<T> itf, String lang, ClassLoader classLoader) throws IOException {
		return create(itf, lang, classLoader, null);
	}

	@SuppressWarnings("unchecked")
	public static <T> T create(Class<T> itf, String lang, ClassLoader classLoader, ResourceBundle bundle) throws IOException {
		return create(itf, lang, classLoader, null, bundle);
	}

	@SuppressWarnings("unchecked")
	public static <T> T create(Class<T> itf, String lang, ClassLoader classLoader, String prefix, ResourceBundle bundle) throws IOException {
		Locale locale = createLocaleFromLang(lang);
		final String key = ( (prefix == null) ? "" : prefix + "#" ) + itf.getName() + ((lang == null) ? "" : ("_" + lang));
		if (useCache) {
//			Object o = cache.get(key).get();
    		T msg = null;
    		try {
    			msg = (T) cache.get(key).get();
    		} catch (Throwable th) {
			}
    		if (msg == null) {
    			msg = createProxy(itf, locale.getLanguage(), locale.getCountry(), locale.getVariant(), classLoader, bundle);
    			cache.put(key, new WeakReference(msg));
    		}
            return msg;
		} else {
		    return createProxy(itf, locale.getLanguage(), locale.getCountry(), locale.getVariant(), classLoader, bundle);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T createProxy(Class<T> itf, String lang, String country, String variant, ClassLoader classLoader, ResourceBundle bundle) throws IOException {
	    InvocationHandler ih;
        if (GenericX.isA(itf, Constants.class)) {
            ih = new GenericConstants(itf, lang, country, variant, classLoader, bundle);
        } else if (GenericX.isA(itf, Messages.class)) {
            ih = new GenericMessages(itf, lang, country, variant, classLoader, bundle);
        } else {
            ih = new GenericMessages(itf, lang, country, variant, classLoader, bundle);
        }
        
        //    throw new InvalidParameterException("Class " + itf.getName() + " is not a GWT i18n subclass");
        //}
        return (T) Proxy.newProxyInstance(itf.getClassLoader(), new Class[] { itf }, ih);
	}

	
    private static Map<Object, Object> convertResourceBundleToProperties(ResourceBundle resource) {
        Map<Object, Object> properties = new HashMap<Object, Object>();

        Enumeration<String> keys = resource.getKeys();
        while (keys.hasMoreElements()) {
          String key = keys.nextElement();
          properties.put(key, resource.getString(key));
        }

        return properties;
      }

}
