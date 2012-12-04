/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.liveSense.server.i18n.loader;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.apache.sling.jcr.api.SlingRepository;
import org.liveSense.server.i18n.I18N;
import org.liveSense.server.i18n.service.I18nService.I18nService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The <code>I18nLoader</code> is the service
 * providing the following functionality:
 * <ul>
 * <li>Bundle listener to load initial I18n resource bundles for bundles.
 * <li>Fires OSGi EventAdmin events on behalf of internal helper objects
 * </ul>
 **/


@Component(label="%i18nLoader.name", description="%i18nLoader.description",
immediate=true, metatype=true, configurationFactory=true, policy=ConfigurationPolicy.OPTIONAL, createPid=false)
@Properties(value={
		@Property(name=I18nLoader.PROP_CONFIGURATION_I18N_PATH, value=I18nLoader.DEFAULT_CONFIGURATION_I18N_PATH)
})
public class I18nLoader implements SynchronousBundleListener {

	Logger log = LoggerFactory.getLogger(I18nLoader.class);

	private final static String CONFIGURATION_PROPERTY_NAME = "i18nConfigLoaderName";

	public final static String PROP_CONFIGURATION_I18N_PATH = "resourcePath";
	public final static String DEFAULT_CONFIGURATION_I18N_PATH = "i18n";

	@Reference(cardinality=ReferenceCardinality.MANDATORY_UNARY, policy=ReferencePolicy.DYNAMIC)
	ConfigurationAdmin configurationAdmin;

	@Reference(cardinality=ReferenceCardinality.MANDATORY_UNARY, policy=ReferencePolicy.DYNAMIC)
	I18nService i18nService;

	@Reference(cardinality=ReferenceCardinality.MANDATORY_UNARY, policy=ReferencePolicy.DYNAMIC)
	SlingRepository repository;


	private String path = DEFAULT_CONFIGURATION_I18N_PATH;

	// ---------- BundleListener -----------------------------------------------
	/**
	 * Loads and unloads any configuration provided by the bundle whose state
	 * changed. If the bundle has been started, the configuration is loaded. If
	 * the bundle is about to stop, the configurations are unloaded.
	 *
	 * @param event The <code>BundleEvent</code> representing the bundle state
	 *            change.
	 */
	@Override
	public void bundleChanged(BundleEvent event) {

		//
		// NOTE:
		// This is synchronous - take care to not block the system !!
		//

		switch (event.getType()) {
		case BundleEvent.STARTING:
			try {
				registerBundle(event.getBundle());
			} catch (Throwable t) {
				log.error(
						"bundleChanged: Problem loading I18n configuration of bundle "
								+ event.getBundle().getSymbolicName() + " ("
								+ event.getBundle().getBundleId() + ")", t);
			} finally {
			}
			break;
		case BundleEvent.STOPPED:
			try {
				unregisterBundle(event.getBundle());
			} catch (Throwable t) {
				log.error(
						"bundleChanged: Problem unloading I18n configuration of bundle "
								+ event.getBundle().getSymbolicName() + " ("
								+ event.getBundle().getBundleId() + ")", t);
			} finally {
			}
			break;
		}
	}


	@Activate
	public void activate(ComponentContext context) throws Exception {

		context.getBundleContext().addBundleListener(this);

		path = PropertiesUtil.toString(context.getProperties().get(PROP_CONFIGURATION_I18N_PATH), DEFAULT_CONFIGURATION_I18N_PATH);
		int ignored = 0;
		try {
			Bundle[] bundles = context.getBundleContext().getBundles();
			for (int i=0;i<bundles.length; i++) {
				Bundle bundle = bundles[i];

				if ((bundle.getState() & (Bundle.ACTIVE)) != 0) {
					// load configurations from bundles which are ACTIVE
					try {
						registerBundle(bundle);
					} catch (Throwable t) {
						log.error(
								"Problem loading I18n configuration of bundle "
										+ bundle.getSymbolicName() + " ("
										+ bundle.getBundleId() + ")", t);
					} finally {
					}
				} else {
					ignored++;
				}

				if ((bundle.getState() & (Bundle.ACTIVE)) == 0) {
					// remove configurations from bundles which are not ACTIVE
					try {
						unregisterBundle(bundle);
					} catch (Throwable t) {
						log.error(
								"Problem loading I18n configuration of bundle "
										+ bundle.getSymbolicName() + " ("
										+ bundle.getBundleId() + ")", t);
					} finally {
					}
				} else {
					ignored++;
				}


			}
			log.info(
					"Out of "+bundles.length+" bundles, "+ignored+" were not in a suitable state for I18n configuration loading");
		} catch (Throwable t) {
			log.error("activate: Problem while loading I18n configuration", t);
		} finally {
		}


	}

