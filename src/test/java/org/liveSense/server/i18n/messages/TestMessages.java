package org.liveSense.server.i18n.messages;


import com.google.gwt.i18n.client.Messages;

import java.lang.reflect.Method;

import org.junit.Test;
import org.liveSense.server.i18n.I18N;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestMessages extends TestCase {

    protected final static String ENGLISH_MSG = "The message is ";
    protected final static String FRENCH_MSG = "Le message est ";
    private final static Byte Byte_VALUE = 1;
    private final static Short Short_VALUE = 1;
    private final static Integer Integer_VALUE = 1;
    private final static Long Long_VALUE = 1l;
    private final static Float Float_VALUE = 1.0f;
    private final static Double Double_VALUE = 1.0d;
    private final static Character Character_VALUE = '1';
    private final static String String_VALUE = "1";
    private final static Object Object_VALUE = "1";
    
    @Test
    public void testMessages() throws Exception {
        test(FooMessages.class, "");
    }

    @Test
    public void testMessagesPureInterface() throws Exception {
        test(FooMessagesPureInterface.class, "");
    }

    @Test
    public void testMessagesWithAnnotation() throws Exception {
        test(FooMessagesWithAnnotation.class, "method_");
    }
    
    @Test
    public void testPluralMessages() throws Exception {
        FooPluralMessages msg = I18N.create(FooPluralMessages.class);
        Assert.assertEquals("method_test_simple_int(0)", "the value is zero", msg.method_test_simple_int(0));
        Assert.assertEquals("method_test_simple_int(1)", "the value is O N E", msg.method_test_simple_int(1));
        Assert.assertEquals("method_test_simple_int(2)", "the value is T W O", msg.method_test_simple_int(2));
        Assert.assertEquals("method_test_simple_int(3)", "the value 3 is between THREE and TEN", msg.method_test_simple_int(3));
        Assert.assertEquals("method_test_simple_int(4)", "the value 4 is between THREE and TEN", msg.method_test_simple_int(4));
        Assert.assertEquals("method_test_simple_int(10)", "the value 10 is between THREE and TEN", msg.method_test_simple_int(10));
        Assert.assertEquals("method_test_simple_int(11)", "the value 11 is between ELEVEN and a HUNDRED", msg.method_test_simple_int(11));
        Assert.assertEquals("method_test_simple_int(99)", "the value 99 is between ELEVEN and a HUNDRED", msg.method_test_simple_int(99));
        Assert.assertEquals("method_test_simple_int(100)", "the value is 100", msg.method_test_simple_int(100));
        Assert.assertEquals("method_test_simple_int(101)", "the value is 101", msg.method_test_simple_int(101));
    }
    
    protected <T> void test(Class<T> c, String method_prefix) throws Exception {
        T msg_default = I18N.create(c);
        T msg_french = I18N.create(c, "fr");
        Method[] methods = c.getMethods();
        for (Method method : methods) {
//            System.out.println("testing method " + method.toGenericString());
            invoke(msg_default, ENGLISH_MSG, method, false, method_prefix);
            invoke(msg_default, ENGLISH_MSG, method, true, method_prefix);
            invoke(msg_french, FRENCH_MSG, method, false, method_prefix);
            invoke(msg_french, FRENCH_MSG, method, true, method_prefix);
        }
    }

    protected void invoke(Object o, String languagePrefix, Method method, boolean setToNullIfPossible, String method_prefix) throws Exception {
        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] paramValues = new Object[paramTypes.length];
        String expected = languagePrefix + method.getName().substring(method_prefix.length()) + '(';
        String sep = "";
        int paramIdx = 0;
        for(Class<?> paramType : paramTypes) {
            String paramAsString;
            if (setToNullIfPossible && !paramType.isPrimitive()) { 
                paramValues[paramIdx] = null;
                paramAsString = "null";
            } else if (Byte.class.equals(paramType) || byte.class.equals(paramType)) {
                paramValues[paramIdx] = Byte_VALUE;
                paramAsString = Byte_VALUE.toString();
                
            } else if (Short.class.equals(paramType) || short.class.equals(paramType)) {
                paramValues[paramIdx] = Short_VALUE;
                paramAsString = Short_VALUE.toString();
                
            } else if (Integer.class.equals(paramType) || int.class.equals(paramType)) {
                paramValues[paramIdx] = Integer_VALUE;
                paramAsString = Integer_VALUE.toString();
                
            } else if (Long.class.equals(paramType) || long.class.equals(paramType)) {
                paramValues[paramIdx] = Long_VALUE;
                paramAsString = Long_VALUE.toString();
                
            } else if (Float.class.equals(paramType) || float.class.equals(paramType)) {
                paramValues[paramIdx] = Float_VALUE;
                paramAsString = Float_VALUE.toString();
                
            } else if (Double.class.equals(paramType) || double.class.equals(paramType)) {
                paramValues[paramIdx] = Double_VALUE;
                paramAsString = Double_VALUE.toString();
                
            } else if (String.class.equals(paramType)) {
                paramValues[paramIdx] = String_VALUE;
                paramAsString = String_VALUE.toString();
                
            } else if (Character.class.equals(paramType) || char.class.equals(paramType)) {
                paramValues[paramIdx] = Character_VALUE;
                paramAsString = Character_VALUE.toString();
                
            } else {
                paramValues[paramIdx] = Object_VALUE;
                paramAsString = Object_VALUE.toString();
            } 
            expected += sep + paramAsString;
            sep =",";
            paramIdx++;
        }
        expected += ')';
        Object found = null;
        try {
            found = method.invoke(o, paramValues);
        } catch(Throwable t) {
            t.printStackTrace();
            fail(t.getMessage());
            return;
        }
        String assertMsg =  method.toString() + ", lang=" + languagePrefix + ", setToNull="+setToNullIfPossible;
        Assert.assertNotNull(assertMsg, found);
        Assert.assertEquals(assertMsg, String.class, found.getClass());
        if (!expected.equals(found)) {
            System.out.println("expected: '" + expected + "'");
            System.out.println("found: '" + found + "'");
        }
        Assert.assertEquals(assertMsg, expected, (String) found);
    }
}
