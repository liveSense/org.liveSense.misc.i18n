package org.liveSense.server.i18n.service.I18nService;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.framework.Bundle;

public interface I18nService {

	/**
	 * Create an I18N proxy interface
	 * @param itf The interface mapped for ResourceBundle - The name of interface have to be equal with resource message
	 * @return The proxy object
	 * @throws IOException
	 */
	public <T> T create(Class<T> itf) throws IOException;

	/**
	 * Create an I18N proxy interface
	 * @param itf The interface mapped for ResourceBundle - The name of interface have to be equal with resource message
	 * @param locale Use the given locale only
	 * @return The proxy object
	 * @throws IOException
	 */
	public <T> T create(Class<T> itf, Locale locale) throws IOException;

	/**
	 * Create an I18N proxy interface
	 * @param itf The interface mapped for ResourceBundle - The name of interface have to be equal with resource message
	 * @param locale Use the given locale only
	 * @param classLoader Use the given classLoader
	 * @return The proxy object
	 * @throws IOException
	 */
	public <T> T create(Class<T> itf, Locale locale, ClassLoader classLoader) throws IOException;

	/**
	 * Create an I18N proxy interface
	 * @param itf The interface mapped for ResourceBundle - The name of interface have to be equal with resource message
	 * @param lang Use the given locale only
	 * @return The proxy object
	 * @throws IOException
	 */
	public <T> T create(Class<T> itf, String lang) throws IOException;

	/**
	 * Create an I18N proxy interface
	 * @param itf The interface mapped for ResourceBundle - The name of interface have to be equal with resource message
	 * @param lang Use the given locale only
	 * @param classLoader Use the given classLoader
	 * @return The proxy object
	 * @throws IOException
	 */
	public <T> T create(Class<T> itf, String lang, ClassLoader classLoader) throws IOException;

	/**
	 * Create an I18N proxy interface
	 * @param itf The interface mapped for ResourceBundle - The name of interface have to be equal with resource message
	 * @param locale Use the given locale only
	 * @param classLoader Use the given classLoader
	 * @return The proxy object
	 * @throws IOException
	 */
	public Object create(String className) throws IOException, ClassNotFoundException;

	
	/**
	 * Create an I18N proxy with the given name of interface
	 * @param className The interface mapped for ResourceBundle - The name of interface have to be equal with resource message
	 * @param locale Use the given locale only
	 * @return The proxy object
	 * @throws IOException
	 */
	public Object create(String className, Locale locale) throws IOException, ClassNotFoundException;

	/**
	 * Create an I18N proxy with the given name of interface
	 * @param className The interface mapped for ResourceBundle - The name of interface have to be equal with resource message
	 * @param locale Use the given locale only
	 * @param classLoader Use the given classLoader
	 * @return The proxy object
	 * @throws IOException
	 */
	public Object create(String className, Locale locale, ClassLoader classLoader) throws IOException, ClassNotFoundException;

	/**
	 * Create an I18N proxy with the given name of interface
	 * @param className The interface mapped for ResourceBundle - The name of interface have to be equal with resource message
	 * @param lang Use the given locale only
	 * @return The proxy object
	 * @throws IOException
	 */
	public Object create(String className, String lang) throws IOException, ClassNotFoundException;


	/**
	 * Create an I18N proxy with the given name of interface
	 * @param className The interface mapped for ResourceBundle - The name of interface have to be equal with resource message
	 * @param lang Use the given locale only
	 * @param classLoader Use the given classLoader
	 * @return The proxy object
	 * @throws IOException
	 */
	public Object create(String className, String lang, ClassLoader classLoader) throws IOException, ClassNotFoundException;

	public ResourceBundle getResourceBundle(String className) throws IOException, ClassNotFoundException;

	public ResourceBundle getResourceBundle(String className, Locale locale) throws IOException, ClassNotFoundException;

	public ResourceBundle getResourceBundle(String className, Locale locale, ClassLoader classLoader) throws IOException, ClassNotFoundException;

	public ResourceBundle getResourceBundle(String className, String lang) throws IOException, ClassNotFoundException;

	public ResourceBundle getResourceBundle(String className, String lang, ClassLoader classLoader) throws IOException, ClassNotFoundException;
	
	public ResourceBundle getDynamicResourceBundle(Locale locale);	

	public ResourceBundle getDynamicResourceBundle();

	public void registerResourceBundle(Bundle bundle, String className);
	
	public void unregisterResourceBundle(Bundle bundle, String className);
}