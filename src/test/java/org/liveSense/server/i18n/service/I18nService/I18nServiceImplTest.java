/**
 * 
 */
package org.liveSense.server.i18n.service.I18nService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.sling.commons.testing.osgi.MockBundle;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.liveSense.server.i18n.I18N;
import org.liveSense.server.i18n.messages.FooMessages;
import org.liveSense.server.i18n.messages.FooMessagesPureInterface;
import org.liveSense.server.i18n.messages.FooMessagesWithAnnotation;

/**
 * @author robson
 *
 */
public class I18nServiceImplTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	
	private I18nService service;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		I18N.resetCache();
		service = new I18nServiceImpl();
	}

	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#create(java.lang.Class)}.
	 * @throws IOException 
	 */
	@Test
	public void testCreateClassOfT() throws IOException {
		Locale.setDefault(Locale.ENGLISH);
		FooMessages msg1 = service.create(FooMessages.class);
		FooMessagesPureInterface msg2 = service.create(FooMessagesPureInterface.class);
		FooMessagesWithAnnotation msg3 = service.create(FooMessagesWithAnnotation.class);
	
		assertEquals("The message is test_None()", msg1.test_None());
		assertEquals("The message is test_None()", msg2.test_None());
		assertEquals("The message is test_None()", msg3.method_test_None());

	}

	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#create(java.lang.Class, java.util.Locale)}.
	 * @throws IOException 
	 */
	@Test
	public void testCreateClassOfTLocale() throws IOException {
		FooMessages msg1 = service.create(FooMessages.class, new Locale("fr"));
		FooMessagesPureInterface msg2 = service.create(FooMessagesPureInterface.class, new Locale("fr"));
		FooMessagesWithAnnotation msg3 = service.create(FooMessagesWithAnnotation.class, new Locale("fr"));
	
		assertEquals("Le message est test_None()", msg1.test_None());
		assertEquals("Le message est test_None()", msg2.test_None());
		assertEquals("Le message est test_None()", msg3.method_test_None());

	}

	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#create(java.lang.Class, java.util.Locale, java.lang.ClassLoader)}.
	 * @throws IOException 
	 */
	@Test
	public void testCreateClassOfTLocaleClassLoader() throws IOException {
		FooMessages msg1 = service.create(FooMessages.class, new Locale("fr"), this.getClass().getClassLoader());
		FooMessagesPureInterface msg2 = service.create(FooMessagesPureInterface.class, new Locale("fr"), this.getClass().getClassLoader());
		FooMessagesWithAnnotation msg3 = service.create(FooMessagesWithAnnotation.class, new Locale("fr"), this.getClass().getClassLoader());
	
		assertEquals("Le message est test_None()", msg1.test_None());
		assertEquals("Le message est test_None()", msg2.test_None());
		assertEquals("Le message est test_None()", msg3.method_test_None());
	}

	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#create(java.lang.Class, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testCreateClassOfTString() throws IOException {
		FooMessages msg1 = service.create(FooMessages.class, "fr");
		FooMessagesPureInterface msg2 = service.create(FooMessagesPureInterface.class, "fr");
		FooMessagesWithAnnotation msg3 = service.create(FooMessagesWithAnnotation.class, "fr");
	
		assertEquals("Le message est test_None()", msg1.test_None());
		assertEquals("Le message est test_None()", msg2.test_None());
		assertEquals("Le message est test_None()", msg3.method_test_None());
	}


	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#create(java.lang.Class, java.lang.String, java.lang.ClassLoader)}.
	 * @throws IOException 
	 */
	@Test
	public void testCreateClassOfTStringClassLoader() throws IOException {
		FooMessages msg1 = service.create(FooMessages.class, "fr", this.getClass().getClassLoader());
		FooMessagesPureInterface msg2 = service.create(FooMessagesPureInterface.class, "fr", this.getClass().getClassLoader());
		FooMessagesWithAnnotation msg3 = service.create(FooMessagesWithAnnotation.class, "fr", this.getClass().getClassLoader());
	
		assertEquals("Le message est test_None()", msg1.test_None());
		assertEquals("Le message est test_None()", msg2.test_None());
		assertEquals("Le message est test_None()", msg3.method_test_None());
	}

	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#create(java.lang.String)}.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testCreateString() throws IOException, ClassNotFoundException {
		Locale.setDefault(Locale.ENGLISH);

		FooMessages msg1 = (FooMessages) service.create("org.liveSense.server.i18n.messages.FooMessages");
		FooMessagesPureInterface msg2 = (FooMessagesPureInterface) service.create("org.liveSense.server.i18n.messages.FooMessagesPureInterface");
		FooMessagesWithAnnotation msg3 = (FooMessagesWithAnnotation) service.create("org.liveSense.server.i18n.messages.FooMessagesWithAnnotation");

		assertEquals("The message is test_None()", msg1.test_None());
		assertEquals("The message is test_None()", msg2.test_None());
		assertEquals("The message is test_None()", msg3.method_test_None());
	}

	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#create(java.lang.String, java.util.Locale)}.
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	@Test
	public void testCreateStringLocale() throws IOException, ClassNotFoundException {
		FooMessages msg1 = (FooMessages) service.create("org.liveSense.server.i18n.messages.FooMessages", new Locale("fr"));
		FooMessagesPureInterface msg2 = (FooMessagesPureInterface) service.create("org.liveSense.server.i18n.messages.FooMessagesPureInterface", new Locale("fr"));
		FooMessagesWithAnnotation msg3 = (FooMessagesWithAnnotation) service.create("org.liveSense.server.i18n.messages.FooMessagesWithAnnotation", new Locale("fr"));

		assertEquals("Le message est test_None()", msg1.test_None());
		assertEquals("Le message est test_None()", msg2.test_None());
		assertEquals("Le message est test_None()", msg3.method_test_None());
	}

	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#create(java.lang.String, java.util.Locale, java.lang.ClassLoader)}.
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	@Test
	public void testCreateStringLocaleClassLoader() throws IOException, ClassNotFoundException {
		FooMessages msg1 = (FooMessages) service.create("org.liveSense.server.i18n.messages.FooMessages", new Locale("fr"), this.getClass().getClassLoader());
		FooMessagesPureInterface msg2 = (FooMessagesPureInterface) service.create("org.liveSense.server.i18n.messages.FooMessagesPureInterface", new Locale("fr"), this.getClass().getClassLoader());
		FooMessagesWithAnnotation msg3 = (FooMessagesWithAnnotation) service.create("org.liveSense.server.i18n.messages.FooMessagesWithAnnotation", new Locale("fr"), this.getClass().getClassLoader());

		assertEquals("Le message est test_None()", msg1.test_None());
		assertEquals("Le message est test_None()", msg2.test_None());
		assertEquals("Le message est test_None()", msg3.method_test_None());
	}

	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#create(java.lang.String, java.lang.String)}.
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	@Test
	public void testCreateStringString() throws IOException, ClassNotFoundException {
		FooMessages msg1 = (FooMessages) service.create("org.liveSense.server.i18n.messages.FooMessages", "fr");
		FooMessagesPureInterface msg2 = (FooMessagesPureInterface) service.create("org.liveSense.server.i18n.messages.FooMessagesPureInterface", "fr");
		FooMessagesWithAnnotation msg3 = (FooMessagesWithAnnotation) service.create("org.liveSense.server.i18n.messages.FooMessagesWithAnnotation", "fr");

		assertEquals("Le message est test_None()", msg1.test_None());
		assertEquals("Le message est test_None()", msg2.test_None());
		assertEquals("Le message est test_None()", msg3.method_test_None());
	}

	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#create(java.lang.String, java.lang.String, java.lang.ClassLoader)}.
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	@Test
	public void testCreateStringStringClassLoader() throws IOException, ClassNotFoundException {
		FooMessages msg1 = (FooMessages) service.create("org.liveSense.server.i18n.messages.FooMessages", "fr", this.getClass().getClassLoader());
		FooMessagesPureInterface msg2 = (FooMessagesPureInterface) service.create("org.liveSense.server.i18n.messages.FooMessagesPureInterface", "fr", this.getClass().getClassLoader());
		FooMessagesWithAnnotation msg3 = (FooMessagesWithAnnotation) service.create("org.liveSense.server.i18n.messages.FooMessagesWithAnnotation", "fr", this.getClass().getClassLoader());

		assertEquals("Le message est test_None()", msg1.test_None());
		assertEquals("Le message est test_None()", msg2.test_None());
		assertEquals("Le message est test_None()", msg3.method_test_None());
	}

	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#getResourceBundle(java.lang.String)}.
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	@Test
	public void testGetResourceBundleString() throws IOException, ClassNotFoundException {
		Locale.setDefault(Locale.ENGLISH);

		ResourceBundle res1 = service.getResourceBundle("org.liveSense.server.i18n.messages.FooMessages");
		ResourceBundle res2 = service.getResourceBundle("org/liveSense/server/i18n/messages/FooMessages");

		assertEquals("The message is test_None()", res1.getString("test_None"));
		assertEquals("The message is test_None()", res2.getString("test_None"));

	}

	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#getResourceBundle(java.lang.String, java.util.Locale)}.
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	@Test
	public void testGetResourceBundleStringLocale() throws IOException, ClassNotFoundException {
		ResourceBundle res1 = service.getResourceBundle("org.liveSense.server.i18n.messages.FooMessages", new Locale("fr"));
		ResourceBundle res2 = service.getResourceBundle("org/liveSense/server/i18n/messages/FooMessages", new Locale("fr"));
		ResourceBundle res3 = service.getResourceBundle("org.liveSense.server.i18n.messages.FooMessages", new Locale("en_US"));
		ResourceBundle res4 = service.getResourceBundle("org/liveSense/server/i18n/messages/FooMessages", new Locale("en_US"));

		assertEquals("Le message est test_None()", res1.getString("test_None"));
		assertEquals("Le message est test_None()", res2.getString("test_None"));
		assertEquals("The message is test_None()", res3.getString("test_None"));
		assertEquals("The message is test_None()", res4.getString("test_None"));

	}

	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#getResourceBundle(java.lang.String, java.util.Locale, java.lang.ClassLoader)}.
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	@Test
	public void testGetResourceBundleStringLocaleClassLoader() throws IOException, ClassNotFoundException {
		ResourceBundle res1 = service.getResourceBundle("org.liveSense.server.i18n.messages.FooMessages", new Locale("fr"), this.getClass().getClassLoader());
		ResourceBundle res2 = service.getResourceBundle("org/liveSense/server/i18n/messages/FooMessages", new Locale("fr"), this.getClass().getClassLoader());
		ResourceBundle res3 = service.getResourceBundle("org.liveSense.server.i18n.messages.FooMessages", new Locale("en_US"), this.getClass().getClassLoader());
		ResourceBundle res4 = service.getResourceBundle("org/liveSense/server/i18n/messages/FooMessages", new Locale("en_US"), this.getClass().getClassLoader());

		assertEquals("Le message est test_None()", res1.getString("test_None"));
		assertEquals("Le message est test_None()", res2.getString("test_None"));
		assertEquals("The message is test_None()", res3.getString("test_None"));
		assertEquals("The message is test_None()", res4.getString("test_None"));
	}

	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#getResourceBundle(java.lang.String, java.lang.String)}.
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	@Test
	public void testGetResourceBundleStringString() throws IOException, ClassNotFoundException {
		ResourceBundle res1 = service.getResourceBundle("org.liveSense.server.i18n.messages.FooMessages", "fr");
		ResourceBundle res2 = service.getResourceBundle("org/liveSense/server/i18n/messages/FooMessages", "fr");
		ResourceBundle res3 = service.getResourceBundle("org.liveSense.server.i18n.messages.FooMessages", "en_US");
		ResourceBundle res4 = service.getResourceBundle("org/liveSense/server/i18n/messages/FooMessages","en_US");

		assertEquals("Le message est test_None()", res1.getString("test_None"));
		assertEquals("Le message est test_None()", res2.getString("test_None"));
		assertEquals("The message is test_None()", res3.getString("test_None"));
		assertEquals("The message is test_None()", res4.getString("test_None"));
	}

	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#getResourceBundle(java.lang.String, java.lang.String, java.lang.ClassLoader)}.
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	@Test
	public void testGetResourceBundleStringStringClassLoader() throws IOException, ClassNotFoundException {
		ResourceBundle res1 = service.getResourceBundle("org.liveSense.server.i18n.messages.FooMessages", "fr", this.getClass().getClassLoader());
		ResourceBundle res2 = service.getResourceBundle("org/liveSense/server/i18n/messages/FooMessages", "fr", this.getClass().getClassLoader());
		ResourceBundle res3 = service.getResourceBundle("org.liveSense.server.i18n.messages.FooMessages", "en_US", this.getClass().getClassLoader());
		ResourceBundle res4 = service.getResourceBundle("org/liveSense/server/i18n/messages/FooMessages", "en_US", this.getClass().getClassLoader());

		assertEquals("Le message est test_None()", res1.getString("test_None"));
		assertEquals("Le message est test_None()", res2.getString("test_None"));
		assertEquals("The message is test_None()", res3.getString("test_None"));
		assertEquals("The message is test_None()", res4.getString("test_None"));
	}

	
	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#getDynamicResourceBundle()}.
	 */
	@Test
	public void testGetDynamicResourceBundle() {
		MockBundle bundle1 = new MockBundle(1) {
			@Override
			public java.net.URL getResource(String name) {
				return this.getClass().getClassLoader().getResource(name);
			};
			
			@Override
			public Class<?> loadClass(String name)
					throws ClassNotFoundException {
				return this.getClass().getClassLoader().loadClass(name);
			}
			
		};

		// Register DynamicMessage 1
		service.registerResourceBundle(bundle1, "org.liveSense.server.i18n.messages.DynamicMessages1");
		ResourceBundle dyn = service.getDynamicResourceBundle();

		assertEquals("The message is test_None1()", dyn.getString("test_None1"));
		
		// The DynamicMessage 2 have to throw MissingResourceException
		try {
			assertNull(dyn.getString("test_None2"));
			fail("No MissingResourceExceptionm");
		} catch (MissingResourceException e) {
		}

		// Register DynamicMessage 2
		service.registerResourceBundle(bundle1, "org.liveSense.server.i18n.messages.DynamicMessages2");
		assertEquals("The message is test_None1()", dyn.getString("test_None1"));
		assertEquals("The message is test_None2()", dyn.getString("test_None2"));

		// unRegister DynamicMessage 2
		service.unregisterResourceBundle(bundle1, "org.liveSense.server.i18n.messages.DynamicMessages2");
		assertEquals("The message is test_None1()", dyn.getString("test_None1"));

		// The DynamicMessage 2 have to throw MissingResourceException
		try {
			assertNull(dyn.getString("test_None2"));
			fail("No MissingResourceExceptionm");
		} catch (MissingResourceException e) {
		}
	}

	/**
	 * Test method for {@link org.liveSense.server.i18n.service.I18nService.I18nServiceImpl#getDynamicResourceBundle(java.util.Locale)}.
	 */
	@Test
	public void testGetDynamicResourceBundleLocale() {
		MockBundle bundle1 = new MockBundle(1) {
			@Override
			public java.net.URL getResource(String name) {
				return this.getClass().getClassLoader().getResource(name);
			};
			
			@Override
			public Class<?> loadClass(String name)
					throws ClassNotFoundException {
				return this.getClass().getClassLoader().loadClass(name);
			}
			
		};
		
		// Register DynamicMessage 1
		service.registerResourceBundle(bundle1, "org.liveSense.server.i18n.messages.DynamicMessages1");
		ResourceBundle dyn = service.getDynamicResourceBundle(new Locale("fr"));
		ResourceBundle dyn2 = service.getDynamicResourceBundle(new Locale("en_US"));

		assertEquals("Le message est test_None1()", dyn.getString("test_None1"));
		assertEquals("The message is test_None1()", dyn2.getString("test_None1"));
		
		// The DynamicMessage 2 have to throw MissingResourceException
		try {
			assertNull(dyn.getString("test_None2"));
			assertNull(dyn2.getString("test_None2"));
			fail("No MissingResourceExceptionm");
		} catch (MissingResourceException e) {
		}

		// Register DynamicMessage 2
		service.registerResourceBundle(bundle1, "org.liveSense.server.i18n.messages.DynamicMessages2");
		assertEquals("Le message est test_None1()", dyn.getString("test_None1"));
		assertEquals("Le message est test_None2()", dyn.getString("test_None2"));
		assertEquals("The message is test_None1()", dyn2.getString("test_None1"));
		assertEquals("The message is test_None2()", dyn2.getString("test_None2"));

		// unRegister DynamicMessage 2
		service.unregisterResourceBundle(bundle1, "org.liveSense.server.i18n.messages.DynamicMessages2");
		assertEquals("Le message est test_None1()", dyn.getString("test_None1"));
		assertEquals("The message is test_None1()", dyn2.getString("test_None1"));

		// The DynamicMessage 2 have to throw MissingResourceException
		try {
			assertNull(dyn.getString("test_None2"));
			assertNull(dyn2.getString("test_None2"));
			fail("No MissingResourceExceptionm");
		} catch (MissingResourceException e) {
		}
		
	}


}
