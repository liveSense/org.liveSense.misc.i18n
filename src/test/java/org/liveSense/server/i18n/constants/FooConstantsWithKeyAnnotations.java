package org.liveSense.server.i18n.constants;

import com.google.gwt.i18n.client.Constants;

import java.util.Map;

public interface FooConstantsWithKeyAnnotations extends Constants {

    @Key("test_String")
    String method_test_String();
    
    @Key("test_int")
    int method_test_int();
    
    @Key("test_Integer")
    Integer method_test_Integer();
    
    @Key("test_float")
    float method_test_float();
    
    @Key("test_Float")
    Float method_test_Float();
    
    @Key("test_double")
    double method_test_double();

    @Key("test_Double")
    Double method_test_Double();

    @Key("test_boolean")
    boolean method_test_boolean();
    
    @Key("test_Boolean")
    Boolean method_test_Boolean();
    
    @Key("test_StringArray")
    String[] method_test_StringArray();
    
    @Key("test_MapStringString")
    Map<String,String> method_test_MapStringString();
    
}
