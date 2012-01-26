package org.liveSense.server.i18n.service.I18nService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.classloader.DynamicClassLoaderManager;
import org.apache.sling.commons.classloader.DynamicClassLoaderProvider;
import org.liveSense.server.i18n.GenericX;
import org.liveSense.server.i18n.I18N;

import com.google.gwt.i18n.client.LocalizableResource;
import com.google.gwt.i18n.client.Messages;

@Component(immediate=true)
@Service
public class I18nServiceImpl implements I18nService {

    @Reference
    private DynamicClassLoaderManager dynamicClassLoaderManager;
    
	@Activate
	protected void activate() {
		I18N.resetCache();
	}
	
	/* (non-Javadoc)
	 * @see org.liveSense.server.i18n.service.I18nService.I18NService#create(java.lang.Class)
	 */
	public <T> T create(Class<T> itf) throws IOException {
		return I18N.create(itf, (Locale)null, dynamicClassLoaderManager.getDynamicClassLoader());
	}

	/* (non-Javadoc)
	 * @see org.liveSense.server.i18n.service.I18nService.I18NService#create(java.lang.Class, java.util.Locale)
	 */
	public <T> T create(Class<T> itf, Locale locale) throws IOException {
		return I18N.create(itf, locale, dynamicClassLoaderManager.getDynamicClassLoader());
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

	ClassLoader getClassLoader(ClassLoader classLoader) {
		if (classLoader != null) {
			return classLoader;
		} else if (dynamicClassLoaderManager != null && dynamicClassLoaderManager.getDynamicClassLoader() != null) {
			return dynamicClassLoaderManager.getDynamicClassLoader();
		} else
			return I18nServiceImpl.class.getClassLoader();
		
	}

	Locale getLocale(Locale locale) {
		if (locale != null) {
			return locale;
		} else return Locale.getDefault();
	}
	
	@SuppressWarnings("unchecked")
	<T> T getClassByName(String className, ClassLoader classLoader) throws ClassNotFoundException, ClassCastException {
		Object ret = null;
		if (classLoader != null) {
			try {
				ret = classLoader.loadClass(className);
			} catch (ClassNotFoundException e) {
			}
		}
		if (ret == null) {
			if (dynamicClassLoaderManager != null) {
				try {
					ret = dynamicClassLoaderManager.getDynamicClassLoader().loadClass(className);
				} catch (ClassNotFoundException e) {
					throw new ClassNotFoundException("Class not found: "+className+" (liveSense I18N Service)");
				}
			} else {
				throw new ClassNotFoundException("Class not found: "+className+" (liveSense I18N Service)");
			}
		}
		/*
		if (ret instanceof LocalizableResource) {
			return (T)ret;			
		} else {
			throw new ClassCastException(className+" is not instance of com.google.gwt.i18n.client.LocalizableResource");
		} */
		return (T)ret;
	}

	@SuppressWarnings("unchecked")
	<T> T getClassByName(String className) throws ClassNotFoundException {
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

	
}
