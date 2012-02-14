package org.liveSense.server.i18n.service.I18nService;

import java.util.Locale;
import java.util.ResourceBundle;

public interface DynamicI18nResourceBundleService {
	

	public ResourceBundle getCompositeProxyBundle(Locale locale);

	public ResourceBundle getCompositeProxyBundle();
	
	public void registerResourceBundle(String className, Locale locale);
	
	public void unregisterResourceBundle(String className, Locale locale);

}