	@Deactivate
	public void deactivate(BundleContext context) throws Exception {
		context.removeBundleListener(this);
		int ignored = 0;
		try {
			Bundle[] bundles = context.getBundles();
			for (int i=0;i<bundles.length; i++) {
				Bundle bundle = bundles[i];

				if ((bundle.getState()) == 0) {
					// remove configurations from bundles which are not ACTIVE
					try {
						unregisterBundle(bundle);
					} catch (Throwable t) {
						log.error(
								"Problem loading I18n configuration of bundle "
										+ bundle.getSymbolicName() + " ("
										+ bundle.getBundleId() + ")", t);
					} finally {
					}
				} else {
					ignored++;
				}


			}
			log.info(
					"Out of "+bundles.length+" bundles, "+ignored+" were not in a suitable state for I18n configuration loading");
		} catch (Throwable t) {
			log.error("activate: Problem while loading I18n configuration", t);
		} finally {
		}

	}



	// ---------- Implementation helpers --------------------------------------
	/**
	 * Register a bundle and install the configurations included them.
	 *
	 * @param bundle
	 */
	public void registerBundle(final Bundle bundle) throws Exception {
		// if this is an update, we have to uninstall the old content first

		log.debug("Registering bundle "+bundle.getSymbolicName()+" for I18n configuration loading.");
		registerBundleInternal(bundle);
		/*
        if (registerBundleInternal(bundle)) {

            // handle delayed bundles, might help now
            int currentSize = -1;
            for (int i = delayedBundles.size(); i > 0
                    && currentSize != delayedBundles.size()
                    && !delayedBundles.isEmpty(); i--) {

                Iterator di = delayedBundles.iterator();
                while (di.hasNext()) {
                    Bundle delayed = (Bundle)di.next();
                    if (registerBundleInternal(delayed)) {
                        di.remove();
                    }
                }
                currentSize = delayedBundles.size();
            }

        } else {
            // add to delayed bundles - if this is not an update!
            delayedBundles.add(bundle);
        }*/
	}



	private boolean registerBundleInternal(
			final Bundle bundle) throws Exception {


		// check if bundle has initial configuration
		final Iterator<?> pathIter = PathEntry.getEntries(bundle);
		if (pathIter == null) {
			log.debug("Bundle "+bundle.getSymbolicName()+" has no I18n configuration(s)");
			return true;
		}
		log.info("Bundle "+bundle.getSymbolicName()+" has I18n configuration(s)");

		while (pathIter.hasNext()) {
			PathEntry path = (PathEntry)pathIter.next();
			String i18nBundleName = path.getPath();
			install(bundle, i18nBundleName);
		}

		return false;
	}

	/**
	 * Unregister a bundle. Remove installed content.
	 *
	 * @param bundle The bundle.
	 */
	public void unregisterBundle(final Bundle bundle) throws Exception {


		final Iterator<?> pathIter = PathEntry.getEntries(bundle);
		if (pathIter == null) {
			log.debug("Bundle "+bundle.getSymbolicName()+" has no I18n configuration(s)");
			return;
		}
		log.info("Bundle "+bundle.getSymbolicName()+" has I18n configuration(s)");

		while (pathIter.hasNext()) {
			PathEntry path = (PathEntry)pathIter.next();
			String i18nBundleName = path.getPath();
			uninstall(bundle, i18nBundleName);
		}

	}

