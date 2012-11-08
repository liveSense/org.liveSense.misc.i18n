package org.liveSense.server.i18n.service.I18nService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.liveSense.server.i18n.CompositeProxyResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate=true)
@Service
public class DynamicI18nResourceBundleServiceImpl implements DynamicI18nResourceBundleService {

	Logger log = LoggerFactory.getLogger(DynamicI18nResourceBundleServiceImpl.class);

	@Reference
	I18nService i18nService;
	
	CompositeProxyResourceBundle resourceBundle = new CompositeProxyResourceBundle();
	Set<String> classNames = new  HashSet<String>();
			
	public ResourceBundle getCompositeProxyBundle() {
		return resourceBundle;
	}
	
	private void refresh(String className, Locale locale) {
		try {
			ResourceBundle actBundle = i18nService.getResourceBundle(className, locale);
		} catch (IOException e) {
			log.error("Could not refresh bundle: "+className+" Locale: "+locale == null ? "" : locale.toString()+" The resource is in the Export-Package list?", e);
		} catch (ClassNotFoundException e) {
			log.error("Could not refresh bundle: "+className+" Locale: "+locale == null ? "" : locale.toString()+" The resource is in the Export-Package list?", e);
		}
	}

	
	public void registerResourceBundle(String className, Locale locale) {
		classNames.add(className);
		refresh(className, locale);
	}

	public void unregisterResourceBundle(String className, Locale locale) {
		classNames.remove(className);
		refresh(className, locale);
	}

	public ResourceBundle getCompositeProxyBundle(Locale locale) {
		return null;
	}

	
}
