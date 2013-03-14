package org.liveSense.server.i18n;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class CompositeProxyResourceBundle extends ResourceBundle {

	private final Map<String, ResourceBundle> cache = new HashMap<String, ResourceBundle>();
	
	private class CacheResourceBundleEnumeration implements Enumeration<String> {

		private final Iterator<Entry<String, ResourceBundle>> cacheIterator = cache.entrySet().iterator();
		private Enumeration<String> itemEnumeratrion = null;
						
		@Override
		public boolean hasMoreElements() {
			synchronized (cache) {
				while (true) {
					if (itemEnumeratrion != null) {
						if (itemEnumeratrion.hasMoreElements()) {
							return true;
						} else {
							if (cacheIterator.hasNext()) {
								itemEnumeratrion = cacheIterator.next().getValue().getKeys();
							} else {
								return false;
							}
						}
					} else {
						if (cacheIterator.hasNext()) {
							itemEnumeratrion = cacheIterator.next().getValue().getKeys();
						} else {
							return false;
						}					
					}
				}
			}
		}

		@Override
		public String nextElement() {
			synchronized (cache) {
				while (true) {
					if (itemEnumeratrion != null) {
						if (itemEnumeratrion.hasMoreElements()) {
							return itemEnumeratrion.nextElement();
						} else {
							if (cacheIterator.hasNext()) {
								itemEnumeratrion = cacheIterator.next().getValue().getKeys();
							} else {
								return null;
							}
						}
					} else {
						if (cacheIterator.hasNext()) {
							itemEnumeratrion = cacheIterator.next().getValue().getKeys();
						} else {
							return null;
						}					
					}
				}
			} 
		}
		
	}
	
	@Override
	public Enumeration<String> getKeys() {
		return new CacheResourceBundleEnumeration();
	}

	@Override
	protected Object handleGetObject(String key) {
		synchronized (cache) {
			Iterator<Entry<String, ResourceBundle>> cacheIterator = cache.entrySet().iterator();
			Object ret = null;
			while (cacheIterator.hasNext() && ret == null) {
				try {
					ret = cacheIterator.next().getValue().getObject(key);
				} catch (MissingResourceException e) {
				}
			}
			if (ret == null) throw new MissingResourceException("No message found", "CompositeProxyResourceBundle", key);
			return ret;
		}
	}
		
	
	public void addToCache(String className, ResourceBundle rb) {
		synchronized (cache) {
			cache.put(className, rb);			
		}
	}

	public void removeFromCache(String className) {
		synchronized (cache) {
			cache.remove(className);			
		}
	}

}
