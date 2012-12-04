package org.liveSense.server.i18n.service.I18nService;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.LocaleUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.i18n.ResourceBundleProvider;
import org.liveSense.core.BundleProxyClassLoader;
import org.liveSense.core.service.OSGIClassLoaderManager;
import org.liveSense.server.i18n.CompositeProxyResourceBundle;
import org.liveSense.server.i18n.I18N;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate=true)
@Service
public class I18nServiceImpl implements I18nService {

	Logger log = LoggerFactory.getLogger(I18nServiceImpl.class);

	@Reference(cardinality=ReferenceCardinality.MANDATORY_UNARY, policy=ReferencePolicy.DYNAMIC)
	private OSGIClassLoaderManager dynamicClassLoaderManager;

	@Reference(cardinality=ReferenceCardinality.OPTIONAL_UNARY, policy=ReferencePolicy.DYNAMIC)
	ResourceBundleProvider slingI18nService;

	BundleContext context = null;
	ClassLoader classLoader = I18nServiceImpl.class.getClassLoader();
	
	Map<String, ClassLoader> bundleResourceCaches = new ConcurrentHashMap<String, ClassLoader>();
	
	private ClassLoader getClassLoader(String resourceName, ClassLoader classLoader) {
//		if (classLoader != null) {
//			return classLoader;
//		} else {
//			if (bundleResourceCaches.containsKey(resourceName)) {
//				return bundleResourceCaches.get(resourceName);
//			}
		return this.classLoader;
	}
	
	@Activate
	protected void activate(BundleContext context) {
		I18N.resetCache();
		this.context = context;
		this.classLoader = dynamicClassLoaderManager.getPackageAdminClassLoader(context);
	}

	@Override
	public <T> T create(Class<T> itf) throws IOException {
		return I18N.create(itf, (Locale)null, getClassLoader(itf.getName(), null), getResourceBundleInternal(itf.getName(), getLocale(null), getClassLoader(itf.getName(), null)));
	}

	@Override
	public <T> T create(Class<T> itf, Locale locale) throws IOException {
		return I18N.create(itf, locale, getClassLoader(itf.getName(), null), getResourceBundleInternal(itf.getName(), getLocale(locale), getClassLoader(itf.getName(), null)));
	}

	@Override
	public <T> T create(Class<T> itf, Locale locale, ClassLoader classLoader) throws IOException {
		return I18N.create(itf, locale, classLoader, getResourceBundleInternal(itf.getName(), getLocale(locale), getClassLoader(itf.getName(), classLoader)));
	}

	@Override
	public <T> T create(Class<T> itf, String lang) throws IOException {
		return I18N.create(itf, lang, getResourceBundleInternal(itf.getName(), getLocale(LocaleUtils.toLocale(lang)), getClassLoader(itf.getName(), null)));
	}

	@Override
	public <T> T create(Class<T> itf, String lang, ClassLoader classLoader) throws IOException {
		return I18N.create(itf, lang, classLoader, getResourceBundleInternal(itf.getName(), getLocale(LocaleUtils.toLocale(lang)), getClassLoader(itf.getName(), classLoader)));
	}


	private Locale getLocale(Locale locale) {
		if (locale != null) {
			return locale;
		} else 
			return Locale.getDefault();
	}

	@SuppressWarnings("unchecked")
	private <T> T getClassByName(String className, ClassLoader classLoader) throws ClassNotFoundException, ClassCastException {
		Object ret = null;
		if (classLoader != null) {
			try {
				ret = classLoader.loadClass(className);
			} catch (ClassNotFoundException e) {
			}
		}
		if (ret == null) {
			try {
				ret = getClassLoader(className, null).loadClass(className);
			} catch (ClassNotFoundException e) {
				throw new ClassNotFoundException("Class not found: "+className+" (liveSense I18N Service)");
			}
		}
		return (T)ret;
	}

	private <T> T getClassByName(String className) throws ClassNotFoundException {
		return getClassByName(className, null);
	}

	@Override
	public Object create(String className) throws IOException, ClassNotFoundException {
		return I18N.create((Class<?>) getClassByName(className), getResourceBundleInternal(className, getLocale(null), getClassLoader(className, null)));
	}

	@Override
	public Object create(String className, Locale locale) throws IOException, ClassNotFoundException {
		return I18N.create((Class<?>)getClassByName(className), locale, getResourceBundleInternal(className, getLocale(locale), getClassLoader(className, null)));
	}

	@Override
	public Object create(String className, Locale locale, ClassLoader classLoader) throws IOException, ClassNotFoundException {
		return I18N.create((Class<?>)getClassByName(className, classLoader), locale, getClassLoader(className, classLoader), getResourceBundleInternal(className, getLocale(locale), getClassLoader(className, classLoader)));
	}

	@Override
	public Object create(String className, String lang) throws IOException, ClassNotFoundException {
		return I18N.create((Class<?>)getClassByName(className), lang, getResourceBundleInternal(className, getLocale(LocaleUtils.toLocale(lang)), getClassLoader(className, null)));
	}

