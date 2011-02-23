package org.liveSense.server.i18n.constants;

import com.google.gwt.i18n.client.Constants;

import java.util.Map;

public interface FooConstantsWithKeyAndDefaultAnnotations extends Constants {

    @Key("test_String")
    @DefaultStringValue(value="1")
    String method_test_String();
    
    @Key("test_int")
    @DefaultIntValue(value=1)
    int method_test_int();
    
    @Key("test_Integer")
    @DefaultIntValue(value=1)
    Integer method_test_Integer();
    
    @Key("test_float")
    @DefaultFloatValue(value=1.0f)
    float method_test_float();
    
    @Key("test_Float")
    @DefaultFloatValue(value=1.0f)
    Float method_test_Float();
    
    @Key("test_double")
    @DefaultDoubleValue(value=1.0)
    double method_test_double();

    @Key("test_Double")
    @DefaultDoubleValue(value=1.0)
    Double method_test_Double();

    @Key("test_boolean")
    @DefaultBooleanValue(value=true)
    boolean method_test_boolean();
    
    @Key("test_Boolean")
    @DefaultBooleanValue(value=true)
    Boolean method_test_Boolean();
    
    @Key("test_StringArray")
    @DefaultStringArrayValue(value={"1","2","3"})
    String[] method_test_StringArray();
    
    @Key("test_MapStringString")
    @DefaultStringMapValue(value={"a","1", "b","2", "c","3"})
    Map<String,String> method_test_MapStringString();    
}