	private static final String FOLDER_NODE_TYPE = "nt:unstructured";
	/**
	 * Creates or gets the {@link javax.jcr.Node Node} at the given Path.
	 *
	 * @param session The session to use for node creation
	 * @param absolutePath absolute node path
	 * @param nodeType to use for creation of the final node
	 * @return the Node at path
	 * @throws RepositoryException in case of exception accessing the Repository
	 */
	private Node createPath(final Session session,
			final String absolutePath,
			final String nodeType)
					throws RepositoryException {
		final Node parentNode = session.getRootNode();
		String relativePath = absolutePath.substring(1);
		if (!parentNode.hasNode(relativePath)) {
			Node node = parentNode;
			int pos = relativePath.lastIndexOf('/');
			if ( pos != -1 ) {
				final StringTokenizer st = new StringTokenizer(relativePath.substring(0, pos), "/");
				while ( st.hasMoreTokens() ) {
					final String token = st.nextToken();
					if ( !node.hasNode(token) ) {
						try {
							node.addNode(token, FOLDER_NODE_TYPE);
						} catch (RepositoryException re) {
							// we ignore this as this folder might be created from a different task
							node.refresh(false);
						}
					}
					node = node.getNode(token);
				}
				relativePath = relativePath.substring(pos + 1);
			}
			if ( !node.hasNode(relativePath) ) {
				node.addNode(relativePath, nodeType);
			}
			return node.getNode(relativePath);
		}
		return parentNode.getNode(relativePath);
	}

	public void install(Bundle bundle, String bundleName) throws Exception {
		// Removes proxy classes
		I18N.resetCache();
		log.info("Registering I18n: "+bundleName);
		i18nService.registerResourceBundle(bundle, bundleName);

		Session session = null;
		try {
			session = repository.loginAdministrative(null);
			// Writing entries to Repository
			String i18nPath = new File(bundleName.replace(".", "/")).getParent();
			String i18nName = new File(bundleName.replace(".", "/")).getName();
			Enumeration entries = bundle.getEntryPaths(i18nPath);

			if (entries != null) {
				while (entries.hasMoreElements()) {
					URL url = bundle.getEntry((String)entries.nextElement());
					String urlFileName = new File(url.getFile()).getName();
					if (urlFileName.endsWith(".properties") && (urlFileName.startsWith(i18nName))) {
						log.info("Loading "+url+" into JCR repository");
						String locale = urlFileName.substring(i18nName.length(),  urlFileName.length()-".properties".length());
						Locale loc = Locale.getDefault();
						if (StringUtils.isNotEmpty(locale)) {
							loc = LocaleUtils.toLocale(locale.substring(1));
							
							Node n = createPath(session,"/"+path+"/"+bundleName+"/"+loc.toString(), FOLDER_NODE_TYPE);
							boolean foundType = false;
							for (NodeType t : n.getMixinNodeTypes()) {
								if (t.getName().equals(NodeType.MIX_LANGUAGE)) {
									foundType = true;
								}
							}
							if (!foundType)
								n.addMixin(NodeType.MIX_LANGUAGE);
								n.setProperty("jcr:language", loc.toString());
								n.setProperty("sling:basename", bundleName);
							
							java.util.Properties props = new java.util.Properties();
							InputStream in = url.openStream();
							props.load(in);
							in.close();
							
							for (Object key : props.keySet()) {
								if (!n.hasNode((String)key)) {
									log.info("Creating "+(String)key);
									Node msgNode = n.addNode((String)key, "sling:MessageEntry");
									msgNode.setProperty("sling:key", (String)key);
									msgNode.setProperty("sling:message", props.getProperty((String) key));
								}
							}
						}
					}
				}
			}
			if (session.hasPendingChanges())
				session.save();
		} catch (RepositoryException e) {
			log.error("Cannot get session", e);
		} finally {
			if (session != null && session.isLive()) {
				try {
					session.logout();
				} catch (Exception e) {
				}
			}
		}

	}

	public void uninstall(Bundle bundle, String bundleName) throws Exception {
		log.info("UnRegistering I18n: "+bundleName);
		i18nService.unregisterResourceBundle(bundle, bundleName);
	}

}