	@Override
	public Object create(String className, String lang, ClassLoader classLoader) throws IOException, ClassNotFoundException {
		return I18N.create((Class<?>)getClassByName(className, classLoader), lang, getClassLoader(className, classLoader), getResourceBundleInternal(className, getLocale(LocaleUtils.toLocale(lang)), getClassLoader(className, classLoader)));				
	}

	private ResourceBundle getResourceBundleInternal(String className, Locale locale, ClassLoader classLoader) {
		if (slingI18nService != null) {
			return slingI18nService.getResourceBundle(className, locale);
		} else {
			return ResourceBundle.getBundle(className.replaceAll("\\.", "/"), locale, classLoader);
		}
	}
	
	// Resource Bundle specific
	@Override
	public ResourceBundle getResourceBundle(String className) throws IOException, ClassNotFoundException {
		return getResourceBundleInternal(className, getLocale(null), getClassLoader(className, null));
	}

	@Override
	public ResourceBundle getResourceBundle(String className, Locale locale) throws IOException, ClassNotFoundException {
		return getResourceBundleInternal(className, getLocale(locale), getClassLoader(className, null));
	}

	@Override
	public ResourceBundle getResourceBundle(String className, Locale locale, ClassLoader classLoader)
			throws IOException, ClassNotFoundException {
		return getResourceBundleInternal(className, getLocale(locale), getClassLoader(className, classLoader));
	}

	@Override
	public ResourceBundle getResourceBundle(String className, String lang) throws IOException, ClassNotFoundException {
		return getResourceBundleInternal(className, getLocale(I18N.createLocaleFromLang(lang)), getClassLoader(className, null));
	}

	@Override
	public ResourceBundle getResourceBundle(String className, String lang, ClassLoader classLoader) throws IOException,
	ClassNotFoundException {
		return getResourceBundleInternal(className, getLocale(I18N.createLocaleFromLang(lang)), getClassLoader(className, classLoader));
	}	

	// Composite Resource Loader
	Map<Locale, CompositeProxyResourceBundle> resourceBundles = new HashMap<Locale, CompositeProxyResourceBundle>();
	Set<String> classNames = new  HashSet<String>();

	private void addToCache(String className, Locale locale, CompositeProxyResourceBundle crb) {
		if (locale == null) locale = Locale.getDefault();
		try {
			crb.addToCache(className, getResourceBundle(className, locale, getClassLoader(className, null)));
		} catch (IOException e) {
			log.error("Could not refresh bundle: "+className+" Locale: "+locale == null ? "" : locale.toString()+" Is the resource in the Export-Package list?", e);
		} catch (ClassNotFoundException e) {
			log.error("Could not refresh bundle: "+className+" Locale: "+locale == null ? "" : locale.toString()+" Is the resource in the Export-Package list?", e);
		}
	}

	private void removeFromCache(String className, Locale locale, CompositeProxyResourceBundle crb) {		
		if (locale == null) locale = Locale.getDefault();
		crb.removeFromCache(className);
	}

	private CompositeProxyResourceBundle getCompositeProxyResourceBundleFromCache(Locale locale) {
		
		
		// If CompositeResource bundle does not exists for the given locale, load all message classes for
		// the given locale
		if (resourceBundles.get(locale) == null) {
			CompositeProxyResourceBundle crb = new CompositeProxyResourceBundle();
			resourceBundles.put(locale, crb);
			
			// Iterate over all registered classNames and load ResourceBundles for it.
			for (String cn : classNames) {
				addToCache(cn, locale, crb);
			}
		}
		return resourceBundles.get(locale);
	}

	@Override
	public ResourceBundle getDynamicResourceBundle(Locale locale) {
		synchronized (classNames) {
			return getCompositeProxyResourceBundleFromCache(locale);
		}
	}

	@Override
	public ResourceBundle getDynamicResourceBundle() {
		synchronized (classNames) {
			return getCompositeProxyResourceBundleFromCache(Locale.getDefault());
		}
	}

	@Override
	public void registerResourceBundle(Bundle bundle, String className) {
		synchronized (classNames) {
			ResourceBundle.clearCache(getClassLoader(className, null));
			classNames.add(className);
			bundleResourceCaches.put(className, new BundleProxyClassLoader(bundle));

			// Iterate over existing locales in cache and add new entry
			for (Locale locale : resourceBundles.keySet()) {
				addToCache(className, locale, getCompositeProxyResourceBundleFromCache(locale));
			}			
		}
	}

	@Override
	public void unregisterResourceBundle(Bundle bundle, String className) {
		synchronized (classNames) {
			ResourceBundle.clearCache(getClassLoader(className, null));
			classNames.remove(className);
			bundleResourceCaches.remove(className);

			// Iterate over existing locales in cache and add new entry
			for (Locale locale : resourceBundles.keySet()) {
				removeFromCache(className, locale, getCompositeProxyResourceBundleFromCache(locale));
			}
		}
	}
}
