package org.liveSense.server.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.Properties;
import java.util.ResourceBundle;

public abstract class GenericX implements InvocationHandler {

    protected final Properties properties = new Properties();
    protected final Class<?> itf;
    protected final ClassLoader classLoader;
    protected final ResourceBundle resourceBundle;

    @Override
	public abstract Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
    
    public GenericX(Class<?> _itf, String lang, String country, String variant, ClassLoader classLoader, ResourceBundle bundle) throws IOException, InvalidParameterException {
        this.itf = _itf;
        this.classLoader = classLoader;
        if (bundle == null) fillProperties(itf, lang, country, variant);
        this.resourceBundle = bundle;
    }

    
    public void fillProperties(Class<?> itf, String lang, String country, String variant) throws IOException {
        for (Class<?> superItf : itf.getInterfaces()) {
            fillProperties(superItf, lang, country, variant);
        }
        String suffixLang = ((lang == null || "".equals(lang)) ? "" : ("_" +lang)) ;
        String suffixCountry = ((country == null || "".equals(country)) ? "" : ("_" +country));
        String suffixVariant = ((variant == null || "".equals(variant)) ? "" : ("_" +variant));

        String baseName = itf.getName().replace('.', '/');
        InputStream in = null;
        if (in == null && !suffixVariant.equals("")) in = load(baseName + suffixLang + suffixCountry + suffixVariant + ".properties");
        if (in == null && !suffixCountry.equals("")) in = load(baseName + suffixLang + suffixCountry + ".properties");
        if (in == null && !suffixLang.equals("")) in = load(baseName + suffixLang + ".properties");
        if (in == null) {
            in = load(baseName + ".properties");
        }
        
        if (in != null) {
            properties.load(in);
        }
        
    }
    protected InputStream load(String s) {
        InputStream in = null;

        if (classLoader != null) {
        	in = classLoader.getResourceAsStream(s);
        }
        if (in == null) {
	        ClassLoader cl;
	        cl = Thread.currentThread().getContextClassLoader();
	        if (cl != null) {
	            in = cl.getResourceAsStream(s);
	        }
	        if (in == null) {
	            cl = getClass().getClassLoader();
	            if (cl != null) {
	                in = getClass().getClassLoader().getResourceAsStream(s);
	            }
	            if (in == null) {
	                cl = ClassLoader.getSystemClassLoader();
	                if (cl != null) {
	                    in = cl.getResourceAsStream(s);
	                }
	            }
	        }
        }
        return in;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public int hashCode() {
    	if (resourceBundle != null)
    		return resourceBundle.hashCode();
        return properties.size();
    }
    
    public static boolean isA(Class<?> c1, Class<?> c2) {
        return c2.isAssignableFrom(c1);
    }
}
