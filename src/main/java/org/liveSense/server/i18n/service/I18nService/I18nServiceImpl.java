package org.liveSense.server.i18n.service.I18nService;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.liveSense.core.service.OSGIClassLoaderManager;
import org.liveSense.server.i18n.CompositeProxyResourceBundle;
import org.liveSense.server.i18n.I18N;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate=true)
@Service
public class I18nServiceImpl implements I18nService {

	Logger log = LoggerFactory.getLogger(I18nServiceImpl.class);

	@Reference
	private OSGIClassLoaderManager dynamicClassLoaderManager;

	BundleContext context = null;
	ClassLoader classLoader = I18nServiceImpl.class.getClassLoader();
	
	@Activate
	protected void activate(BundleContext context) {
		I18N.resetCache();
		this.context = context;
		this.classLoader = dynamicClassLoaderManager.getPackageAdminClassLoader(context);
	}

	/* (non-Javadoc)
	 * @see org.liveSense.server.i18n.service.I18nService.I18NService#create(java.lang.Class)
	 */
	public <T> T create(Class<T> itf) throws IOException {
		return I18N.create(itf, (Locale)null, classLoader);
	}

	/* (non-Javadoc)
	 * @see org.liveSense.server.i18n.service.I18nService.I18NService#create(java.lang.Class, java.util.Locale)
	 */
	public <T> T create(Class<T> itf, Locale locale) throws IOException {
		return I18N.create(itf, locale, classLoader);
	}

	/* (non-Javadoc)
	 * @see org.liveSense.server.i18n.service.I18nService.I18NService#create(java.lang.Class, java.util.Locale, java.lang.ClassLoader)
	 */
	public <T> T create(Class<T> itf, Locale locale, ClassLoader classLoader) throws IOException {
		return I18N.create(itf, locale, classLoader);
	}

	/* (non-Javadoc)
	 * @see org.liveSense.server.i18n.service.I18nService.I18NService#create(java.lang.Class, java.lang.String)
	 */
	public <T> T create(Class<T> itf, String lang) throws IOException {
		return I18N.create(itf, lang);
	}

	/* (non-Javadoc)
	 * @see org.liveSense.server.i18n.service.I18nService.I18NService#create(java.lang.Class, java.lang.String, java.lang.ClassLoader)
	 */
	public <T> T create(Class<T> itf, String lang, ClassLoader classLoader) throws IOException {
		return I18N.create(itf, lang, classLoader);
	}

	private ClassLoader getClassLoader(ClassLoader classLoader) {
		if (classLoader != null) {
			return classLoader;
		} else 
			return this.classLoader;
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
			if (this.classLoader != null) {
				try {
					ret = this.classLoader.loadClass(className);
				} catch (ClassNotFoundException e) {
					throw new ClassNotFoundException("Class not found: "+className+" (liveSense I18N Service)");
				}
			} else {
				throw new ClassNotFoundException("Class not found: "+className+" (liveSense I18N Service)");
			}
		}
		return (T)ret;
	}

	private <T> T getClassByName(String className) throws ClassNotFoundException {
		return getClassByName(className, null);
	}

	public Object create(String className) throws IOException, ClassNotFoundException {
		return I18N.create((Class<?>) getClassByName(className));
	}

	public Object create(String className, Locale locale) throws IOException, ClassNotFoundException {
		return I18N.create((Class<?>)getClassByName(className), locale);
	}

	public Object create(String className, Locale locale, ClassLoader classLoader) throws IOException, ClassNotFoundException {
		return I18N.create((Class<?>)getClassByName(className, classLoader), locale, getClassLoader(classLoader));
	}

	public Object create(String className, String lang) throws IOException, ClassNotFoundException {
		return I18N.create((Class<?>)getClassByName(className), lang);
	}

	public Object create(String className, String lang, ClassLoader classLoader) throws IOException, ClassNotFoundException {
		return I18N.create((Class<?>)getClassByName(className, classLoader), lang, getClassLoader(classLoader));				
	}

	// Resource Bundle specific
	public ResourceBundle getResourceBundle(String className) throws IOException, ClassNotFoundException {
		return ResourceBundle.getBundle(className.replace('.', '/'), getLocale(null), getClassLoader(null));
	}

	public ResourceBundle getResourceBundle(String className, Locale locale) throws IOException, ClassNotFoundException {
		return ResourceBundle.getBundle(className.replace('.', '/'), getLocale(locale), getClassLoader(null));
	}

	public ResourceBundle getResourceBundle(String className, Locale locale, ClassLoader classLoader)
			throws IOException, ClassNotFoundException {
		return ResourceBundle.getBundle(className.replace('.', '/'), getLocale(locale), getClassLoader(classLoader));
	}

	public ResourceBundle getResourceBundle(String className, String lang) throws IOException, ClassNotFoundException {
		return ResourceBundle.getBundle(className.replace('.', '/'), getLocale(I18N.createLocaleFromLang(lang)), getClassLoader(null));
	}

	public ResourceBundle getResourceBundle(String className, String lang, ClassLoader classLoader) throws IOException,
	ClassNotFoundException {
		return ResourceBundle.getBundle(className.replace('.', '/'), getLocale(I18N.createLocaleFromLang(lang)), getClassLoader(classLoader));
	}	

	// Composite Resource Loader
	Map<Locale, CompositeProxyResourceBundle> resourceBundles = new HashMap<Locale, CompositeProxyResourceBundle>();
	Set<String> classNames = new  HashSet<String>();

	private void addToCache(String className, Locale locale, CompositeProxyResourceBundle crb) {

		if (locale == null) locale = Locale.getDefault();
		try {
			crb.addToCache(className, getResourceBundle(className, locale, classLoader));
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

	public ResourceBundle getDynamicResourceBundle(Locale locale) {
		synchronized (classNames) {
			return getCompositeProxyResourceBundleFromCache(locale);
		}
	}

	public ResourceBundle getDynamicResourceBundle() {
		synchronized (classNames) {
			return getCompositeProxyResourceBundleFromCache(Locale.getDefault());
		}
	}

	public void registerResourceBundle(String className) {
		synchronized (classNames) {
			ResourceBundle.clearCache(classLoader);
			classNames.add(className);

			// Iterate over existing locales in cache and add new entry
			for (Locale locale : resourceBundles.keySet()) {
				addToCache(className, locale, getCompositeProxyResourceBundleFromCache(locale));
			}			
		}
	}

	public void unregisterResourceBundle(String className) {
		synchronized (classNames) {
			ResourceBundle.clearCache(classLoader);
			classNames.remove(className);

			// Iterate over existing locales in cache and add new entry
			for (Locale locale : resourceBundles.keySet()) {
				removeFromCache(className, locale, getCompositeProxyResourceBundleFromCache(locale));
			}
		}
	}
}
