package org.liveSense.server.i18n.service.I18nService;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import com.google.gwt.i18n.client.LocalizableResource;

public interface I18nService {

	public <T> T create(Class<T> itf) throws IOException;

	public <T> T create(Class<T> itf, Locale locale) throws IOException;

	public <T> T create(Class<T> itf, Locale locale, ClassLoader classLoader) throws IOException;

	public <T> T create(Class<T> itf, String lang) throws IOException;

	public <T> T create(Class<T> itf, String lang, ClassLoader classLoader) throws IOException;

	public Object create(String className) throws IOException, ClassNotFoundException;

	public Object create(String className, Locale locale) throws IOException, ClassNotFoundException;

	public Object create(String className, Locale locale, ClassLoader classLoader) throws IOException, ClassNotFoundException;

	public Object create(String className, String lang) throws IOException, ClassNotFoundException;

	public Object create(String className, String lang, ClassLoader classLoader) throws IOException, ClassNotFoundException;

	public ResourceBundle getResourceBundle(String className) throws IOException, ClassNotFoundException;

	public ResourceBundle getResourceBundle(String className, Locale locale) throws IOException, ClassNotFoundException;

	public ResourceBundle getResourceBundle(String className, Locale locale, ClassLoader classLoader) throws IOException, ClassNotFoundException;

	public ResourceBundle getResourceBundle(String className, String lang) throws IOException, ClassNotFoundException;

	public ResourceBundle getResourceBundle(String className, String lang, ClassLoader classLoader) throws IOException, ClassNotFoundException;


}